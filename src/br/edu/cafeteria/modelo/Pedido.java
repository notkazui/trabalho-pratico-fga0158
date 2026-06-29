package br.edu.cafeteria.modelo;

import br.edu.cafeteria.servico.Promocional;
import br.edu.cafeteria.excecao.EstoqueInsuficienteException;
import java.util.ArrayList;
import java.util.List;

public class Pedido implements Promocional {
    private static int contadorPedidos = 1;

    private int numeroPedido;
    private String nomeAtendente;
    private Cliente cliente;
    private List<ItemPedido> itens;

    public Pedido(String nomeAtendente, Cliente cliente) {
        this.numeroPedido = contadorPedidos++;
        this.nomeAtendente = nomeAtendente;
        this.cliente = cliente;
        this.itens = new ArrayList<>();
    }

    public void adicionarItem(Produto produto) throws EstoqueInsuficienteException {
        adicionarItem(produto, 1);
    }

    public void adicionarItem(Produto produto, int quantidade) throws EstoqueInsuficienteException {
        produto.reduzirEstoque(quantidade);
        itens.add(new ItemPedido(produto, quantidade));
    }

    public double calcularTotal() {
        double total = 0.0;
        for (ItemPedido item : itens) {
            total += item.getProduto().getPrecoBase() * item.getQuantidade();
        }
        return total;
    }

    public static void decrementarContador() {
        contadorPedidos--;
    }

    @Override
    public double aplicarDesconto(double valorOriginal) {
        return valorOriginal * 0.90;
    }

    public int getNumeroPedido() {
        return numeroPedido;
    }

    public String getNomeAtendente() {
        return nomeAtendente;
    }

    public void setNomeAtendente(String nomeAtendente) {
        this.nomeAtendente = nomeAtendente;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public List<ItemPedido> getItens() {
        return itens;
    }

    public void setItens(List<ItemPedido> itens) {
        this.itens = itens;
    }
}