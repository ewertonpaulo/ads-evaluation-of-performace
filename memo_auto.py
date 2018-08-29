import os
from time import sleep

#Funcção recursiva exponencial de kbites até tamanho informado
def exp_tamanho_array(kb):
    os.system("java -cp bin/;lib/* BenchmarkAcessoMemoria "+str(kb)+" 80000000")
    sleep(10)
    if kb < 1048576:
        exp_tamanho_array(kb*2)


def rep_acessos():
    for acesso in range(200000000, 1600000000,10000000):
        os.system("java -cp bin/;lib/* BenchmarkAcessoMemoria 1024 "+str(acesso))
        sleep(5)

#exp_tamanho_array(1024)

rep_acessos()
