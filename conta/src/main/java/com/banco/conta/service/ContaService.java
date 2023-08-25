package com.banco.conta.service;


import com.banco.conta.model.ContaModel;
import com.banco.conta.repository.ContasRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ContaService {
    @Autowired
    ContasRepository contasRepository;

    public List<ContaModel> retornarTodas(){
        return contasRepository.findAll();
    }

    public Optional<ContaModel> retornarPorId(Long id){
        return contasRepository.findById(id);
    }

    public ContaModel cadastrar(ContaModel contaModel){
        return contasRepository.save(contaModel);
    }

    public void excluir(Long id){
        contasRepository.deleteById(id);
    }

    public ResponseEntity<String> depositar(Long id, double valor){
        ContaModel conta = retornarPorId(id).get();

        if(valor <=0){
            return ResponseEntity.badRequest().body("O valor deve ser maior que 0.");
        }

        if(conta != null){
            double novoSaldo = conta.getSaldo()+valor;
            conta.setSaldo(novoSaldo);
            contasRepository.save(conta);
            return ResponseEntity.ok("Depósito realizado com sucesso!");
        }else{
            return ResponseEntity.notFound().build();
        }
    }


    public ResponseEntity<String> sacar(Long id, double valor){
        ContaModel conta = contasRepository.findById(id).orElse(null);

        if(valor <0){
            return ResponseEntity.badRequest().body("O valor deve ser maior que 0.");
        }

        if(conta != null){
            if(valor > conta.getSaldo()){
                return ResponseEntity.badRequest().body("Saldo insuficiente para está operação.");
            }
            double novoSaldo = conta.getSaldo()-valor;
            conta.setSaldo(novoSaldo);
            contasRepository.save(conta);
            return ResponseEntity.ok("Saque realizado com sucesso!");
        }else{
            return ResponseEntity.notFound().build();
        }
    }
}
