package tarefa3;
import java.util.Random;
import java.util.concurrent.Semaphore;

public class Filosofo extends Thread {
    private final int id;
    private final Garfo garfoEsquerdo;
    private final Garfo garfoDireito;
    private final Semaphore semaforoMesa; 
    private final Random random = new Random();
    private int refeicoes = 0;

    //Atributos para métrica
    private long tempoEsperaTotal = 0;
    private long tempoComendoTotal = 0;
    private int tentativasComer = 0;

    public long getTempoEsperaTotal() { return tempoEsperaTotal; }
    public long getTempoComendoTotal() { return tempoComendoTotal; }

    public Filosofo(int id, Garfo esquerdo, Garfo direito, Semaphore semaforoMesa) {
        this.id = id;
        this.garfoEsquerdo = esquerdo;
        this.garfoDireito = direito;
        this.semaforoMesa = semaforoMesa;
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

                log("Está com FOME. Esperando permissão para sentar à mesa...");
                semaforoMesa.acquire(); // Bloqueia se já tiver 4 pessoas comendo
                
                try {
                    log("Conseguiu lugar à mesa. Tentando pegar garfo esquerdo (" + garfoEsquerdo.getId() + ")...");
                    
                    synchronized (garfoEsquerdo) {

                        log("Pegou garfo esquerdo (" + garfoEsquerdo.getId() + "). Tentando pegar direito (" + garfoDireito.getId() + ")...");
                        
                        synchronized (garfoDireito) {
                            long fimEspera = System.currentTimeMillis();
                            tempoEsperaTotal += (fimEspera - inicioEspera);
                            tentativasComer++;

                            long inicioComer = System.currentTimeMillis();
                            log("Pegou garfo direito (" + garfoDireito.getId() + "). COMEÇOU A COMER.");
                            refeicoes++;
                            simularTempo();

                            long fimComer = System.currentTimeMillis();
                            tempoComendoTotal += (fimComer - inicioComer);
                        }
                    }
                    log("Terminou de comer e SOLTOU os garfos.");

                } finally {
                    log("Levantou-se da mesa (liberou vaga).");
                    semaforoMesa.release();
                }
            }
        } catch (InterruptedException e) {
            log("Execução finalizada pelo temporizador.");
        }
    }
}