package com.retailstore.billing.service.impl;

import com.retailstore.billing.entity.Bill;
import com.retailstore.billing.entity.Customer;
import com.retailstore.billing.entity.Product;
import com.retailstore.billing.enums.CustomerType;
import com.retailstore.billing.enums.ProductCategory;
import com.retailstore.billing.factory.AffiliateDiscountStrategy;
import com.retailstore.billing.factory.DiscountStrategyFactory;
import com.retailstore.billing.factory.EmployeeDiscountStrategy;
import com.retailstore.billing.factory.RegularCustomerDiscountStrategy;
import com.retailstore.billing.repository.CustomerRepository;
import com.retailstore.billing.service.DiscountServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class DiscountServiceImplTest {


    @InjectMocks
    private DiscountServiceImpl discountServiceImpl;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private DiscountStrategyFactory strategyFactory;



    @Test
    public void testEmployeeDiscount() {
        Customer user = new Customer(1L, "Alice", CustomerType.EMPLOYEE, LocalDate.now().minusYears(3));
        Product product1 = new Product(1L, "Milk", new BigDecimal("250"), ProductCategory.GROCERY);
        Product product2 = new Product(2L, "Bread", new BigDecimal("250"), ProductCategory.GROCERY);
        Product product3 = new Product(3L, "Cupboard", new BigDecimal("10000"), ProductCategory.NON_GROCERY);
        Product product4 = new Product(4L, "Sofa", new BigDecimal("10000"), ProductCategory.NON_GROCERY);
        List<Product> productList = List.of(product1,product2,product3,product4);

        Bill bill = new Bill(1L,1L,productList );
        Mockito.when(customerRepository.findById(1L)).thenReturn(Optional.of(user));
        when(strategyFactory.getStrategy(any())).thenReturn(new EmployeeDiscountStrategy());
        BigDecimal finalAmount = discountServiceImpl.calculateFinalAmount(bill);
        Assertions.assertEquals(new BigDecimal("13475.00"), finalAmount); // 30% discount + bill-based discount
    }

    @Test
    public void testAffiliateDiscount() {
        Customer user = new Customer(1L, "Alice", CustomerType.AFFILIATE, LocalDate.now().minusYears(3));
        Product product1 = new Product(1L, "Milk", new BigDecimal("250"), ProductCategory.GROCERY);
        Product product2 = new Product(2L, "Bread", new BigDecimal("250"), ProductCategory.GROCERY);
        Product product3 = new Product(3L, "Cupboard", new BigDecimal("10000"), ProductCategory.NON_GROCERY);
        Product product4 = new Product(4L, "Sofa", new BigDecimal("10000"), ProductCategory.NON_GROCERY);
        List<Product> productList = List.of(product1,product2,product3,product4);

        Bill bill = new Bill(1L, 1L,productList );
        Mockito.when(customerRepository.findById(1L)).thenReturn(Optional.of(user));
        when(strategyFactory.getStrategy(any())).thenReturn(new AffiliateDiscountStrategy());
        BigDecimal finalAmount = discountServiceImpl.calculateFinalAmount(bill);
        Assertions.assertEquals(new BigDecimal("17475.00"), finalAmount); // 10% discount + bill-based discount
    }

    @Test
    public void testRegularUserDiscount() {
        Customer user = new Customer(1L, "Alice", CustomerType.REGULAR, LocalDate.now().minusYears(3));
        Product product1 = new Product(1L, "Milk", new BigDecimal("250"), ProductCategory.GROCERY);
        Product product2 = new Product(2L, "Bread", new BigDecimal("250"), ProductCategory.GROCERY);
        Product product3 = new Product(3L, "Cupboard", new BigDecimal("10000"), ProductCategory.NON_GROCERY);
        Product product4 = new Product(4L, "Sofa", new BigDecimal("10000"), ProductCategory.NON_GROCERY);
        List<Product> productList = List.of(product1,product2,product3,product4);

        Bill bill = new Bill(1L,1L,productList );
        Mockito.when(customerRepository.findById(1L)).thenReturn(Optional.of(user));
        when(strategyFactory.getStrategy(any())).thenReturn(new RegularCustomerDiscountStrategy());
        BigDecimal finalAmount = discountServiceImpl.calculateFinalAmount(bill);
        Assertions.assertEquals(new BigDecimal("18475.00"), finalAmount); // 5% discount + bill-based discount
    }


    @Test
    public void testRegularUserDiscount_CustomerIsAssociatedForLessThanTwoYearsWithStore() {
        Customer user = new Customer(1L, "Alice", CustomerType.REGULAR, LocalDate.now().minusYears(1));
        Product product1 = new Product(1L, "Milk", new BigDecimal("250"), ProductCategory.GROCERY);
        Product product2 = new Product(2L, "Bread", new BigDecimal("250"), ProductCategory.GROCERY);
        Product product3 = new Product(3L, "Cupboard", new BigDecimal("10000"), ProductCategory.NON_GROCERY);
        Product product4 = new Product(4L, "Sofa", new BigDecimal("10000"), ProductCategory.NON_GROCERY);
        List<Product> productList = List.of(product1,product2,product3,product4);

        Bill bill = new Bill(1L,1L,productList );
        Mockito.when(customerRepository.findById(1L)).thenReturn(Optional.of(user));
        when(strategyFactory.getStrategy(any())).thenReturn(new RegularCustomerDiscountStrategy());
        BigDecimal finalAmount = discountServiceImpl.calculateFinalAmount(bill);
        Assertions.assertEquals(new BigDecimal("19475.00"), finalAmount); // 5% discount + bill-based discount
    }



}
