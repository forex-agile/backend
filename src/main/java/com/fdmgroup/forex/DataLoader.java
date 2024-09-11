package com.fdmgroup.forex;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.fdmgroup.forex.enums.*;
import com.fdmgroup.forex.models.*;
import com.fdmgroup.forex.repos.*;
import com.fdmgroup.forex.services.*;

import jakarta.transaction.Transactional;

@Component
public class DataLoader implements ApplicationRunner {

	private static final String username = "sampleuser";
	private static final String hkdCode = "HKD";
	private static final String usdCode = "USD";

	@Value("${forex.default.role}")
	private String defaultRole;

	@Value("${forex.default.currency.code}")
	private String defaultCurrencyCode;

	@Value("${forex.default.currency.name}")
	private String defaultCurrencyName;

	@Value("${forex.create.sample.data}")
	private boolean doCreateSampleData;

	private PasswordEncoder pwdEncoder;

	private RoleRepo roleRepo;
	private UserRepo userRepo;
	private OrderRepo orderRepo;
	private CurrencyRepo currencyRepo;
	private PortfolioRepo portfolioRepo;
	private CurrencyService currencyService;
	private FxRateService fxRateService;

	public DataLoader(RoleRepo roleRepo, UserRepo userRepo, OrderRepo orderRepo, CurrencyRepo currencyRepo,
			PortfolioRepo portfolioRepo, CurrencyService currencyService, FxRateService fxRateService) {
		this.roleRepo = roleRepo;
		this.userRepo = userRepo;
		this.orderRepo = orderRepo;
		this.currencyRepo = currencyRepo;
		this.portfolioRepo = portfolioRepo;
		this.currencyService = currencyService;
		this.fxRateService = fxRateService;
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
		fetchCurrenciesAndFxRates();
		createDefaultRecordsIfNotExist();
		if (doCreateSampleData)
			createSampleData();
	}

	@Transactional
	private void createDefaultRecordsIfNotExist() {
		if (roleRepo.findByRole(defaultRole).isEmpty())
			roleRepo.save(new Role(defaultRole));
		if (currencyRepo.findById(defaultCurrencyCode).isEmpty())
			currencyRepo.save(new Currency(defaultCurrencyCode, defaultCurrencyName));
	}

	@Transactional
	private void fetchCurrenciesAndFxRates() {
		if (currencyRepo.count() == 0)
			currencyService.fetchAndCreateCurrencies();
		fxRateService.getUpdatedFxRates();
	}

	@Transactional
	private void createSampleData() {
		Role role = roleRepo.findByRole(defaultRole).get();

		Currency hkd = currencyRepo.findById(hkdCode).orElseGet(() -> {
			return currencyRepo.save(new Currency(hkdCode, "Hong Kong Dollar"));
		});
		Currency usd = currencyRepo.findById(usdCode).orElseGet(() -> {
			return currencyRepo.save(new Currency(usdCode, "US Dollar"));
		});

		User user = userRepo.findByUsername(username).orElseGet(() -> {
			return userRepo.save(new User(UUID.randomUUID(), username, "sampleuser@example.com",
					pwdEncoder.encode("sampleuserpassword"), hkd, "sample_bank_account", role));
		});

		Portfolio portfolio = portfolioRepo.findByUser_Id(user.getId()).orElseGet(() -> {
			return portfolioRepo.save(new Portfolio(user));
		});

		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.WEEK_OF_YEAR, 1);
		Date oneWeekFromNow = calendar.getTime();
		if (orderRepo.findByPortfolio_User_Id(user.getId()).size() == 0) {
			orderRepo.save(
					new Order(portfolio, OrderType.LIMIT, OrderSide.BUY, OrderStatus.ACTIVE, oneWeekFromNow, hkd, usd,
							7800, 7800, 1000));
		}
	}

}
