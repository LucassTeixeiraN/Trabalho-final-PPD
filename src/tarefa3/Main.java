package tarefa3;

import java.util.concurrent.Semaphore;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        int quantidade = 5;
        Semaphore semaforoMesa = new Semaphore(4); 
        
        Garfo[] garfos = new Garfo[quantidade];
        Filosofo[] filosofos = new Filosofo[quantidade];

        System.out.println("=== Jantar dos Filósofos com Semáforo (Limitação de Recursos) ===");
        System.out.println("Executando por 2 minutos...");

        for (int i = 0; i < quantidade; i++) {
            garfos[i] = new Garfo(i);
        }

        for (int i = 0; i < quantidade; i++) {
            Garfo esq = garfos[i];
            Garfo dir = garfos[(i + 1) % quantidade];
            
            filosofos[i] = new Filosofo(i, esq, dir, semaforoMesa);
            filosofos[i].start();
        }

        // Executa por 2 minutos
        Thread.sleep(120000);

        System.out.println("\n=== Fim da Simulação ===");
        
        int totalRefeicoes = 0;
        for (Filosofo f : filosofos) {
            f.interrupt();
            f.join(1000);
            System.out.printf("Filósofo %d comeu %dw vezes.\n", f.getId(), f.getRefeicoes()); // Mudado para getId()
            totalRefeicoes += f.getRefeicoes();
        }
        
        System.out.println("-----------------------------");
        System.out.printf("Total de refeições servidas: %d\n", totalRefeicoes);
    }
}
