package test;

import tarefa3.Filosofo;
import tarefa3.Garfo;
import java.util.concurrent.Semaphore;

public class TestadorTarefa3 {
    public static void main(String[] args) {
        System.out.println("=========================================");
        System.out.println("TESTE TAREFA 3: SEMÁFOROS (O GARÇOM)");
        System.out.println("=========================================\n");

        boolean testePassou = testarExecucaoEIntegridade();

        System.out.println("\n=========================================");
        if (testePassou) {
            System.out.println("RESULTADO FINAL: [ APROVADO ]");
        } else {
            System.out.println("RESULTADO FINAL: [ REPROVADO ]");
        }
        System.out.println("=========================================");
    }

    private static boolean testarExecucaoEIntegridade() {
        System.out.println(">>> Iniciando simulação de 5 segundos...");
        
        // Configuração conforme requisitos
        int maxPermissoes = 4;
        Semaphore semaforo = new Semaphore(maxPermissoes);
        
        int nFilosofos = 5;
        Garfo[] garfos = new Garfo[nFilosofos];
        Filosofo[] filosofos = new Filosofo[nFilosofos];

        try {
            for (int i = 0; i < nFilosofos; i++) garfos[i] = new Garfo(i);
            
            for (int i = 0; i < nFilosofos; i++) {
                Garfo esq = garfos[i];
                Garfo dir = garfos[(i + 1) % nFilosofos];
                filosofos[i] = new Filosofo(i, esq, dir, semaforo);
                filosofos[i].start();
            }

            Thread.sleep(2000);
            int permissoesDurante = semaforo.availablePermits();
            System.out.println("   [INFO] Permissões disponíveis no meio do jantar: " + permissoesDurante);
            
            Thread.sleep(3000);

            System.out.println("   >>> Interrompendo jantar...");
            for (Filosofo f : filosofos) f.interrupt();
            for (Filosofo f : filosofos) f.join(); // Espera todos morrerem

            // --- FASE DE VERIFICAÇÃO ---

            // Verificação de Deadlock (Alguém comeu?)
            long totalRefeicoes = 0;
            for (Filosofo f : filosofos) totalRefeicoes += f.getRefeicoes();

            if (totalRefeicoes == 0) {
                printFalha("Nenhuma refeição servida. Possível Deadlock ou erro de lógica.");
                return false;
            } else {
                printSucesso("Refeições servidas com sucesso: " + totalRefeicoes);
            }

            // Verificação de Vazamento de Semáforo 
            // Se o semáforo começou com 4, ele DEVE terminar com 4.
            // Se terminar com menos, alguém deu acquire() e não deu release().
            int permissoesFinais = semaforo.availablePermits();
            
            if (permissoesFinais != maxPermissoes) {
                printFalha("Vazamento de Recurso Detectado!");
                System.out.println("      Esperado: " + maxPermissoes + " permissões livres.");
                System.out.println("      Encontrado: " + permissoesFinais + " permissões livres.");
                System.out.println("      Dica: Verifique se o semaforo.release() está dentro do bloco 'finally'.");
                return false;
            } else {
                printSucesso("Integridade do Semáforo mantida (todas permissões devolvidas).");
            }

            return true;

        } catch (InterruptedException e) {
            e.printStackTrace();
            return false;
        }
    }

    private static void printSucesso(String msg) { System.out.println("   [PASSOU] " + msg); }
    private static void printFalha(String msg) { System.out.println("   [FALHOU] " + msg); }
}