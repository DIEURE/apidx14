package com.d2ticom.api.modules.customers.controller;

import com.d2ticom.api.modules.customers.domain.TipoPessoa;
import com.d2ticom.api.modules.customers.dto.PessoaRequestDTO;
import com.d2ticom.api.modules.customers.dto.PessoaResponseDTO;
import com.d2ticom.api.modules.customers.service.PessoaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pessoas")
public class PessoaController {

    private final PessoaService pessoaService;

    public PessoaController(PessoaService pessoaService) {
        this.pessoaService = pessoaService;
    }

    @PostMapping
    public ResponseEntity<PessoaResponseDTO> salvar(@RequestBody @Valid PessoaRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(pessoaService.salvar(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PessoaResponseDTO> atualizar(@PathVariable Long id, @RequestBody @Valid PessoaRequestDTO dto) {
        return ResponseEntity.ok(pessoaService.atualizar(id, dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PessoaResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(pessoaService.buscarPorId(id));
    }

    @GetMapping
    public ResponseEntity<List<PessoaResponseDTO>> listar(
            @RequestParam(required = false) TipoPessoa tipo) {
        if (tipo != null) {
            return ResponseEntity.ok(pessoaService.listarPorTipo(tipo));
        }
        return ResponseEntity.ok(pessoaService.listar());
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<PessoaResponseDTO>> buscarPorNome(@RequestParam String termo) {
        return ResponseEntity.ok(pessoaService.buscarPorNome(termo));
    }
}