package com.zeroHunger.demo.repository;


import com.zeroHunger.demo.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
}
