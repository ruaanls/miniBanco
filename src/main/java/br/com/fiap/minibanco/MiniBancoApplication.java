package br.com.fiap.minibanco;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MiniBancoApplication {

    public static void main(String[] args) {
        System.out.println(System.getenv("DB_USERNAME"));
        System.out.println(System.getProperty("oracle.net.tns_admin"));
        SpringApplication.run(MiniBancoApplication.class, args);
    }

}
