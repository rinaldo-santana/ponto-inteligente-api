package com.everest.pontointeligente.api.repositories;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.math.BigDecimal;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.everest.pontointeligente.api.entities.Empresa;
import com.everest.pontointeligente.api.entities.Funcionario;
import com.everest.pontointeligente.api.enums.PerfilEnum;
import com.everest.pontointeligente.api.utils.PasswordUtils;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class FuncionarioRepositoryTest {
	
	private static final String EMAIL = "joao@gmail.com.br";
	private static final String CPF = "57782269971";
	
	
	@Autowired
	private FuncionarioRepository funcionarioRepository;

	@Autowired
	private EmpresaRepository empresaRepository;
	
	@Before
	public void setUp() throws Exception {
		Empresa empresa = this.empresaRepository.save(obterEmpresa());
		this.funcionarioRepository.save(obterFuncionario(empresa));	
	}


	@After
	public void tearDown() {
		this.empresaRepository.deleteAll();
	}
	
	@Test
	public void testBuscarFuncionarioPorEmail() {
		Funcionario funcionario = this.funcionarioRepository.findByEmail(EMAIL);
		
		assertEquals(EMAIL, funcionario.getEmail());
	}
	
	@Test
	public void testBuscarFuncioarioPorCpf() {
		Funcionario funcionario = this.funcionarioRepository.findByCpf(CPF);
		
		assertEquals(CPF, funcionario.getCpf());
	}

	@Test
	public void testBuscarFuncionarioPorCpfOuPorEmail() {
		Funcionario funcionario = this.funcionarioRepository.findByEmailOrCpf(EMAIL, CPF);
		
		assertNotNull(funcionario);
	}
	
	
	@Test
	public void testBuscarFuncionarioPorCpfEPorEmailInvalido() {
		Funcionario funcionario = this.funcionarioRepository.findByEmailOrCpf("", CPF);
		
		assertNotNull(funcionario);
	}
	
	@Test
	public void testBuscarFuncionarioPorCpfInvalidoOuPorEmail() {
		Funcionario funcionario = this.funcionarioRepository.findByEmailOrCpf(EMAIL, "");
		
		assertNotNull(funcionario);
	}
	
	private Empresa obterEmpresa() {
		Empresa empresa = new Empresa();
		empresa.setCnpj("02125487000198");
		empresa.setRazaoSocial("Empresa de teste Ltda");

		return empresa;		
	}

	private Funcionario obterFuncionario(Empresa empresa) {
		Funcionario funcionario = new Funcionario();
		funcionario.setCpf(CPF);
		funcionario.setEmail(EMAIL);
		funcionario.setNome("Fulando de Tal e Silva");
		funcionario.setSenha(PasswordUtils.gerarBCrypt("123456"));
		funcionario.setPerfil(PerfilEnum.ROLE_ADMIN);
		funcionario.setEmpresa(empresa);
		funcionario.setQtdHorasAlmoco(0f);
		funcionario.setQtdHorasTrabalhoDia(0f);
		funcionario.setValorHora(BigDecimal.ZERO);
		
		return funcionario;
	}
	
}
