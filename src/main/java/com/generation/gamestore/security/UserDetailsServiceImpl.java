package com.generation.gamestore.security;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.generation.gamestore.model.Usuario;
import com.generation.gamestore.repository.UsuarioRepository;


@Service
public class UserDetailsServiceImpl implements UserDetailsService{
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
		Optional<Usuario> usuario = usuarioRepository.findByEmailUsuario(userName);
		
		usuario.orElseThrow(() -> new UsernameNotFoundException (userName + " Este usuario não foi encontrado"));
		return usuario.map(UserDetailsImpl::new).get();
	}
	
}


