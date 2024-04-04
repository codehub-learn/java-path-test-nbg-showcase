package gr.codelearn.eshop.service;

import gr.codelearn.eshop.domain.Customer;
import gr.codelearn.eshop.domain.Product;

public interface ReportService {
	void totalNumberAndCostOfPurchases(Customer customer);

	void totalNumberAndCostOfPurchases(Product product);

	void totalNumberAndCostOfPurchasesPerCustomer();

	void totalNumberAndCostOfPurchasesPerCustomerCategory();

	void totalNumberAndCostOfPurchasesPerPaymentMethod();

	void averageOrderCost();

	void averageOrderCostPerCustomer();

	void getCustomersPurchasedMostExpensiveProduct();

}
