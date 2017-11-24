/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifrs.restinga.ads.jezer.restExemplo.aut;
import br.edu.ifrs.restinga.ads.jezer.restExemplo.modelo.Usuario;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
public class UsuarioAut extends User {
    private Usuario usuario;
    public UsuarioAut(Usuario usuario) {
        super(usuario.getLogin(),
                usuario.getSenha(),
                AuthorityUtils.createAuthorityList(
                        usuario.getPermissoes().toArray(new String[]{})));
        this.usuario=usuario;
    }

    public Usuario getUsuario() {
        return usuario;
    }
    
    
}
