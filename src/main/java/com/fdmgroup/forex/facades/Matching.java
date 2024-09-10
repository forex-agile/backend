package com.fdmgroup.forex.facades;

import java.util.List;
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

    public Matching(OrderRepo orderRepo, AssetService assetService, AssetRepo assetRepo) {
        this.orderRepo = orderRepo;
        this.assetService = assetService;
        this.assetRepo = assetRepo;
    }
	
	@Transactional
	public synchronized Order matchOrders(Order newOrder) {
		List<Order> filteredAndSortedOrders = filterAndSortOrders(newOrder);
		
	    // if empty filtered list for market order, throw no matching order exception (keep it ACTIVE for limit order)
	    if (newOrder.getOrderType() == OrderType.MARKET && filteredAndSortedOrders.isEmpty()) {
	    	newOrder.setOrderStatus(OrderStatus.CANCELLED);
	    	throw new NoMatchingOrderException("No matching limit order found currently. Please try again later");
	    }
	    // loop through filtered list
	    for (Order outstandingOrder : filteredAndSortedOrders) {
	    	// if new order is CANCELLED, CLOSED, CLEARED, break the for loop and return the status
	        if (newOrder.getOrderStatus() != OrderStatus.ACTIVE) break;
	        handleMatchedOrder(newOrder,outstandingOrder);
	    }
	    
	    return newOrder;
	}
	
	// keeping the System out for later BE integration testing
    private void handleMatchedOrder(Order newOrder, Order outstandingOrder) {
    	
    	double rateOfNewOrderAsset = getRateForMatchedOrders(newOrder,outstandingOrder);
    	// if same order side, the rate will be limit/total for outstanding order
    	double rateOfOutstandingOrderAsset = newOrder.getOrderSide() == outstandingOrder.getOrderSide() ? 1/rateOfNewOrderAsset : rateOfNewOrderAsset;
    	
    	// get residual and assets(for balance)
        double newOrderResidual = newOrder.getResidual();
        double outstandingOrderResidual = outstandingOrder.getResidual();
        Asset newOrderBaseAsset = findAssetByOrderAndCurrency(newOrder, newOrder.getBaseFx());
        Asset outstandingOrderBaseAsset = findAssetByOrderAndCurrency(outstandingOrder, outstandingOrder.getBaseFx());
        System.out.println("newOrderResidual:"+ newOrderResidual);
        System.out.println("outstandingOrderResidual:" + outstandingOrderResidual);
        System.out.println("newOrderBaseAsset:" + newOrderBaseAsset.getCurrency().getCurrencyCode() + " Balance: " + newOrderBaseAsset.getBalance());
        System.out.println("outstandingOrderBaseAsset:" + outstandingOrderBaseAsset.getCurrency().getCurrencyCode() + " Balance: " + outstandingOrderBaseAsset.getBalance());
             
        // find the minimum amount among orders or user balance
        double newOrderSettledAmount = Math.min(
        		newOrderResidual,
                Math.min(outstandingOrderResidual * rateOfNewOrderAsset, 
                         Math.min(newOrderBaseAsset.getBalance(), outstandingOrderBaseAsset.getBalance() * rateOfNewOrderAsset))
            );
        System.out.println("newOrderSettledAmount(minimum out of the four):"+newOrderSettledAmount);
        
        // if account balance is 0, order will be cancelled        
        if (newOrderSettledAmount == 0) {
            if (newOrderBaseAsset.getBalance() == 0) {
                newOrder.setOrderStatus(OrderStatus.CANCELLED);
            } else {
                outstandingOrder.setOrderStatus(OrderStatus.CANCELLED);
            }
            return;
        }
        
        // calculate outstanding order settled amount
        double outstandingOrderSettledAmount = newOrderSettledAmount / rateOfNewOrderAsset;
        System.out.println("outstandingOrderSettledAmount:"+outstandingOrderSettledAmount);
        // Update residual amounts and balances
        newOrder.setResidual(newOrder.getResidual() - newOrderSettledAmount);
        outstandingOrder.setResidual(outstandingOrder.getResidual() - outstandingOrderSettledAmount);
        newOrderBaseAsset.setBalance(newOrderBaseAsset.getBalance() - newOrderSettledAmount);
        outstandingOrderBaseAsset.setBalance(outstandingOrderBaseAsset.getBalance() - outstandingOrderSettledAmount);
        
        // handle updating order status
        updateOrderStatus(newOrder, outstandingOrder, newOrderBaseAsset, outstandingOrderBaseAsset);
        
        // Save the updated orders
        orderRepo.save(newOrder);
        orderRepo.save(outstandingOrder);
        // update baseFx Asset
        assetRepo.save(newOrderBaseAsset);
        assetRepo.save(outstandingOrderBaseAsset);
        // update quoteFx Asset too (with deposit method because a case of no existing quote asset)
        assetService.depositAsset(newOrder.getPortfolio(), newOrder.getQuoteFx(), newOrderSettledAmount * rateOfNewOrderAsset);
        assetService.depositAsset(outstandingOrder.getPortfolio(), outstandingOrder.getQuoteFx(), outstandingOrderSettledAmount * rateOfOutstandingOrderAsset);
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

	private double getRateForMatchedOrders(Order newOrder, Order outstandingOrder) {
		// if one of them is market order, get rate of counter order
		if (newOrder.getOrderType() == OrderType.MARKET) 
            return getRateByOrderSide(newOrder, outstandingOrder);
        else if (outstandingOrder.getOrderType() == OrderType.MARKET) 
        	return getRateByOrderSide(outstandingOrder, newOrder);
        
		// get rate of newer order
		if (newOrder.getCreationDate().after(outstandingOrder.getCreationDate())) 
			return getRateByOrderSide(outstandingOrder, newOrder);
		else return getRateByOrderSide(newOrder, outstandingOrder);
	}
	
	private Asset findAssetByOrderAndCurrency(Order order, Currency fx) {
		Portfolio portfolio = order.getPortfolio();
		return assetService.findAssetByPortfolioAndCurrency(portfolio, fx);
	}
    
	private List<Order> filterAndSortOrders(Order newOrder) {
        // find active order with opposite currency in the DB
		List<Order> filteredOrders = orderRepo.findActiveOrdersByFx(newOrder.getQuoteFx().getCurrencyCode(), newOrder.getBaseFx().getCurrencyCode(), OrderStatus.ACTIVE);
		
        List<Order> filteredLimitOrders = filteredOrders.stream()
            .filter(order -> order.getOrderType() == OrderType.LIMIT)
            .filter(outStandingOrder -> {
                double outStandingOrderRate = getRateByOrderSide(newOrder, outStandingOrder);
                double newOrderRate = newOrder.getTotal() / newOrder.getLimit();
                // buy order: outstanding rate > me, sell order is reverse
                // example can refer to bottom of algo design document
                return newOrder.getOrderSide() == OrderSide.BUY ? outStandingOrderRate >= newOrderRate : outStandingOrderRate <= newOrderRate;
            })
            .collect(Collectors.toList());

        List<Order> filteredAndSortedOrders = filteredLimitOrders.stream()
            .sorted((a, b) -> {
                double rateA = getRateByOrderSide(newOrder,a);
                double rateB = getRateByOrderSide(newOrder,b);
                int rateComparison = Double.compare(rateA, rateB);
                
                if (rateComparison != 0) return rateComparison;
                return a.getCreationDate().compareTo(b.getCreationDate());
            })
            .collect(Collectors.toList());

        return filteredAndSortedOrders;
    }
    
    private double getRateByOrderSide(Order newOrder, Order orderToGetRate) {
    	return orderToGetRate.getOrderSide() == newOrder.getOrderSide() ? 
              	orderToGetRate.getLimit() / orderToGetRate.getTotal() :
            	orderToGetRate.getTotal() / orderToGetRate.getLimit();
    }    
}