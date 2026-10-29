package pe.edu.upeu.autoandes.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;
import pe.edu.upeu.autoandes.security.*;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final UsuarioDetailsService usuarioDetailsService;
    private final RestAuthenticationEntryPoint restAuthenticationEntryPoint;
    private final RestAccessDeniedHandler restAccessDeniedHandler;
    private final CorsConfigurationSource corsConfigurationSource;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource))
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(exceptions -> exceptions
                        .authenticationEntryPoint(restAuthenticationEntryPoint)
                        .accessDeniedHandler(restAccessDeniedHandler)
                )
                .authorizeHttpRequests(auth -> auth
                        // Endpoints publicos
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers("/api/v1/auth/login").permitAll()
                        .requestMatchers("/api/v1/health").permitAll()
                        .requestMatchers(
                                "/v3/api-docs/**",
                                "/v3/api-docs.yaml",
                                "/swagger-ui/**",
                                "/swagger-ui.html"
                        ).permitAll()

                        // Clientes (VEHICULO_LEER)
                        .requestMatchers(HttpMethod.GET, "/api/v1/clientes/**").hasAuthority(Permisos.VEHICULO_LEER)

                        // Servicios (SERVICIO_LEER / SERVICIO_ESCRIBIR)
                        .requestMatchers(HttpMethod.GET, "/api/v1/servicios/**").hasAuthority(Permisos.SERVICIO_LEER)
                        .requestMatchers(HttpMethod.POST, "/api/v1/servicios/**").hasAuthority(Permisos.SERVICIO_ESCRIBIR)
                        .requestMatchers(HttpMethod.PUT, "/api/v1/servicios/**").hasAuthority(Permisos.SERVICIO_ESCRIBIR)
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/servicios/**").hasAuthority(Permisos.SERVICIO_ESCRIBIR)
                        .requestMatchers(HttpMethod.PATCH, "/api/v1/servicios/*/estado").hasAuthority(Permisos.SERVICIO_ESCRIBIR)

                        // Vehiculos (VEHICULO_LEER / VEHICULO_ESCRIBIR)
                        .requestMatchers(HttpMethod.GET, "/api/v1/vehiculos/**").hasAuthority(Permisos.VEHICULO_LEER)
                        .requestMatchers(HttpMethod.POST, "/api/v1/vehiculos/**").hasAuthority(Permisos.VEHICULO_ESCRIBIR)
                        .requestMatchers(HttpMethod.PUT, "/api/v1/vehiculos/**").hasAuthority(Permisos.VEHICULO_ESCRIBIR)
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/vehiculos/**").hasAuthority(Permisos.VEHICULO_ESCRIBIR)

                        // Ordenes de Servicio
                        .requestMatchers(HttpMethod.POST, "/api/v1/ordenes").hasAuthority(Permisos.ORDEN_REGISTRAR)
                        .requestMatchers(HttpMethod.GET, "/api/v1/ordenes/**").hasAuthority(Permisos.ORDEN_LEER)
                        .requestMatchers(HttpMethod.PATCH, "/api/v1/ordenes/*/anular").hasAuthority(Permisos.ORDEN_ANULAR)

                        // Reportes
                        .requestMatchers(HttpMethod.GET, "/api/v1/reportes/**").hasAuthority(Permisos.REPORTE_VER)

                        // Todo lo demas exige estar autenticado
                        .anyRequest().authenticated()
                )
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(usuarioDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
