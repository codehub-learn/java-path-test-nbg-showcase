package gr.codelearn.rentbnb.service;

import java.math.BigDecimal;

public interface PaymentService {
	boolean executePayment(BigDecimal cost);

	boolean addMoney(BigDecimal value);

	BigDecimal getBalance();
}
