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


def inicio(dados):
    return dados[0]



def vazao_calc(time, requisicoes):
    elapsed_time_segundos = time/1000
    return requisicoes/elapsed_time_segundos

def dados_primos():
    for item in dados:
        vaz = vazao_calc(item[3],item[1])
        
        vazao.append(vaz)
        requisicoes.append(item[1])
        tempo_medio_servico.append((item[1]/1000)/item[3])

    tempo_medio_servico.sort()

def dados_memo():
    for item in dados:
        vazao.append(item[3]/(item[6]/1000))
        requisicoes.append(item[2])
        tempo_medio_servico.append((item[6]/1000)/item[3])
        tamanho_array.append(item[0])
    
    #vazao.sort()

def grafico_primo_vazao_req():
    plt.plot(requisicoes,vazao)
    plt.title('Vazão vs Requisições')
    plt.show()

def grafico_primo_tempo_req():
    plt.plot(requisicoes, tempo_medio_servico)
    plt.title('Requisições vc Tempo médio')
    plt.show()

def grafico_memo_arraySize_vazao():
    plt.plot(tamanho_array, vazao)
    plt.title('ArraySize vs Vazão')
    plt.show()

def grafico_memo_arraySize_temposerv():
    plt.plot(tamanho_array, tempo_medio_servico)
    plt.title('ArraySize vs Tempo de Serviço')
    plt.show()

    
vazao = []
requisicoes = []
tempo_medio_servico = []
tamanho_array = []

dir_memoria = "output-bench-memoria"
dir_memoria2 = "output-bench-memoria2"
dir_primos = "output-bench-primos"

"""
#output-bench-primos
dados_arquivos(dir_primos)
dados_primos()
dados.sort(key = inicio)
grafico_primo_vazao_req()
grafico_primo_tempo_req()

"""
#output-bench-memoria-requisiçoes-fixas
dados_arquivos(dir_memoria)
dados.sort(key = inicio)
dados_memo()
grafico_memo_arraySize_vazao()
grafico_memo_arraySize_temposerv()



    
