package com.book.refactoring.ch6._11_priceorder;

public class BeforePriceOrder {
    public int priceOrder(Product product, int quantity, ShippingMethod shippingMethod) {
        // 한 함수에서 서로 다른 두 대상을 한꺼번에 다루는 중
        final int basePrice = product.basePrice() * quantity;
        final int discount = Math.max(quantity - product.discountThreshold(), 0);
        final int shippingPerCase = (basePrice > shippingMethod.discountThreshold())
                ? shippingMethod.discountedFee() : shippingMethod.feePerCase();
        final int shippingCost = quantity * shippingPerCase;
        final int price = basePrice - discount + shippingCost;
        return price;
    }

    record Product(int basePrice, int discountThreshold) {}

    record ShippingMethod(int discountThreshold, int discountedFee, int feePerCase) {}

}
