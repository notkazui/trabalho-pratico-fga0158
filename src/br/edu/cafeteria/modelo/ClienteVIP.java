package br.edu.cafeteria.modelo;

import br.edu.cafeteria.excecao.PontosInsuficientesException;

public class ClienteVIP extends Cliente {
    private static final double TAXA_CONVERSAO_XP = 10.0;

    public ClienteVIP(String nome, String cpf) {
        super(nome, cpf);
    }

    @Override
    public void adicionarXP(double valorGasto) {
        this.saldoXP += (valorGasto * 2);
    }

    public void resgatarPontos(double valorTotalPedido) throws PontosInsuficientesException {
        double pontosNecessarios = valorTotalPedido * TAXA_CONVERSAO_XP;

        if (this.saldoXP < pontosNecessarios) {
            throw new PontosInsuficientesException("Saldo de XP insuficiente. Necessário: " + pontosNecessarios + ", Atual: " + this.saldoXP);
        }

        this.saldoXP -= pontosNecessarios;
    }
}