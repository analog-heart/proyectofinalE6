package eggporIzquierda.solucionesactivas;

import eggporIzquierda.solucionesactivas.service.ServicioUsuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SeguridadWeb {

    @Autowired
    public ServicioUsuario usuarioServicio;

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(usuarioServicio)
                .passwordEncoder(new BCryptPasswordEncoder());
    }

    @Bean
public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
  http
    .authorizeHttpRequests((authorize) ->
      authorize
        .requestMatchers("/admin/*", "/servicioOfrecido/*").hasRole("ADMIN")
        .requestMatchers("/css/*", "/js/*", "/img/*", "/**").permitAll()  
    )
    .formLogin((form) ->  
      form
        .loginPage("/login")
        .loginProcessingUrl("/logincheck")
        .usernameParameter("email")
        .passwordParameter("password") 
        .defaultSuccessUrl("/inicio")
        .permitAll()
    )
    .logout((logout) ->
      logout
        .logoutUrl("/logout")
        .logoutSuccessUrl("/login")
        .permitAll()
    )
    .csrf((csrf) -> csrf.disable());

  return http.build();
}
}
