package gr.codelearn.eshop.service.extension;

import gr.codelearn.eshop.domain.Customer;
import gr.codelearn.eshop.domain.CustomerCategory;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;

import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Slf4j
public class CustomerParameterResolver implements ParameterResolver {
    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        String type = parameterContext.getParameter().getParameterizedType().getTypeName();
        return Objects.equals("java.util.Map<java.lang.String, gr.codelearn.eshop.domain.Customer>", type);
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        Map<String, Customer>  customerMap = new HashMap<>();
        customerMap.put("Ioannis Daniil", Customer.builder("aidaniil@gmail.com", CustomerCategory.GOVERNMENT)
                .setFirstname("Ioannis").setLastname("Daniil").setAge(27).setAddress("Schiller Diec 17").build());
        customerMap.put("Ioannis Klian", Customer.builder("biklian@gmail.com", CustomerCategory.INDIVIDUAL).setFirstname(
                "Ioannis").setLastname("Klian").setAge(16).setAddress("New Illinois 1").build());
        customerMap.put("Constantinos Giannacoulis", Customer.builder("cgiannacoulis@gmail.com",
                CustomerCategory.BUSINESS).setFirstname(
                "Constantinos").setLastname("Giannacoulis").setAge(80).setAddress("Athens St. 1337").build());
        customerMap.put("Meletis Katsifis", Customer.builder("dmkatsifis@gmail.com", CustomerCategory.INDIVIDUAL)
                .setFirstname("Meletis").setLastname("Katsifis").setAge(68).setAddress("Leof. Alexandras 205").build());
        customerMap.put("SpongeBob SquarePants", Customer.builder("ssquarepants@gmail.com", CustomerCategory.GOVERNMENT)
                .setFirstname("SpongeBob").setLastname("SquarePants").setAge(22).setAddress("124 Conch Street")
                .build());
        return customerMap;
    }
}
