package test;

import tarefa2.Filosofo;
import tarefa2.Garfo;

public class TestadorTarefa2 {

    public static void main(String[] args) {
        System.out.println("=========================================");
        System.out.println("INICIANDO BATERIA DE TESTES (PACOTE TAREFA 2)");
        System.out.println("=========================================\n");

        boolean teste1 = testarLogicaDeInversao();
        System.out.println("-----------------------------------------");
        boolean teste2 = testarExecucaoSemTravamento();

        System.out.println("\n=========================================");
        if (teste1 && teste2) {
            System.out.println("RESULTADO FINAL: [ APROVADO ]");
        } else {
            System.out.println("RESULTADO FINAL: [ REPROVADO ]");
        }
        System.out.println("=========================================");
    }

    private static boolean testarLogicaDeInversao() {
        System.out.println("TESTE 1: Verificação da Ordem dos Garfos");
        try {
            int quantidade = 5;
            Garfo[] garfos = new Garfo[quantidade];
            for (int i = 0; i < quantidade; i++) garfos[i] = new Garfo(i);

            Filosofo[] filosofos = new Filosofo[quantidade];
            for (int i = 0; i < quantidade; i++) {
                Garfo esq = garfos[i];
                Garfo dir = garfos[(i + 1) % quantidade];
                filosofos[i] = new Filosofo(i, esq, dir);
            }

            Filosofo normal = filosofos[0];
            // Verifica se getters estão funcionando e lógica normal
            if (normal.getPrimeiroGarfo().getId() != 0) {
                printFalha("Filósofo 0 deveria pegar garfo 0 primeiro.");
                return false;
            }

            Filosofo invertido = filosofos[4];
            // Lógica do último filósofo (inversão)
            if (invertido.getPrimeiroGarfo().getId() != 0) {
                printFalha("Filósofo 4 NÃO inverteu a ordem (deveria pegar Dir/0 primeiro).");
                return false;
            }
            
            printSucesso("Filósofo 4 inverteu a ordem corretamente.");
            return true;

        } catch (Exception e) {
            printFalha("Erro no teste: " + e.getMessage());
            e.printStackTrace(); // Ajuda a ver erros de visibilidade
            return false;
        }
    }

    private static boolean testarExecucaoSemTravamento() {
        System.out.println("TESTE 2: Simulação de Execução (5 segundos)");
        try {
            int quantidade = 5;
            Garfo[] garfos = new Garfo[quantidade];
            Filosofo[] filosofos = new Filosofo[quantidade];

            for (int i = 0; i < quantidade; i++) garfos[i] = new Garfo(i);
            
            for (int i = 0; i < quantidade; i++) {
                filosofos[i] = new Filosofo(i, garfos[i], garfos[(i + 1) % quantidade]);
                filosofos[i].start();
            }

            System.out.println("   >>> Rodando...");
            Thread.sleep(5000);

            for (Filosofo f : filosofos) f.interrupt();
            for (Filosofo f : filosofos) f.join();

            long total = 0;
            for (Filosofo f : filosofos) total += f.getRefeicoes();

            if (total > 0) {
                printSucesso("Sistema serviu " + total + " refeições.");
                return true;
            } else {
                printFalha("Nenhuma refeição servida (Possível Deadlock).");
                return false;
            }
        } catch (InterruptedException e) {
            return false;
        }
    }

    private static void printSucesso(String msg) { System.out.println("   [PASSOU] " + msg); }
    private static void printFalha(String msg) { System.out.println("   [FALHOU] " + msg); }
}