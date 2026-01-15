package br.com.fiap.minibanco.infra.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;

//@Component
//public class DatabaseTest implements CommandLineRunner {
//
//    private final DataSource dataSource;
//
//    public DatabaseTest(DataSource dataSource) {
//        this.dataSource = dataSource;
//    }
//
//    @Override
//    public void run(String... args) throws Exception {
//        try (Connection conn = dataSource.getConnection()) {
//            System.out.println("✅ CONEXÃO ESTABELECIDA COM SUCESSO!");
//            System.out.println("Database: " + conn.getMetaData().getDatabaseProductName());
//            System.out.println("Versão: " + conn.getMetaData().getDatabaseProductVersion());
//        } catch (Exception e) {
//            System.err.println("❌ ERRO NA CONEXÃO:");
//            e.printStackTrace();
//        }
//    }
//}