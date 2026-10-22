package com.d2ticom.api.modules.company.repository;

import com.d2ticom.api.modules.company.domain.Empresa;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmpresaRepository extends JpaRepository<Empresa, Long> {
}