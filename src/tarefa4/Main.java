package tarefa4;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        int quantidade = 5;
        
        // Instancia o Monitor Único
        Mesa mesa = new Mesa(quantidade);
        
        Filosofo[] filosofos = new Filosofo[quantidade];

        System.out.println("=== Jantar dos Filósofos com Monitor (Fairness & No Deadlock) ===");
        System.out.println("Executando por 2 minutos...");

        for (int i = 0; i < quantidade; i++) {
            filosofos[i] = new Filosofo(i, mesa);
            filosofos[i].start();
        }

        Thread.sleep(120000); // 2 minutos

        System.out.println("\n=== Fim da Simulação ===");
        
        int totalRefeicoes = 0;
        for (Filosofo f : filosofos) {
            f.interrupt();
            f.join(1000);
            System.out.printf("Filósofo %d comeu %d vezes.\n", f.getId(), f.getRefeicoes()); // Assumindo getId() herdado de Thread ou implementado
            totalRefeicoes += f.getRefeicoes();
        }
        
        System.out.println("-----------------------------");
        System.out.printf("Total de refeições servidas: %d\n", totalRefeicoes);
    }
}