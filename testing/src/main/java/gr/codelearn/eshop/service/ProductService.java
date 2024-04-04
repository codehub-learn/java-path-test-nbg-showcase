package gr.codelearn.eshop.service;

import gr.codelearn.eshop.domain.Product;
import gr.codelearn.eshop.exception.InvalidObjectValuesException;

import java.util.List;

public interface ProductService {
	boolean register(Product product) throws InvalidObjectValuesException, NullPointerException;

	List<Product> getProducts();

	Product getProduct(String serial);
}
