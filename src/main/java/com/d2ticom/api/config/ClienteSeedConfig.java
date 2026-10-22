package com.d2ticom.api.config;

import com.d2ticom.api.modules.customers.domain.Pessoa;
import com.d2ticom.api.modules.customers.domain.TipoPessoa;
import com.d2ticom.api.modules.customers.repository.PessoaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Configuration
@Order(2)
public class ClienteSeedConfig implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(ClienteSeedConfig.class);

    private final PessoaRepository pessoaRepository;

    public ClienteSeedConfig(PessoaRepository pessoaRepository) {
        this.pessoaRepository = pessoaRepository;
    }

    @Override
    @Transactional
    public void run(String... args) {
        // Só semeia se tiver apenas o CONSUMIDOR FINAL no banco.
        if (pessoaRepository.count() > 1) {
            return;
        }

        List<Pessoa> clientes = List.of(
            fisica("Carlos Almeida", "11111111111", "MG-1111111", LocalDate.of(1980, 5, 12),
                "(31) 98888-1111", "carlos.almeida@exemplo.com",
                "Rua das Acácias, 145", "Belo Horizonte", "MG", "30000-000"),

            fisica("João Pereira", "22222222222", "MG-2222222", LocalDate.of(1975, 10, 3),
                "(31) 97777-2222", "joao.pereira@exemplo.com",
                "Rua São Bento, 22", "Contagem", "MG", "32000-000"),

            fisica("Maria Souza", "33333333333", "MG-3333333", LocalDate.of(1988, 2, 20),
                "(31) 96666-3333", "maria.souza@exemplo.com",
                "Av. Amazonas, 890", "Belo Horizonte", "MG", "30180-001"),

            fisica("Antônio Carlos Ferreira", "44444444444", "MG-4444444", LocalDate.of(1969, 7, 15),
                "(31) 95555-4444", "antonio.ferreira@exemplo.com",
                "Rua Ouro Preto, 340", "Betim", "MG", "32600-000"),

            fisica("Fernanda Lima", "55555555555", "MG-5555555", LocalDate.of(1992, 11, 7),
                "(31) 94444-5555", "fernanda.lima@exemplo.com",
                "Rua Rio de Janeiro, 1210", "Belo Horizonte", "MG", "30160-041"),

            fisica("Roberto Nogueira", "66666666666", "MG-6666666", LocalDate.of(1983, 4, 28),
                "(31) 93333-6666", "roberto.nogueira@exemplo.com",
                "Rua dos Caetés, 78", "Belo Horizonte", "MG", "30120-030"),

            juridica("Construtora Alfa Ltda", "Alfa Obras", "11111111000111", "1234567890",
                "(31) 3222-1111", "contato@alfaobras.com.br",
                "Av. Industrial, 720", "Contagem", "MG", "32000-000"),

            juridica("Serralheria Diamante ME", "Diamante", "22222222000122", "2234567890",
                "(31) 3333-2222", "contato@serralheriadiamante.com.br",
                "Rua dos Ferreiros, 55", "Betim", "MG", "32600-000"),

            juridica("Marcenaria Bom Sucesso", "Bom Sucesso", "33333333000133", "3234567890",
                "(31) 3444-3333", "contato@marcenariabomsucesso.com.br",
                "Av. Madeireira, 200", "Sabarб", "MG", "34500-000"),

            juridica("Vidraçaria Cristal", "Cristal", "44444444000144", "4234567890",
                "(31) 3555-4444", "contato@vidracariacristal.com.br",
                "Rua do Vidro, 310", "Belo Horizonte", "MG", "31270-000")
        );

        pessoaRepository.saveAll(clientes);
        log.info("Seed de clientes concluído: {} pessoas cadastradas.", clientes.size());
    }

    private Pessoa fisica(String nome, String documento, String rg, LocalDate nascimento,
                          String telefone, String email,
                          String endereco, String cidade, String uf, String cep) {
        Pessoa p = new Pessoa();
        p.setTipo(TipoPessoa.FISICA);
        p.setNome(nome);
        p.setDocumento(documento);
        p.setRg(rg);
        p.setDataNascimento(nascimento);
        p.setTelefone(telefone);
        p.setEmail(email);
        p.setEndereco(endereco);
        p.setCidade(cidade);
        p.setUf(uf);
        p.setCep(cep);
        p.setAtivo(true);
        return p;
    }

    private Pessoa juridica(String razaoSocial, String nomeFantasia, String documento,
                            String inscricaoEstadual, String telefone, String email,
                            String endereco, String cidade, String uf, String cep) {
        Pessoa p = new Pessoa();
        p.setTipo(TipoPessoa.JURIDICA);
        p.setNome(razaoSocial);
        p.setNomeFantasia(nomeFantasia);
        p.setDocumento(documento);
        p.setInscricaoEstadual(inscricaoEstadual);
        p.setTelefone(telefone);
        p.setEmail(email);
        p.setEndereco(endereco);
        p.setCidade(cidade);
        p.setUf(uf);
        p.setCep(cep);
        p.setAtivo(true);
        return p;
    }
}