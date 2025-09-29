package com.zeroHunger.demo.controller;

import com.zeroHunger.demo.DTO.CompraRequest;
import com.zeroHunger.demo.exceptions.ResourceNotFoundException;
import com.zeroHunger.demo.model.Cart;
import com.zeroHunger.demo.model.Product;
import com.zeroHunger.demo.repository.ProductRepository;
import com.zeroHunger.demo.service.CartService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/carts")
public class CartController {

    private final CartService cartService;

    private ProductRepository productRepository;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping("/{cartId}/items")
    public ResponseEntity<Cart> addItemToCart(@PathVariable Long cartId, @RequestParam Long productId, @RequestParam Integer quantity) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado com ID: " + productId));

        Cart updatedCart = cartService.addItemToCart(cartId, product, quantity);
        return ResponseEntity.ok(updatedCart);
    }

    @PostMapping("/registraCompra")
    public ResponseEntity<String> registerPurchase(@RequestBody CompraRequest request) {
        try {
            cartService.registerPurchase(request);
            return ResponseEntity.ok("Compra realizada com sucesso.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erro ao realizar a compra: " + e.getMessage());
        }
    }

}
