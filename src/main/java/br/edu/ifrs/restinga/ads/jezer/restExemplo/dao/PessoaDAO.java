/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifrs.restinga.ads.jezer.restExemplo.dao;

import br.edu.ifrs.restinga.ads.jezer.restExemplo.modelo.Pessoa;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author jezer
 */

@Repository
public interface PessoaDAO extends PagingAndSortingRepository<Pessoa, Integer>{
    
}
