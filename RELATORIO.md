# Relatório Comparativo: O Jantar dos Filósofos

## 1. Introdução
O problema do Jantar dos Filósofos é um cenário clássico de sincronização proposto por Dijkstra. O objetivo deste trabalho foi implementar e comparar três estratégias diferentes para resolver a contenção de recursos (garfos) e evitar Deadlock (impasse) e Starvation (inanição).

## 2. Metodologia
Foram implementadas três soluções em Java:
1.  **Hierarquia de Recursos (Tarefa 2):** Quebra de simetria onde um filósofo inverte a ordem de aquisição.
2.  **Semáforo/Garçom (Tarefa 3):** Limita o acesso à mesa a no máximo 4 filósofos simultaneamente.
3.  **Monitor (Tarefa 4):** Centraliza o controle de estado e sincronização em um objeto Mesa.

Os testes foram realizados em um ambiente controlado, executando cada solução por **5 minutos (300 segundos)**. Os tempos de pensar e comer foram aleatórios entre 1 e 3 segundos.

## 3. Resultados Obtidos

| Métrica | Tarefa 2 (Hierarquia) | Tarefa 3 (Semáforo) | Tarefa 4 (Monitor) |
| :--- | :---: | :---: | :---: |
| **Total de Refeições** | 273 | 280 | 284 |
| **Tempo Médio de Espera (ms)** | 1469,90 | 3344,43 | 3251,95 |
| **Utilização de Garfos (%)** | 74,13 | 74,47 | 76,3 |
| **Coeficiente de Variação (CV)** | 0,0486 | 0,0000 | 0,0282 |

*(Nota: O CV mede a justiça. Quanto mais próximo de 0, mais justa foi a distribuição de refeições entre os filósofos).*

## 4. Análise Comparativa

### 4.1 Prevenção de Deadlock
* **Hierarquia:** Previne deadlock eliminando a condição de "Espera Circular". Como a ordem de aquisição é linear para o último filósofo, o ciclo nunca fecha.
* **Semáforo:** Previne deadlock limitando a concorrência. Garante que sempre haverá pelo menos um garfo sobrando para os filósofos sentados (Princípio da Casa dos Pombos).
* **Monitor:** Previne deadlock através da verificação atômica de estado. O filósofo só transita para "COMENDO" se ambos os recursos estiverem livres, eliminando a posse parcial de recursos.

### 4.2 Prevenção de Starvation e Fairness
* **Hierarquia:** Não garante fairness explicitamente. Depende do escalonador do SO. Em testes longos, tende a equalizar, mas teoricamente um filósofo pode sofrer inanição.
* **Semáforo:** Também depende do escalonador para a aquisição do semáforo. É justo na entrada da sala, mas não necessariamente na aquisição dos garfos.
* **Monitor:** Implementou a maior garantia de fairness. O uso de filas de espera (`wait` set) e notificação (`notifyAll`) garante que filósofos famintos sejam acordados para tentar comer.

### 4.3 Performance e Utilização
* A solução de **Hierarquia** geralmente apresenta o menor overhead, pois utiliza apenas locks intrínsecos sem lógica complexa de gestão.
* A solução de **Semáforo** limita artificialmente a concorrência (max 4). Em cenários de alta carga, isso pode reduzir ligeiramente o throughput total, pois impede que um par específico (ex: 0 e 2) coma se a sala estiver cheia com outros esperando.
* O **Monitor** possui overhead de sincronização (context switch em `wait`/`notify`), mas oferece o controle mais fino.

## 5. Conclusão
Após a análise, conclui-se que:

* Para sistemas onde o **desempenho bruto** é prioridade e o risco de starvation é aceitável, a **Solução de Hierarquia (Tarefa 2)** é a mais eficiente e simples.
* Para sistemas que exigem **robustez e justiça** garantida, a **Solução de Monitor (Tarefa 4)** é a mais adequada, pois abstrai a complexidade e gerencia os estados de forma previsível, facilitando a manutenção e a extensibilidade do código.