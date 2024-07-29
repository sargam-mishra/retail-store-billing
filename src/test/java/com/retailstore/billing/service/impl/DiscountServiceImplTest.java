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
import org.junit.jupiter.api.BeforeEach;
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
import java.util.ArrayList;
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


    private Customer employeeCustomer;
    private Customer affiliateCustomer;
    private Customer regularCustomer;
    private Product groceryProduct1;
    private Product groceryProduct2;
    private Product nonGroceryProduct1;
    private Product nonGroceryProduct2;

    List<Product> productList = new ArrayList<>();

    private Bill bill;

    @BeforeEach
    void setUp() {
        employeeCustomer = new Customer(1L, "Alice", CustomerType.EMPLOYEE, LocalDate.now().minusYears(3));
        affiliateCustomer =  new Customer(1L, "Alice", CustomerType.AFFILIATE, LocalDate.now().minusYears(3));
        regularCustomer = new Customer(1L, "Alice", CustomerType.REGULAR, LocalDate.now().minusYears(3));
        groceryProduct1 = new Product(1L, "Milk", new BigDecimal("250"), ProductCategory.GROCERY);
        groceryProduct2 = new Product(2L, "Bread", new BigDecimal("250"), ProductCategory.GROCERY);
        nonGroceryProduct1 = new Product(3L, "Cupboard", new BigDecimal("10000"), ProductCategory.NON_GROCERY);
        nonGroceryProduct2 = new Product(4L, "Sofa", new BigDecimal("10000"), ProductCategory.NON_GROCERY);

        productList.add(groceryProduct1);
        productList.add(groceryProduct2);
        productList.add(nonGroceryProduct1);
        productList.add(nonGroceryProduct2);

        bill = new Bill(1L,1L,productList );
    }



    // Calculate discount if the customer is EMPLOYEE
    @Test
    public void testEmployeeDiscount() {
        Mockito.when(customerRepository.findById(1L)).thenReturn(Optional.of(employeeCustomer));
        when(strategyFactory.getStrategy(any())).thenReturn(new EmployeeDiscountStrategy());
        BigDecimal finalAmount = discountServiceImpl.calculateFinalAmount(bill);
        Assertions.assertEquals(new BigDecimal("13475.00"), finalAmount); // 30% discount + bill-based discount
    }


    // Calculate discount if the customer is AFFILIATE
    @Test
    public void testAffiliateDiscount() {

        Mockito.when(customerRepository.findById(1L)).thenReturn(Optional.of(affiliateCustomer));
        when(strategyFactory.getStrategy(any())).thenReturn(new AffiliateDiscountStrategy());
        BigDecimal finalAmount = discountServiceImpl.calculateFinalAmount(bill);
        Assertions.assertEquals(new BigDecimal("17475.00"), finalAmount); // 10% discount + bill-based discount
    }

    // Calculate discount if the customer is REGULAR
    @Test
    public void testRegularUserDiscount() {

        Mockito.when(customerRepository.findById(1L)).thenReturn(Optional.of(regularCustomer));
        when(strategyFactory.getStrategy(any())).thenReturn(new RegularCustomerDiscountStrategy());
        BigDecimal finalAmount = discountServiceImpl.calculateFinalAmount(bill);
        Assertions.assertEquals(new BigDecimal("18475.00"), finalAmount); // 5% discount + bill-based discount
    }


    // Calculate discount if the customer is associated for less than 2 years with the retail store
    // Bill based discount.
    @Test
    public void testRegularUserDiscount_CustomerIsAssociatedForLessThanTwoYearsWithStore() {
        regularCustomer.setJoinDate(LocalDate.now().minusYears(1));
        Mockito.when(customerRepository.findById(1L)).thenReturn(Optional.of(regularCustomer));
        when(strategyFactory.getStrategy(any())).thenReturn(new RegularCustomerDiscountStrategy());
        BigDecimal finalAmount = discountServiceImpl.calculateFinalAmount(bill);
        Assertions.assertEquals(new BigDecimal("19475.00"), finalAmount); //  bill-based discount
    }


    // test cases for grocery items only
    // bill based discount
    @Test
    public void testRegularUserDiscount_CustomerIsAssociatedForLessThanTwoYearsWithStore_GroceryProductsOnly() {
        productList.clear();
        productList.add(groceryProduct1);
        productList.add(groceryProduct2);
        Bill bill = new Bill(1L,1L,productList);
        regularCustomer.setJoinDate(LocalDate.now().minusYears(1));
        Mockito.when(customerRepository.findById(1L)).thenReturn(Optional.of(regularCustomer));
        when(strategyFactory.getStrategy(any())).thenReturn(new RegularCustomerDiscountStrategy());
        BigDecimal finalAmount = discountServiceImpl.calculateFinalAmount(bill);
        Assertions.assertEquals(new BigDecimal("475.00"), finalAmount); // 5% discount + bill-based discount
    }


    // test cases for non-grocery items only
    // percentage based discount
    @Test
    public void testRegularUserDiscount_CustomerIsAssociatedForLessThanTwoYearsWithStore_NonGroceryProductsOnly() {
        productList.clear();
        productList.add(nonGroceryProduct1);
        productList.add(nonGroceryProduct2);
        Bill bill = new Bill(1L,1L,productList);
        regularCustomer.setJoinDate(LocalDate.now().minusYears(3));
        Mockito.when(customerRepository.findById(1L)).thenReturn(Optional.of(regularCustomer));
        when(strategyFactory.getStrategy(any())).thenReturn(new RegularCustomerDiscountStrategy());
        BigDecimal finalAmount = discountServiceImpl.calculateFinalAmount(bill);
        Assertions.assertEquals(new BigDecimal("18000.00"), finalAmount); // 5% discount + bill-based discount
    }


    // A bill with no products
    @Test
    public void testRegularUserDiscount_BillWithNoProduct() {
        productList.clear();
        Bill bill = new Bill(1L,1L,productList);
        regularCustomer.setJoinDate(LocalDate.now().minusYears(3));
        Mockito.when(customerRepository.findById(1L)).thenReturn(Optional.of(regularCustomer));
        when(strategyFactory.getStrategy(any())).thenReturn(new RegularCustomerDiscountStrategy());
        BigDecimal finalAmount = discountServiceImpl.calculateFinalAmount(bill);
        Assertions.assertEquals(new BigDecimal("0.00"), finalAmount);
    }



}
