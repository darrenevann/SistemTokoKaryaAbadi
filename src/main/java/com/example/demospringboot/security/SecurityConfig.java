package com.example.demospringboot.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                // Public resources
                .requestMatchers("/css/**", "/js/**", "/images/**", "/favicon.ico").permitAll()
                .requestMatchers("/login", "/error").permitAll()

                // Admin only
                .requestMatchers("/admin/**", "/users/**").hasRole("ADMIN")
                .requestMatchers("/price-history/update").hasRole("ADMIN")

                // Kasir access
                .requestMatchers("/pos/**", "/sales/receipt/**").hasAnyRole("KASIR", "ADMIN")

                // Owner + Admin access
                .requestMatchers("/reports/**").hasAnyRole("OWNER", "ADMIN")

                // Shared: Admin, Kasir can access these
                .requestMatchers("/products/**", "/suppliers/**", "/stock/**",
                        "/damaged/**", "/price-history/**", "/cash/**").hasAnyRole("ADMIN", "KASIR", "OWNER")

                // Dashboard accessible by all authenticated users
                .requestMatchers("/", "/dashboard/**").authenticated()

                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .defaultSuccessUrl("/dashboard", true)
                .failureUrl("/login?error=true")
                .usernameParameter("username")
                .passwordParameter("password")
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout=true")
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .deleteCookies("JSESSIONID")
                .permitAll()
            )
            .sessionManagement(session -> session
                .maximumSessions(1)
                .expiredUrl("/login?expired=true")
            )
            .csrf(csrf -> csrf.ignoringRequestMatchers("/api/**"));

        return http.build();
    }
}
