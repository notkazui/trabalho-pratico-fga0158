package br.edu.cafeteria.modelo;

import br.edu.cafeteria.excecao.EstoqueInsuficienteException;

public abstract class Produto {
    private String codigoUnico;
    private String nome;
    private double precoBase;
    private int quantidadeEstoque;

    public Produto(String codigoUnico, String nome, double precoBase, int quantidadeEstoque) {
        this.codigoUnico = codigoUnico;
        this.nome = nome;
        this.precoBase = precoBase;
        this.quantidadeEstoque = quantidadeEstoque;
    }

    public void reduzirEstoque(int quantidade) throws EstoqueInsuficienteException {
        if (this.quantidadeEstoque < quantidade) {
            throw new EstoqueInsuficienteException("Estoque insuficiente para o produto: " + this.nome);
        }
        this.quantidadeEstoque -= quantidade;
    }

    public void setCodigoUnico(String codigoUnico) {
        this.codigoUnico = codigoUnico;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setPrecoBase(double precoBase) {
        this.precoBase = precoBase;
    }

    public void setQuantidadeEstoque(int quantidadeEstoque) {
        this.quantidadeEstoque = quantidadeEstoque;
    }

    public String getCodigoUnico() {
        return codigoUnico;
    }

    public String getNome() {
        return nome;
    }

    public double getPrecoBase() {
        return precoBase;
    }

    public int getQuantidadeEstoque() {
        return quantidadeEstoque;
    }
}
