package tarefa1;
import java.util.Random;

class Filosofo extends Thread {
    private final int id;
    private final Garfo garfoEsquerdo;
    private final Garfo garfoDireito;
    private final Random random = new Random();

    public Filosofo(int id, Garfo esquerdo, Garfo direito) {
        this.id = id;
        this.garfoEsquerdo = esquerdo;
        this.garfoDireito = direito;
    }

    // Método auxiliar para gerar logs
    private void log(String acao) {
        System.out.printf("Filósofo %d: %s%n", id, acao);
    }

    private void simularTempo() throws InterruptedException {
        // Tempo aleatório entre 1000ms (1s) e 3000ms (3s)
        int tempo = random.nextInt(2001) + 1000; 
        Thread.sleep(tempo);
    }

    @Override
    public void run() {
        try {
            while (true) {
                log("Começou a PENSAR.");
                simularTempo();

                log("Está com FOME e tentando pegar o garfo esquerdo (" + garfoEsquerdo.getId() + ").");
                
                // Bloqueia o garfo esquerdo
                synchronized (garfoEsquerdo) {
                    log("Pegou o garfo esquerdo (" + garfoEsquerdo.getId() + "). Tentando pegar o direito (" + garfoDireito.getId() + ")...");

                    Thread.sleep(100);
                    
                    // Bloqueia o garfo direito
                    synchronized (garfoDireito) {
                        log("Pegou o garfo direito (" + garfoDireito.getId() + "). COMEÇOU A COMER.");
                        simularTempo();
                    }
                }
                log("Terminou de comer e SOLTOU os garfos.");
            }
        } catch (InterruptedException e) {
            log("Foi interrompido.");
            Thread.currentThread().interrupt();
        }
    }
}