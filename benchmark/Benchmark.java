package benchmark;

import java.util.concurrent.Semaphore;
import tarefa4.Mesa;

public class Benchmark {
    
    // CONFIGURAÇÃO: 5 minutos = 300.000 ms
    private static final long TEMPO_EXECUCAO_MS = 300000; 
    private static final int QTD_FILOSOFOS = 5;

    public static void main(String[] args) throws InterruptedException {
        System.out.println("==============================================");
        System.out.println("     BENCHMARK JANTAR DOS FILÓSOFOS");
        System.out.println("==============================================");
        System.out.printf("Tempo de execução por cenário: %d segundos\n\n", TEMPO_EXECUCAO_MS/1000);

        // --- EXECUÇÃO TAREFA 2 ---
        System.out.println(">>> Executando Tarefa 2 (Hierarquia de Recursos)...");
        tarefa2.Filosofo[] fil2 = setupTarefa2();
        rodarTeste(fil2);
        imprimirResultados("Tarefa 2 (Hierarquia)", fil2);

        // --- EXECUÇÃO TAREFA 3 ---
        System.out.println("\n>>> Executando Tarefa 3 (Semáforo/Garçom)...");
        tarefa3.Filosofo[] fil3 = setupTarefa3();
        rodarTeste(fil3);
        imprimirResultados("Tarefa 3 (Semáforo)", fil3);

        // // --- EXECUÇÃO TAREFA 4 ---
        // System.out.println("\n>>> Executando Tarefa 4 (Monitor)...");
        // tarefa4.Filosofo[] fil4 = setupTarefa4();
        // rodarTeste(fil4);
        // imprimirResultados("Tarefa 4 (Monitor)", fil4);
    }

    // --- SETUP DOS CENÁRIOS ---
    
    private static tarefa2.Filosofo[] setupTarefa2() {
        tarefa2.Garfo[] garfos = new tarefa2.Garfo[QTD_FILOSOFOS];
        for(int i=0; i<QTD_FILOSOFOS; i++) garfos[i] = new tarefa2.Garfo(i);
        tarefa2.Filosofo[] f = new tarefa2.Filosofo[QTD_FILOSOFOS];
        for(int i=0; i<QTD_FILOSOFOS; i++) {
            tarefa2.Garfo esq = garfos[i];
            tarefa2.Garfo dir = garfos[(i+1)%QTD_FILOSOFOS];
            f[i] = new tarefa2.Filosofo(i, esq, dir);
        }
        return f;
    }

    private static tarefa3.Filosofo[] setupTarefa3() {
        tarefa3.Garfo[] garfos = new tarefa3.Garfo[QTD_FILOSOFOS];
        Semaphore sem = new Semaphore(4);
        for(int i=0; i<QTD_FILOSOFOS; i++) garfos[i] = new tarefa3.Garfo(i);
        tarefa3.Filosofo[] f = new tarefa3.Filosofo[QTD_FILOSOFOS];
        for(int i=0; i<QTD_FILOSOFOS; i++) {
            f[i] = new tarefa3.Filosofo(i, garfos[i], garfos[(i+1)%QTD_FILOSOFOS], sem);
        }
        return f;
    }

    private static tarefa4.Filosofo[] setupTarefa4() {
        Mesa mesa = new Mesa(QTD_FILOSOFOS);
        tarefa4.Filosofo[] f = new tarefa4.Filosofo[QTD_FILOSOFOS];
        for(int i=0; i<QTD_FILOSOFOS; i++) {
            f[i] = new tarefa4.Filosofo(i, mesa);
        }
        return f;
    }

    // --- MOTOR DE EXECUÇÃO ---

    private static void rodarTeste(Thread[] threads) throws InterruptedException {
        for(Thread t : threads) t.start();
        Thread.sleep(TEMPO_EXECUCAO_MS);
        for(Thread t : threads) t.interrupt();
        for(Thread t : threads) t.join();
    }

    // --- CÁLCULO DE MÉTRICAS ---

    private static void imprimirResultados(String nomeCenario, Object[] filosofos) {
        long totalRefeicoes = 0;
        long tempoEsperaAcumulado = 0;
        long tempoComendoAcumulado = 0;
        double[] refeicoesPorFilosofo = new double[QTD_FILOSOFOS];

        // Reflexão simplificada ou casting manual (vamos usar casting manual pelo polimorfismo pobre aqui)
        for(int i=0; i<QTD_FILOSOFOS; i++) {
            if (filosofos instanceof tarefa2.Filosofo[]) {
                tarefa2.Filosofo f = ((tarefa2.Filosofo[])filosofos)[i];
                totalRefeicoes += f.getRefeicoes();
                tempoEsperaAcumulado += f.getTempoEsperaTotal();
                tempoComendoAcumulado += f.getTempoComendoTotal();
                refeicoesPorFilosofo[i] = f.getRefeicoes();
            } else if (filosofos instanceof tarefa3.Filosofo[]) {
                tarefa3.Filosofo f = ((tarefa3.Filosofo[])filosofos)[i];
                totalRefeicoes += f.getRefeicoes();
                tempoEsperaAcumulado += f.getTempoEsperaTotal();
                tempoComendoAcumulado += f.getTempoComendoTotal();
                refeicoesPorFilosofo[i] = f.getRefeicoes();
            } else {
                tarefa4.Filosofo f = ((tarefa4.Filosofo[])filosofos)[i];
                totalRefeicoes += f.getRefeicoes();
                tempoEsperaAcumulado += f.getTempoEsperaTotal();
                tempoComendoAcumulado += f.getTempoComendoTotal();
                refeicoesPorFilosofo[i] = f.getRefeicoes();
            }
        }

        double mediaRefeicoes = totalRefeicoes / (double) QTD_FILOSOFOS;
        
        // Desvio Padrão
        double somaQuadrados = 0;
        for(double r : refeicoesPorFilosofo) somaQuadrados += Math.pow(r - mediaRefeicoes, 2);
        double desvioPadrao = Math.sqrt(somaQuadrados / QTD_FILOSOFOS);
        double cv = (mediaRefeicoes > 0) ? (desvioPadrao / mediaRefeicoes) : 0.0;

        // Tempo Médio de Espera
        double tempoMedioEspera = (totalRefeicoes > 0) ? (tempoEsperaAcumulado / (double) totalRefeicoes) : 0;
        
        // Utilização dos Garfos (Aprox: Tempo Comendo * 2 Garfos / Tempo Total * 5 Garfos)
        double utilizacao = (double)(tempoComendoAcumulado * 2) / (TEMPO_EXECUCAO_MS * 5) * 100;

        System.out.println("----- RESULTADOS: " + nomeCenario + " -----");
        System.out.printf("Total Refeições: %d\n", totalRefeicoes);
        System.out.printf("Tempo Médio de Espera: %.2f ms\n", tempoMedioEspera);
        System.out.printf("Taxa de Utilização dos Garfos: %.2f%%\n", utilizacao);
        System.out.printf("Desvio Padrão (Justiça): %.4f\n", desvioPadrao);
        System.out.printf("Coeficiente de Variação (CV): %.4f (Quanto menor, mais justo)\n", cv);
        System.out.println("------------------------------------------------\n");
    }
}