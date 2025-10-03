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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes para CartService - Demonstrando Mockito Avançado")
class CartServiceTest {

    @Mock
    private CartRepository cartRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private UsuarioService usuarioService;

    @Mock
    private CartItemRepository cartItemRepository;

    @InjectMocks
    private CartService cartService;

    private Cart cart;
    private Product product;
    private Usuario usuario;
    private CompraRequest compraRequest;

    @BeforeEach
    void setUp() {
        // Setup do usuário
        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setEmail("teste@email.com");
        usuario.setNome("Teste Usuario");

        // Setup do produto
        product = new Product();
        product.setId(1L);
        product.setName("Produto Teste");
        product.setPrice(10.50);
        product.setQuantity(100);

        // Setup do carrinho
        cart = new Cart();
        cart.setId(1L);
        cart.setUser(usuario);
        cart.setItems(new ArrayList<>());

        // Setup da requisição de compra
        CartItemRequest itemRequest = new CartItemRequest();
        itemRequest.setProductId(1L);
        itemRequest.setQuantity(2);

        compraRequest = new CompraRequest();
        compraRequest.setUserId(1L);
        compraRequest.setTotalPrice(BigDecimal.valueOf(21.00));
        compraRequest.setItems(Arrays.asList(itemRequest));
    }

    @Test
    @DisplayName("Deve adicionar item ao carrinho com sucesso")
    void deveAdicionarItemAoCarrinhoComSucesso() {
        // Given
        Long cartId = 1L;
        Integer quantity = 2;
        
        when(cartRepository.findById(cartId)).thenReturn(Optional.of(cart));
        when(cartRepository.save(any(Cart.class))).thenReturn(cart);

        // When
        Cart resultado = cartService.addItemToCart(cartId, product, quantity);

        // Then
        assertNotNull(resultado);
        assertEquals(1, resultado.getItems().size());
        
        CartItem itemAdicionado = resultado.getItems().get(0);
        assertEquals(product, itemAdicionado.getProduct());
        assertEquals(quantity, itemAdicionado.getSelectedQuantity());
        assertEquals(cart, itemAdicionado.getCart());

        // Verificações com Mockito
        verify(cartRepository).findById(cartId);
        verify(cartRepository).save(cart);
    }

    @Test
    @DisplayName("Deve criar novo carrinho quando não existe")
    void deveCriarNovoCarrinhoQuandoNaoExiste() {
        // Given
        Long cartIdInexistente = 999L;
        Integer quantity = 1;
        Cart novoCart = new Cart();
        novoCart.setItems(new ArrayList<>());
        
        when(cartRepository.findById(cartIdInexistente)).thenReturn(Optional.empty());
        when(cartRepository.save(any(Cart.class))).thenReturn(novoCart);

        // When
        Cart resultado = cartService.addItemToCart(cartIdInexistente, product, quantity);

        // Then
        assertNotNull(resultado);
        verify(cartRepository).findById(cartIdInexistente);
        verify(cartRepository).save(any(Cart.class));
    }

    @Test
    @DisplayName("Deve registrar compra com sucesso - Testando ArgumentCaptor")
    void deveRegistrarCompraComSucesso() {
        // Given
        when(usuarioService.buscarUsuarioPorId(1L)).thenReturn(Optional.of(usuario));
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(cartRepository.save(any(Cart.class))).thenReturn(cart);

        // ArgumentCaptor para capturar o objeto Cart que foi salvo
        ArgumentCaptor<Cart> cartCaptor = ArgumentCaptor.forClass(Cart.class);

        // When
        cartService.registerPurchase(compraRequest);

        // Then
        verify(usuarioService).buscarUsuarioPorId(1L);
        verify(productRepository).findById(1L);
        verify(cartRepository).save(cartCaptor.capture());

        // Verificar o objeto Cart capturado
        Cart cartSalvo = cartCaptor.getValue();
        assertNotNull(cartSalvo);
        assertEquals(usuario, cartSalvo.getUser());
        assertEquals(BigDecimal.valueOf(21.00), cartSalvo.getTotalPrice());
        assertNotNull(cartSalvo.getPurchaseDate());
        assertEquals(1, cartSalvo.getItems().size());

        // Verificar o item do carrinho
        CartItem item = cartSalvo.getItems().get(0);
        assertEquals(product, item.getProduct());
        assertEquals(2, item.getSelectedQuantity());
        assertEquals(cartSalvo, item.getCart());
    }

    @Test
    @DisplayName("Deve lançar exceção quando usuário não existe")
    void deveLancarExcecaoQuandoUsuarioNaoExiste() {
        // Given
        when(usuarioService.buscarUsuarioPorId(1L)).thenReturn(Optional.empty());

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, 
            () -> cartService.registerPurchase(compraRequest));
        
        assertEquals("Usuário não encontrado", exception.getMessage());
        
        verify(usuarioService).buscarUsuarioPorId(1L);
        verify(productRepository, never()).findById(anyLong());
        verify(cartRepository, never()).save(any(Cart.class));
    }

    @Test
    @DisplayName("Deve lançar exceção quando produto não existe")
    void deveLancarExcecaoQuandoProdutoNaoExiste() {
        // Given
        when(usuarioService.buscarUsuarioPorId(1L)).thenReturn(Optional.of(usuario));
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, 
            () -> cartService.registerPurchase(compraRequest));
        
        assertEquals("Produto não encontrado: ID 1", exception.getMessage());
        
        verify(usuarioService).buscarUsuarioPorId(1L);
        verify(productRepository).findById(1L);
        verify(cartRepository, never()).save(any(Cart.class));
    }

    @Test
    @DisplayName("Deve verificar ordem de chamadas dos métodos")
    void deveVerificarOrdemDeChamadasDosMetodos() {
        // Given
        when(usuarioService.buscarUsuarioPorId(1L)).thenReturn(Optional.of(usuario));
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(cartRepository.save(any(Cart.class))).thenReturn(cart);

        // When
        cartService.registerPurchase(compraRequest);

        // Then - Verificar ordem das chamadas
        var inOrder = inOrder(usuarioService, productRepository, cartRepository);
        inOrder.verify(usuarioService).buscarUsuarioPorId(1L);
        inOrder.verify(productRepository).findById(1L);
        inOrder.verify(cartRepository).save(any(Cart.class));
    }

    @Test
    @DisplayName("Deve verificar número exato de interações")
    void deveVerificarNumeroExatoDeInteracoes() {
        // Given
        when(usuarioService.buscarUsuarioPorId(1L)).thenReturn(Optional.of(usuario));
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(cartRepository.save(any(Cart.class))).thenReturn(cart);

        // When
        cartService.registerPurchase(compraRequest);

        // Then - Verificar número exato de chamadas
        verify(usuarioService, times(1)).buscarUsuarioPorId(1L);
        verify(productRepository, times(1)).findById(1L);
        verify(cartRepository, times(1)).save(any(Cart.class));
        
        // Verificar que não houve outras interações
        verifyNoMoreInteractions(usuarioService, productRepository, cartRepository);
    }

    @Test
    @DisplayName("Deve testar comportamento com múltiplos itens")
    void deveTestarComportamentoComMultiplosItens() {
        // Given
        Product produto2 = new Product();
        produto2.setId(2L);
        produto2.setName("Produto 2");
        produto2.setPrice(5.00);

        CartItemRequest item1 = new CartItemRequest();
        item1.setProductId(1L);
        item1.setQuantity(2);

        CartItemRequest item2 = new CartItemRequest();
        item2.setProductId(2L);
        item2.setQuantity(3);

        CompraRequest compraMultipla = new CompraRequest();
        compraMultipla.setUserId(1L);
        compraMultipla.setTotalPrice(BigDecimal.valueOf(36.00));
        compraMultipla.setItems(Arrays.asList(item1, item2));

        when(usuarioService.buscarUsuarioPorId(1L)).thenReturn(Optional.of(usuario));
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productRepository.findById(2L)).thenReturn(Optional.of(produto2));
        when(cartRepository.save(any(Cart.class))).thenReturn(cart);

        ArgumentCaptor<Cart> cartCaptor = ArgumentCaptor.forClass(Cart.class);

        // When
        cartService.registerPurchase(compraMultipla);

        // Then
        verify(cartRepository).save(cartCaptor.capture());
        Cart cartSalvo = cartCaptor.getValue();
        
        assertEquals(2, cartSalvo.getItems().size());
        assertEquals(BigDecimal.valueOf(36.00), cartSalvo.getTotalPrice());
        
        // Verificar que ambos os produtos foram buscados
        verify(productRepository).findById(1L);
        verify(productRepository).findById(2L);
    }
}
