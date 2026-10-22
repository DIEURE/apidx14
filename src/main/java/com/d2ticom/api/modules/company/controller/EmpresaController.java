package com.d2ticom.api.modules.company.controller;

import com.d2ticom.api.modules.company.dto.EmpresaRequestDTO;
import com.d2ticom.api.modules.company.dto.EmpresaResponseDTO;
import com.d2ticom.api.modules.company.service.EmpresaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/empresa")
public class EmpresaController {

    private final EmpresaService empresaService;

    public EmpresaController(EmpresaService empresaService) {
        this.empresaService = empresaService;
    }

    @PostMapping
    public ResponseEntity<EmpresaResponseDTO> salvar(@RequestBody @Valid EmpresaRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(empresaService.salvar(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EmpresaResponseDTO> atualizar(@PathVariable Long id, @RequestBody @Valid EmpresaRequestDTO dto) {
        return ResponseEntity.ok(empresaService.atualizar(id, dto));
    }

    @GetMapping("/principal")
    public ResponseEntity<EmpresaResponseDTO> buscarPrincipal() {
        return ResponseEntity.ok(empresaService.buscarPrincipal());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmpresaResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(empresaService.buscarPorId(id));
    }
}