package com.fdmgroup.forex.services;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.fdmgroup.forex.enums.*;
import com.fdmgroup.forex.exceptions.BadRequestException;
import com.fdmgroup.forex.exceptions.InsufficientFundsException;
import com.fdmgroup.forex.exceptions.RecordNotFoundException;
import com.fdmgroup.forex.facades.Matching;
import com.fdmgroup.forex.models.Asset;
import com.fdmgroup.forex.models.Currency;
import com.fdmgroup.forex.models.Order;
import com.fdmgroup.forex.models.Portfolio;
import com.fdmgroup.forex.models.User;
import com.fdmgroup.forex.models.dto.SubmitSpotOrderDTO;
import com.fdmgroup.forex.repos.CurrencyRepo;
import com.fdmgroup.forex.repos.OrderRepo;
import com.fdmgroup.forex.repos.PortfolioRepo;
import com.fdmgroup.forex.security.AuthUserService;

@Service
public class OrderService {

    private OrderRepo orderRepo;
    private PortfolioRepo portfolioRepo;
    private CurrencyRepo currencyRepo;
    private AuthUserService authUserService;
    private Matching matching;
    private AssetService assetService;

    public OrderService(OrderRepo orderRepo, PortfolioRepo portfolioRepo, CurrencyRepo currencyRepo,
			AuthUserService authUserService, Matching matching, AssetService assetService) {
		this.orderRepo = orderRepo;
		this.portfolioRepo = portfolioRepo;
		this.currencyRepo = currencyRepo;
		this.authUserService = authUserService;
		this.matching = matching;
		this.assetService = assetService;
	}
    
    public List<Order> findAllOrders() {
        return orderRepo.findAll();
    }

    public Order findOrderById(UUID id) {
        Optional<Order> orderOptional = orderRepo.findById(id);
        return orderOptional.orElseThrow(() -> new RecordNotFoundException("Order with id '" + id + "'' not found"));
    }

    public List<Order> findOrdersByUserId(UUID userId) {
        List<Order> orders = orderRepo.findByPortfolio_User_Id(userId);
        return orders;
    }

    public List<Order> findOrdersByUserIdAndOrderStatus(UUID userId, OrderStatus orderStatus) {
        List<Order> orders = orderRepo.findByPortfolio_User_IdAndOrderStatus(userId, orderStatus);
        return orders;
    }

    public List<Order> findOrdersByOrderStatus(OrderStatus orderStatus) {
        List<Order> orders = orderRepo.findByOrderStatus(orderStatus);
        return orders;
    }

    public List<Order> findOrdersByOrderType(OrderType orderType) {
        List<Order> orders = orderRepo.findByOrderType(orderType);
        return orders;
    }

    public Order submitSpotOrder(SubmitSpotOrderDTO dto) {
        Date now = new Date();
        validateSpotOrderSubmission(dto, now);
        
        Order order = buildSpotOrder(dto);
        
        validateSpotOrderBaseFxBalance(order);

        return matching.matchOrders(orderRepo.save(order));
    }

    private void validateSpotOrderBaseFxBalance(Order order) {
		double amountToCheck;			
		if (order.getOrderType().equals(OrderType.MARKET)) amountToCheck = 0;
		else if (order.getOrderSide().equals(OrderSide.BUY)) amountToCheck = order.getLimit();
		else amountToCheck = order.getTotal();
		
		Asset asset = assetService.findAssetByPortfolioAndCurrency(order.getPortfolio(), order.getBaseFx());
		if (order.getOrderType().equals(OrderType.MARKET) && asset.getBalance() <= amountToCheck)
			throw new InsufficientFundsException("order is not created due to insufficient balance: balance=" + asset.getBalance());
		else if (order.getOrderType().equals(OrderType.LIMIT) && asset.getBalance() < amountToCheck)
			throw new InsufficientFundsException("order amount cannot exceed balance: balance=" + asset.getBalance() + ", amount=" + amountToCheck);
			
	}

	public void validateSpotOrderSubmission(SubmitSpotOrderDTO dto, Date now) {
        if (dto.getExpiryDate().before(now)) {
            throw new BadRequestException("Expiry date must be in the future.");
        }

        if (dto.getBaseFx() == dto.getQuoteFx()) {
            throw new BadRequestException("Base currency and quote currency must be different.");
        }

        if (dto.getOrderType() == OrderType.LIMIT && dto.getLimit() == null) {
            throw new BadRequestException("Limit is required for a limit order.");
        }

        if (dto.getOrderType() == OrderType.MARKET && dto.getLimit() != null) {
            throw new BadRequestException("Limit should be absent for a market order.");
        }
    }

    public Order buildSpotOrder(SubmitSpotOrderDTO dto) {
        User authUser = authUserService.getAuthenticatedUser();
        Portfolio portfolio = portfolioRepo.findByUser_Id(authUser.getId()).orElseThrow(
                () -> new BadRequestException(
                        "Couldn't find portfolio for user. Please try to logout and login again. If problem persists, contact support."));

        Currency baseFx = currencyRepo.findById(dto.getBaseFx()).orElseThrow(
                () -> new BadRequestException("Base currency is not valid."));
        Currency quoteFx = currencyRepo.findById(dto.getQuoteFx()).orElseThrow(
                () -> new BadRequestException("Base currency is not valid."));

        Order order;

        switch (dto.getOrderType()) {
            case LIMIT:
                order = new Order(portfolio, dto.getOrderType(), dto.getOrderSide(), OrderStatus.ACTIVE,
                        dto.getExpiryDate(), baseFx, quoteFx, dto.getTotal(), dto.getTotal(), dto.getLimit());
                break;
            case MARKET:
                order = new Order(portfolio, dto.getOrderType(), dto.getOrderSide(), OrderStatus.ACTIVE,
                        dto.getExpiryDate(), baseFx, quoteFx, dto.getTotal(), dto.getTotal());
                break;
            default:
                throw new BadRequestException("Invalid order type");
        }

        return order;
    }

}
