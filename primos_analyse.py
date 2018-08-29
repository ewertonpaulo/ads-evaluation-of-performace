import os
import matplotlib.pyplot as plt

dir_memoria = "output-bench-memoria"
dir_primos = "output-bench-primos"

dados = []

def dados_arquivos(dir_):
    arquivos = os.listdir(dir_)
    for i in range(len(arquivos)):
        
        nome = dir_+"/"+arquivos[i]
        txt = open(nome,'r')

        linha = txt.readlines()
        linha[1] = linha[1].replace("\n,","")
        lista_linhas = linha[1].split()

        for i in range(len(lista_linhas)):
            lista_linhas[i] = float(lista_linhas[i])

        dados.append(lista_linhas)
        txt.close()

def dados_vazao():
    for item in dados:
        
        elapsed_time_segundos = item[3]/1000
        numero_max = item[1]
        
        vaz = numero_max/elapsed_time_segundos
        
        vazao.append(vaz)
        requisicoes.append(numero_max)
        tempo_medio_servico.append(elapsed_time_segundos/numero_max)

    #vazao.sort()
    requisicoes.sort()

def grafico_vazao_req():
    plt.plot(requisicoes,vazao)
    plt.title('Vazão vs Requisições')
    plt.xlabel('Tamanho')
    plt.ylabel('Vazão')
    plt.show()

def grafico_tempo_req():
    plt.plot(requisicoes, tempo_medio_servico)
    plt.title('Requisições vc Tempo médio')
    plt.xlabel('Tamanho')
    plt.ylabel('Tempo médio')
    plt.show()

vazao = []
requisicoes = []
tempo_medio_servico = []


dados_arquivos(dir_primos)
dados_vazao()
print(vazao)
#grafico_vazao_req()
#grafico_tempo_req()

    
