/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifrs.restinga.ads.jezer.restExemplo.dao;

import br.edu.ifrs.restinga.ads.jezer.restExemplo.modelo.Produto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author jezer
 */

@Repository
public interface ProdutoDAO extends PagingAndSortingRepository<Produto, Integer>{
    
    Page<Produto> findByNome(String nome, Pageable pageable);
    
    Page<Produto> findByNomeContainingOrderByNome(String nome, Pageable pageable);
    
    Page<Produto> findByMarcas(String marcas, Pageable pageable);
    
    Page<Produto> findByNomeOrMarcas(String nome, String marca, Pageable pageable);
    
    Page<Produto> findByNomeOrMarcasOrValorLessThan(String nome, String marca, Double valor, Pageable pageable);
    
    Page<Produto> findByValorLessThan(Double valor, Pageable pageable);
    
    
    
}
