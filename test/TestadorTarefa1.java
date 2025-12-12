package test;

import tarefa1.Filosofo;
import tarefa1.Garfo;
import java.lang.Thread.State;

public class TestadorTarefa1 {

    public static void main(String[] args) {
        System.out.println("=========================================");
        System.out.println("MONITOR DE DEADLOCK (TAREFA 1)");
        System.out.println("=========================================");
        System.out.println("O objetivo deste teste é detectar quando o sistema TRAVA.\n");

        testarOcorrenciaDeDeadlock();
    }

    private static void testarOcorrenciaDeDeadlock() {
        try {
            int quantidade = 5;
            Garfo[] garfos = new Garfo[quantidade];
            Filosofo[] filosofos = new Filosofo[quantidade];

            for (int i = 0; i < quantidade; i++) garfos[i] = new Garfo(i);

            for (int i = 0; i < quantidade; i++) {
                Garfo esq = garfos[i];
                Garfo dir = garfos[(i + 1) % quantidade]; 
                filosofos[i] = new Filosofo(i, esq, dir);
                filosofos[i].start();
            }

            // MONITORAMENTO
            long ultimasRefeicoesTotal = -1;
            int contadorDeTravamento = 0;
            boolean deadlockDetectado = false;

            // Executa por até 60 segundos ou até detectar deadlock
            for (int i = 0; i < 30; i++) { 
                Thread.sleep(2000); // Verifica a cada 2 segundos

                long totalAtual = 0;
                int threadsBloqueadas = 0;

                System.out.println("--- Checagem " + (i + 1) + " ---");
                
                for (Filosofo f : filosofos) {
                    totalAtual += f.getRefeicoes();
                    // Conta quantos filósofos estão travados esperando recurso (BLOCKED)
                    if (f.getState() == State.BLOCKED) {
                        threadsBloqueadas++;
                    }
                }

                System.out.printf("Total Refeições: %d | Threads Bloqueadas: %d/5\n", totalAtual, threadsBloqueadas);

                // Lógica de Detecção
                if (totalAtual == ultimasRefeicoesTotal) {
                    contadorDeTravamento++;
                    System.out.println("ALERTA: O número de refeições não aumentou.");
                } else {
                    contadorDeTravamento = 0; // Reset se houve progresso
                }

                ultimasRefeicoesTotal = totalAtual;

                // Se passou 10 segundos (5 checagens) sem ninguém comer, ou se todos estão BLOQUEADOS
                if (contadorDeTravamento >= 5 || threadsBloqueadas == 5) {
                    deadlockDetectado = true;
                    System.out.println("\n!!! DEADLOCK DETECTADO !!!");
                    System.out.println("O sistema parou de processar refeições.");
                    if (threadsBloqueadas == 5) {
                        System.out.println("Causa confirmada: Todos os 5 filósofos estão no estado BLOCKED.");
                    }
                    break;
                }
            }

            // Encerramento
            System.out.println("\nEncerrando teste...");
            for (Filosofo f : filosofos) f.interrupt(); 
            // Força parada bruta se necessário, pois eles podem estar travados no synchronized
            // (Interromper thread travada em synchronized nativo é difícil em Java, 
            // mas o System.exit no final resolve para o teste).

            System.out.println("=========================================");
            if (deadlockDetectado) {
                System.out.println("RESULTADO: [ SUCESSO ] - O Deadlock foi reproduzido e detectado.");
            } else {
                System.out.println("RESULTADO: [ INCONCLUSIVO ] - O tempo acabou antes do Deadlock ocorrer.");
                System.out.println("DICA: Para forçar o deadlock, adicione um Thread.sleep(100) dentro do primeiro synchronized na classe Filosofo.");
            }
            System.out.println("=========================================");

            System.exit(0); // Mata as threads zumbis travadas

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}