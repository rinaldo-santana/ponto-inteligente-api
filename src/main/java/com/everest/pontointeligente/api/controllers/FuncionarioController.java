package com.everest.pontointeligente.api.controllers;

import java.math.BigDecimal;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.everest.pontointeligente.api.dtos.FuncionarioDto;
import com.everest.pontointeligente.api.entities.Funcionario;
import com.everest.pontointeligente.api.response.Response;
import com.everest.pontointeligente.api.services.FuncionarioService;
import com.everest.pontointeligente.api.utils.PasswordUtils;

@RestController
@RequestMapping("/api/funcionarios")
@CrossOrigin(origins = "*")
public class FuncionarioController {
	
	@Autowired
	private FuncionarioService funcionarioService;

	
	@PutMapping("/{id}")
	public ResponseEntity<Response<FuncionarioDto>> atualizar(@PathVariable("id") Long id, 
				@Valid @RequestBody FuncionarioDto funcionarioDto, BindingResult result) {
		
		Response<FuncionarioDto> response = new Response<FuncionarioDto>();
		
		Funcionario funcionario = this.validarFuncionarioPorId(id, result).get();
		
		this.atualizarDadosFuncionario(funcionario, funcionarioDto, result);
			
		
		if (result.hasErrors()) {
			result.getAllErrors().forEach(error -> response.getErros().add(error.getDefaultMessage()));
			return ResponseEntity.badRequest().body(response);
		}

		this.funcionarioService.persistir(funcionario);
		response.setData(this.converterFuncionarioDto(funcionario));
		
		
		return ResponseEntity.ok().body(response);
	}



	private FuncionarioDto converterFuncionarioDto(Funcionario funcionario) {
		FuncionarioDto funcionarioDto = new FuncionarioDto();
		funcionarioDto.setId(funcionario.getId());
		funcionarioDto.setEmail(funcionario.getEmail());
		funcionarioDto.setNome(funcionario.getNome());
		funcionario.getQtdHorasAlmocoOpt().ifPresent(
				qtdHorasAlmoco -> funcionarioDto.setQtdHorasAlmoco(Optional.of(Float.toString(qtdHorasAlmoco))));
		funcionario.getQtdHorasTrabalhoDiaOpt().ifPresent(
				qtdHorasTrabDia -> funcionarioDto.setQtdHorasTrabalhoDia(Optional.of(Float.toString(qtdHorasTrabDia))));
		funcionario.getValorHoraOpt()
				.ifPresent(valorHora -> funcionarioDto.setValorHora(Optional.of(valorHora.toString())));

		return funcionarioDto;
	}



	private void atualizarDadosFuncionario(Funcionario funcionario, FuncionarioDto funcionarioDto,
			BindingResult result) {
		
		funcionario.setNome(funcionarioDto.getNome());

		if (!funcionario.getEmail().equals(funcionarioDto.getEmail())) {
			this.funcionarioService.buscarPorEmail(funcionarioDto.getEmail())
					.ifPresent(func -> result.addError(new ObjectError("email", "Email já existente.")));
			funcionario.setEmail(funcionarioDto.getEmail());
		}

		funcionario.setQtdHorasAlmoco(null);
		funcionarioDto.getQtdHorasAlmoco()
				.ifPresent(qtdHorasAlmoco -> funcionario.setQtdHorasAlmoco(Float.valueOf(qtdHorasAlmoco)));

		funcionario.setQtdHorasTrabalhoDia(null);
		funcionarioDto.getQtdHorasTrabalhoDia()
				.ifPresent(qtdHorasTrabDia -> funcionario.setQtdHorasTrabalhoDia(Float.valueOf(qtdHorasTrabDia)));

		funcionario.setValorHora(null);
		funcionarioDto.getValorHora().ifPresent(valorHora -> funcionario.setValorHora(new BigDecimal(valorHora)));

		if (funcionarioDto.getSenha().isPresent()) {
			funcionario.setSenha(PasswordUtils.gerarBCrypt(funcionarioDto.getSenha().get()));
		}		
		
	}



	private Optional<Funcionario> validarFuncionarioPorId(Long id, BindingResult result) {
		Optional<Funcionario> funcionario = this.funcionarioService.buscarPorId(id);		
		if (!funcionario.isPresent()) {
			result.addError(new ObjectError("funcionario", "Funcionario não existe."));
		}
		
		return funcionario;
	}
}
