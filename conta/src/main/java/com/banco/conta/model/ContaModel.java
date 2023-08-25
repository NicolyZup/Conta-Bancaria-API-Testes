package com.banco.conta.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name="TB_CONTAS")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ContaModel {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50, nullable = false)
    private int numeroConta;

    @Column(length = 50, nullable = false)
    private int agencia;

    @Column(length = 50, nullable = false)
    private String nomeUsuario;

    @Column(length = 50, nullable = false)
    private double saldo;
}
