package com.d2ticom.api.modules.stock.service;

import com.d2ticom.api.common.exception.ResourceNotFoundException;
import com.d2ticom.api.modules.catalog.domain.Produto;
import com.d2ticom.api.modules.catalog.repository.ProdutoRepository;
import com.d2ticom.api.modules.stock.domain.MovimentacaoEstoque;
import com.d2ticom.api.modules.stock.domain.TipoMovimentacao;
import com.d2ticom.api.modules.stock.repository.MovimentacaoEstoqueRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class EstoqueService {

    private final ProdutoRepository produtoRepository;
    private final MovimentacaoEstoqueRepository movimentacaoEstoqueRepository;

    public EstoqueService(ProdutoRepository produtoRepository,
                          MovimentacaoEstoqueRepository movimentacaoEstoqueRepository) {
        this.produtoRepository = produtoRepository;
        this.movimentacaoEstoqueRepository = movimentacaoEstoqueRepository;
    }

    @Transactional
    public void baixarEstoque(Long produtoId, BigDecimal quantidade, Long pedidoId) {
        Produto produto = produtoRepository.findById(produtoId)
            .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado ID: " + produtoId));

        if (produto.getEstoqueSaldo().compareTo(quantidade) < 0) {
            throw new IllegalStateException(
                "Estoque insuficiente para " + produto.getDescricao()
                + ". Disponível: " + produto.getEstoqueSaldo()
                + ", solicitado: " + quantidade
            );
        }

        produto.setEstoqueSaldo(produto.getEstoqueSaldo().subtract(quantidade));
        produtoRepository.save(produto);

        MovimentacaoEstoque mov = new MovimentacaoEstoque(
            produto, TipoMovimentacao.SAIDA_VENDA, quantidade, pedidoId
        );
        movimentacaoEstoqueRepository.save(mov);
    }

    @Transactional
    public void estornarEstoque(Long produtoId, BigDecimal quantidade, Long pedidoId) {
        Produto produto = produtoRepository.findById(produtoId)
            .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado ID: " + produtoId));

        produto.setEstoqueSaldo(produto.getEstoqueSaldo().add(quantidade));
        produtoRepository.save(produto);

        MovimentacaoEstoque mov = new MovimentacaoEstoque(
            produto, TipoMovimentacao.ESTORNO_CANCELAMENTO, quantidade, pedidoId
        );
        movimentacaoEstoqueRepository.save(mov);
    }
}