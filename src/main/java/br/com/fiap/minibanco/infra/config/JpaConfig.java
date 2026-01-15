package br.com.fiap.minibanco.infra.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import jakarta.annotation.PostConstruct;
import java.io.File;

//@Configuration
//@EnableJpaRepositories(basePackages = "br.com.fiap.minibanco.adapters.outbound.JPA.repositories")
//@EntityScan(basePackages = "br.com.fiap.minibanco.adapters.outbound.JPA.entities")
//@EnableTransactionManagement
//public class JpaConfig {
//
//    @PostConstruct
//    public void configureOracleWallet() {
//        String tnsAdmin = System.getProperty("oracle.net.tns_admin");
//        if (tnsAdmin == null || tnsAdmin.isEmpty()) {
//            // Fallback para variável de ambiente ou caminho padrão
//            tnsAdmin = System.getenv("TNS_ADMIN");
//            if (tnsAdmin == null || tnsAdmin.isEmpty()) {
//                tnsAdmin = "D:/oracle/wallet";
//            }
//        }
//
//        // Garantir que o caminho está correto
//        File walletDir = new File(tnsAdmin);
//        if (!walletDir.exists()) {
//            System.err.println("AVISO: Diretório do wallet não encontrado: " + tnsAdmin);
//        } else {
//            System.out.println("Wallet TNS_ADMIN configurado: " + tnsAdmin);
//            System.setProperty("oracle.net.tns_admin", tnsAdmin);
//        }
//
//        // Verificar arquivos essenciais
//        File tnsnames = new File(walletDir, "tnsnames.ora");
//        File sqlnet = new File(walletDir, "sqlnet.ora");
//
//        if (!tnsnames.exists()) {
//            System.err.println("ERRO: tnsnames.ora não encontrado em: " + tnsAdmin);
//        }
//        if (!sqlnet.exists()) {
//            System.err.println("ERRO: sqlnet.ora não encontrado em: " + tnsAdmin);
//        }
//    }
//}

