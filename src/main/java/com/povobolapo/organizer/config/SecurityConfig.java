package com.povobolapo.organizer.config;

import com.povobolapo.organizer.repository.UserRepository;
import com.povobolapo.organizer.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;


@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private UserRepository userRepo;
    private UserDetailsServiceImpl userDetailsServiceImpl;
    private SecurityFilter securityFilter;
    private SecurityAuthenticationEntryPoint securityAuthenticationEntryPoint;

    @Value("${springdoc.api-docs.path}")
    private String restApiDocPath;

    @Value("${springdoc.swagger-ui.path}")
    private String swaggerPath;

    @Autowired
    public SecurityConfig(UserRepository userRepo, UserDetailsServiceImpl userDetailsServiceImpl,
                          SecurityFilter securityFilter, SecurityAuthenticationEntryPoint securityAuthenticationEntryPoint) {
        this.userRepo = userRepo;
        this.userDetailsServiceImpl = userDetailsServiceImpl;
        this.securityFilter = securityFilter;
        this.securityAuthenticationEntryPoint = securityAuthenticationEntryPoint;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable();

        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.authorizeRequests()
                // Добавляем ендпоинты, для которых не нужна авторизация
                .antMatchers("/user/create").permitAll()
                .antMatchers("/login").permitAll()
                .antMatchers(swaggerPath + "/**").permitAll()
                .antMatchers(restApiDocPath + "/**").permitAll()
                // Для всех остальных включаем авторизацию
                .anyRequest().authenticated();

        // Если добавить, то любой запрос должен быть с хедором авторизации
        // При возникновении ошибок в авторизации первым делом проверить этот момент
        // http.exceptionHandling().authenticationEntryPoint(securityAuthenticationEntryPoint);

        // Добавляем свой фильтр, в котором проверяется правильность токена
        http.addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    // Кодировщик пароля юзера
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsFilter corsFilter() {
        var source = new UrlBasedCorsConfigurationSource();
        var config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
