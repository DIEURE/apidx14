package com.d2ticom.api.config;

import com.d2ticom.api.modules.company.domain.Empresa;
import com.d2ticom.api.modules.company.repository.EmpresaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("prod")
public class EmpresaSeedConfig {

    private static final Logger log = LoggerFactory.getLogger(EmpresaSeedConfig.class);

    @Bean
    CommandLineRunner seedEmpresa(EmpresaRepository empresaRepository) {
        return args -> {
            if (empresaRepository.count() > 0) {
                log.info("[SEED] Empresa já existente, seed ignorado.");
                return;
            }

            Empresa empresa = new Empresa();
            empresa.setRazaoSocial("DX14 Soluções Inteligentes LTDA");
            empresa.setNomeFantasia("Ferragista Dois Irmãos");
            empresa.setCnpj("56440275000109");
            empresa.setInscricaoEstadual("123456789");
            empresa.setEndereco("Rua das Ferragens, 100 - Centro");
            empresa.setCidade("Aparecida de Goiânia");
            empresa.setUf("GO");
            empresa.setCep("01000-000");
            empresa.setTelefone("(11) 4002-8922");
            empresa.setEmail("contato@dx14.com.br");
            empresa.setLogoUrl(null);

            empresaRepository.save(empresa);
            log.info("[SEED] Empresa de teste criada: {} (id={})", empresa.getNomeFantasia(), empresa.getId());
        };
    }
}