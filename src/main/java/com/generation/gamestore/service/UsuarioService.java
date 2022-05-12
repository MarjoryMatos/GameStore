package com.generation.gamestore.service;

import java.nio.charset.Charset;
import java.util.Optional;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.generation.gamestore.model.Usuario;
import com.generation.gamestore.model.UsuarioLogin;
import com.generation.gamestore.repository.UsuarioRepository;

@Service
public class UsuarioService {

	@Autowired
	private UsuarioRepository repository;

	//função para cadastrar um usuário
	public Optional<Usuario> cadastraUsuario(Usuario usuario) {
		
		//valida se o usuário já existe no banco
		if (repository.findByEmailUsuario(usuario.getEmailUsuario()).isPresent())
		return Optional.empty();

		//criptografo a senha do usuário
		usuario.setSenha(criptografarSenha(usuario.getSenha()));
		
		//usuário com a senha já criptografada
		return Optional.of(repository.save(usuario)); 
				
	}
	
	//função para criptografar a senha digitada pelo usuário
	private String criptografarSenha(String senha) {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();		
		return encoder.encode(senha);

	}
	
	public Optional<UsuarioLogin> autenticaUsuario(Optional<UsuarioLogin> usuarioLogin){
		Optional<Usuario> usuario = repository.findByEmailUsuario(usuarioLogin.get().getEmailUsuario());
		
		if (usuario.isPresent()) {
			if (compararSenhas(usuarioLogin.get().getSenha(), usuario.get().getSenha())) {
				usuarioLogin.get().setId(usuario.get().getId());
				usuarioLogin.get().setNome(usuario.get().getNome());
				usuarioLogin.get().setFoto(usuario.get().getFoto());
				usuarioLogin.get().setToken(gerarBasicToken(usuarioLogin.get().getEmailUsuario(), usuarioLogin.get().getSenha()));
				usuarioLogin.get().setSenha(usuario.get().getSenha());
				
				return usuarioLogin;
			}
		}	
		return Optional.empty();
	}
	
	private boolean compararSenhas(String senhaDigitada, String senhaBanco) {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		return encoder.matches(senhaDigitada, senhaBanco);

	}
	
	private String gerarBasicToken(String usuario, String senha) {

		String token = usuario + ":" + senha;
		byte[] tokenBase64 = Base64.encodeBase64(token.getBytes(Charset.forName("US-ASCII")));
		return "Basic " + new String(tokenBase64);

	}
	
	public Optional<Usuario> atualizarUsuario(Usuario usuario) {
		if (repository.findById(usuario.getId()).isPresent()) {
			Optional<Usuario> buscarUsuario = repository.findByEmailUsuario(usuario.getEmailUsuario());
			if (buscarUsuario.isPresent()) {
				if (buscarUsuario.get().getId() != usuario.getId())

					throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Usuario já existe!", null);
			}

			usuario.setSenha(criptografarSenha(usuario.getSenha()));

			return Optional.of(repository.save(usuario));
		}
		throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Usuario não encontrado!", null);

	}


}


