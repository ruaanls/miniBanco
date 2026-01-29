package br.com.fiap.minibanco.infra.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("MiniBanco API")
                        .version("1.0.0")
                        .description("""
                                API REST desenvolvida em Java/Spring Boot para simular operações bancárias de um internet banking.
                                
                                ## Funcionalidades
                                - Cadastro e autenticação de usuários
                                - Consulta de saldo
                                - Transferências via PIX
                                - Extrato paginado de transações
                                
                                ## Arquitetura
                                - Arquitetura Hexagonal (Ports & Adapters)
                                - Clean Architecture
                                - Spring Security com JWT
                                
                                ## Infraestrutura
                                - Oracle Autonomous Database (produção)
                                - Oracle Kubernetes Engine (OKE)
                                - Deploy disponível no Render
                                """)
                        .contact(new Contact()
                                .name("Ruan Lima Silva")
                                .email("contato@example.com"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")))
                .servers(List.of(
                        new Server()
                                .url("https://minibanco.onrender.com")
                                .description("Servidor de Produção (Render)"),
                        new Server()
                                .url("http://localhost:8080")
                                .description("Servidor Local (Desenvolvimento)")
                ))
                .addSecurityItem(new SecurityRequirement()
                        .addList("Bearer Authentication"))
                .components(new Components()
                        .addSecuritySchemes("Bearer Authentication", createAPIKeyScheme()));
    }

    private SecurityScheme createAPIKeyScheme() {
        return new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .bearerFormat("JWT")
                .scheme("bearer")
                .description("""
                        Autenticação JWT (JSON Web Token)
                        
                        Para obter o token:
                        1. Faça login através do endpoint `/auth/login`
                        2. Copie o token retornado na resposta
                        3. Clique no botão "Authorize" acima
                        4. Cole o token no formato: `Bearer <seu-token>`
                        
                        **Nota:** Os endpoints `/auth/login` e `/auth/registro` não requerem autenticação.
                        """);
    }
}
