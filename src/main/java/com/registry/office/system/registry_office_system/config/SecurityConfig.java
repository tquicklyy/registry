package com.registry.office.system.registry_office_system.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.RememberMeConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;
import org.thymeleaf.extras.springsecurity6.dialect.SpringSecurityDialect;

@Configuration
@EnableWebSecurity // Проверка безопасности на уровне конфига с фильром HTTP
@EnableMethodSecurity // Проверка безопасности на уровне методов, то есть разные аннотации теперь учитываются
public class SecurityConfig {

    @Bean
    public UserDetailsService userDetailsService() { return new CustomUserDetailService(); }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService());
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/login", "/register", "/css/**",
                                "/info", "/admin/**", "/images/**", "/requests/**", "/js/**").permitAll()
                        .requestMatchers("/documents").authenticated()
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/", true)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/")
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .deleteCookies("remember-me", "JSESSIONID"))
                .rememberMe(new Customizer<RememberMeConfigurer<HttpSecurity>>() {
                    @Override
                    public void customize(RememberMeConfigurer<HttpSecurity> httpSecurityRememberMeConfigurer) {
                        httpSecurityRememberMeConfigurer
                                .rememberMeServices(tokenBasedRememberMeServices())
                                .tokenValiditySeconds(1209600);
                    }
                });
        return http.build();
    }

    @Bean
    public TokenBasedRememberMeServices tokenBasedRememberMeServices() {
        String key = "magic-key";
        return new TokenBasedRememberMeServices(key, userDetailsService());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SpringSecurityDialect springSecurityDialect() {
        return new SpringSecurityDialect();
    }

}
