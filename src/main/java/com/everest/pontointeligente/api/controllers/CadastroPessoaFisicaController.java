package com.everest.pontointeligente.api.controllers;

import java.math.BigDecimal;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

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

import com.everest.pontointeligente.api.dtos.CadastroPessoaFisicaDto;
import com.everest.pontointeligente.api.entities.Empresa;
import com.everest.pontointeligente.api.entities.Funcionario;
import com.everest.pontointeligente.api.enums.PerfilEnum;
import com.everest.pontointeligente.api.response.Response;
import com.everest.pontointeligente.api.services.EmpresaService;
import com.everest.pontointeligente.api.services.FuncionarioService;
import com.everest.pontointeligente.api.utils.PasswordUtils;

@RestController
@RequestMapping("/api/cadastro-pf")
@CrossOrigin(origins = "*")
public class CadastroPessoaFisicaController {
	
	private static final Logger LOG = LoggerFactory.getLogger(CadastroPessoaFisicaController.class);
	
	@Autowired
	private EmpresaService empresaService;
	
	@Autowired
	private FuncionarioService funcionarioService;
	
	@PostMapping
	private ResponseEntity<Response<CadastroPessoaFisicaDto>> cadastrar(
			@Valid @RequestBody CadastroPessoaFisicaDto pessoaFisicaDto, BindingResult result) 
					throws NoSuchAlgorithmException {
	
		LOG.info("Cadastrando PF: {}", pessoaFisicaDto.toString());
		
		Response<CadastroPessoaFisicaDto> response = new Response<CadastroPessoaFisicaDto>();
		
		validarDadosExistentes(pessoaFisicaDto, result);
		
		if (result.hasErrors()) {
			result.getAllErrors().forEach(error -> response.getErros().add(error.getDefaultMessage()));
			
			return ResponseEntity.badRequest().body(response);
		}
		
		Funcionario funcionario = this.converterDtoParaFuncionario(pessoaFisicaDto);
		
		Optional<Empresa> empresa = this.empresaService.buscarPorCnpj(pessoaFisicaDto.getCnpj());
		empresa.ifPresent(emp -> funcionario.setEmpresa(emp));
		this.funcionarioService.persistir(funcionario);		
		
		response.setData(this.converterCadastroPFDto(funcionario));
		
		return ResponseEntity.ok(response);
	}

	
	private CadastroPessoaFisicaDto converterCadastroPFDto(Funcionario funcionario) {
		CadastroPessoaFisicaDto pessoaFisicaDto = new CadastroPessoaFisicaDto();
		pessoaFisicaDto.setId(funcionario.getId());
		pessoaFisicaDto.setNome(funcionario.getNome());
		pessoaFisicaDto.setEmail(funcionario.getEmail());
		pessoaFisicaDto.setCpf(funcionario.getCpf());
		pessoaFisicaDto.setCnpj(funcionario.getEmpresa().getCnpj());
		
		funcionario.getQtdHorasAlmocoOpt().ifPresent(qtdHorasAlmoco -> pessoaFisicaDto
				.setQtdHorasAlmoco(Optional.of(Float.toString(qtdHorasAlmoco))));
		
		funcionario.getQtdHorasTrabalhoDiaOpt().ifPresent(
				qtdHorasTrabDia -> pessoaFisicaDto.setQtdHorasTrabalhoDia(Optional.of(Float.toString(qtdHorasTrabDia))));
		
		funcionario.getValorHoraOpt()
				.ifPresent(valorHora -> pessoaFisicaDto.setValorHora(Optional.of(valorHora.toString())));

		return pessoaFisicaDto;
	}


	private Funcionario converterDtoParaFuncionario(CadastroPessoaFisicaDto pessoaFisicaDto) {
		Funcionario funcionario = new Funcionario();
		funcionario.setNome(pessoaFisicaDto.getNome());
		funcionario.setEmail(pessoaFisicaDto.getEmail());
		funcionario.setCpf(pessoaFisicaDto.getCpf());
		funcionario.setPerfil(PerfilEnum.ROLE_USUARIO);
		funcionario.setSenha(PasswordUtils.gerarBCrypt(pessoaFisicaDto.getSenha()));
		
		pessoaFisicaDto.getQtdHorasAlmoco()
				.ifPresent(qtdHorasAlmoco -> funcionario.setQtdHorasAlmoco(Float.valueOf(qtdHorasAlmoco)));
		pessoaFisicaDto.getQtdHorasTrabalhoDia()
				.ifPresent(qtdHorasTrabDia -> funcionario.setQtdHorasTrabalhoDia(Float.valueOf(qtdHorasTrabDia)));
		pessoaFisicaDto.getValorHora().ifPresent(valorHora -> funcionario.setValorHora(new BigDecimal(valorHora)));

		return funcionario;
	}



	private void validarDadosExistentes(CadastroPessoaFisicaDto pessoaFisicaDto, BindingResult result) {
		Optional<Empresa> empresa = this.empresaService.buscarPorCnpj(pessoaFisicaDto.getCnpj());
		
		if (!empresa.isPresent()) {
			result.addError(new ObjectError("empresa", "Empresa não cadastrada."));
		}
		
		this.funcionarioService.buscarPorCpf(pessoaFisicaDto.getCpf()).ifPresent(
				fun -> result.addError(new ObjectError("funcionario", "Funcionario com cpf existente.")));
		
		this.funcionarioService.buscarPorEmail(pessoaFisicaDto.getEmail()).ifPresent(
				fun -> result.addError(new ObjectError("funcionario", "Funcionario com email existente.")));		
	}
	

}
