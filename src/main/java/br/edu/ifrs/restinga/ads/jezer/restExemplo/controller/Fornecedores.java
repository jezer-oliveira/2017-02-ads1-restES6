/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifrs.restinga.ads.jezer.restExemplo.controller;

import br.edu.ifrs.restinga.ads.jezer.restExemplo.dao.FornecedorDAO;
import br.edu.ifrs.restinga.ads.jezer.restExemplo.modelo.Fornecedor;
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
public class Fornecedores {
    
    @Autowired
    FornecedorDAO fornecedorDAO; 
    
    @RequestMapping(path = "/fornecedores", method = RequestMethod.GET)
    public Iterable<Fornecedor> listar(@RequestParam(required = false , defaultValue = "0" ) int pagina) {
        PageRequest pageRequest =  new PageRequest(pagina, 5);
        return fornecedorDAO.findAll(pageRequest);
    }

    
    @RequestMapping(path = "/fornecedores", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public Fornecedor inserir(@RequestBody Fornecedor fornecedor) {
        fornecedor.setId(0);
        Fornecedor fornecedorSalvo = fornecedorDAO.save(fornecedor);
        return fornecedorSalvo;
    }

    @RequestMapping(path = "/fornecedores/{id}", method = RequestMethod.GET)
    public Fornecedor recuperar(@PathVariable int id) {
        return fornecedorDAO.findOne(id);
    }
    
    @RequestMapping(path = "/fornecedores/{id}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public void atualizar(@PathVariable int id, @RequestBody Fornecedor fornecedor){
        if(fornecedorDAO.exists(id)){
            fornecedor.setId(id);
            fornecedorDAO.save(fornecedor);
        }
    
    }
    
    @RequestMapping(path= "/fornecedores/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    public void apagar(@PathVariable int id) {
        if(fornecedorDAO.exists(id)){
            fornecedorDAO.delete(id);
        }
        
    }

}
