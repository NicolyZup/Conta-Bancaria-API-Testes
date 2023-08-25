package com.banco.conta.controllerTest;

import com.banco.conta.controller.ContaController;
import com.banco.conta.model.ContaModel;
import com.banco.conta.service.ContaService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringJUnitConfig
@WebMvcTest(ContaController.class)
public class ContaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ContaService contaService;

    @Test
    public void testarListarTodasContas() throws Exception{
        ContaModel conta1 = new ContaModel(1L,6666,001,"Nicoly Barros",2000);

        Mockito.when(contaService.retornarTodas()).thenReturn(List.of(conta1));

        mockMvc.perform(get("/contas"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id").value(1L))
            .andExpect(jsonPath("$[0].numeroConta").value(6666))
            .andExpect(jsonPath("$[0].agencia").value(001))
            .andExpect(jsonPath("$[0].nomeUsuario").value("Nicoly Barros"))
            .andExpect(jsonPath("$[0].saldo").value(2000))
            .andExpect(jsonPath("$.length()").value(1))
            .andDo(print());
    }

    @Test
    public void testarListarContaPorId() throws Exception{
        ContaModel conta1 = new ContaModel(1L,6666,001,"Nicoly Barros",2000);

        Mockito.when(contaService.retornarPorId(conta1.getId())).thenReturn(Optional.of(conta1));

        mockMvc.perform(get("/contas/{1}", conta1.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.numeroConta").value(6666))
                .andExpect(jsonPath("$.agencia").value(001))
                .andExpect(jsonPath("$.nomeUsuario").value("Nicoly Barros"))
                .andExpect(jsonPath("$.saldo").value(2000))
                .andDo(print());
    }

    @Test
    public void testarCadastroDeConta() throws Exception{
        ContaModel conta1 = new ContaModel(1L,7777,003,"Marcos Paulo",4000);

        Mockito.when(contaService.cadastrar(Mockito.any(conta1.getClass()))).thenReturn(conta1);

        mockMvc.perform(post("/contas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(conta1)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.numeroConta").value(7777))
                .andExpect(jsonPath("$.agencia").value(003))
                .andExpect(jsonPath("$.nomeUsuario").value("Marcos Paulo"))
                .andExpect(jsonPath("$.saldo").value(4000))
                .andDo(print());
    }

    @Test
    public void testarDeletarConta() throws Exception{
        mockMvc.perform(delete("/contas/1"))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void testarDepositarValorCorretamente() throws Exception{
        ContaModel conta1 = new ContaModel(1L,7777,003,"Marcos Paulo",4000);
        ResponseEntity<String> responseEntity = ResponseEntity.ok("Depósito realizado com sucesso!");

        Mockito.when(contaService.depositar(conta1.getId(), 300)).thenReturn(responseEntity);

        mockMvc.perform(put( "/contas/{id}/depositar", conta1.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(String.valueOf("300")))
                .andExpect(status().isOk())
                .andExpect(content().string("Depósito realizado com sucesso!"))
                .andDo(print());
    }

    @Test
    public void testarDepositarValorMenorQueZero() throws Exception{
        ContaModel conta1 = new ContaModel(1L,6666,001,"Nicoly Barros",2000);
        ResponseEntity<String> responseEntity = ResponseEntity.badRequest().body("O valor deve ser maior que 0.");

        double valorNegativo = -10.0;

        Mockito.when(contaService.depositar(conta1.getId(), valorNegativo)).thenReturn(responseEntity);

        mockMvc.perform(put( "/contas/{id}/depositar", conta1.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(String.valueOf(valorNegativo)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("O valor deve ser maior que 0."))
                .andDo(print());
    }

    @Test
    public void testarSacarValorCorretamente() throws Exception{
        ContaModel conta1 = new ContaModel(2L,7333,002,"Lucas Ernesto",2000);
        ResponseEntity<String> responseEntity = ResponseEntity.ok("Saque realizado com sucesso!");

        Mockito.when(contaService.sacar(conta1.getId(), 300)).thenReturn(responseEntity);

        mockMvc.perform(put( "/contas/{id}/sacar", conta1.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(String.valueOf("300")))
                .andExpect(status().isOk())
                .andExpect(content().string("Saque realizado com sucesso!"))
                .andDo(print());
    }

    @Test
    public void testarSacarValorMenorQueZero() throws Exception{
        ContaModel conta1 = new ContaModel(2L,7333,002,"Lucas Ernesto",2000);
        ResponseEntity<String> responseEntity = ResponseEntity.badRequest().body("O valor deve ser maior que 0.");

        double valorNegativo = -40.0;

        Mockito.when(contaService.sacar(conta1.getId(), valorNegativo)).thenReturn(responseEntity);

        mockMvc.perform(put( "/contas/{id}/sacar", conta1.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(String.valueOf(valorNegativo)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("O valor deve ser maior que 0."))
                .andDo(print());
    }

    @Test
    public void testarSacarValorMaiorQueSaldo() throws Exception{
        ContaModel conta1 = new ContaModel(2L,7333,002,"Lucas Ernesto",2000);
        ResponseEntity<String> responseEntity = ResponseEntity.badRequest().body("Saldo insuficiente para está operação.");

        Mockito.when(contaService.sacar(conta1.getId(),3000)).thenReturn(responseEntity);

        mockMvc.perform(put( "/contas/{id}/sacar", conta1.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(String.valueOf(3000)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Saldo insuficiente para está operação."))
                .andDo(print());
    }
}
