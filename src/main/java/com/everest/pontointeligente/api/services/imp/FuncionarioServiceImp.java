package com.everest.pontointeligente.api.services.imp;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.everest.pontointeligente.api.entities.Funcionario;
import com.everest.pontointeligente.api.repositories.FuncionarioRepository;
import com.everest.pontointeligente.api.services.FuncionarioService;

@Service
public class FuncionarioServiceImp implements FuncionarioService {

	private static final Logger logger = LoggerFactory.getLogger(FuncionarioServiceImp.class);
	
	@Autowired
	private FuncionarioRepository funcionarioRepository; 
	
	@Override
	public Optional<Funcionario> buscarPorId(Long id) {
		logger.info("Buscando um funcionario pelo id {}", id);
		return Optional.ofNullable(this.funcionarioRepository.findOne(id));
	}

	@Override
	public Optional<Funcionario> buscarPorCpf(String cpf) {
		logger.info("Buscando um funcionario pelo cpf: {}", cpf);
		return Optional.ofNullable(this.funcionarioRepository.findByCpf(cpf));
	}

	@Override
	public Optional<Funcionario> buscarPorEmail(String email) {
		logger.info("Buscando um funcionario pelo email: {}", email);
		return Optional.ofNullable(this.funcionarioRepository.findByEmail(email));
	}

	@Override
	public Funcionario persistir(Funcionario funcionario) {
		logger.info("Persistindo funcionario: {}", funcionario);
		return this.funcionarioRepository.save(funcionario);
	}

}
