package com.fdmgroup.forex.facades;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.fdmgroup.forex.enums.*;
import com.fdmgroup.forex.exceptions.order.NoMatchingOrderException;
import com.fdmgroup.forex.models.*;
import com.fdmgroup.forex.repos.*;
import com.fdmgroup.forex.services.AssetService;

import jakarta.transaction.Transactional;

@Service
public class Matching {
	
    private OrderRepo orderRepo;
    private AssetService assetService;
    private AssetRepo assetRepo;
    private TradeRepo tradeRepo;

    public Matching(OrderRepo orderRepo, AssetService assetService, AssetRepo assetRepo, TradeRepo tradeRepo) {
        this.orderRepo = orderRepo;
        this.assetService = assetService;
        this.assetRepo = assetRepo;
        this.tradeRepo = tradeRepo;
    }
	
	@Transactional
	public synchronized Order matchOrders(Order newOrder) {
		newOrder = orderRepo.save(newOrder);
		List<Order> filteredAndSortedOrders = filterAndSortOrders(newOrder);
		System.out.println("filteredAndSortedOrders:" +filteredAndSortedOrders.size());
	    // if empty filtered list for market order, throw no matching order exception (keep it ACTIVE for limit order)
	    if (newOrder.getOrderType() == OrderType.MARKET && filteredAndSortedOrders.isEmpty()) 
	    	throw new NoMatchingOrderException("No matching limit order found currently. Please try again later");
	    
	    // loop through filtered list
	    for (Order outstandingOrder : filteredAndSortedOrders) {
	    	// if new order is CANCELLED, CLOSED, CLEARED, break the for loop and return the status
	        if (newOrder.getOrderStatus() != OrderStatus.ACTIVE) return newOrder;
	        handleMatchedOrder(newOrder,outstandingOrder);
	    }
	    
	    if (newOrder.getOrderType() == OrderType.MARKET && newOrder.getOrderStatus() == OrderStatus.ACTIVE) {
	    	newOrder.setOrderStatus(OrderStatus.CLOSED);
	    	newOrder = orderRepo.save(newOrder);
	    }
	    return newOrder;
	}
	
	// keeping the System out for later BE integration testing
    private void handleMatchedOrder(Order newOrder, Order outstandingOrder) {
    	// rate=base/quote of new order
        BigDecimal rateOfNewOrderAsset = getRateForMatchedOrders(newOrder, outstandingOrder);
        // rate=base/quote of ourstanding order: 1/rate because base and quote fx is reverse
        BigDecimal rateOfOutstandingOrderAsset = BigDecimal.ONE.divide(rateOfNewOrderAsset, 20, RoundingMode.HALF_UP);
		System.out.println("Side,Base,Quote:"+newOrder.getOrderSide()+newOrder.getBaseFx()+","+newOrder.getQuoteFx());
    	System.out.println("rateOfNewOrderAsset:"+ rateOfNewOrderAsset);
    	System.out.println("rateOfOutstandingOrderAsset:"+ rateOfOutstandingOrderAsset);
    	// get residual based on baseFx of new order asset, given that buy order base asset is in opposite currency
        BigDecimal newOrderResidual = newOrder.getOrderSide() == OrderSide.BUY
            ? BigDecimal.valueOf(newOrder.getResidual()).divide(rateOfNewOrderAsset, 20, RoundingMode.HALF_UP)
            : BigDecimal.valueOf(newOrder.getResidual());
        // get residual in the quoteFx --> baseFx of new order
        BigDecimal outstandingOrderResidual = outstandingOrder.getOrderSide() == OrderSide.BUY
            ? BigDecimal.valueOf(outstandingOrder.getResidual()) 
            : BigDecimal.valueOf(outstandingOrder.getResidual()).multiply(rateOfOutstandingOrderAsset);
        
        // get base assets(to check balance)
        Asset newOrderBaseAsset = findAssetByOrderAndCurrency(newOrder, newOrder.getBaseFx());
        Asset outstandingOrderBaseAsset = findAssetByOrderAndCurrency(outstandingOrder, outstandingOrder.getBaseFx());
        
        System.out.println("newOrderResidual:"+ newOrderResidual + newOrderBaseAsset.getCurrency().getCurrencyCode());
        System.out.println("outstandingOrderResidual:" + outstandingOrderResidual + newOrderBaseAsset.getCurrency().getCurrencyCode());
        System.out.println("newOrderBaseAsset:" + newOrderBaseAsset.getCurrency().getCurrencyCode() + " Balance: " + newOrderBaseAsset.getBalance());
        System.out.println("outstandingOrderBaseAsset:" + newOrderBaseAsset.getCurrency().getCurrencyCode() +" Balance: " +outstandingOrderBaseAsset.getBalance() * rateOfOutstandingOrderAsset.doubleValue());
             
        // Find the minimum amount among orders or user balance
        // first three are in new order baseFx, last one base asset=newOrderquoteFx
        BigDecimal settledAmountInNewOrderBaseFx = newOrderResidual.min(
            outstandingOrderResidual.min(
                BigDecimal.valueOf(newOrderBaseAsset.getBalance()).min(
                         BigDecimal.valueOf(outstandingOrderBaseAsset.getBalance()).multiply(rateOfOutstandingOrderAsset)))
        );
        
        // Calculate settled amount based on residual Fx
        BigDecimal newOrderSettledAmount = newOrder.getOrderSide() == OrderSide.BUY
        		? settledAmountInNewOrderBaseFx.multiply(rateOfNewOrderAsset)
        		: settledAmountInNewOrderBaseFx;
        System.out.println("settledAmountInNewOrderBaseFx:"+settledAmountInNewOrderBaseFx);
        // if account balance is 0, order will be cancelled        
        if (newOrderSettledAmount.compareTo(BigDecimal.ZERO) == 0) {
            if (newOrderBaseAsset.getBalance() == 0) {
                newOrder.setOrderStatus(OrderStatus.CANCELLED);
            } else {
                outstandingOrder.setOrderStatus(OrderStatus.CANCELLED);
            }
            return;
        }
        
        // calculate outstanding settled amount in outstanding order baseFx
        BigDecimal settledAmountInOutstandingOrderBaseFx = settledAmountInNewOrderBaseFx.multiply(rateOfNewOrderAsset);
        // Calculate outstanding order settled amount based on residualFx
        BigDecimal outstandingOrderSettledAmount = outstandingOrder.getOrderSide() == OrderSide.BUY
        		? settledAmountInOutstandingOrderBaseFx.multiply(rateOfOutstandingOrderAsset)
        		: settledAmountInOutstandingOrderBaseFx;
        
        System.out.println("settledAmountInOutstandingOrderBaseFx:"+settledAmountInOutstandingOrderBaseFx);
        // Update residual amounts and balances
        newOrder.setResidual(newOrder.getResidual() - newOrderSettledAmount.doubleValue());
        outstandingOrder.setResidual(outstandingOrder.getResidual() - outstandingOrderSettledAmount.doubleValue());
        newOrderBaseAsset.setBalance(newOrderBaseAsset.getBalance() - settledAmountInNewOrderBaseFx.doubleValue());
        outstandingOrderBaseAsset.setBalance(outstandingOrderBaseAsset.getBalance() - settledAmountInOutstandingOrderBaseFx.doubleValue());    
        
        // handle updating order status
        updateOrderStatus(newOrder, outstandingOrder, newOrderBaseAsset, outstandingOrderBaseAsset);
        
        // Save the updated orders
        newOrder = orderRepo.save(newOrder);
        outstandingOrder = orderRepo.save(outstandingOrder);
        // update baseFx Asset
        newOrderBaseAsset = assetRepo.save(newOrderBaseAsset);        
        outstandingOrderBaseAsset = assetRepo.save(outstandingOrderBaseAsset);
        
        System.out.println("newOrderResidual--------------->"+(newOrder.getResidual()));
        System.out.println("outstandingOrderResidual--------------->"+(outstandingOrder.getResidual()));
        System.out.println("newOrderBaseAsset--------------->balance:"+newOrderBaseAsset.getBalance());
        System.out.println("outstandingOrderBaseAsset--------------->balance:"+outstandingOrderBaseAsset.getBalance());

        // Update quoteFx Asset too
        assetService.depositAsset(newOrder.getPortfolio(), newOrder.getQuoteFx(), settledAmountInOutstandingOrderBaseFx.doubleValue());
        assetService.depositAsset(outstandingOrder.getPortfolio(), outstandingOrder.getQuoteFx(), settledAmountInNewOrderBaseFx.doubleValue());
        // create trade records
	    UUID tradeId = UUID.randomUUID();
	    tradeRepo.save(new Trade(tradeId,newOrder,settledAmountInNewOrderBaseFx.doubleValue(), settledAmountInOutstandingOrderBaseFx.doubleValue()));
	    tradeRepo.save(new Trade(tradeId,outstandingOrder,settledAmountInOutstandingOrderBaseFx.doubleValue(), settledAmountInNewOrderBaseFx.doubleValue()));
    }
    
	private void updateOrderStatus(Order newOrder, Order outstandingOrder, Asset newOrderBaseAsset,
			Asset outstandingOrderBaseAsset) {
		// close order if balance is 0
		if (newOrderBaseAsset.getBalance() == 0) newOrder.setOrderStatus(OrderStatus.CLOSED);
		if (outstandingOrderBaseAsset.getBalance() == 0) outstandingOrder.setOrderStatus(OrderStatus.CLOSED);
		// clear order if residual is 0
		if (newOrder.getResidual() == 0) newOrder.setOrderStatus(OrderStatus.CLEARED);
		if (outstandingOrder.getResidual() == 0) outstandingOrder.setOrderStatus(OrderStatus.CLEARED);
	}

	private BigDecimal getRateForMatchedOrders(Order newOrder, Order outstandingOrder) {
		// if one of them is market order, get rate of counter order
		// if using outstanding rate, need to 1/rate to get rate base on new order baseFx/quoteFx
		if (newOrder.getOrderType() == OrderType.MARKET) 
            return inverseRate(getRateByOrderSide(outstandingOrder));
        else if (outstandingOrder.getOrderType() == OrderType.MARKET) 
        	return getRateByOrderSide(newOrder);
        
		// get rate of newer order
		if (newOrder.getCreationDate().after(outstandingOrder.getCreationDate()))
			return getRateByOrderSide(newOrder);
		else 
			return inverseRate(getRateByOrderSide(outstandingOrder));
	}
	
	private Asset findAssetByOrderAndCurrency(Order order, Currency fx) {
		Portfolio portfolio = order.getPortfolio();
		return assetService.findAssetByPortfolioAndCurrency(portfolio, fx);
	}
    
	private List<Order> filterAndSortOrders(Order newOrder) {
		BigDecimal newOrderRate = newOrder.getOrderType()==OrderType.MARKET ? null : getRateByOrderSide(newOrder);
        // find active order with opposite currency in the DB
		List<Order> filteredLimitOrders = orderRepo.findActiveOrdersByFx(newOrder.getQuoteFx(), newOrder.getBaseFx(), OrderStatus.ACTIVE, OrderType.LIMIT, newOrder.getPortfolio());
		System.out.println("filteredLimitOrders:" + filteredLimitOrders.size());
        List<Order> filteredLimitOrdersWithRate = filteredLimitOrders.stream()
            .filter(outStandingOrder -> {
            	if (newOrder.getOrderType()==OrderType.MARKET) return true;                
            	BigDecimal outStandingOrderRate = inverseRate(getRateByOrderSide(outStandingOrder));
                // buy order: outstanding rate > me, sell order is reverse
                // example can refer to bottom of algo design document
            	return newOrder.getOrderSide() == OrderSide.BUY 
            		    ? outStandingOrderRate.compareTo(newOrderRate) >= 0 
            		    : outStandingOrderRate.compareTo(newOrderRate) <= 0;
            })
            .collect(Collectors.toList());

        List<Order> filteredAndSortedOrders = filteredLimitOrdersWithRate.stream()
            .sorted((a, b) -> {
            	BigDecimal rateA = inverseRate(getRateByOrderSide(a));
            	BigDecimal rateB = inverseRate(getRateByOrderSide(b));
                int rateComparison = rateA.compareTo(rateB);
                
                // For buy orders, sort by ascending rate
                // For sell orders, sort by descending rate
                if (newOrder.getOrderSide() == OrderSide.SELL) 
                    rateComparison = -rateComparison;
                if (rateComparison != 0) return rateComparison;
                return a.getCreationDate().compareTo(b.getCreationDate());
            })
            .collect(Collectors.toList());

        return filteredAndSortedOrders;
    }    
    
	private BigDecimal getRateByOrderSide(Order order) {
	    BigDecimal limit = BigDecimal.valueOf(order.getLimit());
	    BigDecimal total = BigDecimal.valueOf(order.getTotal());
	    
	    return order.getOrderSide() == OrderSide.BUY
	    		?	total.divide(limit, 20, RoundingMode.HALF_UP)
	    		:	limit.divide(total, 20, RoundingMode.HALF_UP);
	}
	
	private BigDecimal inverseRate(BigDecimal rate) {
		return BigDecimal.ONE.divide(rate, 20, RoundingMode.HALF_UP);
	}	

}