
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TimerTask;

import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;

public class MonitorThread extends TimerTask {

	public static final long DEFAULT_TIME_INTERVAL = 100; // 0.1 second
	public static final String DEFAULT_CPU_UTILIZATION_FILE = "cpu-utilization";
	public static final String DEFAULT_MEASUREMENT_FILE = "measurements";
	public static final String FIELD_SEP = "\t";
	public static final String END_LINE = System.getProperty("line.separator");
	public static final String PATH_SEP = "/";

	private Sigar sigar;
	private long startTime;
	private double utilSum;
	private long utilCount;
	private FileWriter measurementsOut;
	private Map<String, String> measurementInfo;
	private String executionId;

	public MonitorThread(String outputDir, String executionId)
			throws IOException {
		this.executionId = executionId;
		this.measurementInfo = new LinkedHashMap<String, String>();
		this.sigar = new Sigar();
		this.utilSum = 0;
		this.utilCount = 0;
		this.startTime = System.currentTimeMillis();
		initOutput(outputDir);
	}

	public void putMeasurementInfo(String key, String value) {
		measurementInfo.put(key, value);
	}

	private void initOutput(String outputDir) throws IOException {
		new File(outputDir).mkdir();
		measurementsOut = new FileWriter(outputDir + PATH_SEP
				+ DEFAULT_MEASUREMENT_FILE + "-" + executionId + "-st"
				+ startTime + ".txt");
	}

	@Override
	public synchronized void run() {
		try {
			double cpuUtilization = sigar.getCpuPerc().getCombined();
			if (cpuUtilization >= 0) {
				utilSum += cpuUtilization;
				utilCount++;
			}
		} catch (SigarException e) {
			e.printStackTrace();
		}
	}

	public void finishMeasurement(Map<String, String> benchmarkInfo) {
		try {
			recordMeasurementInfo(benchmarkInfo);
			writeMeasurementInfo();
			closeFiles();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private synchronized void closeFiles() throws IOException {
		measurementsOut.close();
	}

	private void writeMeasurementInfo() throws IOException {
		StringBuilder fieldsLine = new StringBuilder();
		StringBuilder valuesLine = new StringBuilder();
		for (Entry<String, String> entry : measurementInfo.entrySet()) {
			fieldsLine.append(entry.getKey() + FIELD_SEP);
			valuesLine.append(entry.getValue() + FIELD_SEP);
		}
		fieldsLine.setLength(fieldsLine.length() - 1);
		valuesLine.setLength(valuesLine.length() - 1);

		measurementsOut.write(fieldsLine.toString() + END_LINE);
		measurementsOut.write(valuesLine.toString() + END_LINE);
	}

	private void recordMeasurementInfo(Map<String, String> benchmarkInfo) {
		long elapsedTime = System.currentTimeMillis() - startTime;
		double meanUtil = utilSum / utilCount;
		measurementInfo.putAll(benchmarkInfo);
		measurementInfo.put("StartTime", Long.toString(startTime));
		measurementInfo.put("ElapsedTime", Long.toString(elapsedTime));
		measurementInfo.put("MeanUtilization", Double.toString(meanUtil));
	}
}