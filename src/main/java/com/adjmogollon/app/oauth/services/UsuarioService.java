package com.adjmogollon.app.oauth.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.adjmogollon.app.commons.usuarios.models.entity.Usuario;
import com.adjmogollon.app.oauth.clients.UsuarioFeingClient;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class UsuarioService implements UserDetailsService {

	@Autowired
	private UsuarioFeingClient client;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		Usuario usuario = client.findByUsername(username);

		if (usuario == null) {
			log.error("Error en el login, no existe el usuario ' " + username + "' en el sistema");
			throw new UsernameNotFoundException(
					"Error en el login, no existe el usuario ' " + username + "' en el sistema");
		}

		List<GrantedAuthority> authorities = usuario.getRoles()
				.stream()
				.map(role -> new SimpleGrantedAuthority(role.getNombre()))
				.peek(authoriry -> log.info("Role: " + authoriry.getAuthority()))
				.collect(Collectors.toList());
		
		log.info("Usuario autenticado: " + username);
		
		return new User(usuario.getUsername(), usuario.getPassword(), usuario.getEnabled(), true, true, true,
				authorities);
	}

}
