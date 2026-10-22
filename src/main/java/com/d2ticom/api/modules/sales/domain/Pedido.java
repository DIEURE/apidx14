package com.d2ticom.api.modules.sales.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "pedido")
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "numero_orcamento", unique = true, length = 20)
    private String numeroOrcamento;

    @Column(name = "numero_venda", unique = true, length = 20)
    private String numeroVenda;

    @Column(name = "numero_orcamento_origem", length = 20)
    private String numeroOrcamentoOrigem;

    @Column(name = "cliente_id")
    private Long clienteId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatusPedido status;

    @Enumerated(EnumType.STRING)
    @Column(name = "forma_pagamento", length = 30)
    private FormaPagamento formaPagamento;

    @Column(name = "valor_total", nullable = false, precision = 15, scale = 2)
    private BigDecimal valorTotal = BigDecimal.ZERO;

    @Column(name = "valor_desconto", precision = 15, scale = 2)
    private BigDecimal valorDesconto = BigDecimal.ZERO;

    @Column(name = "desconto_autorizado_por", length = 100)
    private String descontoAutorizadoPor;

    @Column(name = "desconto_autorizado_em")
    private LocalDateTime descontoAutorizadoEm;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemPedido> itens = new ArrayList<>();

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "finalizado_at")
    private LocalDateTime finalizadoAt;

    public Pedido() {}

    @PrePersist
    public void prePersist() {
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNumeroOrcamento() { return numeroOrcamento; }
    public void setNumeroOrcamento(String numeroOrcamento) { this.numeroOrcamento = numeroOrcamento; }

    public String getNumeroVenda() { return numeroVenda; }
    public void setNumeroVenda(String numeroVenda) { this.numeroVenda = numeroVenda; }

    public String getNumeroOrcamentoOrigem() { return numeroOrcamentoOrigem; }
    public void setNumeroOrcamentoOrigem(String numeroOrcamentoOrigem) { this.numeroOrcamentoOrigem = numeroOrcamentoOrigem; }

    public Long getClienteId() { return clienteId; }
    public void setClienteId(Long clienteId) { this.clienteId = clienteId; }

    public StatusPedido getStatus() { return status; }
    public void setStatus(StatusPedido status) { this.status = status; }

    public FormaPagamento getFormaPagamento() { return formaPagamento; }
    public void setFormaPagamento(FormaPagamento formaPagamento) { this.formaPagamento = formaPagamento; }

    public BigDecimal getValorTotal() { return valorTotal; }
    public void setValorTotal(BigDecimal valorTotal) { this.valorTotal = valorTotal; }

    public BigDecimal getValorDesconto() { return valorDesconto; }
    public void setValorDesconto(BigDecimal valorDesconto) { this.valorDesconto = valorDesconto; }

    public String getDescontoAutorizadoPor() { return descontoAutorizadoPor; }
    public void setDescontoAutorizadoPor(String descontoAutorizadoPor) { this.descontoAutorizadoPor = descontoAutorizadoPor; }

    public LocalDateTime getDescontoAutorizadoEm() { return descontoAutorizadoEm; }
    public void setDescontoAutorizadoEm(LocalDateTime descontoAutorizadoEm) { this.descontoAutorizadoEm = descontoAutorizadoEm; }

    public List<ItemPedido> getItens() { return itens; }
    public void setItens(List<ItemPedido> itens) { this.itens = itens; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getFinalizadoAt() { return finalizadoAt; }
    public void setFinalizadoAt(LocalDateTime finalizadoAt) { this.finalizadoAt = finalizadoAt; }

    @Transient
    public String getNumeroExibicao() {
        if (numeroVenda != null) return numeroVenda;
        return numeroOrcamento;
    }
}