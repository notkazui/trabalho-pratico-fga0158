package br.edu.cafeteria.modelo;

public class Bebida extends Produto {
    private String tamanho; // P, M, G
    private double cafeinaMg;

    public Bebida(String codigoUnico, String nome, double precoBase, int quantidadeEstoque,
                  String tamanho, double cafeinaMg) {
        super(codigoUnico, nome, precoBase, quantidadeEstoque);
        this.tamanho = tamanho;
        this.cafeinaMg = cafeinaMg;
    }

    public String getTamanho() {
        return tamanho;
    }

    public void setTamanho(String tamanho) {
        this.tamanho = tamanho;
    }

    public double getCafeinaMg() {
        return cafeinaMg;
    }

    public void setCafeinaMg(double cafeinaMg) {
        this.cafeinaMg = cafeinaMg;
    }
}