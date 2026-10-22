package com.d2ticom.api.modules.customers.service;

import com.d2ticom.api.common.exception.ResourceNotFoundException;
import com.d2ticom.api.modules.customers.domain.Pessoa;
import com.d2ticom.api.modules.customers.domain.TipoPessoa;
import com.d2ticom.api.modules.customers.dto.PessoaRequestDTO;
import com.d2ticom.api.modules.customers.dto.PessoaResponseDTO;
import com.d2ticom.api.modules.customers.repository.PessoaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PessoaService {

    public static final Long CONSUMIDOR_FINAL_ID = 1L;

    private final PessoaRepository pessoaRepository;

    public PessoaService(PessoaRepository pessoaRepository) {
        this.pessoaRepository = pessoaRepository;
    }

    @Transactional
    public PessoaResponseDTO salvar(PessoaRequestDTO dto) {
        if (pessoaRepository.existsByDocumento(dto.documento())) {
            throw new IllegalArgumentException("Já existe uma pessoa cadastrada com esse documento.");
        }

        Pessoa pessoa = new Pessoa();
        aplicar(pessoa, dto);
        return toDTO(pessoaRepository.save(pessoa));
    }

    @Transactional
    public PessoaResponseDTO atualizar(Long id, PessoaRequestDTO dto) {
        Pessoa pessoa = pessoaRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Pessoa não encontrada ID: " + id));

        if (!pessoa.getDocumento().equals(dto.documento())
                && pessoaRepository.existsByDocumento(dto.documento())) {
            throw new IllegalArgumentException("Já existe uma pessoa cadastrada com esse documento.");
        }

        aplicar(pessoa, dto);
        return toDTO(pessoaRepository.save(pessoa));
    }

    @Transactional(readOnly = true)
    public PessoaResponseDTO buscarPorId(Long id) {
        Pessoa pessoa = pessoaRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Pessoa não encontrada ID: " + id));
        return toDTO(pessoa);
    }

    @Transactional(readOnly = true)
    public List<PessoaResponseDTO> listar() {
        return pessoaRepository.findByAtivoTrueOrderByNomeAsc()
            .stream()
            .map(this::toDTO)
            .toList();
    }

    @Transactional(readOnly = true)
    public List<PessoaResponseDTO> listarPorTipo(TipoPessoa tipo) {
        return pessoaRepository.findByTipoAndAtivoTrueOrderByNomeAsc(tipo)
            .stream()
            .map(this::toDTO)
            .toList();
    }

    @Transactional(readOnly = true)
    public List<PessoaResponseDTO> buscarPorNome(String termo) {
        return pessoaRepository.findByNomeContainingIgnoreCaseAndAtivoTrueOrderByNomeAsc(termo)
            .stream()
            .map(this::toDTO)
            .toList();
    }

    private void aplicar(Pessoa pessoa, PessoaRequestDTO dto) {
        pessoa.setTipo(dto.tipo());
        pessoa.setNome(dto.nome());
        pessoa.setNomeFantasia(dto.nomeFantasia());
        pessoa.setDocumento(dto.documento());
        pessoa.setInscricaoEstadual(dto.inscricaoEstadual());
        pessoa.setRg(dto.rg());
        pessoa.setDataNascimento(dto.dataNascimento());
        pessoa.setTelefone(dto.telefone());
        pessoa.setEmail(dto.email());
        pessoa.setEndereco(dto.endereco());
        pessoa.setCidade(dto.cidade());
        pessoa.setUf(dto.uf());
        pessoa.setCep(dto.cep());
        pessoa.setAtivo(dto.ativo() != null ? dto.ativo() : true);
    }

    private PessoaResponseDTO toDTO(Pessoa p) {
        return new PessoaResponseDTO(
            p.getId(),
            p.getTipo(),
            p.getNome(),
            p.getNomeFantasia(),
            p.getDocumento(),
            p.getInscricaoEstadual(),
            p.getRg(),
            p.getDataNascimento(),
            p.getTelefone(),
            p.getEmail(),
            p.getEndereco(),
            p.getCidade(),
            p.getUf(),
            p.getCep(),
            p.getAtivo(),
            p.getCreatedAt(),
            p.getUpdatedAt()
        );
    }
}