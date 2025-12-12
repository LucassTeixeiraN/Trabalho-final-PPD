package tarefa1;

public class Main {
    public static void main(String[] args) {
        int quantidade = 5;
        Garfo[] garfos = new Garfo[quantidade];
        Filosofo[] filosofos = new Filosofo[quantidade];

        System.out.println("=== Início do Jantar dos Filósofos ===");

        for (int i = 0; i < quantidade; i++) {
            garfos[i] = new Garfo(i);
        }

        for (int i = 0; i < quantidade; i++) {
            Garfo garfoEsq = garfos[i];
            Garfo garfoDir = garfos[(i + 1) % quantidade]; 
            filosofos[i] = new Filosofo(i, garfoEsq, garfoDir);
            filosofos[i].start();
        }
        
    }
}
