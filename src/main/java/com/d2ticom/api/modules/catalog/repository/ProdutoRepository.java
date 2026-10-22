package com.d2ticom.api.modules.catalog.repository;

import com.d2ticom.api.modules.catalog.domain.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {
    Optional<Produto> findByCodigoBarras(String codigoBarras);
    List<Produto> findByDescricaoContainingIgnoreCaseAndAtivoTrue(String descricao);
}