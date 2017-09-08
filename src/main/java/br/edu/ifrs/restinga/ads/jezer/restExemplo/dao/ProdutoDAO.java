/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifrs.restinga.ads.jezer.restExemplo.dao;

import br.edu.ifrs.restinga.ads.jezer.restExemplo.modelo.Produto;
import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author jezer
 */

@Repository
public interface ProdutoDAO extends CrudRepository<Produto, Integer>{
    
    List<Produto> findByNome(String nome);
    
    List<Produto> findByNomeContainingOrderByNome(String nome);
    
    List<Produto> findByMarcas(String marcas);
    
    List<Produto> findByNomeOrMarcas(String nome, String marca);
    
    List<Produto> findByNomeOrMarcasOrValorLessThan(String nome, String marca, Double valor);
    
    List<Produto> findByValorLessThan(Double valor);
}
