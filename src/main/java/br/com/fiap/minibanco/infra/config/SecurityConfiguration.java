package br.com.fiap.minibanco.infra.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration
{

    @Autowired
    SecurityFilter securityFilter;

    @Autowired
    private CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

    @Autowired
    private CustomAccessDeniedHandler customAccessDeniedHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(HttpMethod.POST, "auth/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "auth/registro").permitAll()
                        .requestMatchers(HttpMethod.DELETE, "auth/*").hasRole("ADMIM")
                        .requestMatchers(HttpMethod.PUT, "auth/*").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "banco/pix").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )

                // Ativando as chamadas de exceções personalizadas
                .exceptionHandling(ex -> ex
                        // Exceções de login e registro
                        .authenticationEntryPoint(customAuthenticationEntryPoint)
                        // Exceções de acesso negado
                        .accessDeniedHandler(customAccessDeniedHandler)
                )
                // Adicione um filtro PERSONALIZADO ANTES DO FILTRO PADRÃO
                // UsernamePasswordAuthenticationFilter.class = Filtro padrão que está acima
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager (AuthenticationConfiguration authenticationConfiguration) throws Exception
    {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder()
    {
        return new BCryptPasswordEncoder();
    }
}
