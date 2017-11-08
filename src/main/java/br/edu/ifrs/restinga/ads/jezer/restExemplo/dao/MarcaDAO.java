/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifrs.restinga.ads.jezer.restExemplo.dao;

import br.edu.ifrs.restinga.ads.jezer.restExemplo.modelo.Marca;
import br.edu.ifrs.restinga.ads.jezer.restExemplo.modelo.Produto;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 *
 * @author jezer
 */

@Repository
public interface MarcaDAO 
        extends PagingAndSortingRepository<Marca, Integer>{
        @Query(value = "SELECT produto FROM Produto produto "
            + "WHERE :marca MEMBER OF produto.marcas")
    List<Produto> listaProdutosMarca(@Param("marca") Marca marca);

}
