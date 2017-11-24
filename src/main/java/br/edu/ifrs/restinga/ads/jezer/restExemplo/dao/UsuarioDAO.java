/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifrs.restinga.ads.jezer.restExemplo.dao;

import br.edu.ifrs.restinga.ads.jezer.restExemplo.modelo.Usuario;
import org.springframework.data.repository.PagingAndSortingRepository;


public interface UsuarioDAO extends 
        PagingAndSortingRepository<Usuario, Integer>{
    public Usuario findByLogin(String login);
    
}
