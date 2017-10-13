package com.everest.pontointeligente.api.services;

import java.util.Optional;

import com.everest.pontointeligente.api.entities.Funcionario;

public interface FuncionarioService {

	
	/**
	 * Busca um funcionario pelo id informado.
	 * @param id
	 * @return Optional<Funcionario>
	 */
	Optional<Funcionario> buscarPorId(Long id);
	
	
	/**
	 * Busca um funcionario por um cpf informado.
	 * @param cpf
	 * @return Optional<Funcionario>
	 */
	Optional<Funcionario> buscarPorCpf(String cpf);
	
	
	/**
	 * Busca um funcionario por um email informado.
	 * @param email
	 * @return Optional<Funcionario>
	 */
	Optional<Funcionario> buscarPorEmail(String email);
	
	
	/**
	 * Persiste um funcionario no banco de dados.
	 * @param funcionario
	 * @return Funcioario
	 */
	Funcionario persistir(Funcionario funcionario);

}
