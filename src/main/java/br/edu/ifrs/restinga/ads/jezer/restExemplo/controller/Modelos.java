/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifrs.restinga.ads.jezer.restExemplo.controller;

import br.edu.ifrs.restinga.ads.jezer.restExemplo.dao.ModeloDAO;
import br.edu.ifrs.restinga.ads.jezer.restExemplo.modelo.Modelo;
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
public class Modelos {
    
    @Autowired
    ModeloDAO modeloDAO; 
    
    @RequestMapping(path = "/modelos", method = RequestMethod.GET)
    public Iterable<Modelo> listar(@RequestParam(required = false , defaultValue = "0" ) int pagina) {
        PageRequest pageRequest =  new PageRequest(pagina, 5);
        return modeloDAO.findAll(pageRequest);
    }

    
    @RequestMapping(path = "/modelos", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public Modelo inserir(@RequestBody Modelo modelo) {
        modelo.setId(0);
        Modelo modeloSalvo = modeloDAO.save(modelo);
        return modeloSalvo;
    }

    
    
    @RequestMapping(path = "/modelos/{id}", method = RequestMethod.GET)
    public Modelo recuperar(@PathVariable int id) {
        return modeloDAO.findOne(id);
    }
    
    @RequestMapping(path = "/modelos/{id}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public void atualizar(@PathVariable int id, @RequestBody Modelo modelo){
        if(modeloDAO.exists(id)){
            modelo.setId(id);
            modeloDAO.save(modelo);
        }
    
    }
    
    @RequestMapping(path= "/modelos/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    public void apagar(@PathVariable int id) {
        if(modeloDAO.exists(id)){
            modeloDAO.delete(id);
        }
        
    }

}
