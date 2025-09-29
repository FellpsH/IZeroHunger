package com.zeroHunger.demo.model;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "cart_items")
public class CartItem implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer selectedQuantity;

    // Referência ao produto adicionado ao carrinho
    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    // Relacionamento com o carrinho
    @ManyToOne
    @JoinColumn(name = "cart_id", nullable = false)
    private Cart cart;


    public CartItem() {}


    // Getters e setters


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getSelectedQuantity() {
        return selectedQuantity;
    }

    public void setSelectedQuantity(Integer selectedQuantity) {
        this.selectedQuantity = selectedQuantity;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }
}

