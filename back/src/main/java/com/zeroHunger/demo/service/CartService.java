package com.zeroHunger.demo.service;

import com.zeroHunger.demo.DTO.CartItemRequest;
import com.zeroHunger.demo.DTO.CompraRequest;
import com.zeroHunger.demo.model.Cart;
import com.zeroHunger.demo.model.CartItem;
import com.zeroHunger.demo.model.Product;
import com.zeroHunger.demo.model.Usuario;
import com.zeroHunger.demo.repository.CartItemRepository;
import com.zeroHunger.demo.repository.CartRepository;
import com.zeroHunger.demo.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UsuarioService usuarioRepository;

    private final CartItemRepository cartItemRepository;

    public CartService(CartRepository cartRepository, CartItemRepository cartItemRepository) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
    }

    public Cart addItemToCart(Long cartId, Product product, Integer quantity) {
        Cart cart = cartRepository.findById(cartId).orElse(new Cart());

        CartItem cartItem = new CartItem();
        cartItem.setProduct(product);
        cartItem.setSelectedQuantity(quantity);
        cartItem.setCart(cart);

        cart.getItems().add(cartItem);

        return cartRepository.save(cart);
    }


    public void registerPurchase(CompraRequest request) {
        // Verifica se o usuário existe
        Usuario user = usuarioRepository.buscarUsuarioPorId(request.getUserId())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        // Cria um novo carrinho
        Cart cart = new Cart();
        cart.setUser(user);
        cart.setPurchaseDate(LocalDateTime.now()); // Define a data da compra
        cart.setTotalPrice(request.getTotalPrice()); // Usa o valor enviado pelo front

        // Monta os itens do carrinho
        for (CartItemRequest itemRequest : request.getItems()) {
            Product product = productRepository.findById(itemRequest.getProductId())
                    .orElseThrow(() -> new RuntimeException("Produto não encontrado: ID " + itemRequest.getProductId()));

            CartItem cartItem = new CartItem();
            cartItem.setProduct(product);
            cartItem.setSelectedQuantity(itemRequest.getQuantity());
            cartItem.setCart(cart);

            cart.getItems().add(cartItem);
        }

        // Salva o carrinho e os itens
        cartRepository.save(cart);
    }

}

