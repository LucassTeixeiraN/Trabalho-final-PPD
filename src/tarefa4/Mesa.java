package tarefa4;

public class Mesa {
    // PENSANDO: Não quer garfos
    // FAMINTO: Quer comer, mas está esperando
    // COMENDO: Está com os garfos
    private enum Estado { PENSANDO, FAMINTO, COMENDO }

    private final int quantidade;
    private final Estado[] estados;

    public Mesa(int quantidade) {
        this.quantidade = quantidade;
        this.estados = new Estado[quantidade];
        
        // Todos começam pensando
        for (int i = 0; i < quantidade; i++) {
            estados[i] = Estado.PENSANDO;
        }
    }

    public synchronized void pegarGarfos(int id) throws InterruptedException {
        // Declara intenção (entra na "fila" de prioridade lógica)
        estados[id] = Estado.FAMINTO;
        
        // Tenta adquirir os recursos
        tentarComer(id);

        // Bloqueio (Wait)
        // Enquanto não conseguir mudar seu estado para COMENDO, dorme.
        // O while é essencial para evitar "spurious wakeups" e garantir a condição.
        while (estados[id] != Estado.COMENDO) {
            wait(); // Libera o lock da Mesa e dorme até alguém chamar notifyAll()
        }
    }

    public synchronized void soltarGarfos(int id) {
        // Libera recursos
        estados[id] = Estado.PENSANDO;

        // Notifica vizinhos
        // Ao sair, verifica se os vizinhos (que podiam estar bloqueados) agora podem comer
        int esquerda = (id + quantidade - 1) % quantidade;
        int direita = (id + 1) % quantidade;

        tentarComer(esquerda);
        tentarComer(direita);
    }

    // Lógica Central do Monitor
    private void tentarComer(int id) {
        int esquerda = (id + quantidade - 1) % quantidade;
        int direita = (id + 1) % quantidade;

        // Condição para comer:
        // 1. Eu estou com fome (FAMINTO)
        // 2. Meu vizinho da esquerda NÃO está comendo
        // 3. Meu vizinho da direita NÃO está comendo
        if (estados[id] == Estado.FAMINTO &&
            estados[esquerda] != Estado.COMENDO &&
            estados[direita] != Estado.COMENDO) {
            
            estados[id] = Estado.COMENDO;
            
            // Acorda as threads que estão dormindo no wait() para que elas
            // verifiquem se o estado delas mudou para COMENDO.
            notifyAll(); 
        }
    }
}