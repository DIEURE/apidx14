package com.d2ticom.api.modules.sales.repository;

import com.d2ticom.api.modules.sales.domain.Pedido;
import com.d2ticom.api.modules.sales.domain.StatusPedido;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    List<Pedido> findByStatusOrderByCreatedAtDesc(StatusPedido status);
    
    List<Pedido> findByStatusAndCreatedAtBetween(
        StatusPedido status,
        LocalDateTime inicio,
        LocalDateTime fim
    );

    Optional<Pedido> findByNumeroOrcamento(String numeroOrcamento);

    Optional<Pedido> findByNumeroVenda(String numeroVenda);

    long countByStatusAndCreatedAtBetween(StatusPedido status, LocalDateTime inicio, LocalDateTime fim);

    long countByNumeroVendaStartingWith(String prefixo);
    
    long countByNumeroOrcamentoStartingWith(String prefixo);

}
