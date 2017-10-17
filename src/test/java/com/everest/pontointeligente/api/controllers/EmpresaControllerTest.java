package com.everest.pontointeligente.api.controllers;


import static org.hamcrest.CoreMatchers.equalTo;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.everest.pontointeligente.api.entities.Empresa;
import com.everest.pontointeligente.api.services.EmpresaService;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class EmpresaControllerTest {
	
	@MockBean
	private EmpresaService empresaService;

	@Autowired
	private MockMvc mvc;
	
	private static final String BUSCAR_EMPRESA_CNPJ_URL = "/api/empresas/cnpj/";
	private static final Long ID = Long.valueOf(1);
	private static final String CNPJ = "85771435000192";
	private static final String RAZAO_SOCIAL = "Empresa YXZ";
	
	
	@Test
	public void buscarEmpresaPorCnpjInvalido() throws Exception {
		BDDMockito.given(this.empresaService.buscarPorCnpj(Mockito.anyString())).willReturn(Optional.empty());
		
		mvc.perform(MockMvcRequestBuilders.get(BUSCAR_EMPRESA_CNPJ_URL + CNPJ).accept(MediaType.APPLICATION_JSON))
						.andExpect(status().isBadRequest())
						.andExpect(jsonPath("$.erros").value("Empresa não existe para o cnpj: " + CNPJ));

	}
	
	@Test
	public void buscarEmpresaPorCnpjValido() throws Exception {
		BDDMockito.given(this.empresaService.buscarPorCnpj(Mockito.anyString())).willReturn(Optional.of(this.obterEmpresaValida()));
		
		mvc.perform(MockMvcRequestBuilders.get(BUSCAR_EMPRESA_CNPJ_URL + CNPJ).accept(MediaType.APPLICATION_JSON))
						.andExpect(status().isOk())
						.andExpect(jsonPath("$.data.id").value(ID))
						.andExpect(jsonPath("$.data.razaoSocial", equalTo(RAZAO_SOCIAL)))
						.andExpect(jsonPath("$.data.cnpj", equalTo(CNPJ)))
						.andExpect(jsonPath("$.erros").isEmpty());
			
	}

	private Empresa obterEmpresaValida() {
		Empresa empresa = new Empresa();
		empresa.setCnpj(CNPJ);
		empresa.setRazaoSocial(RAZAO_SOCIAL);
		empresa.setId(ID);
		
		return empresa;
	}
}
