package com.book.refactoring.ch6._11_priceorder;

public class AfterPriceOrder {
    public int priceOrder(Product product, int quantity, ShippingMethod shippingMethod) {
        // 상품가격 계산 로직 / 배송비 계산 로직
        PriceData priceData = calculatePricingData(product, quantity);
        return applyShipping(priceData, shippingMethod);
    }

    private PriceData calculatePricingData(Product product, int quantity) {
        final int basePrice = product.basePrice() * quantity;
        final int discount = Math.max(quantity - product.discountThreshold(), 0);
        PriceData priceData = new PriceData(basePrice, quantity, discount);
        return priceData;
    }

    private int applyShipping(PriceData priceData, ShippingMethod shippingMethod) {
        final int shippingPerCase = (priceData.basePrice() > shippingMethod.discountThreshold())
                ? shippingMethod.discountedFee() : shippingMethod.feePerCase();
        final int shippingCost = priceData.quantity() * shippingPerCase;
        final int price = priceData.basePrice() - priceData.discount() + shippingCost;
        return price;
    }

    // 중간 레코드 추가
    record PriceData(int basePrice, int quantity, int discount) {}

    record Product(int basePrice, int discountThreshold) {}

    record ShippingMethod(int discountThreshold, int discountedFee, int feePerCase) {}

}
