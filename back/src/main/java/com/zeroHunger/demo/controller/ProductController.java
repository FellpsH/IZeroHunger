package com.zeroHunger.demo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zeroHunger.demo.model.Product;
import com.zeroHunger.demo.model.Usuario;
import com.zeroHunger.demo.repository.ProductRepository;
import com.zeroHunger.demo.repository.UsuarioRepository;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "*") // Aceitar requisições de qualquer origem
@RequestMapping("/api/products")
public class ProductController {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @GetMapping
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Product createProduct(
            @RequestPart("product") String productJson,
            @RequestPart(value = "image", required = false) MultipartFile imageFile,
            @RequestParam("userId") Long userId) throws Exception {

        // Converte o JSON para o objeto Product
        ObjectMapper objectMapper = new ObjectMapper();
        Product product = objectMapper.readValue(productJson, Product.class);

        // Busca o fornecedor pelo ID
        Optional<Usuario> optionalUsuario = usuarioRepository.findById(userId);
        if (optionalUsuario.isEmpty()) {
            throw new IllegalArgumentException("Fornecedor com o ID " + userId + " não encontrado");
        }
        Usuario fornecedor = optionalUsuario.get();

        // Associa o fornecedor ao produto
        product.setFornecedor(fornecedor);

        // Lógica para salvar a imagem em base64
        if (imageFile != null && !imageFile.isEmpty()) {
            // Converte a imagem para um array de bytes
            byte[] imageBytes = imageFile.getBytes();

            // Codifica os bytes em base64
            String base64Image = Base64.encodeBase64String(imageBytes);

            // Define a string base64 da imagem no produto
            product.setImage("data:" + imageFile.getContentType() + ";base64," + base64Image);
        }

        // Salva o produto no banco de dados
        return productRepository.save(product);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        return productRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody Product productDetails) {
        return productRepository.findById(id).map(product -> {
            product.setName(productDetails.getName());
            product.setDescription(productDetails.getDescription());
            product.setPrice(productDetails.getPrice());
            product.setQuantity(productDetails.getQuantity());
            product.setExpiryDate(productDetails.getExpiryDate());
            product.setCategories(productDetails.getCategories());
            product.setLocation(productDetails.getLocation());
            return ResponseEntity.ok(productRepository.save(product));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteProduct(@PathVariable Long id) {
        return productRepository.findById(id).map(product -> {
            productRepository.delete(product);
            return ResponseEntity.noContent().build();
        }).orElse(ResponseEntity.notFound().build());
    }
}