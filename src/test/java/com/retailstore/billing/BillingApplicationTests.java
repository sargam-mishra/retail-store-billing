package com.retailstore.billing;

import com.retailstore.billing.entity.Bill;
import com.retailstore.billing.entity.Customer;
import com.retailstore.billing.entity.Product;
import com.retailstore.billing.enums.CustomerType;
import com.retailstore.billing.enums.ProductCategory;
import com.retailstore.billing.repository.CustomerRepository;
import com.retailstore.billing.service.DiscountServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ComponentScan(basePackages = "com.retailstore")
class BillingApplicationTests {


	@Test
	void contextLoads() {
		assertTrue(true);
	}


}
