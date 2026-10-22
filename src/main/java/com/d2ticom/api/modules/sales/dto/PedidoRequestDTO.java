package com.d2ticom.api.modules.sales.dto;

import com.d2ticom.api.modules.sales.domain.FormaPagamento;
import com.d2ticom.api.modules.sales.domain.StatusPedido;
 
import java.math.BigDecimal;
import java.util.List;

public record PedidoRequestDTO(
    Long clienteId,
    StatusPedido status,
    FormaPagamento formaPagamento,
    BigDecimal valorDesconto,   
    String senhaAutorizacao,
    List<ItemPedidoDTO> itens
) {}

 