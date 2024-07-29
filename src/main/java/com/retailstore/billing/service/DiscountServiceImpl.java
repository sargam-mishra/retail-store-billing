package com.retailstore.billing.service;

import com.retailstore.billing.entity.Bill;
import com.retailstore.billing.entity.Customer;
import com.retailstore.billing.entity.Product;
import com.retailstore.billing.enums.CustomerType;
import com.retailstore.billing.enums.ProductCategory;
import com.retailstore.billing.factory.DiscountStrategy;
import com.retailstore.billing.factory.DiscountStrategyFactory;
import com.retailstore.billing.factory.RegularCustomerDiscountStrategy;
import com.retailstore.billing.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class DiscountServiceImpl implements DiscountService{

    private BigDecimal billDiscountPer100 = new BigDecimal(5.00);

    private final CustomerRepository customerRepository;

    private final DiscountStrategyFactory strategyFactory;


    @Override
    public BigDecimal calculateFinalAmount(Bill bill) {
        BigDecimal discountAmount = BigDecimal.ZERO;
        BigDecimal totalAmount = bill.getPurchasedProducts().stream().map(Product::getPrice).reduce(BigDecimal.ZERO, BigDecimal::add);

        // Calculate bill-based discount
        discountAmount = discountAmount.add(calculateBillBasedDiscount(totalAmount));

        // Apply percentage-based discount if applicable
        discountAmount = discountAmount.add(calculatePercentageBasedDiscount(bill.getCustomerId(), bill.getPurchasedProducts().stream().filter(product -> product.getProductType().equals(ProductCategory.NON_GROCERY)).map(Product::getPrice).reduce(BigDecimal.ZERO, BigDecimal::add)));

       return totalAmount.subtract(discountAmount).setScale(2, RoundingMode.HALF_UP);

    }


    // Calculate percentage based discount
    private BigDecimal calculatePercentageBasedDiscount(Long customerId, BigDecimal amount) {

        Optional<Customer> customerOpt = customerRepository.findById(customerId);
        if(customerOpt.isPresent()){
            Customer customer = customerOpt.get();
            DiscountStrategy strategy = strategyFactory.getStrategy(customer.getCustomerType());
            if(strategy != null){
                if (customer.getCustomerType() == CustomerType.REGULAR) {
                    RegularCustomerDiscountStrategy regularStrategy = (RegularCustomerDiscountStrategy) strategy;
                    if (!regularStrategy.isEligible(customer.getJoinDate())) {
                        strategy = null; // No discount if not eligible
                    }
            }
                if(strategy != null){
                    BigDecimal percentageDiscountAmount = strategy.applyDiscount(amount);
                   return percentageDiscountAmount;
                }
            }
        }
      return BigDecimal.ZERO;
    }



    // Calculate amount based discount
    private BigDecimal calculateBillBasedDiscount(BigDecimal amount) {
        int hundredDollarChunks = amount.intValue() / 100;
        return billDiscountPer100.multiply(new BigDecimal(hundredDollarChunks));
    }
}
