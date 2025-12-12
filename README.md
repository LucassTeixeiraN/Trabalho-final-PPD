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