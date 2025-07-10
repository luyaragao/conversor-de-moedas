package br.com.luy.conversor;

import br.com.luy.conversor.modelo.Conversao;
import br.com.luy.conversor.servicos.ConsultaApi;
import br.com.luy.conversor.utilitario.Historico;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Historico historico = new Historico();
        ConsultaApi consulta;

        try {
            consulta = new ConsultaApi();
        } catch (RuntimeException e) {
            System.out.println("Erro na inicialização: " + e.getMessage());
            System.out.println("O programa será encerrado. Verifique seu arquivo de configuração.");
            return;
        }

        executarConversor(consulta, historico);

        historico.salvarEmArquivo();
        System.out.println("Histórico de pesquisa salvo com sucesso!");
        System.out.println("Programa finalizado");
    }

    private static void executarConversor(ConsultaApi consulta, Historico historico) {
        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                System.out.println("*************************************************");
                System.out.println("Seja bem vindo(a) ao Conversor de moedas!:D\n");
                System.out.println("1) Dólar >> Peso argentino");
                System.out.println("2) Peso argentino >> Dólar");
                System.out.println("3) Dólar >> Real brasileiro");
                System.out.println("4) Real brasileiro >> Dólar");
                System.out.println("5) Dólar >> Peso colombiano");
                System.out.println("6) Peso colombiano >> Dólar");
                System.out.println("7) Sair");
                System.out.println("*************************************************");
                System.out.print("Escolha uma opção válida: ");

                int opcao;
                try {
                    opcao = scanner.nextInt();
                } catch (InputMismatchException e) {
                    System.out.println("\nErro: Por favor, digite apenas o número da opção.\n");
                    scanner.nextLine();
                    continue;
                }

                scanner.nextLine();

                if (opcao == 7) {
                    System.out.println("Saindo do programa...");
                    break;
                }

                String moedaOrigem = "";
                String moedaDestino = "";

                switch (opcao) {
                    case 1:
                        moedaOrigem = "USD";
                        moedaDestino = "ARS";
                        break;
                    case 2:
                        moedaOrigem = "ARS";
                        moedaDestino = "USD";
                        break;
                    case 3:
                        moedaOrigem = "USD";
                        moedaDestino = "BRL";
                        break;
                    case 4:
                        moedaOrigem = "BRL";
                        moedaDestino = "USD";
                        break;
                    case 5:
                        moedaOrigem = "USD";
                        moedaDestino = "COP";
                        break;
                    case 6:
                        moedaOrigem = "COP";
                        moedaDestino = "USD";
                        break;
                    default:
                        System.out.println("\nOpção inválida! Por favor, selecione apenas um número no menu.\n");
                        continue;
                }

                System.out.print("Digite o valor que deseja converter: ");
                double valor;
                try {
                    valor = scanner.nextDouble();
                } catch (InputMismatchException e) {
                    System.out.println("\nErro: Valor inválido. Por favor, use números e vírgula para decimais.\n");
                    scanner.nextLine();
                    continue;
                }
                scanner.nextLine();

                Conversao resultado = consulta.converter(moedaOrigem, moedaDestino, valor);

                if (resultado != null) {
                    System.out.printf("\nO valor de %.2f [%s] corresponde ao valor final de =>>> %.2f [%s]\n",
                            resultado.valorOriginal(), resultado.origem(),
                            resultado.valorConvertido(), resultado.destino());
                    historico.salvar(resultado);
                } else {
                    System.out.println("\nDesculpe, ocorreu um erro ao realizar a conversão :(");
                }

                System.out.println("\n-------------------------------------------------");
            }
        }
    }
    //feito a base de lagrimas e muito cafézinho, mas consegui <3
}
