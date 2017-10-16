package com.everest.pontointeligente.api.controllers;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.everest.pontointeligente.api.dtos.CadastroPessoaJuridicaDto;
import com.everest.pontointeligente.api.entities.Empresa;
import com.everest.pontointeligente.api.entities.Funcionario;
import com.everest.pontointeligente.api.enums.PerfilEnum;
import com.everest.pontointeligente.api.response.Response;
import com.everest.pontointeligente.api.services.EmpresaService;
import com.everest.pontointeligente.api.services.FuncionarioService;
import com.everest.pontointeligente.api.utils.PasswordUtils;

@RestController
@RequestMapping("/api/cadastrar-pj")
@CrossOrigin(origins = "*")
public class CadastroPessoaJuridicaController {
	
	private static final Logger log = LoggerFactory.getLogger(CadastroPessoaJuridicaController.class);
	
	@Autowired
	private EmpresaService empresaService;
	
	@Autowired	
	private FuncionarioService funcionarioService;

	
	@PostMapping
	public ResponseEntity<Response<CadastroPessoaJuridicaDto>> cadastrar(
			@Valid @RequestBody CadastroPessoaJuridicaDto pessoaJuridicaDto, BindingResult result){
		
		Response<CadastroPessoaJuridicaDto> response = new Response<CadastroPessoaJuridicaDto>();
		
		this.validarDadosExistentes(pessoaJuridicaDto, result);
		Empresa empresa = this.converterDtoParaEmpresa(pessoaJuridicaDto); 
		Funcionario funcionario = this.converteDtoParaFuncionario(pessoaJuridicaDto, result);
		
		if (result.hasErrors()) {
			log.info("validando erros {}", result.getAllErrors());
			result.getAllErrors().forEach(error -> response.getErros().add(error.getDefaultMessage()));
			
			return ResponseEntity.badRequest().body(response);
		}
		
		this.empresaService.persistir(empresa);
		funcionario.setEmpresa(empresa);
		this.funcionarioService.persistir(funcionario);
		
		response.setData(this.converterPessoaJuridicaDto(funcionario));
		
		return ResponseEntity.ok().body(response);
	}


	private CadastroPessoaJuridicaDto converterPessoaJuridicaDto(Funcionario funcionario) {
		CadastroPessoaJuridicaDto dto = new CadastroPessoaJuridicaDto();
		dto.setId(funcionario.getId());
		dto.setNome(funcionario.getNome());
		dto.setEmail(funcionario.getEmail());
		dto.setCpf(funcionario.getCpf());
		dto.setRazaoSocial(funcionario.getEmpresa().getRazaoSocial());
		dto.setCnpj(funcionario.getEmpresa().getCnpj());
		
		return dto;
	}


	private Funcionario converteDtoParaFuncionario(CadastroPessoaJuridicaDto pessoaJuridicaDto, BindingResult result) {
		Funcionario funcionario = new Funcionario();
		funcionario.setNome(pessoaJuridicaDto.getNome());
		funcionario.setEmail(pessoaJuridicaDto.getEmail());
		funcionario.setCpf(pessoaJuridicaDto.getCnpj());
		funcionario.setPerfil(PerfilEnum.ROLE_ADMIN);
		funcionario.setSenha(PasswordUtils.gerarBCrypt(pessoaJuridicaDto.getSenha()));
		
		return funcionario;
	}


	private Empresa converterDtoParaEmpresa(CadastroPessoaJuridicaDto pessoaJuridicaDto) {
		Empresa empresa = new Empresa();
		empresa.setRazaoSocial(pessoaJuridicaDto.getRazaoSocial());
		empresa.setCnpj(pessoaJuridicaDto.getCnpj());
		
		return empresa;
	}


	private void validarDadosExistentes(CadastroPessoaJuridicaDto pessoaJuridicaDto, BindingResult result) {
		this.empresaService.buscarPorCnpj(pessoaJuridicaDto.getCnpj()).ifPresent(emp -> 
				result.addError(new ObjectError("empresa", "Empresa já existe")));
				
		this.funcionarioService.buscarPorCpf(pessoaJuridicaDto.getCpf()).ifPresent(func -> 
				result.addError(new ObjectError("funcionario", "CPF já existe")));	
		
		this.funcionarioService.buscarPorEmail(pessoaJuridicaDto.getEmail()).ifPresent(func -> 
				result.addError(new ObjectError("funcionario", "Email já existe")));	
	}
}
