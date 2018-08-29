
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Timer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

public class BenchmarkNumerosPrimos {

	public static final int DEFAULT_NUM_THREADS = 1;
	public static final long DEFAULT_NUMERO_MAXIMO = 100000;
	public static final String DEFAULT_OUTPUT_DIR = "output-bench-primos";

	private long numeroMax;
	private AtomicLong numPrimos;
	private int numThreads;
	private String outputDir;

	public BenchmarkNumerosPrimos(long numeroMax, int numThreads,
			String outputDir) {
		this.numThreads = numThreads;
		this.outputDir = outputDir;
		this.numeroMax = numeroMax;
		this.numPrimos = new AtomicLong(0L);
	}

	public long getNumPrimos() {
		return numPrimos.get();
	}

	public class NumerosPrimosThread implements Runnable {

		protected long numInicial;

		public NumerosPrimosThread(long numInicial) {
			this.numInicial = numInicial;
		}

		@Override
		public void run() {
			long localNumPrimos = 0;
			for (long numero = numInicial; numero < numeroMax; numero += numThreads) {
				long numDivisores = 0;
				for (long divisor = 1L; divisor <= numero / 2; divisor++) {
					if (numero % divisor == 0) {
						numDivisores++;
					}
				}
				if (numDivisores == 1) {
					localNumPrimos++;
				}
			}
			numPrimos.getAndAdd(localNumPrimos);
		}
	}

	public Map<String, String> getBenchmarkInfo() {
		Map<String, String> benchInfo = new LinkedHashMap<String, String>();
		benchInfo.put("NumThreads", Integer.toString(numThreads));
		benchInfo.put("NumeroMaximo", Long.toString(numeroMax));
		return benchInfo;
	}

	public void start(String idExecucao) {
		try {
			Timer timer = new Timer();
			ExecutorService threadPool = Executors
					.newFixedThreadPool(numThreads);
			MonitorThread monitor = new MonitorThread(outputDir, idExecucao);
			timer.schedule(monitor, 0, 1000);
			for (int i = 1; i <= numThreads; i++) {
				threadPool.execute(new NumerosPrimosThread(i));
			}
			threadPool.shutdown();
			threadPool.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);
			timer.cancel();
			monitor.finishMeasurement(getBenchmarkInfo());
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		if (args.length <= 0) {
			System.err.println("Erro! Faltam argumentos.");
			System.err
					.println("Uso: java BenchmarkNumerosPrimos <Numero de threads> [Numero maximo para buscar primos]");
			System.exit(1);
		}

		int numThreads = Integer.parseInt(args[0]);
		long numeroMaximo = args.length > 1 ? Long.parseLong(args[1])
				: DEFAULT_NUMERO_MAXIMO;
		String outputDir = args.length > 2 ? args[2] : DEFAULT_OUTPUT_DIR;
		BenchmarkNumerosPrimos benchmark = new BenchmarkNumerosPrimos(
				numeroMaximo, numThreads, outputDir);
		System.out.println("Encontrando numeros primos de 1 a " + numeroMaximo
				+ " usando " + numThreads + " thread(s) ...");
		String executionId = "mn" + numeroMaximo + "-nt" + numThreads;
		benchmark.start(executionId);
		System.out.println("Quantidade de numeros primos encontrados: "
				+ benchmark.getNumPrimos());
	}
}