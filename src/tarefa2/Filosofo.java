package tarefa2;
import java.util.Random;

public class Filosofo extends Thread {
    private final int id;
    private final Garfo garfoEsquerdo; 
    private final Garfo garfoDireito;  
    private final Garfo primeiroGarfo;
    private final Garfo segundoGarfo;

    //Atributos para métrica
    private long tempoEsperaTotal = 0;
    private long tempoComendoTotal = 0;
    private int tentativasComer = 0;

    public long getTempoEsperaTotal() { return tempoEsperaTotal; }
    public long getTempoComendoTotal() { return tempoComendoTotal; }
    
    private final Random random = new Random();
    private int refeicoes = 0; 

    public Garfo getPrimeiroGarfo() {
        return primeiroGarfo;
    }

    public Garfo getSegundoGarfo() {
        return segundoGarfo;
    }

    public long getId() {
        return id;
    }

    public Filosofo(int id, Garfo esquerdo, Garfo direito) {
        this.id = id;
        this.garfoEsquerdo = esquerdo;
        this.garfoDireito = direito;

        // Se for o último filósofo, inverte a ordem de pegar os garfos.
        if (id == 4) {
            this.primeiroGarfo = direito;
            this.segundoGarfo = esquerdo;
        } else {
            this.primeiroGarfo = esquerdo;
            this.segundoGarfo = direito;
        }
    }

    public int getRefeicoes() {
        return refeicoes;
    }

    private void log(String acao) {
        System.out.printf("Filósofo %d: %s%n", id, acao);
    }

    private void simularTempo() throws InterruptedException {
        int tempo = random.nextInt(2001) + 1000; // 1 a 3 segundos
        Thread.sleep(tempo);
    }

    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                log("Começou a PENSAR.");
                simularTempo();

                // Tentar pegar garfos 
                log("Está com FOME. Tentando pegar o primeiro garfo (" + primeiroGarfo.getId() + ")...");

                long inicioEspera = System.currentTimeMillis();

                synchronized (primeiroGarfo) {
                    log("Pegou o primeiro garfo (" + primeiroGarfo.getId() + "). Tentando pegar o segundo (" + segundoGarfo.getId() + ")...");
                    
                    synchronized (segundoGarfo) {
                        long fimEspera = System.currentTimeMillis();
                        tempoEsperaTotal += (fimEspera - inicioEspera);
                        tentativasComer++;

                        long inicioComer = System.currentTimeMillis();

                        log("Pegou o segundo garfo (" + segundoGarfo.getId() + "). COMEÇOU A COMER.");
                        refeicoes++;
                        simularTempo();

                        long fimComer = System.currentTimeMillis();
                        tempoComendoTotal += (fimComer - inicioComer);
                    }
                }

                log("Terminou de comer e SOLTOU os garfos.");
            }
        } catch (InterruptedException e) {
            log("Execução finalizada pelo temporizador.");
        }
    }
}