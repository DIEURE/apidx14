package com.d2ticom.api.modules.sales.controller;

import com.d2ticom.api.modules.sales.dto.FinalizacaoRequestDTO;
import com.d2ticom.api.modules.sales.dto.PedidoRequestDTO;
import com.d2ticom.api.modules.sales.dto.PedidoResponseDTO;
import com.d2ticom.api.modules.sales.service.PedidoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

    private final PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @PostMapping
    public ResponseEntity<PedidoResponseDTO> criarPedido(@RequestBody @Valid PedidoRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(pedidoService.criarPedido(dto));
    }

    @PatchMapping("/{id}/finalizar")
    public ResponseEntity<PedidoResponseDTO> finalizarOrcamento(
            @PathVariable Long id,
            @RequestBody(required = false) FinalizacaoRequestDTO dto) {
        BigDecimal desconto = (dto != null && dto.valorDesconto() != null)
            ? dto.valorDesconto()
            : BigDecimal.ZERO;
        String senha = dto != null ? dto.senhaAutorizacao() : null;
        return ResponseEntity.ok(pedidoService.finalizarOrcamento(id, desconto, senha));
    }

    @PatchMapping("/{id}/cancelar")
    public ResponseEntity<Void> cancelarPedido(@PathVariable Long id) {
        pedidoService.cancelarPedido(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PedidoResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(pedidoService.buscarPorId(id));
    }

    @GetMapping("/orcamentos")
    public ResponseEntity<List<PedidoResponseDTO>> listarOrcamentos() {
        return ResponseEntity.ok(pedidoService.listarOrcamentosPendentes());
    }
}