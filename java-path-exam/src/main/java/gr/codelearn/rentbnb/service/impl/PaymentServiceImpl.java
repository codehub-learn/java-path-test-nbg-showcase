package gr.codelearn.rentbnb.service.impl;

import gr.codelearn.rentbnb.service.PaymentService;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;

@Slf4j
public class PaymentServiceImpl implements PaymentService {
	private BigDecimal balance = BigDecimal.valueOf(1000);

	@Override
	public boolean executePayment(BigDecimal cost) {
		BigDecimal newBalance = new BigDecimal(String.valueOf(balance.subtract(cost)));
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		if (newBalance.compareTo(BigDecimal.valueOf(0)) < 0) {
			return false;
		}
		log.info("Payment completed successfully.");
		log.info("Previous balance {}", balance);
		log.info("New balance {}", newBalance);
		balance = newBalance;
		return true;
	}

	@Override
	public boolean addMoney(BigDecimal value) {
		if (value.compareTo(BigDecimal.valueOf(0)) <= 0) {
			log.warn("Invalid amount");
			return false;
		}
		log.info("Payment returned successfully.");
		log.info("Previous balance {}", balance);
		balance = balance.add(value);
		log.info("New balance {}", balance);
		return true;
	}

	@Override
	public BigDecimal getBalance() {
		return balance;
	}
}
