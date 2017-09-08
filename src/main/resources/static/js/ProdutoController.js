/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


class ProdutoController {

    carregarLista(lista){
        let corpoTabela= document.getElementById("corpoTabela");
        for (let i = 0; i < lista.length; i++) {
            let linha= corpoTabela.insertRow();
            
            let nomeCell = linha.insertCell();
            nomeCell.innerHTML=lista[i].nome;
            
            let valorCell = linha.insertCell();
            valorCell.innerHTML=lista[i].valor;
            
        }
        
    }
    
    listar(){
        fetch("produtos/", {method:"GET"})
                .then((resultado)=>{ 
                    if(resultado.ok) {
                        // retorno ok
                        resultado.json().then(
                              (lista)=>{
                                  this.carregarLista(lista);
                                  console.log(lista);
                              }
                            );
                
                    } else {
                        // tratar o erro 
                        console.log("Erro na excecução");
                        
                        
                    }
                
                }  
                
                );
        
    }
    
    confirmar(){
        let nome= document.getElementById("nome").value;
        let valor =document.getElementById("valor").valueAsNumber;
        
        let item={
            nome:nome,
            valor:valor
        };
        
        this.inserir(item);
    }
    
    inserir(item){
        fetch("produtos/", {
            method:"POST",
            headers: new Headers({
                'Content-Type': 'application/json'
               }),
            body:JSON.stringify(item)
        }).then((resultado)=>{ 
            if(resultado.ok){
                this.listar();
            } else {
                console.log("Erro na execução");
            }
        
        });
        
    } 
    
    
    
}