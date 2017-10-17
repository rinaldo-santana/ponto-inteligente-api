package com.everest.pontointeligente.api.controllers;

import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.everest.pontointeligente.api.dtos.EmpresaDto;
import com.everest.pontointeligente.api.entities.Empresa;
import com.everest.pontointeligente.api.response.Response;
import com.everest.pontointeligente.api.services.EmpresaService;

@RestController
@RequestMapping("/api/empresas")
@CrossOrigin(origins = "*")
public class EmpresaController {
	
	@Autowired
	private EmpresaService empresaService;
	
	
	@PostMapping
	public ResponseEntity<Response<EmpresaDto>>  cadastrar(@Valid @RequestBody EmpresaDto empresaDto, BindingResult result) {
		Response<EmpresaDto> response = new Response<EmpresaDto>();
		
		this.empresaService.buscarPorCnpj(empresaDto.getCnpj()).ifPresent(
				emp -> result.addError(new ObjectError("empresa", "Empresa já cadastrada.")));
		
		if (result.hasErrors()) {
			result.getAllErrors().forEach(error -> response.getErros().add(error.getDefaultMessage()));
			
			return ResponseEntity.badRequest().body(response);
		}
		
		
		Empresa empresa = converveterEmpresaDtoParaEmpresa(empresaDto);		
		
		empresa = this.empresaService.persistir(empresa);
	
		EmpresaDto empresaDtoResult = this.converveterEmpresaParaEmpresaDto(empresa);
		
		response.setData(empresaDtoResult);
		
		return ResponseEntity.ok().body(response);
	}
	
	
	@GetMapping("/cnpj/{cnpj}")
	public ResponseEntity<Response<EmpresaDto>> buscarPorCnpj(@PathVariable String cnpj) {
		Response<EmpresaDto> response = new Response<EmpresaDto>();
		
		Optional<Empresa> empresa = this.empresaService.buscarPorCnpj(cnpj);
		
		if (!empresa.isPresent()) {
			
			response.getErros().add("Empresa não existe para o cnpj: " + cnpj);
			return ResponseEntity.badRequest().body(response);
		}
		
		
		
		response.setData(this.converveterEmpresaParaEmpresaDto(empresa.get()));
		
		return ResponseEntity.ok().body(response);
	}
	
	

	private EmpresaDto converveterEmpresaParaEmpresaDto(Empresa empresa) {
		EmpresaDto empresaDto = new EmpresaDto();
		empresaDto.setId(empresa.getId());
		empresaDto.setCnpj(empresa.getCnpj());
		empresaDto.setRazaoSocial(empresa.getRazaoSocial());
				
		return empresaDto;
	}


	private Empresa converveterEmpresaDtoParaEmpresa(EmpresaDto empresaDto) {
		Empresa empresa = new Empresa();
		empresa.setId(empresaDto.getId());
		empresa.setCnpj(empresaDto.getCnpj());
		empresa.setRazaoSocial(empresaDto.getRazaoSocial());
		
		return empresa;
	}


}
