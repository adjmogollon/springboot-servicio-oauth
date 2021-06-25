package com.adjmogollon.app.oauth.services;



import com.adjmogollon.app.commons.usuarios.models.entity.Usuario;

public interface IUsuarioService {

	public Usuario findByUsername(String username);
	
	public Usuario update(Usuario usuario, Long id);
	
}
