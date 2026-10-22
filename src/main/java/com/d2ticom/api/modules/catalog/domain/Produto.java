package com.d2ticom.api.modules.catalog.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "produto")
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "codigo_barras", length = 50)
    private String codigoBarras;

    @Column(nullable = false, length = 150)
    private String descricao;

    @Column(nullable = false, length = 10)
    private String unidade = "UN";

    @Column(name = "preco_custo", nullable = false, precision = 15, scale = 2)
    private BigDecimal precoCusto = BigDecimal.ZERO;

    @Column(name = "preco_venda", nullable = false, precision = 15, scale = 2)
    private BigDecimal precoVenda;

    @Column(name = "estoque_saldo", nullable = false, precision = 15, scale = 3)
    private BigDecimal estoqueSaldo = BigDecimal.ZERO;

    @Column(nullable = false)
    private Boolean ativo = true;

    public Produto() {}

    public Produto(Long id, String codigoBarras, String descricao, String unidade, 
                   BigDecimal precoCusto, BigDecimal precoVenda, BigDecimal estoqueSaldo, Boolean ativo) {
        this.id = id;
        this.codigoBarras = codigoBarras;
        this.descricao = descricao;
        this.unidade = unidade != null ? unidade : "UN";
        this.precoCusto = precoCusto != null ? precoCusto : BigDecimal.ZERO;
        this.precoVenda = precoVenda;
        this.estoqueSaldo = estoqueSaldo != null ? estoqueSaldo : BigDecimal.ZERO;
        this.ativo = ativo != null ? ativo : true;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCodigoBarras() { return codigoBarras; }
    public void setCodigoBarras(String codigoBarras) { this.codigoBarras = codigoBarras; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public String getUnidade() { return unidade; }
    public void setUnidade(String unidade) { this.unidade = unidade; }

    public BigDecimal getPrecoCusto() { return precoCusto; }
    public void setPrecoCusto(BigDecimal precoCusto) { this.precoCusto = precoCusto; }

    public BigDecimal getPrecoVenda() { return precoVenda; }
    public void setPrecoVenda(BigDecimal precoVenda) { this.precoVenda = precoVenda; }

    public BigDecimal getEstoqueSaldo() { return estoqueSaldo; }
    public void setEstoqueSaldo(BigDecimal estoqueSaldo) { this.estoqueSaldo = estoqueSaldo; }

    public Boolean getAtivo() { return ativo; }
    public void setAtivo(Boolean ativo) { this.ativo = ativo; }
}