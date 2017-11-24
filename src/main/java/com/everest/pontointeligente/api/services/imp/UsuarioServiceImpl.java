package com.everest.pontointeligente.api.services.imp;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import com.everest.pontointeligente.api.security.entities.Usuario;
import com.everest.pontointeligente.api.security.repositories.UsuarioRepository;
import com.everest.pontointeligente.api.services.UsuarioService;

public class UsuarioServiceImpl implements UsuarioService{

	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Override
	public Optional<Usuario> buscarPorEmail(String email) {		
		return Optional.ofNullable(this.usuarioRepository.findByEmail(email));
	}

	
	
}
