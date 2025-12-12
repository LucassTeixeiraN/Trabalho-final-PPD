# Tarefa 1

## Explicação do deadlock

Da forma com que esse código foi feito, esperasse mesmo que, em algum momento enquanto ele estiver rodando, ele resulte em um deadlock. Isso porque, mesmo utilizando o synchronized, nada impede que dois filósofos tentem pegar o mesmo garfo ao mesmo tempo, e, neste caso, esse recurso já vai estar travado por outro filosofo (por conta do synchronized) e isso vai fazer com que o código não consiga terminar a execução.

