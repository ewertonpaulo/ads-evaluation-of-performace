import os
import matplotlib.pyplot as plt

dados = []
vazao = []
requisicoes = []
tempo_medio_servico = []
tamanho_array = []
acessos = []

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

def inicio_mem2(dados):
    return dados[2]

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
        vazao.append(item[2]/(item[6]/1000))
        tempo_medio_servico.append((item[6]/1000)/item[2])
        tamanho_array.append(item[0])
        acessos.append(item[2])

def grafico(lista1, lista2, title):
    plt.plot(lista1,lista2)
    plt.title(title)
    plt.show()

dir_memoria = "output-bench-memoria"
dir_memoria2 = "output-bench-memoria2"
dir_primos = "output-bench-primos"

print("1 - questão 1 output-bench-primos")
print("2 - questão 2.1 output-bench-memoria-requisiçoes-fixas")
print("3 - questão 2.2 output-bench-memoria2")
escolha = input("escolha: ")

if escolha == "1":
    #output-bench-primos
    dados_arquivos(dir_primos)
    dados_primos()
    dados.sort(key = inicio)
    grafico(requisicoes, vazao, 'Vazão vs Requisições')
    grafico(requisicoes, tempo_medio_servico, 'Requisições vc Tempo médio')
if escolha == "2":
    #output-bench-memoria-requisiçoes-fixas
    dados_arquivos(dir_memoria)
    dados.sort(key = inicio)
    dados_memo()
    grafico(tamanho_array, vazao, 'ArraySize vs Vazão')
    grafico(tamanho_array, tempo_medio_servico, 'ArraySize vs Tempo de Serviço')
if escolha == "3":
    #output-bench-memoria2
    dados_arquivos(dir_memoria2)
    dados.sort(key = inicio_mem2)
    dados_memo()
    vazao.sort()
    grafico(acessos, vazao, 'Acessos vs Vazão')
    grafico(acessos, tempo_medio_servico, 'Acessos vs Tempo médio de Serviço')
