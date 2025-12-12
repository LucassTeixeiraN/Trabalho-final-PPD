# Tarefa 1

## Explicação do deadlock

Da forma com que esse código foi feito, esperasse mesmo que, em algum momento enquanto ele estiver rodando, ele resulte em um deadlock. Isso porque, mesmo utilizando o synchronized, nada impede que dois filósofos tentem pegar o mesmo garfo ao mesmo tempo, e, neste caso, esse recurso já vai estar travado por outro filosofo (por conta do synchronized) e isso vai fazer com que o código não consiga terminar a execução.

# Tarefa 2

## Solução de Prevenção de Deadlock

Agora, todos os filósofos, exceto o quinto, pegam o garfo da esquerda primeiro, enquanto o quinto pega o da direito. Isso faz com que o deadlock não ocorra porque todos os filosofos vão conseguir pegar o garfo da esquerda, enquanto o último quando for tentar pegar o garfo da direita não vai conseguir pois ele vai estar travado pelo primeiro filósofo.

## Análise de Starvation

Em teoria é possível, mas estremamente improvável. Isso porque ainda é possível que dois filósofos tenham exatamente o mesmo tempo de espera e tentem pegar o mesmo garfo ao mesmo tempo.

## Comparação com a tarefa 1

* **Tarefa 1:** O sistema é instável. A execução é interrompida abruptamente assim que o ciclo de espera se forma (ocorre em segundos ou poucos minutos).

* **Tarefa 2:** O sistema é estável e robusto. O programa executou pelo tempo determinado (2 minutos) sem travamentos, com todos os filósofos conseguindo se alimentar múltiplas vezes.

## Estatísticas de Execução:
```bash
ilósofo 0: Execução finalizada pelo temporizador.
Filósofo 0 comeu 21 vezes.
Filósofo 1: Execução finalizada pelo temporizador.
Filósofo 1 comeu 21 vezes.
Filósofo 3: Terminou de comer e SOLTOU os garfos.
Filósofo 3: Começou a PENSAR.
Filósofo 2: Execução finalizada pelo temporizador.
Filósofo 2 comeu 22 vezes.
Filósofo 3: Execução finalizada pelo temporizador.
Filósofo 3 comeu 23 vezes.
Filósofo 4: Execução finalizada pelo temporizador.
Filósofo 4 comeu 21 vezes.
-----------------------------
Total de refeições servidas: 108
```

# Tarefa 3

A solução desse exercício limita a quantidade de filósofos que podem tentar pegar os garfos simultaneamente. Primeiro, o filósofo verifica se há vagas na mesa, se não houver ele entra em uma fila de espera, e quando um filósofo que está na mesa termina de comer, ele libera o espaço para que outro filósofo possa entrar na mesa.

## Por que previne Deadlock?
O deadlock clássico exige que todos os 5 filósofos segurem seu garfo esquerdo ao mesmo tempo, esperando pelo direito. Ao limitar a concorrência para 4 filósofos:

* No pior caso, 4 filósofos sentam à mesa e pegam seus garfos da esquerda.
* Resta 1 garfo livre na mesa (pois são 5 garfos no total).
* Um desses 4 filósofos terá necessariamente o garfo da direita livre (Pelo Princípio da Casa dos Pombos).
* Esse filósofo consegue comer e liberar seus dois garfos, permitindo que os outros prossigam.

## Vantagens e Desvantagens
* **Vantagem:** É uma solução justa e simples de implementar. Não exige que os filósofos saibam seus IDs ou se são pares/ímpares (como na Tarefa 2). Garante a ausência de Deadlock. 
* **Desvantagem:** Reduz ligeiramente o paralelismo potencial. Em um cenário onde os recursos (garfos) estão livres, um filósofo pode ser impedido de comer apenas porque o limite de "pessoas na sala" foi atingido, mesmo que os garfos específicos que ele precisa estejam disponíveis. Além disso, semáforos adicionam um pequeno overhead de troca de contexto.

## Comparação de Desempenho
### Desempenho:
```bash
Filósofo 0: Execução finalizada pelo temporizador.
Filósofo 0 comeu 23 vezes.
Filósofo 1 comeu 22 vezes.
Filósofo 2: Execução finalizada pelo temporizador.
Filósofo 2 comeu 22 vezes.
Filósofo 3: Execução finalizada pelo temporizador.
Filósofo 3 comeu 22 vezes.
Filósofo 4: Execução finalizada pelo temporizador.
Filósofo 4 comeu 22 vezes.
-----------------------------
Total de refeições servidas: 111
```

* **Tarefa 2 (Hierarquia):** Tende a ser ligeiramente mais rápida, pois não há bloqueio prévio. O filósofo só para se o garfo estiver ocupado.
* **Tarefa 3 (Semáforo):** Pode ter um número total de refeições levemente menor devido ao gargalo do semáforo, mas a diferença é marginal em sistemas com poucos processos (5 filósofos).

# Tarefa 4
Esta solução utiliza o padrão de projeto Monitor, onde uma classe intermediária controla o acesso aos recursos e gerencia os estados das threads.

## Garantia de Fairness e prevenção de Starvation
O Fairness é garantido pelo sistema de estados e notificação:

1. Quando um filósofo fica FAMINTO, ele sinaliza sua intenção.
2. Ele só espera (wait) se realmente não puder comer.
3. Quando os vizinhos terminam, eles são obrigados (soltarGarfos) a testar se os vizinhos (o filósofo que estava esperando) podem comer agora.
4. Isso cria um mecanismo de passagem de bastão, onde a liberação de recursos ativa ativamente os vizinhos interessados.

## Prevenção de Deadlock
O Deadlock é impossível porque a aquisição dos recursos é atômica. O filósofo só muda para o estado COMENDO se, e somente se, ambos os garfos estiverem livres naquele instante exato. Não existe o estado parcial "peguei o esquerdo, esperando o direito".

## Comparação e Trade-offs com as outras tarefas
* Tarefa 2: É rápida, mas requer que as threads saibam uma ordem global de recursos.

* Tarefa 3: É muito segura, mas limita artificialmente a concorrência (max 4 comendo), mesmo que garfos estejam livres (ex: filósofos 0 e 2 poderiam comer, mas se 0,1,2,3 estiverem sentados, o 4 não entra).

* Tarefa 4: É a solução mais robusta logicamente. Ela maximiza o paralelismo (permite que 0 e 2 comam, e depois 1 e 3) sem risco de deadlock e com controle fino de estado.

<table>
    <tr>
        <th><b>Abordagem</b></th>
        <th><b>Deadlock</b></th>
        <th><b>Starvation</b></th>
        <th><b>Complexidade</b></th>
        <th><b>Desempenho</b></th>
    </tr>
    <tr>
        <th><b>Tarefa 1</b></th>
        <th>Sim</th>
        <th>Sim</th>
        <th>Baixo</th>
        <th>N/A</th>
    </tr>
    <tr>
        <th><b>Tarefa 2</b></th>
        <th>Não</th>
        <th>Possível</th>
        <th>Média</th>
        <th>Alto</th>
    </tr>
    <tr>
        <th><b>Tarefa 3</b></th>
        <th>Não</th>
        <th>Não</th>
        <th>Baixo</th>
        <th>Médio</th>
    </tr>
    <tr>
        <th><b>Tarefa 4</b></th>
        <th>Não</th>
        <th>Não</th>
        <th>Alta</th>
        <th>Alto</th>
    </tr>
</table>