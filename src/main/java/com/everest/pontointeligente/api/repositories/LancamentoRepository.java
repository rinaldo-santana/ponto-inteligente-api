package com.everest.pontointeligente.api.repositories;

import java.util.List;

import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.everest.pontointeligente.api.entities.Lancamento;

@NamedQueries({
	@NamedQuery(name = "LancamentoRepository.findByFuncionarioId",
				query = "SELECT lanc FROM Lancamento lanc WHERE lanc.funcionario.id = :funcionarioId")
})
@Transactional(readOnly = true)
public interface LancamentoRepository extends JpaRepository<Lancamento, Long> {
	
//	@Query("select lanc from Lancamento lanc where lanc.funcionario.id = :funcionarioId")
	public List<Lancamento> findByFuncionarioId(@Param("funcionarioId") Long funcionarioId );

	public Page<Lancamento> findByFuncionarioId(@Param("funcionarioId") Long funcionarioId, Pageable pageable);
}
