package com.everest.pontointeligente.api.repositories;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.everest.pontointeligente.api.entities.Empresa;
import com.everest.pontointeligente.api.entities.Funcionario;
import com.everest.pontointeligente.api.entities.Lancamento;
import com.everest.pontointeligente.api.enums.PerfilEnum;
import com.everest.pontointeligente.api.enums.TipoEnum;
import com.everest.pontointeligente.api.utils.PasswordUtils;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class LancamentoRepositoryTest {

	private Long funcionarioId;
	
	@Autowired
	private EmpresaRepository empresaRepository;
	
	@Autowired
	private FuncionarioRepository funcionarioRepository;
	
	@Autowired
	private LancamentoRepository lancamentoRepository;
	
	
	@Before
	public void setUp() throws Exception {
		Empresa empresa = this.empresaRepository.save(obterEmpresa());
		
		Funcionario funcionario = this.funcionarioRepository.save(obterFuncionario(empresa));
		funcionarioId = funcionario.getId();
		
		this.lancamentoRepository.save(obterLancamento(funcionario));
		this.lancamentoRepository.save(obterLancamento(funcionario));
	}
	
	
	@After
	public void tearDown() {
		this.empresaRepository.deleteAll();
	}

	
	@Test
	public void testBuscarLancamentoPorFuncionarioId() {
		List<Lancamento> lancamentos =  this.lancamentoRepository.findByFuncionarioId(funcionarioId);
		
		assertEquals(2, lancamentos.size());
	}
	
	
	@Test
	public void testBuscarLancamentoPorFuncionarioIdPaginado() {
		PageRequest page = new PageRequest(0, 10);
		
		Page<Lancamento> lancamentos =  this.lancamentoRepository.findByFuncionarioId(funcionarioId, page);
		
		assertEquals(2, lancamentos.getTotalElements());
	}	

	private Lancamento obterLancamento(Funcionario funcionario) {
		Lancamento lancamento = new Lancamento();
		lancamento.setDescricao("Saída para almoço");
		lancamento.setLocalizacao("local");
		lancamento.setData(new Date());
		lancamento.setTipo(TipoEnum.INICIO_ALMOCO);
		lancamento.setFuncionario(funcionario);
		
		return lancamento;
	}


	private Funcionario obterFuncionario(Empresa empresa) {
		Funcionario funcionario = new Funcionario();
		funcionario.setCpf("12345678910");
		funcionario.setEmail("joao@gmail.com");
		funcionario.setNome("Fulando de Tal e Silva");
		funcionario.setSenha(PasswordUtils.gerarBCrypt("123456"));
		funcionario.setPerfil(PerfilEnum.ROLE_ADMIN);
		funcionario.setEmpresa(empresa);
		funcionario.setQtdHorasAlmoco(0f);
		funcionario.setQtdHorasTrabalhoDia(0f);
		funcionario.setValorHora(BigDecimal.ZERO);
		
		return funcionario;
	}


	private Empresa obterEmpresa() {
		Empresa empresa = new Empresa();
		empresa.setCnpj("02125487000198");
		empresa.setRazaoSocial("Empresa de teste Ltda");

		return empresa;	
	}
	
}
