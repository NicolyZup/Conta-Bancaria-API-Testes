package com.banco.conta.repository;

import com.banco.conta.model.ContaModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContasRepository extends JpaRepository<ContaModel, Long> {
}
