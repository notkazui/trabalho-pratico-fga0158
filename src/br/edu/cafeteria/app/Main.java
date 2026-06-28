package br.edu.cafeteria.app;

import br.edu.cafeteria.modelo.*;
import br.edu.cafeteria.excecao.*;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Main {
    private static List<Produto> estoqueProdutos = new ArrayList<>();
    private static List<Cliente> clientes = new ArrayList<>();
    private static Scanner scanner = new Scanner(System.in);

    private static final String[] ATENDENTES = {"Júlia", "Carlos", "Yoda", "Leon", "Ryu", "R2D2"};

    public static void main(String[] args) {
        dadosIniciais();

        boolean executando = true;

        while (executando) {
            System.out.println("\n=== BYTE & BREW: SISTEMA DE GESTÃO ===");
            System.out.println("1. Abrir Novo Pedido");
            System.out.println("2. Cadastrar Novo Cliente");
            System.out.println("3. Cadastrar Novo Produto");
            System.out.println("4. Sair");
            System.out.print("Escolha uma opção: ");

            int opcao;
            try {
                opcao = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Por favor, digite um número válido.");
                continue;
            }

            switch (opcao) {
                case 1:
                    realizarVenda();
                    break;
                case 2:
                    cadastrarCliente();
                    break;
                case 3:
                    cadastrarProduto();
                    break;
                case 4:
                    executando = false;
                    System.out.println("Que a Força esteja com você!");
                    break;
                default:
                    System.out.println("Opção inválida! Tente novamente.");
            }
        }
        scanner.close();
    }

    private static void realizarVenda() {
        boolean produtoDisponivel = false;
        for (Produto p : estoqueProdutos) {
            if (p.getQuantidadeEstoque() > 0) {
                produtoDisponivel = true;
                break;
            }
        }

        if (!produtoDisponivel) {
            System.out.println("\nNão há nenhum produto com estoque disponível no momento!");
            return;
        }

        System.out.println("\n-== NOVO PEDIDO ==-");

        String atendente = ATENDENTES[new Random().nextInt(ATENDENTES.length)];
        System.out.println("Atendente responsável: " + atendente);

        System.out.print("CPF do Cliente (Deixe em branco se for Cliente Casual): ");
        String cpf = scanner.nextLine();

        Cliente clienteAtual = buscarCliente(cpf);
        if (clienteAtual == null && !cpf.isBlank()) {
            System.out.println("CPF não encontrado. A venda será registrada como Cliente Casual.");
        } else if (clienteAtual != null) {
            System.out.println("Bem-vindo de volta, " + clienteAtual.getNome() + "!");
        }

        boolean isDiaGeek = false;
        while (true) {
            System.out.print("Hoje é Dia de Evento Geek? (S/N): ");
            String resposta = scanner.nextLine().trim().toUpperCase();
            if (resposta.equals("S")) {
                isDiaGeek = true;
                break;
            } else if (resposta.equals("N")) {
                break;
            } else {
                System.out.println("Opção inválida! Digite apenas 'S' para Sim ou 'N' para Não.");
            }
        }

        Pedido pedido = new Pedido(atendente, clienteAtual);

        boolean adicionarItens = true;
        while (adicionarItens) {

            if (pedido.getItens() != null && !pedido.getItens().isEmpty()) {
                System.out.println("\n CARRINHO ATUAL");
                for (ItemPedido ip : pedido.getItens()) {
                    double subtotalItem = ip.getQuantidade() * ip.getProduto().getPrecoBase();
                    System.out.printf("%d x %s (R$ %.2f) = R$ %.2f\n",
                            ip.getQuantidade(), ip.getProduto().getNome(), ip.getProduto().getPrecoBase(), subtotalItem);
                }
                System.out.printf("Total Parcial: R$ %.2f\n", pedido.calcularTotal());
                System.out.println("-------------------------");
            }

            int itensDisponiveis = 0;
            System.out.println("\n-- Cardápio Disponível --");

            for (Produto p : estoqueProdutos) {
                if (p.getQuantidadeEstoque() > 0) {
                    if (p instanceof Bebida) {
                        Bebida b = (Bebida) p;
                        System.out.printf("[%s] %s [Tamanho: %s | Cafeína: %.1fmg] - R$ %.2f (Estoque: %d unid.)\n",
                                b.getCodigoUnico(), b.getNome(), b.getTamanho(), b.getCafeinaMg(), b.getPrecoBase(), b.getQuantidadeEstoque());
                    } else if (p instanceof Comida) {
                        Comida c = (Comida) p;
                        String restricao = c.isVeganoOuSemGluten() ? "[Vegano/Sem Glúten]" : "[Tradicional]";
                        System.out.printf("[%s] %s %s [Preparo: %d min] - R$ %.2f (Estoque: %d unid.)\n",
                                c.getCodigoUnico(), c.getNome(), restricao, c.getTempoPreparoMinutos(), c.getPrecoBase(), c.getQuantidadeEstoque());
                    }
                    itensDisponiveis++;
                }
            }

            if (itensDisponiveis == 0) {
                System.out.println("\nTodos os produtos esgotaram no momento!");
                System.out.println("\nIndo para o caixa...");
                break;
            }

            System.out.print("Digite o código do produto (ou 'F' para finalizar pedido): ");
            String codigo = scanner.nextLine();

            if (codigo.equalsIgnoreCase("F")) {
                adicionarItens = false;
                continue;
            }

            Produto produtoEscolhido = buscarProduto(codigo);

            if (produtoEscolhido == null || produtoEscolhido.getQuantidadeEstoque() <= 0) {
                System.out.println("Produto não encontrado ou fora de estoque!");
                continue;
            }

            if (produtoEscolhido instanceof Bebida) {
                String temperatura = "";
                while (true) {
                    System.out.print("Qual a temperatura desejada? (Q - Quente | G - Gelada): ");
                    String respTemp = scanner.nextLine().trim().toUpperCase();
                    if (respTemp.equals("Q")) {
                        temperatura = "Quente";
                        break;
                    } else if (respTemp.equals("G")) {
                        temperatura = "Gelada";
                        break;
                    } else {
                        System.out.println("Opção inválida! Digite 'Q' para Quente ou 'G' para Gelada.");
                    }
                }
                System.out.println("Anotado! Preparar bebida " + temperatura.toLowerCase() + ".");
            }

            boolean quantidadeValida = false;
            while (!quantidadeValida) {
                System.out.print("Quantidade desejada (ou 0 para cancelar este item): ");
                try {
                    int quantidade = Integer.parseInt(scanner.nextLine());

                    if (quantidade == 0) {
                        System.out.println("Item cancelado. Escolha outro produto.");
                        break;
                    }

                    pedido.adicionarItem(produtoEscolhido, quantidade);
                    System.out.println("Item adicionado com sucesso!");
                    quantidadeValida = true;

                } catch (NumberFormatException e) {
                    System.out.println("Por favor, digite um número inteiro válido.");
                } catch (EstoqueInsuficienteException e) {
                    System.out.println("\nERRO: " + e.getMessage());
                    System.out.println("Estoque atual de " + produtoEscolhido.getNome() + ": " + produtoEscolhido.getQuantidadeEstoque() + " unidades.");
                    System.out.println("Por favor, digite um valor menor ou igual ao estoque.\n");
                }
            }
        }

        processarPagamento(pedido, clienteAtual, isDiaGeek);
    }

    private static void processarPagamento(Pedido pedido, Cliente cliente, boolean isDiaGeek) {
        double total = pedido.calcularTotal();
        if (total == 0) {
            System.out.println("Pedido vazio. Venda cancelada. Retornando ao menu...");
            return;
        }

        System.out.println("\n--- FECHAMENTO DE CONTA ---");

        if (isDiaGeek) {
            total = pedido.aplicarDesconto(total);
            System.out.println("Desconto de Evento Geek (10%) aplicado!");
        }

        System.out.printf("Total parcial da conta: R$ %.2f\n", total);

        if (cliente != null && cliente.getSaldoXP() > 0) {
            double pontosNecessariosPara100 = total * 10.0;
            boolean processarDesconto = true;

            if (cliente instanceof ClienteVIP && cliente.getSaldoXP() >= pontosNecessariosPara100) {
                System.out.print("Você pode pagar 100% do pedido com XP! Deseja resgatar? (S/N): ");
                if (scanner.nextLine().trim().equalsIgnoreCase("S")) {
                    try {
                        ((ClienteVIP) cliente).resgatarPontos(total);
                        System.out.println("Pagamento realizado 100% com XP! Aproveite seu benefício VIP.");
                        return;
                    } catch (PontosInsuficientesException e) {
                        System.out.println("Falha no resgate: " + e.getMessage());
                    }
                    processarDesconto = false;
                }
            }

            if (processarDesconto) {
                double valorDescontoDisponivel = cliente.getSaldoXP() / 10.0;
                System.out.printf("Você possui %.2f pontos de XP (equivale a R$ %.2f de desconto).\n", cliente.getSaldoXP(), valorDescontoDisponivel);
                System.out.print("Deseja usar seus pontos para abater o valor final? (S/N): ");

                if (scanner.nextLine().trim().equalsIgnoreCase("S")) {
                    if (valorDescontoDisponivel >= total) {
                        cliente.debitarXP(total * 10.0);
                        total = 0;
                        System.out.println("Desconto aplicado! O pedido saiu de graça.");
                    } else {
                        total -= valorDescontoDisponivel;
                        cliente.debitarXP(cliente.getSaldoXP());
                        System.out.printf("Desconto aplicado! Novo total a pagar: R$ %.2f\n", total);
                    }
                }
            }
        }

        if (total > 0) {
            System.out.printf("Pagamento de R$ %.2f em Dinheiro/Cartão confirmado.\n", total);
        }

        if (cliente != null) {
            cliente.adicionarXP(total);
            System.out.printf("XP desta compra adicionado com sucesso! Novo saldo: %.2f XP\n", cliente.getSaldoXP());
        }
    }

    private static void cadastrarCliente() {
        System.out.println("\n--- CADASTRAR CLIENTE ---");
        System.out.print("Nome do Cliente: ");
        String nome = scanner.nextLine();
        System.out.print("CPF (somente números): ");
        String cpf = scanner.nextLine();
        System.out.print("Categoria (1 - Standard | 2 - VIP): ");
        int tipo;
        try {
            tipo = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Opção inválida. Operação cancelada.");
            return;
        }

        if (tipo == 1) {
            clientes.add(new ClienteStandard(nome, cpf));
        } else {
            clientes.add(new ClienteVIP(nome, cpf));
        }
        System.out.println("Cliente cadastrado com sucesso!");
    }

    private static void cadastrarProduto() {
        System.out.println("\n--- CADASTRAR PRODUTO ---");
        System.out.print("Tipo (1 - Comida | 2 - Bebida): ");
        int tipo;
        try {
            tipo = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Opção inválida. Operação cancelada.");
            return;
        }

        String codigo = gerarCodigo(tipo);

        System.out.print("Nome: ");
        String nome = scanner.nextLine();
        System.out.print("Preço Base em R$: ");
        double preco = Double.parseDouble(scanner.nextLine());
        System.out.print("Quantidade em Estoque: ");
        int estoque = Integer.parseInt(scanner.nextLine());

        if (tipo == 1) {
            System.out.print("Tempo de preparo em minutos: ");
            int tempo = Integer.parseInt(scanner.nextLine());

            System.out.print("É Vegano ou Sem Glúten? (Digite 'V' para sim, qualquer outra tecla para não): ");
            String respostaVegano = scanner.nextLine();
            boolean vegano = respostaVegano.equalsIgnoreCase("V");

            estoqueProdutos.add(new Comida(codigo, nome, preco, estoque, tempo, vegano));

        } else if (tipo == 2) {
            System.out.print("Tamanho (P, M, G): ");
            String tamanho = scanner.nextLine().toUpperCase();
            System.out.print("Cafeína (mg): ");
            double cafeina = Double.parseDouble(scanner.nextLine());

            estoqueProdutos.add(new Bebida(codigo, nome, preco, estoque, tamanho, cafeina));
        }
        System.out.println("Produto adicionado ao cardápio!");
    }

    private static String gerarCodigo(int tipo) {
        Random r = new Random();
        String codigoGerado;
        char letra = (tipo == 1) ? 'C' : 'B';

        do {
            int numero = r.nextInt(100);
            codigoGerado = String.format("%c%02d", letra, numero);
        } while (buscarProduto(codigoGerado) != null);

        return codigoGerado;
    }

    private static Cliente buscarCliente(String cpf) {
        if (cpf == null || cpf.isBlank()) return null;
        for (Cliente c : clientes) {
            if (c.getCpf().equals(cpf)) {
                return c;
            }
        }
        return null;
    }

    private static Produto buscarProduto(String codigo) {
        for (Produto p : estoqueProdutos) {
            if (p.getCodigoUnico().equalsIgnoreCase(codigo)) {
                return p;
            }
        }
        return null;
    }

    private static void dadosIniciais() {
        estoqueProdutos.add(new Comida("C01", "Lembas Bread", 12.00, 15, 10, true));
        estoqueProdutos.add(new Comida("C02", "Portal Cake", 18.50, 8, 5, false));
        estoqueProdutos.add(new Bebida("B01", "Café do Programador", 8.00, 50, "P", 250.0));
        estoqueProdutos.add(new Bebida("B02", "Poção de Mana", 14.00, 30, "M", 0.0));
    }
}