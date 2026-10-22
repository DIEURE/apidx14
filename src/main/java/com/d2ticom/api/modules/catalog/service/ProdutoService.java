package com.d2ticom.api.modules.catalog.service;

import com.d2ticom.api.common.exception.ResourceNotFoundException;
import com.d2ticom.api.modules.catalog.domain.Produto;
import com.d2ticom.api.modules.catalog.dto.ProdutoRequestDTO;
import com.d2ticom.api.modules.catalog.dto.ProdutoResponseDTO;
import com.d2ticom.api.modules.catalog.repository.ProdutoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ProdutoService {

    private final ProdutoRepository produtoRepository;

    public ProdutoService(ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
    }

    @Transactional
    public ProdutoResponseDTO salvar(ProdutoRequestDTO dto) {
        Produto produto = new Produto();
        produto.setCodigoBarras(dto.codigoBarras());
        produto.setDescricao(dto.descricao());
        produto.setUnidade(dto.unidade() != null ? dto.unidade() : "UN");
        produto.setPrecoCusto(dto.precoCusto() != null ? dto.precoCusto() : BigDecimal.ZERO);
        produto.setPrecoVenda(dto.precoVenda());
        produto.setEstoqueSaldo(dto.estoqueSaldo() != null ? dto.estoqueSaldo() : BigDecimal.ZERO);
        produto.setAtivo(dto.ativo() != null ? dto.ativo() : true);

        return toResponseDTO(produtoRepository.save(produto));
    }

    @Transactional
    public ProdutoResponseDTO atualizar(Long id, ProdutoRequestDTO dto) {
        Produto produto = produtoRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado ID: " + id));

        produto.setCodigoBarras(dto.codigoBarras());
        produto.setDescricao(dto.descricao());
        if (dto.unidade() != null) produto.setUnidade(dto.unidade());
        if (dto.precoCusto() != null) produto.setPrecoCusto(dto.precoCusto());
        if (dto.precoVenda() != null) produto.setPrecoVenda(dto.precoVenda());
        if (dto.estoqueSaldo() != null) produto.setEstoqueSaldo(dto.estoqueSaldo());
        if (dto.ativo() != null) produto.setAtivo(dto.ativo());

        return toResponseDTO(produtoRepository.save(produto));
    }

    @Transactional(readOnly = true)
    public ProdutoResponseDTO buscarPorId(Long id) {
        return produtoRepository.findById(id)
            .map(this::toResponseDTO)
            .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado ID: " + id));
    }

    @Transactional(readOnly = true)
    public ProdutoResponseDTO buscarPorCodigoBarras(String codigoBarras) {
        return produtoRepository.findByCodigoBarras(codigoBarras)
            .map(this::toResponseDTO)
            .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado com código de barras: " + codigoBarras));
    }

    @Transactional(readOnly = true)
    public List<ProdutoResponseDTO> buscarPorDescricao(String termo) {
        return produtoRepository.findByDescricaoContainingIgnoreCaseAndAtivoTrue(termo)
            .stream()
            .map(this::toResponseDTO)
            .toList();
    }

    @Transactional(readOnly = true)
    public List<ProdutoResponseDTO> listarTodos() {
        return produtoRepository.findAll()
            .stream()
            .map(this::toResponseDTO)
            .toList();
    }

    private ProdutoResponseDTO toResponseDTO(Produto p) {
        return new ProdutoResponseDTO(
            p.getId(),
            p.getCodigoBarras(),
            p.getDescricao(),
            p.getUnidade(),
            p.getPrecoCusto(),
            p.getPrecoVenda(),
            p.getEstoqueSaldo(),
            p.getAtivo()
        );
    }
}