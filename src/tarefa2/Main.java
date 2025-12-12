package tarefa2;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        int quantidade = 5;
        Garfo[] garfos = new Garfo[quantidade];
        Filosofo[] filosofos = new Filosofo[quantidade];

        System.out.println("=== Jantar dos Filósofos (Solução Assimétrica) ===");
        System.out.println("Executando por 2 minutos...");

        for (int i = 0; i < quantidade; i++) {
            garfos[i] = new Garfo(i);
        }

        for (int i = 0; i < quantidade; i++) {
            Garfo garfoEsq = garfos[i];
            Garfo garfoDir = garfos[(i + 1) % quantidade];
            filosofos[i] = new Filosofo(i, garfoEsq, garfoDir);
            filosofos[i].start();
        }

        // Executa por 2 minutos (120.000 ms)
        Thread.sleep(120000);

        System.out.println("\n=== Fim da Simulação ===");
        System.out.println("Interrompendo filósofos e gerando relatório...\n");

        int totalRefeicoes = 0;
        for (Filosofo f : filosofos) {
            f.interrupt(); // Para o loop infinito
            f.join(1000); 
            
            System.out.printf("Filósofo %d comeu %d vezes.\n", f.getId(), f.getRefeicoes());
            totalRefeicoes += f.getRefeicoes();
        }
        
        System.out.println("-----------------------------");
        System.out.printf("Total de refeições servidas: %d\n", totalRefeicoes);
    }
}
