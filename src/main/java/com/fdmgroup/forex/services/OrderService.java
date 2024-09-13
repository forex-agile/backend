package com.fdmgroup.forex.services;

import java.util.*;

import org.springframework.stereotype.Service;

import com.fdmgroup.forex.enums.*;
import com.fdmgroup.forex.exceptions.*;
import com.fdmgroup.forex.facades.Matching;
import com.fdmgroup.forex.models.*;
import com.fdmgroup.forex.models.Currency;
import com.fdmgroup.forex.models.dto.SubmitSpotOrderDTO;
import com.fdmgroup.forex.repos.*;
import com.fdmgroup.forex.security.AuthUserService;

@Service
public class OrderService {

    private OrderRepo orderRepo;
    private PortfolioService portfolioService;
    private CurrencyRepo currencyRepo;
    private AuthUserService authUserService;
    private Matching matching;
    private AssetService assetService;

    public OrderService(OrderRepo orderRepo, PortfolioService portfolioService, CurrencyRepo currencyRepo,
			AuthUserService authUserService, Matching matching, AssetService assetService) {
		this.orderRepo = orderRepo;
		this.portfolioService = portfolioService;
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

        return matching.matchOrders(order);
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
        Portfolio portfolio = portfolioService.findPortfolioByUserId(authUser.getId());

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
    public List<Order> findOrdersByPortfolioId(UUID portfolioId) {
        List<Order> orders = orderRepo.findByPortfolio_Id(portfolioId);
        return orders;
    }

    public List<Order> findOrdersByPortfolioIdAndOrderStatus(UUID portfolioId, OrderStatus orderStatus) {
        List<Order> orders = orderRepo.findByPortfolio_IdAndOrderStatus(portfolioId, orderStatus);
        return orders;
    }

    public Order cancelOrderByUserIdAndOrderId(UUID userId, UUID orderId) throws RecordNotFoundException {
        Order order = findOrderById(orderId);
        Portfolio portfolio = portfolioService.findPortfolioByUserId(userId);
        if (portfoliosMatch(order, portfolio)) {
            order.setOrderStatus(OrderStatus.CANCELLED);
            return orderRepo.save(order);
        } else {
            throw new RecordNotFoundException("No order with ID " + orderId + " was found for user ID " + userId);
        }
    }

    public boolean portfoliosMatch(Order order, Portfolio portfolio) {
        return order.getPortfolio().getId() == portfolio.getId();
    }

}
