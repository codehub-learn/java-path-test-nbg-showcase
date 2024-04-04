package gr.codelearn.eshop.service;

import gr.codelearn.eshop.domain.Product;
import gr.codelearn.eshop.exception.InvalidObjectValuesException;

import java.util.List;

public class ProductServiceImpl extends AbstractServiceImpl implements ProductService {
	@Override
	public boolean register(Product product) throws InvalidObjectValuesException, NullPointerException {
		if (product == null) {
			throw new NullPointerException("Product must not be null.");
		} else if (product.getSerial() == null || product.getName() == null || product.getPrice() == null) {
			throw new InvalidObjectValuesException("Product must not have any empty fields.");
		}
		return dataRepositoryService.save(product);
	}

	@Override
	public List<Product> getProducts() {
		return dataRepositoryService.getProducts();
	}

	@Override
	public Product getProduct(String serial) {
		return dataRepositoryService.getProduct(serial);
	}
}
