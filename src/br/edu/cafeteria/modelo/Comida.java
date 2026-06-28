package br.edu.cafeteria.modelo;

public class Comida extends Produto {
    private int tempoPreparoMinutos;
    private boolean veganoOuSemGluten;

    public Comida(String codigoUnico, String nome, double precoBase, int quantidadeEstoque,
                  int tempoPreparo, boolean veganoOuSemGluten) {
        super(codigoUnico, nome, precoBase, quantidadeEstoque);
        this.tempoPreparoMinutos = tempoPreparo;
        this.veganoOuSemGluten = veganoOuSemGluten;
    }

    public int getTempoPreparoMinutos() {
        return tempoPreparoMinutos;
    }

    public void setTempoPreparoMinutos(int tempoPreparoMinutos) {
        this.tempoPreparoMinutos = tempoPreparoMinutos;
    }

    public boolean isVeganoOuSemGluten() {
        return veganoOuSemGluten;
    }

    public void setVeganoOuSemGluten(boolean veganoOuSemGluten) {
        this.veganoOuSemGluten = veganoOuSemGluten;
    }
}