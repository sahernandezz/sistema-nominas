package com.example.sistemanominas.conf.security;

import com.example.sistemanominas.component.JWTTokenHelperComponent;
import com.example.sistemanominas.service.CustomUserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.MessageDigestPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.HashMap;

import static org.springframework.security.crypto.password.NoOpPasswordEncoder.getInstance;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private CustomUserServiceImpl userService;

    @Autowired
    private JWTTokenHelperComponent jWTTokenHelper;

    @Autowired
    private AuthenticationEntryPoint entryPoint;

    @Override
    protected void configure(final AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(this.userService).passwordEncoder(this.passwordEncoder());
    }

    @Bean
    protected PasswordEncoder passwordEncoder() {
        final HashMap<String, PasswordEncoder> encoders = new HashMap<>();
        encoders.put("noop", getInstance());
        encoders.put("bcrypt", new BCryptPasswordEncoder());
        encoders.put(null, new MessageDigestPasswordEncoder("SHA-1"));
        encoders.put("SHA-1", new MessageDigestPasswordEncoder("SHA-1"));
        return new DelegatingPasswordEncoder("bcrypt", encoders);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and().exceptionHandling()
                .authenticationEntryPoint(this.entryPoint).and()
                .authorizeRequests(
                        (request) -> request
                                .antMatchers("/auth/api/v1/*", "/", "/assets/*").permitAll()
                                .antMatchers("/carga-archivo/api/v1/*", "/formato-archivo/api/v1/lista/activos").hasAuthority("usuario operativo")
                                .antMatchers("/**").hasAuthority("administrador")
                                .anyRequest().fullyAuthenticated()
                )

                .addFilterBefore(new JWTAuthenticationFilter(this.userService, this.jWTTokenHelper),
                        UsernamePasswordAuthenticationFilter.class);


        http.csrf().disable().cors().and().headers().frameOptions().disable();
    }
}
