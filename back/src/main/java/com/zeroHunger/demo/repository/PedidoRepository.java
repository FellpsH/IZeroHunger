package com.zeroHunger.demo.repository;

import com.zeroHunger.demo.model.Pedido;
import com.zeroHunger.demo.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    
    // Buscar pedido pelo ID único
    Optional<Pedido> findByPedidoId(String pedidoId);
    
    // Buscar pedidos por usuário
    List<Pedido> findByUsuarioOrderByDataPedidoDesc(Usuario usuario);
    
    // Buscar pedidos por usuário ID
    List<Pedido> findByUsuarioIdOrderByDataPedidoDesc(Long usuarioId);
    
    // Buscar pedidos por status
    List<Pedido> findByStatusOrderByDataPedidoDesc(Pedido.StatusPedido status);
    
    // Buscar pedidos por usuário e status
    List<Pedido> findByUsuarioAndStatusOrderByDataPedidoDesc(Usuario usuario, Pedido.StatusPedido status);
    
    // Contar pedidos por usuário
    long countByUsuario(Usuario usuario);
    
    // Buscar pedidos com itens (fetch join para evitar N+1)
    @Query("SELECT p FROM Pedido p LEFT JOIN FETCH p.itens WHERE p.id = :id")
    Optional<Pedido> findByIdWithItens(@Param("id") Long id);
    
    // Buscar pedidos com itens por pedidoId
    @Query("SELECT p FROM Pedido p LEFT JOIN FETCH p.itens WHERE p.pedidoId = :pedidoId")
    Optional<Pedido> findByPedidoIdWithItens(@Param("pedidoId") String pedidoId);
    
    // Buscar pedidos de um usuário com itens
    @Query("SELECT p FROM Pedido p LEFT JOIN FETCH p.itens WHERE p.usuario.id = :usuarioId ORDER BY p.dataPedido DESC")
    List<Pedido> findByUsuarioIdWithItens(@Param("usuarioId") Long usuarioId);
}
