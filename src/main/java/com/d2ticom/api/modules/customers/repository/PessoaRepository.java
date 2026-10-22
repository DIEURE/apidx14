package com.d2ticom.api.modules.customers.repository;

import com.d2ticom.api.modules.customers.domain.Pessoa;
import com.d2ticom.api.modules.customers.domain.TipoPessoa;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PessoaRepository extends JpaRepository<Pessoa, Long> {

    Optional<Pessoa> findByDocumento(String documento);

    List<Pessoa> findByAtivoTrueOrderByNomeAsc();

    List<Pessoa> findByTipoAndAtivoTrueOrderByNomeAsc(TipoPessoa tipo);

    List<Pessoa> findByNomeContainingIgnoreCaseAndAtivoTrueOrderByNomeAsc(String termo);

    boolean existsByDocumento(String documento);
}