package com.everest.pontointeligente.api.services;

import java.util.Optional;

import com.everest.pontointeligente.api.entities.Empresa;

public interface EmpresaService {
	
	/**
	 * Busca uma empresa por um cnpj
	 * @param cnpj
	 * @return Optional<Empresa
	 */
	Optional<Empresa> buscarPorCnpj(String cnpj);
	
	/**
	 * Cadastra uma entidade empresa no banco de dados
	 * @param empresa
	 * @return Empresa
	 */
	Empresa persistir(Empresa empresa);
}
