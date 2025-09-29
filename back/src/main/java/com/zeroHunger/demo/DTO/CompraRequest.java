package com.zeroHunger.demo.DTO;

import java.math.BigDecimal;
import java.util.List;

public class CompraRequest {
    private Long userId;
    private List<CartItemRequest> items;
    private BigDecimal totalPrice;

    // Getters e Setters
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public List<CartItemRequest> getItems() {
        return items;
    }

    public void setItems(List<CartItemRequest> items) {
        this.items = items;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }
}

