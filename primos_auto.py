import os
from time import sleep
"""
250000
"""
def primos():
    for i in range(100000,500000,10000):
        array = str(i)
        os.system("java -cp bin\;lib\* BenchmarkNumerosPrimos 1 "+array)
        sleep(2)
primos()
