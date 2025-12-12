package tarefa4;

import java.util.Random;

public class Filosofo extends Thread {
    private final int id;
    private final Mesa mesa;
    private final Random random = new Random();
    private int refeicoes = 0;

    //Atributos para métrica
    private long tempoEsperaTotal = 0;
    private long tempoComendoTotal = 0;
    private int tentativasComer = 0;

    public long getTempoEsperaTotal() { return tempoEsperaTotal; }
    public long getTempoComendoTotal() { return tempoComendoTotal; }

    public Filosofo(int id, Mesa mesa) {
        this.id = id;
        this.mesa = mesa;
    }

    public long getId() {
        return id;
    }

    public int getRefeicoes() {
        return refeicoes;
    }

    private void log(String acao) {
        System.out.printf("Filósofo %d: %s%n", id, acao);
    }

    private void simularTempo() throws InterruptedException {
        int tempo = random.nextInt(2001) + 1000;
        Thread.sleep(tempo);
    }

    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                long inicioEspera = System.currentTimeMillis();
                log("Começou a PENSAR.");
                simularTempo();

                log("Está com FOME (Estado: FAMINTO).");
                
                // Esta chamada bloqueia a thread até que seja seguro comer
                mesa.pegarGarfos(id);

                long fimEspera = System.currentTimeMillis();
                tempoEsperaTotal += (fimEspera - inicioEspera);
                tentativasComer++;

                long inicioComer = System.currentTimeMillis();

                log("Conseguiu os garfos (Estado: COMENDO). COMEÇOU A COMER.");
                refeicoes++;
                simularTempo();
                
                long fimComer = System.currentTimeMillis();
                tempoComendoTotal += (fimComer - inicioComer);

                mesa.soltarGarfos(id);
                log("Terminou de comer e avisou a mesa (Estado: PENSANDO).");
            }
        } catch (InterruptedException e) {
            log("Execução finalizada.");
        }
    }
}