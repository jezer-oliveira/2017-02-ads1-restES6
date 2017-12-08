/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifrs.restinga.ads.jezer.restExemplo.controller;

import br.edu.ifrs.restinga.ads.jezer.restExemplo.aut.ForbiddenException;
import br.edu.ifrs.restinga.ads.jezer.restExemplo.aut.UsuarioAut;
import br.edu.ifrs.restinga.ads.jezer.restExemplo.dao.UsuarioDAO;
import br.edu.ifrs.restinga.ads.jezer.restExemplo.modelo.Usuario;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
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
public class Usuarios {

    public static final PasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder();

    @RequestMapping(path = "/usuarios", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public Usuario inserir(@AuthenticationPrincipal UsuarioAut usuarioAut, @RequestBody Usuario usuario) {
        usuario.setId(0);
        usuario.setSenha(PASSWORD_ENCODER.encode(usuario.getNovaSenha()));

        if (usuarioAut == null || !usuarioAut.getUsuario().getPermissoes().contains("administrador")) {
            ArrayList<String> permissao = new ArrayList<String>();
            permissao.add("usuario");
            usuario.setPermissoes(permissao);
        }
        Usuario usuarioSalvo = usuarioDAO.save(usuario);
        return usuarioSalvo;
    }

    @Autowired
    UsuarioDAO usuarioDAO;

    @PreAuthorize("hasAuthority('administrador')")
    @RequestMapping(path = "/usuarios", method = RequestMethod.GET)
    public Iterable<Usuario> listar(@RequestParam(required = false, defaultValue = "0") int pagina) {
        PageRequest pageRequest = new PageRequest(pagina, 5);
        return usuarioDAO.findAll(pageRequest);
    }

    @RequestMapping(path = "/usuarios/{id}", method = RequestMethod.GET)
    public Usuario recuperar(@AuthenticationPrincipal UsuarioAut usuarioAut, @PathVariable int id) {
        if (usuarioAut.getUsuario().getId() == id
                || usuarioAut.getUsuario().getPermissoes().contains("administrador")) {
            return usuarioDAO.findOne(id);
        } else {
            throw new ForbiddenException("Não é permitido acessar dados de outro usuários");
        }
    }

    @RequestMapping(path = "/usuarios/{id}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public void atualizar(@PathVariable int id, @RequestBody Usuario usuario) {
        if (usuarioDAO.exists(id)) {
            usuario.setId(id);
            usuarioDAO.save(usuario);
        }
    }

    @RequestMapping(path = "/usuarios/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    public void apagar(@PathVariable int id) {
        if (usuarioDAO.exists(id)) {
            usuarioDAO.delete(id);
        }

    }

    public static final String SEGREDO
            = "string grande para c*, usada como chave para assinatura! Queijo!";

    @RequestMapping(path = "/usuarios/login", method = RequestMethod.GET)
    public ResponseEntity<Usuario> login(@AuthenticationPrincipal UsuarioAut usuarioAut)
            throws IllegalArgumentException, UnsupportedEncodingException {
        Algorithm algorithm = Algorithm.HMAC256(SEGREDO);
        Calendar agora = Calendar.getInstance();
        agora.add(Calendar.MINUTE, 4);
        Date expira = agora.getTime();

        String token = JWT.create()
                .withClaim("id", usuarioAut.getUsuario().getId()).
                withExpiresAt(expira).
                sign(algorithm);
        HttpHeaders respHeaders = new HttpHeaders();
        respHeaders.set("token", token);

        return new ResponseEntity<>(usuarioAut.getUsuario(), respHeaders, HttpStatus.OK);
    }

    @RequestMapping(path = "/usuarios/login/google", method = RequestMethod.GET)
    public ResponseEntity<Usuario> loginGoogle(@RequestParam String googleToken)
            throws IllegalArgumentException, UnsupportedEncodingException, GeneralSecurityException, IOException {

        HttpTransport transport = new NetHttpTransport();
        JsonFactory jsonFactory = new JacksonFactory();
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(transport, jsonFactory)
                .setAudience(Collections.singletonList("69512171033-q6qt702o2lanhrcnp2qdvb84t3kmfuph.apps.googleusercontent.com"))
                .build();

        GoogleIdToken idToken = verifier.verify(googleToken);
        if (idToken != null) {
            GoogleIdToken.Payload payload = idToken.getPayload();

            // Print user identifier
            String userId = payload.getSubject();
            System.out.println("User ID: " + userId);

            // Get profile information from payload
            String email = payload.getEmail();
            boolean emailVerified = Boolean.valueOf(payload.getEmailVerified());
            String name = (String) payload.get("name");
            String pictureUrl = (String) payload.get("picture");
            String locale = (String) payload.get("locale");
            String familyName = (String) payload.get("family_name");
            String givenName = (String) payload.get("given_name");
            Usuario usuario = usuarioDAO.findByEmail(email);
            if (usuario == null) {
                usuario = new Usuario();
                usuario.setLogin(email);
                usuario.setSenha("vazio");
                usuario.setNome(name);
                usuario.setEmail(email);
                ArrayList<String> permissao = new ArrayList<String>();
                permissao.add("usuario");
                usuario.setPermissoes(permissao);
                usuario = usuarioDAO.save(usuario);
            }

            Algorithm algorithm = Algorithm.HMAC256(SEGREDO);
            Calendar agora = Calendar.getInstance();
            agora.add(Calendar.MINUTE, 4);
            Date expira = agora.getTime();
///http://localhost:3000/callback#access_token=eUL1cgOj13GSrid7JLsYbfcfepTDD2xc&expires_in=7200&token_type=Bearer&state=tb51mnicasO96UtQAwRYVDBfCDF0nE~R&id_token=eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImtpZCI6IlJUTTBNVE00UlVRM056VkNOMFZETlRBNE5UZzRRVE5HT0RnelJqQTFORVF4UmpNME56VTBSUSJ9.eyJpc3MiOiJodHRwczovL2RzdzEuYXV0aDAuY29tLyIsInN1YiI6Imdvb2dsZS1vYXV0aDJ8MTE2OTgxNTg5NTIwNDE4MDI3MjY2IiwiYXVkIjoiQWZOcFpmb2RmYnduT2dZckRWZ1Y2Q3JscmhOS0xBNEIiLCJpYXQiOjE1MTI3MzI0MDEsImV4cCI6MTUxMjc2ODQwMSwiYXRfaGFzaCI6Il80RUF1eHZlZmFJQW5aSmJRdFhDbVEiLCJub25jZSI6ImZyWmh2YW9ofkFDZ1Iwcy4zeHQyeWVYTjZoUjZJZENOIn0.KK0rTlUYzLPaeEd5RTL-ajqgsi5amFmcyEmapcxMCmOygH_kpZOWlCrYbHXv-CZpuWvwjvV_CLZKkFhHw134LEceOd9tWa9MBXCUkTtOhR6qzFhhcdzJEGZ08D4wRYqEEZR13f6eXA_3kQzdJ8I623qRWaJXvG-dONP1n2xZtPyyJROi5N55rvh2fNe6kze6pXsei64CzVh0cQmfEgoBQViILKJb8AvfjV0ZcftbR8QRjfdnMKNj-iQNfF36-DtOTklvnfZhsNpkCyjMSLExYh8c8Wd0U2k5K7kOI_CnThkCImJBjY__WarZDbIXkMsUDyf9FUGBlcilmMUDaGCfnQ
            String token = JWT.create()
                    .withClaim("id", usuario.getId()).
                    withExpiresAt(expira).
                    sign(algorithm);
            HttpHeaders respHeaders = new HttpHeaders();
            respHeaders.set("token", token);

            return new ResponseEntity<>(usuario, respHeaders, HttpStatus.OK);
        }
        throw new ForbiddenException("Erro de autenticação do google");

    }

    @RequestMapping(path = "/usuarios/{id}/foto", method = RequestMethod.POST)
    public void inserirFoto(@PathVariable int id,
            @RequestParam("arquivo") MultipartFile uploadfiles) {
        Usuario usuario = usuarioDAO.findOne(id);

        try {
            usuario.setTipoFoto(uploadfiles.getContentType());
            usuario.setFoto(uploadfiles.getBytes());
            usuarioDAO.save(usuario);

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @RequestMapping(value = "/usuarios/{id}/foto", method = RequestMethod.GET)
    public ResponseEntity<InputStreamResource> recuperarFoto(@PathVariable int id)
            throws IOException {
        Usuario usuario = usuarioDAO.findOne(id);
        if (usuario.getFoto() == null) {
            return ResponseEntity.notFound().build();
        }
        HttpHeaders respHeaders = new HttpHeaders();
        respHeaders.setContentType(MediaType.valueOf(usuario.getTipoFoto()));
        InputStreamResource img
                = new InputStreamResource(new ByteArrayInputStream(usuario.getFoto()));
        return new ResponseEntity<InputStreamResource>(img, respHeaders, HttpStatus.OK);
    }

}
