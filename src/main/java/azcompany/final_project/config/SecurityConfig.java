package azcompany.final_project.config;

import azcompany.final_project.exception.handler.CustomAccessDeniedHandler;
import azcompany.final_project.exception.handler.JwtAuthenticationEntryPoint;
import azcompany.final_project.filter.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                        .accessDeniedHandler(customAccessDeniedHandler))
                .authorizeHttpRequests(auth ->
                        auth.requestMatchers(
                                        "/v2/api-docs",
                                        "/v3/api-docs",
                                        "/v3/api-docs/**",
                                        "/swagger-resources/**",
                                        "/swagger-ui/**",
                                        "/swagger-ui.html",
                                        "/webjars/**",
                                        "/actuator/**"
                                ).permitAll()
                                .requestMatchers("/v1/auth/**").permitAll()
                                .requestMatchers("/v1/files").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.POST, "/v1/categories").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.GET, "/v1/categories/**").permitAll()
                                .requestMatchers(HttpMethod.PUT, "/v1/categories/**").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.DELETE, "/v1/categories/**").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.POST, "/v1/books").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.GET, "/v1/books/**").permitAll()
                                .requestMatchers(HttpMethod.PUT, "/v1/books/**").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.DELETE, "/v1/books/**").hasRole("ADMIN")
                                .requestMatchers("/v1/cart-items/**").hasAnyRole("ADMIN", "USER")
                                .anyRequest().authenticated())
                .sessionManagement(sessionManagement ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) {
        return config.getAuthenticationManager();
    }
}
