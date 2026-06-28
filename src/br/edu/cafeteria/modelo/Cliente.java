package br.edu.cafeteria.modelo;

public abstract class Cliente {
    private String nome;
    private String cpf;
    protected double saldoXP;

    public Cliente(String nome, String cpf) {
        this.nome = nome;
        this.cpf = cpf;
        this.saldoXP = 0.0;
    }

    public void debitarXP(double pontos) {
        if (pontos <= this.saldoXP) {
            this.saldoXP -= pontos;
        }
    }

    public abstract void adicionarXP(double valorGasto);

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public double getSaldoXP() {
        return saldoXP;
    }

    public void setSaldoXP(double saldoXP) {
        this.saldoXP = saldoXP;
    }

}