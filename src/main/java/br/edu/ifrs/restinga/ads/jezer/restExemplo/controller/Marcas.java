/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifrs.restinga.ads.jezer.restExemplo.controller;

import br.edu.ifrs.restinga.ads.jezer.restExemplo.dao.MarcaDAO;
import br.edu.ifrs.restinga.ads.jezer.restExemplo.modelo.Marca;
import br.edu.ifrs.restinga.ads.jezer.restExemplo.modelo.Produto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author jezer
 */
@RestController
@RequestMapping(path = "/api")
public class Marcas {
    
    @Autowired
    MarcaDAO marcaDAO; 
    
    @RequestMapping(path = "/marcas", method = RequestMethod.GET)
    public Iterable<Marca> listar(@RequestParam(required = false , defaultValue = "0" ) int pagina) {
        PageRequest pageRequest =  new PageRequest(pagina, 5);
        return marcaDAO.findAll(pageRequest);
    }

    
    @RequestMapping(path = "/marcas", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public Marca inserir(@RequestBody Marca marca) {
        marca.setId(0);
        Marca marcaSalvo = marcaDAO.save(marca);
        return marcaSalvo;
    }

    @RequestMapping(path = "/marcas/{id}", method = RequestMethod.GET)
    public Marca recuperar(@PathVariable int id) {
        return marcaDAO.findOne(id);
    }
    
    @RequestMapping(path = "/marcas/{id}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public void atualizar(@PathVariable int id, @RequestBody Marca marca){
        if(marcaDAO.exists(id)){
            marca.setId(id);
            marcaDAO.save(marca);
        }
    
    }
    
    @RequestMapping(path= "/marcas/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    public void apagar(@PathVariable int id) {
        if(marcaDAO.exists(id)){
            marcaDAO.delete(id);
        }
        
    }

        @RequestMapping(path = "/marcas/{id}/produtos", method = RequestMethod.GET)
    public Iterable<Produto> listaProdutosMarca(@PathVariable int id) {
        Marca marca = marcaDAO.findOne(id);
        return marcaDAO.listaProdutosMarca(marca);
    }


    
    
}
