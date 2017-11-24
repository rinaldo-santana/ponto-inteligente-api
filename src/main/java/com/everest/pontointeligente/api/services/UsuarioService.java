package com.everest.pontointeligente.api.services;

import java.util.Optional;

import com.everest.pontointeligente.api.security.entities.Usuario;

public interface UsuarioService {
	
	Optional<Usuario> buscarPorEmail(String email);
}
