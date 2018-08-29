import os
import matplotlib.pyplot as plt

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

def vazao_calc(time, requisicoes):
    elapsed_time_segundos = time/1000
    numero_max = requisicoes
    return numero_max/elapsed_time_segundos

def tempo_medio_serv(time, requisicoes):
    return time/requisicoes

def dados_vazao():
    for item in dados:
        vaz = vazao_calc(item[3],item[1])
        temp_med_ser(item[3],item[1])
        
        vazao.append(vaz)
        requisicoes.append(item[1])
        tempo_medio_servico.append(temp_med_ser)

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

dir_memoria = "output-bench-memoria"
dir_memoria2 = "output-bench-memoria2"
dir_primos = "output-bench-primos"

dados_arquivos(dir_primos)
dados_vazao()
print(vazao)
#grafico_vazao_req()
#grafico_tempo_req()

    
