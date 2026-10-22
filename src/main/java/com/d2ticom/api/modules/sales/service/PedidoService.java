package com.d2ticom.api.modules.sales.service;

import com.d2ticom.api.common.exception.ResourceNotFoundException;
import com.d2ticom.api.common.exception.UnauthorizedException;
import com.d2ticom.api.modules.catalog.domain.Produto;
import com.d2ticom.api.modules.catalog.repository.ProdutoRepository;
import com.d2ticom.api.modules.customers.service.PessoaService;
import com.d2ticom.api.modules.sales.domain.ItemPedido;
import com.d2ticom.api.modules.sales.domain.Pedido;
import com.d2ticom.api.modules.sales.domain.StatusPedido;
import com.d2ticom.api.modules.sales.dto.ItemPedidoDTO;
import com.d2ticom.api.modules.sales.dto.PedidoRequestDTO;
import com.d2ticom.api.modules.sales.dto.PedidoResponseDTO;
import com.d2ticom.api.modules.sales.repository.PedidoRepository;
import com.d2ticom.api.modules.stock.service.EstoqueService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final ProdutoRepository produtoRepository;
    private final EstoqueService estoqueService;

    @Value("${pdv.desconto.limite-percentual:5}")
    private BigDecimal limitePercentualDesconto;

    @Value("${pdv.desconto.senha-gerente:}")
    private String senhaGerenteHash;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public PedidoService(PedidoRepository pedidoRepository,
                         ProdutoRepository produtoRepository,
                         EstoqueService estoqueService) {
        this.pedidoRepository = pedidoRepository;
        this.produtoRepository = produtoRepository;
        this.estoqueService = estoqueService;
    }

    @Transactional
    public PedidoResponseDTO criarPedido(PedidoRequestDTO dto) {
        if (dto.itens() == null || dto.itens().isEmpty()) {
            throw new IllegalArgumentException("O pedido precisa conter ao menos um item.");
        }

        StatusPedido status = dto.status() != null ? dto.status() : StatusPedido.ORCAMENTO;

        Pedido pedido = new Pedido();
        pedido.setClienteId(
            dto.clienteId() != null ? dto.clienteId() : PessoaService.CONSUMIDOR_FINAL_ID
        );
        pedido.setStatus(status);
        pedido.setFormaPagamento(dto.formaPagamento());

        if (status == StatusPedido.ORCAMENTO) {
            pedido.setNumeroOrcamento(gerarNumeroOrcamento());
        } else if (status == StatusPedido.FINALIZADO) {
            pedido.setNumeroVenda(gerarNumeroVendaDireta());
        }

        BigDecimal totalBruto = BigDecimal.ZERO;
        List<ItemPedido> itens = new ArrayList<>();

        for (ItemPedidoDTO itemDto : dto.itens()) {
            Produto produto = produtoRepository.findById(itemDto.produtoId())
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado ID: " + itemDto.produtoId()));

            BigDecimal subtotal = itemDto.precoUnitario().multiply(itemDto.quantidade());
            totalBruto = totalBruto.add(subtotal);

            ItemPedido item = new ItemPedido(pedido, produto, itemDto.quantidade(), itemDto.precoUnitario(), subtotal);
            itens.add(item);
        }

        pedido.setItens(itens);

        BigDecimal desconto = dto.valorDesconto() != null ? dto.valorDesconto() : BigDecimal.ZERO;
        boolean autorizadoPorSenha = validarDesconto(totalBruto, desconto, dto.senhaAutorizacao());

        if (autorizadoPorSenha) {
            pedido.setDescontoAutorizadoPor("GERENTE");
            pedido.setDescontoAutorizadoEm(LocalDateTime.now());
        }

        pedido.setValorDesconto(desconto);
        pedido.setValorTotal(totalBruto.subtract(desconto));

        if (status == StatusPedido.FINALIZADO) {
            pedido.setFinalizadoAt(LocalDateTime.now());
            pedidoRepository.save(pedido);

            for (ItemPedido item : pedido.getItens()) {
                estoqueService.baixarEstoque(item.getProduto().getId(), item.getQuantidade(), pedido.getId());
            }
        } else {
            pedidoRepository.save(pedido);
        }

        return toResponseDTO(pedido);
    }

    @Transactional
    public PedidoResponseDTO finalizarOrcamento(Long pedidoId, BigDecimal valorDesconto, String senhaAutorizacao) {
        Pedido pedido = pedidoRepository.findById(pedidoId)
            .orElseThrow(() -> new ResourceNotFoundException("Pedido não encontrado ID: " + pedidoId));

        if (pedido.getStatus() == StatusPedido.FINALIZADO) {
            throw new IllegalStateException("Pedido já se encontra finalizado.");
        }

        if (pedido.getStatus() == StatusPedido.CANCELADO) {
            throw new IllegalStateException("Não é possível finalizar um pedido cancelado.");
        }

        BigDecimal totalBruto = pedido.getItens().stream()
            .map(ItemPedido::getSubtotal)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal desconto = valorDesconto != null ? valorDesconto : BigDecimal.ZERO;
        boolean autorizadoPorSenha = validarDesconto(totalBruto, desconto, senhaAutorizacao);

        if (autorizadoPorSenha) {
            pedido.setDescontoAutorizadoPor("GERENTE");
            pedido.setDescontoAutorizadoEm(LocalDateTime.now());
        }

        pedido.setValorDesconto(desconto);
        pedido.setValorTotal(totalBruto.subtract(desconto));

        String numeroOrcamento = pedido.getNumeroOrcamento();
        if (numeroOrcamento != null) {
            pedido.setNumeroOrcamentoOrigem(numeroOrcamento);
            pedido.setNumeroVenda(numeroOrcamento.replaceFirst("^PED-", "VND-"));
        } else {
            pedido.setNumeroVenda(gerarNumeroVendaDireta());
        }

        for (ItemPedido item : pedido.getItens()) {
            estoqueService.baixarEstoque(item.getProduto().getId(), item.getQuantidade(), pedido.getId());
        }

        pedido.setStatus(StatusPedido.FINALIZADO);
        pedido.setFinalizadoAt(LocalDateTime.now());
        pedidoRepository.save(pedido);

        return toResponseDTO(pedido);
    }

    @Transactional
    public void cancelarPedido(Long pedidoId) {
        Pedido pedido = pedidoRepository.findById(pedidoId)
            .orElseThrow(() -> new ResourceNotFoundException("Pedido não encontrado ID: " + pedidoId));

        if (pedido.getStatus() == StatusPedido.FINALIZADO) {
            for (ItemPedido item : pedido.getItens()) {
                estoqueService.estornarEstoque(item.getProduto().getId(), item.getQuantidade(), pedido.getId());
            }
        }

        pedido.setStatus(StatusPedido.CANCELADO);
        pedidoRepository.save(pedido);
    }

    @Transactional(readOnly = true)
    public PedidoResponseDTO buscarPorId(Long id) {
        Pedido pedido = pedidoRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Pedido não encontrado ID: " + id));
        return toResponseDTO(pedido);
    }

    @Transactional(readOnly = true)
    public List<PedidoResponseDTO> listarOrcamentosPendentes() {
        return pedidoRepository.findByStatusOrderByCreatedAtDesc(StatusPedido.ORCAMENTO)
            .stream()
            .map(this::toResponseDTO)
            .toList();
    }

    private String gerarNumeroOrcamento() {
        LocalDate hoje = LocalDate.now();
        int ano = hoje.getYear();
        int diaDoAno = hoje.getDayOfYear();

        String prefixo = String.format("PED-%d-%03d", ano, diaDoAno);
        long total = pedidoRepository.countByNumeroOrcamentoStartingWith(prefixo);

        int seq = (int) total + 1;
        if (seq > 99) {
            throw new IllegalStateException("Limite diário de orçamentos atingido (99).");
        }

        return String.format("%s%02d", prefixo, seq);
    }


    private String gerarNumeroVendaDireta() {
        LocalDate hoje = LocalDate.now();
        long vendas = pedidoRepository.countByNumeroVendaStartingWith(String.format("VND-%d-", hoje.getYear()));
        return String.format("VND-%d-%05d", hoje.getYear(), vendas + 1);
    }

    /**
     * Retorna true se o desconto exigiu e passou pela senha do gerente.
     */
    private boolean validarDesconto(BigDecimal totalBruto, BigDecimal valorDesconto, String senhaAutorizacao) {
        if (valorDesconto == null || valorDesconto.compareTo(BigDecimal.ZERO) <= 0) {
            return false;
        }

        if (totalBruto.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Não é possível aplicar desconto em pedido sem valor.");
        }

        BigDecimal percentual = valorDesconto
            .multiply(new BigDecimal("100"))
            .divide(totalBruto, 2, RoundingMode.HALF_UP);

        if (percentual.compareTo(limitePercentualDesconto) <= 0) {
            return false;
        }

        if (senhaAutorizacao == null || senhaAutorizacao.isBlank()) {
            throw new UnauthorizedException(
                "Desconto de " + percentual + "% excede o limite de " + limitePercentualDesconto
                + "%. Autorização do gerente necessária."
            );
        }

        if (senhaGerenteHash == null || senhaGerenteHash.isBlank()
                || !passwordEncoder.matches(senhaAutorizacao, senhaGerenteHash)) {
            throw new UnauthorizedException("Senha do gerente incorreta.");
        }

        return true;
    }

    private PedidoResponseDTO toResponseDTO(Pedido p) {
        List<ItemPedidoDTO> itensDto = p.getItens().stream()
            .map(item -> new ItemPedidoDTO(
                item.getProduto().getId(),
                item.getProduto().getDescricao(),
                item.getProduto().getUnidade(),
                item.getQuantidade(),
                item.getPrecoUnitario(),
                item.getSubtotal()
            )).toList();

        return new PedidoResponseDTO(
            p.getId(),
            p.getNumeroExibicao(),
            p.getNumeroOrcamento(),
            p.getNumeroVenda(),
            p.getNumeroOrcamentoOrigem(),
            p.getClienteId(),
            p.getStatus(),
            p.getFormaPagamento(),
            p.getValorTotal(),
            p.getValorDesconto(),
            p.getDescontoAutorizadoPor(),
            p.getDescontoAutorizadoEm(),
            p.getCreatedAt(),
            p.getFinalizadoAt(),
            itensDto
        );
    }
}