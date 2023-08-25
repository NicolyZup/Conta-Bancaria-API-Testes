package com.banco.conta.controller;

import com.banco.conta.model.ContaModel;
import com.banco.conta.service.ContaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@RestController
public class ContaController {

    @Autowired
    ContaService contaService;

    @GetMapping(path = "/contas")
    public List<ContaModel> retornarTodasContas(){
        return contaService.retornarTodas();
    }

    @GetMapping(path = "/contas/{id}")
    public Optional<ContaModel> retornarContaPorId(@PathVariable Long id){
        return contaService.retornarPorId(id);
    }

    @PostMapping(path = "/contas")
    @ResponseStatus(HttpStatus.CREATED)
    public ContaModel cadastrarNovaConta(@RequestBody ContaModel contaModel){
        return contaService.cadastrar(contaModel);
    }

    @DeleteMapping(path = "/contas/{id}")
    public void excluirConta(@PathVariable Long id){
        contaService.excluir(id);
    }

    @PutMapping(path = "/contas/{id}/depositar")
    public ResponseEntity<String> depositarValor(@PathVariable Long id, @RequestBody double valor){
        return contaService.depositar(id, valor);
    }

    @PutMapping(path = "/contas/{id}/sacar")
    public ResponseEntity<String> sacarValor(@PathVariable Long id, @RequestBody double valor){
        return contaService.sacar(id, valor);
    }
}
