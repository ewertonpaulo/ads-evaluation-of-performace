
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class BenchmarkAcessoMemoria {

	public static final int DEFAULT_KB_IN_MEMORY = 1024;
	public static final int DEFAULT_SEARCH_REPETITIONS = 100000000;
	public static final String DEFAULT_OUTPUT_DIR = "output-bench-memoria";
	public static final String SEP = "\t";
	public static final String PATH_SEP = "/";
	public static final String LINE_SEP = System.getProperty("line.separator");

	private int kbInMemory;
	private int arraySize;
	private long[] buffer;
	private Random rand;
	private int searchRepetitions;
	private FileWriter measurementsOut;
	private String outputDir;

	public BenchmarkAcessoMemoria(int kbInMemory, int searchRepetitions,
			String outputDir) {
		this.kbInMemory = kbInMemory;
		this.arraySize = (int) Math.round(kbInMemory * 1024L * 8 / 64);
		this.searchRepetitions = searchRepetitions;
		this.outputDir = outputDir;
		this.buffer = new long[arraySize];
		this.rand = new Random();
	}

	private void populateArray() {
		for (int i = 0; i < arraySize; i++) {
			buffer[i] = rand.nextInt(arraySize - 1);
		}
	}

	private void searchArray(int searchRepetitions) {
		for (int i = 0; i < searchRepetitions; i++) {
			buffer[rand.nextInt(arraySize - 1)]++;
		}
	}

	private void initOutput(String executionId, long startTime)
			throws IOException {
		new File(outputDir).mkdir();
		this.measurementsOut = new FileWriter(outputDir + PATH_SEP
				+ "measurement-kb" + kbInMemory + "-rep" + searchRepetitions
				+ "-st" + startTime + ".txt");

	}

	private void closeOutput() throws IOException {
		measurementsOut.close();
	}

	public long start(String executionId) {
		long searchTime = 0;
		try {
			long startTime = System.currentTimeMillis();
			initOutput(executionId, startTime);
			populateArray();
			long searchStartTime = System.currentTimeMillis();
			searchArray(searchRepetitions);
			long endTime = System.currentTimeMillis();
			long populateTime = searchStartTime - startTime;
			searchTime = endTime - searchStartTime;
			long totalTime = endTime - startTime;
			measurementsOut.write("KBinMemory" + SEP + "ArraySize" + SEP
					+ "SearchRepetitions" + SEP + "StartTime" + SEP + 
					"PopulateTime" + SEP + "SearchTime" + SEP + "TotalTime" + LINE_SEP);
			String valuesLine = kbInMemory + SEP + arraySize + SEP
					+ searchRepetitions + SEP + startTime + SEP + populateTime + SEP 
					+ searchTime + SEP + totalTime + LINE_SEP;
			System.out.print(valuesLine);
			measurementsOut.write(valuesLine);
			closeOutput();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return searchTime;
	}

	public static void main(String[] args) {
		if (args.length <= 0) {
			System.err.println("Erro! Faltam argumentos.");
			System.err
					.println("Usage: java MemoryAcessBenchmark <Tamanho do array na memoria (KB)> "
							+ "<Repeticoes de busca>");
			System.exit(1);
		}
		int kbInMemory = Integer.parseInt(args[0]);
		int searchRepetitions = args.length > 1 ? Integer.parseInt(args[1])
				: DEFAULT_SEARCH_REPETITIONS;
		String outputDir = args.length > 2 ? args[2] : DEFAULT_OUTPUT_DIR;
		BenchmarkAcessoMemoria benchmark = new BenchmarkAcessoMemoria(kbInMemory,
				searchRepetitions, outputDir);
		benchmark.start("exec1");
	}
}