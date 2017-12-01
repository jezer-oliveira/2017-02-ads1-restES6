/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifrs.restinga.ads.jezer.restExemplo.controller;

import br.edu.ifrs.restinga.ads.jezer.restExemplo.dao.ProdutoDAO;
import br.edu.ifrs.restinga.ads.jezer.restExemplo.modelo.Fornecedor;
import br.edu.ifrs.restinga.ads.jezer.restExemplo.modelo.Marca;
import br.edu.ifrs.restinga.ads.jezer.restExemplo.modelo.Modelo;
import br.edu.ifrs.restinga.ads.jezer.restExemplo.modelo.Produto;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author jezer
 */
@RestController
@RequestMapping(path = "/api")
public class Produtos {

    @Autowired
    ProdutoDAO produtoDAO;

    @RequestMapping(path = "/produtos", method = RequestMethod.GET)
    public Iterable<Produto> listar(@RequestParam(required = false, defaultValue = "0") int pagina) {
        PageRequest pageRequest = new PageRequest(pagina, 5);
        return produtoDAO.findAll(pageRequest);
    }

    @RequestMapping(path = "/produtos/{id}/fornecedor", method = RequestMethod.GET)
    public Fornecedor recuperarFornecedor(@PathVariable int id) {
        return produtoDAO.findOne(id).getFornecedor();
    }

    @RequestMapping(path = "/produtos/{id}/marcas", method = RequestMethod.GET)
    public Iterable<Marca> recuperarMarcas(@PathVariable int id) {
        return produtoDAO.findOne(id).getMarcas();
    }

    @RequestMapping(path = "/produtos/{id}/marcas", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public void adicionaMarcas(@PathVariable int id, @RequestBody Marca marca) {
        Produto findOne = produtoDAO.findOne(id);
        findOne.getMarcas().add(marca);
        produtoDAO.save(findOne);
    }

    @RequestMapping(path = "/produtos/{id}/modelos", method = RequestMethod.GET)
    public Iterable<Modelo> recuperarModelos(@PathVariable int id) {
        return produtoDAO.findOne(id).getModelos();
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
    public void atualizar(@PathVariable int id, @RequestBody Produto produto) {
        if (produtoDAO.exists(id)) {
            produto.setId(id);
            produtoDAO.save(produto);
        }

    }

    @RequestMapping(path = "/produtos/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    public void apagar(@PathVariable int id) {
        if (produtoDAO.exists(id)) {
            produtoDAO.delete(id);
        }

    }

    static final String CAMINHO="D:\\imgs";
    
    @RequestMapping(path = "/produtos/{id}/foto", method = RequestMethod.POST)
    public void inserirFoto(@PathVariable int id,
            @RequestParam("arquivo") MultipartFile arquivo) {
        Produto produto = produtoDAO.findOne(id);
        if (produto == null) {
            return;
        }
        produto.setTipoFoto(arquivo.getContentType());
        produtoDAO.save(produto);
        String dir =CAMINHO+File.separator+"produtos";
        try {
            String filePath = dir +File.separator+produto.getId() ;
            File dest = new File(filePath);
            arquivo.transferTo(dest);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @RequestMapping(value = "/produtos/{id}/foto", method = RequestMethod.GET)
    public ResponseEntity<InputStreamResource> recuperarFoto(@PathVariable int id)
            throws IOException {
        Produto produto = produtoDAO.findOne(id);
        
        String arquivo =CAMINHO+File.separator+"produtos"+File.separator+produto.getId();

        System.out.println(arquivo);
            if (!new File(arquivo).exists()) {
                return ResponseEntity.notFound().build();
            }
        
        HttpHeaders respHeaders = new HttpHeaders();
        respHeaders.setContentType(MediaType.valueOf(produto.getTipoFoto()));
        InputStreamResource img
                = new InputStreamResource(new FileInputStream(arquivo));
        return new ResponseEntity<InputStreamResource>(img, respHeaders, HttpStatus.OK);
    }

    /*
    String ApplicationPath = 
        ContextLoader.getCurrentWebApplicationContext().getServletContext().getRealPath("");
     */
}
