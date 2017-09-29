/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifrs.restinga.ads.jezer.restExemplo.controller;

import br.edu.ifrs.restinga.ads.jezer.restExemplo.dao.ProdutoDAO;
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
public class Produtos {
    
    @Autowired
    ProdutoDAO produtoDAO; 
/*
    @RequestMapping(path = "/produtos/pesquisar/nome", method = RequestMethod.GET)
    public Iterable<Produto> pesquisaPorNome(
            @RequestParam(required = false) String igual,
            @RequestParam(required = false) String contem) {
        if(igual!=null){
            return produtoDAO.findByNome(igual);
        } else {
            return produtoDAO.findByNomeContainingOrderByNome(contem);
        }
    }

    @RequestMapping(path = "/produtos/pesquisar/marcas", method = RequestMethod.GET)
    public Iterable<Produto> pesquisaPorMarcas(@RequestParam(required = false) String igual) {
            return produtoDAO.findByMarcas(igual);
    }

    @RequestMapping(path = "/produtos/pesquisar/produto", method = RequestMethod.GET)
    public Iterable<Produto> pesquisaPorNomeOuMarca(
            @RequestParam(required = false) String nomeOuMarca,
            @RequestParam(required = false) String nomeOuMarcaOuValor){
        if(nomeOuMarca!=null){
            return produtoDAO.findByNomeOrMarcas(nomeOuMarca, nomeOuMarca);
        } else {
            Double valor=Double.MIN_NORMAL;
            try {
            valor = Double.valueOf(nomeOuMarcaOuValor);    
            } catch (Exception e) {
            }
            return produtoDAO.findByNomeOrMarcasOrValorLessThan(nomeOuMarcaOuValor, nomeOuMarcaOuValor, valor);
        }
    }

    @RequestMapping(path = "/produtos/pesquisar/valor", method = RequestMethod.GET)
    public Iterable<Produto> pesquisaPorValor(@RequestParam(required = false) Double menor) {
            return produtoDAO.findByValorLessThan(menor);
    }
    
    
    
//produtos/pesquisa/nome?igual=batata    

//produtos/search/nome?l=batata

    
  */
    
    
    @RequestMapping(path = "/produtos", method = RequestMethod.GET)
    public Iterable<Produto> listar(@RequestParam(required = false , defaultValue = "0" ) int pagina) {
        PageRequest pageRequest =  new PageRequest(pagina, 5);
        return produtoDAO.findAll(pageRequest);
    }

    
    @RequestMapping(path = "/produtos", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public Produto inserir(@RequestBody Produto produto) {
        produto.setId(0);
        Produto produtoSalvo = produtoDAO.save(produto);
        return produtoSalvo;
    }

    @RequestMapping(path = "/produtos/{id}", method = RequestMethod.GET)
    public Produto recuperar(@PathVariable int id) {
        return produtoDAO.findOne(id);
    }
    
    @RequestMapping(path = "/produtos/{id}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public void atualizar(@PathVariable int id, @RequestBody Produto produto){
        if(produtoDAO.exists(id)){
            produto.setId(id);
            produtoDAO.save(produto);
        }
    
    }
    
    @RequestMapping(path= "/produtos/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    public void apagar(@PathVariable int id) {
        if(produtoDAO.exists(id)){
            produtoDAO.delete(id);
        }
        
    }

    
    
    /* 
    @RequestMapping(path = "/produtos", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public Produto inserir(@RequestBody Produto produto) {
    produto.setId(maxId++);
    produtos.add(produto);
    return produto;
    }
    @RequestMapping(path= "/produtos/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    public void apagar(@PathVariable int id) {
        Produto produto= this.recuperar(id);
        produtos.remove(produto);
    }
    
    @RequestMapping(path = "/produtos/{id}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public void atualizar(@PathVariable int id, @RequestBody Produto produto){
        Produto produtoServidor = recuperar(id);
        produtoServidor.setNome(produto.getNome());
        produtoServidor.setValor(produto.getValor());
    
    }*/
}
