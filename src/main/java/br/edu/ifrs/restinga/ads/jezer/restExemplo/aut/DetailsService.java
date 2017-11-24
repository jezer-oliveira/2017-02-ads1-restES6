/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifrs.restinga.ads.jezer.restExemplo.aut;

import br.edu.ifrs.restinga.ads.jezer.restExemplo.dao.UsuarioDAO;
import br.edu.ifrs.restinga.ads.jezer.restExemplo.modelo.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class DetailsService implements UserDetailsService {
    @Autowired
    UsuarioDAO usuarioDAO;
    @Override
    public UserDetails loadUserByUsername(String login) 
            throws UsernameNotFoundException {
        Usuario usuario = usuarioDAO.findByLogin(login);
        if (usuario == null) {
            throw new UsernameNotFoundException(login + " não existe!");
        }
        return new UsuarioAut(usuario);
    }
}
