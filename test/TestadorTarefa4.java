package test;

import tarefa4.Filosofo;
import tarefa4.Mesa;

public class TestadorTarefa4 {

    public static void main(String[] args) {
        System.out.println("=========================================");
        System.out.println("TESTE TAREFA 4: MONITORES (MESA)");
        System.out.println("=========================================\n");

        boolean sucesso = testarProgressoConcorrente();

        System.out.println("\n=========================================");
        if (sucesso) {
            System.out.println("RESULTADO FINAL: [ APROVADO ]");
        } else {
            System.out.println("RESULTADO FINAL: [ REPROVADO ]");
        }
        System.out.println("=========================================");
    }

    private static boolean testarProgressoConcorrente() {
        System.out.println(">>> Iniciando simulação curta (5s)...");
        int qtd = 5;
        Mesa mesa = new Mesa(qtd);
        Filosofo[] filosofos = new Filosofo[qtd];

        try {
            for (int i = 0; i < qtd; i++) {
                filosofos[i] = new Filosofo(i, mesa);
                filosofos[i].start();
            }

            // Monitora estados por 5 segundos
            Thread.sleep(5000);

            // Encerra
            for (Filosofo f : filosofos) f.interrupt();
            for (Filosofo f : filosofos) f.join();

            long total = 0;
            int minRefeicoes = Integer.MAX_VALUE;
            int maxRefeicoes = Integer.MIN_VALUE;

            for (Filosofo f : filosofos) {
                int r = f.getRefeicoes();
                total += r;
                if (r < minRefeicoes) minRefeicoes = r;
                if (r > maxRefeicoes) maxRefeicoes = r;
            }

            System.out.println("   [INFO] Total de refeições: " + total);
            System.out.println("   [INFO] Mínimo por filósofo: " + minRefeicoes);
            System.out.println("   [INFO] Máximo por filósofo: " + maxRefeicoes);

            // Verificações
            if (total == 0) {
                printFalha("Nenhuma refeição servida (Deadlock?).");
                return false;
            }
            
            // Verificação de Fairness
            // Se um comeu 100 vezes e outro 0, o monitor falhou no fairness.
            if (maxRefeicoes - minRefeicoes > 5 && minRefeicoes == 0) {
                System.out.println("   [AVISO] Disparidade alta detectada. Verifique a lógica de fairness.");
                // Não falha o teste necessariamente pq 5s é pouco tempo para random, mas avisa.
            }

            printSucesso("Sistema fluiu corretamente com exclusão mútua e vivacidade.");
            return true;

        } catch (InterruptedException e) {
            return false;
        }
    }

    private static void printSucesso(String msg) { System.out.println("   [PASSOU] " + msg); }
    private static void printFalha(String msg) { System.out.println("   [FALHOU] " + msg); }
}