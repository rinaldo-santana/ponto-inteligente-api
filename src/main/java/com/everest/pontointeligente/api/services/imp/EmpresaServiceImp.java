/**
 * 
 */
package com.everest.pontointeligente.api.services.imp;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.everest.pontointeligente.api.entities.Empresa;
import com.everest.pontointeligente.api.repositories.EmpresaRepository;
import com.everest.pontointeligente.api.services.EmpresaService;

/**
 * @author rinaldo
 *
 */
@Service
public class EmpresaServiceImp implements EmpresaService {

	private static final Logger logger = LoggerFactory.getLogger(EmpresaServiceImp.class);
	
	@Autowired
	private EmpresaRepository empresaRepository;
	
	@Override
	public Optional<Empresa> buscarPorCnpj(String cnpj) {
		logger.info("Buscando uma empresa para o cnpj {}", cnpj);
		return Optional.ofNullable(this.empresaRepository.findByCnpj(cnpj));
	}

	@Override
	public Empresa persistir(Empresa empresa) {
		logger.info("Persistindo a empresa: {}", empresa);
		return this.empresaRepository.save(empresa);
	}

}
