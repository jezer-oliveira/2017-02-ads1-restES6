/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifrs.restinga.ads.jezer.restExemplo;

import br.edu.ifrs.restinga.ads.jezer.restExemplo.aut.DetailsService;
import br.edu.ifrs.restinga.ads.jezer.restExemplo.aut.TokenBasedAuthorizationFilter;
import br.edu.ifrs.restinga.ads.jezer.restExemplo.controller.Usuarios;
import br.edu.ifrs.restinga.ads.jezer.restExemplo.dao.UsuarioDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.stereotype.Component;

/**
 *
 * @author jezer
 */
@Component
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    DetailsService detailsService;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UsuarioDAO usuarioDAO;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(detailsService)
                .passwordEncoder(Usuarios.PASSWORD_ENCODER);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        //web.ignoring().antMatchers(HttpMethod.POST,"/api/usuarios/");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers(HttpMethod.POST, "/api/usuarios/").permitAll()
                .antMatchers(HttpMethod.GET, "/api/usuarios/login/**").permitAll()
                .antMatchers("/api/**").authenticated()
                .and().httpBasic()
                .and().
                addFilterBefore(new TokenBasedAuthorizationFilter(authenticationManager, usuarioDAO)
                        , BasicAuthenticationFilter.class)
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().csrf().disable();
    }
}
