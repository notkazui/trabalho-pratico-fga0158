package br.edu.cafeteria.modelo;

public class ClienteStandard extends Cliente {

    public ClienteStandard(String nome, String cpf) {
        super(nome, cpf);
    }

    @Override
    public void adicionarXP(double valorGasto) {
        this.saldoXP += valorGasto;
    }
}