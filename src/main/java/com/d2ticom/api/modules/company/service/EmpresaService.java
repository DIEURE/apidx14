package com.d2ticom.api.modules.company.service;

import com.d2ticom.api.common.exception.ResourceNotFoundException;
import com.d2ticom.api.modules.company.domain.Empresa;
import com.d2ticom.api.modules.company.dto.EmpresaRequestDTO;
import com.d2ticom.api.modules.company.dto.EmpresaResponseDTO;
import com.d2ticom.api.modules.company.repository.EmpresaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EmpresaService {

    private final EmpresaRepository empresaRepository;

    public EmpresaService(EmpresaRepository empresaRepository) {
        this.empresaRepository = empresaRepository;
    }

    @Transactional
    public EmpresaResponseDTO salvar(EmpresaRequestDTO dto) {
        if (empresaRepository.count() > 0) {
            throw new IllegalStateException("Já existe uma empresa cadastrada.");
        }
        Empresa empresa = new Empresa();
        aplicar(empresa, dto);
        return toResponseDTO(empresaRepository.save(empresa));
    }

    @Transactional
    public EmpresaResponseDTO atualizar(Long id, EmpresaRequestDTO dto) {
        Empresa empresa = empresaRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Empresa não encontrada ID: " + id));
        aplicar(empresa, dto);
        return toResponseDTO(empresaRepository.save(empresa));
    }

    @Transactional(readOnly = true)
    public EmpresaResponseDTO buscarPrincipal() {
        List<Empresa> empresas = empresaRepository.findAll();
        if (empresas.isEmpty()) {
            throw new ResourceNotFoundException("Nenhuma empresa cadastrada");
        }
        return toResponseDTO(empresas.get(0));
    }

    @Transactional(readOnly = true)
    public EmpresaResponseDTO buscarPorId(Long id) {
        return empresaRepository.findById(id)
            .map(this::toResponseDTO)
            .orElseThrow(() -> new ResourceNotFoundException("Empresa não encontrada ID: " + id));
    }

    private void aplicar(Empresa empresa, EmpresaRequestDTO dto) {
        empresa.setRazaoSocial(dto.razaoSocial());
        empresa.setNomeFantasia(dto.nomeFantasia());
        empresa.setCnpj(dto.cnpj());
        empresa.setInscricaoEstadual(dto.inscricaoEstadual());
        empresa.setEndereco(dto.endereco());
        empresa.setCidade(dto.cidade());
        empresa.setUf(dto.uf());
        empresa.setCep(dto.cep());
        empresa.setTelefone(dto.telefone());
        empresa.setEmail(dto.email());
        empresa.setLogoUrl(dto.logoUrl());
    }

    private EmpresaResponseDTO toResponseDTO(Empresa e) {
        return new EmpresaResponseDTO(
            e.getId(), e.getRazaoSocial(), e.getNomeFantasia(),
            e.getCnpj(), e.getInscricaoEstadual(), e.getEndereco(),
            e.getCidade(), e.getUf(), e.getCep(), e.getTelefone(),
            e.getEmail(), e.getLogoUrl()
        );
    }
}