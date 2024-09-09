package com.fdmgroup.forex;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.fdmgroup.forex.enums.OrderSide;
import com.fdmgroup.forex.enums.OrderStatus;
import com.fdmgroup.forex.enums.OrderType;
import com.fdmgroup.forex.models.Currency;
import com.fdmgroup.forex.models.Order;
import com.fdmgroup.forex.models.Role;
import com.fdmgroup.forex.models.User;
import com.fdmgroup.forex.repos.*;

import jakarta.transaction.Transactional;

@Component
public class DataLoader implements ApplicationRunner {

	private static final String username = "sampleuser";
	private static final String hkdCode = "HKD";
	private static final String usdCode = "USD";

	@Value("${forex.default.role}")
	private String defaultRole;

	@Value("${forex.create.sample.data}")
	private boolean doCreateSampleData;

	private PasswordEncoder pwdEncoder;

	private RoleRepo roleRepo;
	private UserRepo userRepo;
	private OrderRepo orderRepo;
	private CurrencyRepo currencyRepo;

	public DataLoader(PasswordEncoder pwdEncoder, RoleRepo roleRepo, UserRepo userRepo, OrderRepo orderRepo,
			CurrencyRepo currencyRepo) {
		this.pwdEncoder = pwdEncoder;
		this.roleRepo = roleRepo;
		this.userRepo = userRepo;
		this.orderRepo = orderRepo;
		this.currencyRepo = currencyRepo;
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
		createRoles();
		if (doCreateSampleData)
			createSampleData();
	}

	@Transactional
	private void createRoles() {
		if (roleRepo.findByRole(defaultRole).isEmpty())
			roleRepo.save(new Role(defaultRole));
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

		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.WEEK_OF_YEAR, 1);
		Date oneWeekFromNow = calendar.getTime();
		if (orderRepo.findByUser_Id(user.getId()).size() == 0) {
			orderRepo.save(new Order(user, OrderType.LIMIT, OrderSide.BUY, OrderStatus.ACTIVE, oneWeekFromNow, hkd, usd,
					7800, 7800, 1000));
		}
	}

}
