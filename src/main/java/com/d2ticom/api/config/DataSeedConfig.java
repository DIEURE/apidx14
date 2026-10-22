package com.d2ticom.api.config;

import com.d2ticom.api.modules.customers.domain.Pessoa;
import com.d2ticom.api.modules.customers.domain.TipoPessoa;
import com.d2ticom.api.modules.customers.repository.PessoaRepository;
import com.d2ticom.api.modules.customers.service.PessoaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

@Configuration
public class DataSeedConfig implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataSeedConfig.class);

    private final PessoaRepository pessoaRepository;
    private final JdbcTemplate jdbcTemplate;

    public DataSeedConfig(PessoaRepository pessoaRepository, JdbcTemplate jdbcTemplate) {
        this.pessoaRepository = pessoaRepository;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    @Transactional
    public void run(String... args) {
        if (pessoaRepository.existsById(PessoaService.CONSUMIDOR_FINAL_ID)) {
            return;
        }

        Pessoa consumidor = new Pessoa();
        consumidor.setId(PessoaService.CONSUMIDOR_FINAL_ID);
        consumidor.setTipo(TipoPessoa.FISICA);
        consumidor.setNome("CONSUMIDOR FINAL");
        consumidor.setDocumento("00000000000");
        consumidor.setAtivo(true);
        pessoaRepository.save(consumidor);

        try {
            jdbcTemplate.execute(
                "SELECT setval('pessoa_id_seq', COALESCE((SELECT MAX(id) FROM pessoa), 1))"
            );
        } catch (Exception e) {
            log.warn("Não foi possível ajustar a sequence pessoa_id_seq: {}", e.getMessage());
        }

        log.info("Pessoa CONSUMIDOR FINAL (id=1) criada com sucesso.");
    }
}