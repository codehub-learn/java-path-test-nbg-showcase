package gr.codelearn.eshop.service;

import gr.codelearn.eshop.EshopDemo;
import gr.codelearn.eshop.domain.Customer;
import gr.codelearn.eshop.domain.Product;

public class ReportServiceImpl extends AbstractServiceImpl implements ReportService {
	@Override
	public void totalNumberAndCostOfPurchases(Customer customer) {
		if (customer != null) {
			logger.info("Total number and cost of purchases for Customer[{}]", customer.getEmail());
			dataRepositoryService.totalNumberAndCostOfPurchases(customer);
		}
	}

	@Override
	public void totalNumberAndCostOfPurchases(Product product) {
		if (product != null) {
			logger.info("Total number and cost of purchases for Product[{}]", product.getSerial());
			dataRepositoryService.totalNumberAndCostOfPurchases(product);
		}
	}

	@Override
	public void totalNumberAndCostOfPurchasesPerCustomer() {
		logger.info("Total number and cost of purchases per customer");
		dataRepositoryService.logData(EshopDemo.sqlCommands.getProperty("reports.003"));
	}

	@Override
	public void totalNumberAndCostOfPurchasesPerCustomerCategory() {
		logger.info("Total number and cost of purchases per customer category");
		dataRepositoryService.logData(EshopDemo.sqlCommands.getProperty("reports.004"));
	}

	@Override
	public void totalNumberAndCostOfPurchasesPerPaymentMethod() {
		logger.info("Total number and cost of purchases per payment method");
		dataRepositoryService.logData(EshopDemo.sqlCommands.getProperty("reports.005"));
	}

	@Override
	public void averageOrderCost() {
		logger.info("Average order cost");
		dataRepositoryService.logData(EshopDemo.sqlCommands.getProperty("reports.006"));
	}

	@Override
	public void averageOrderCostPerCustomer() {
		logger.info("Average order cost per customer");
		dataRepositoryService.logData(EshopDemo.sqlCommands.getProperty("reports.007"));
	}

	@Override
	public void getCustomersPurchasedMostExpensiveProduct() {
		logger.info("Customers who purchased most expensive product and how many times");
		dataRepositoryService.logData(EshopDemo.sqlCommands.getProperty("reports.008"));
	}
}
