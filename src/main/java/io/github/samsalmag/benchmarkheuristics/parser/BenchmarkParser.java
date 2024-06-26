package io.github.samsalmag.benchmarkheuristics.parser;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import io.github.samsalmag.benchmarkheuristics.parser.json.JsonCreator;
import io.github.samsalmag.benchmarkheuristics.parser.json.JsonMethodItem;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.List;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Class that uses method and package name of the benchmarks in a benchmarkDict json file to
 * parse them (extracts stats, e.g. nested loops) and generates a new json file containing the results.
 * The structure of the read json file is a list of {@code Pair<String, Double>}, where String is the full
 * benchmark name, and Double is the metric value for a property of the benchmark.
 *
 * @author Malte Åkvist (creator)
 * @author Sam Salek (modified)
 */
public class BenchmarkParser {

    private final String baseTestPath;
    private final List<SimpleEntry<String, Double>> benchmarkMap;

    public BenchmarkParser(String baseTestPath, String benchmarkJsonPath) {
        this.baseTestPath = baseTestPath;
        this.benchmarkMap = readJson(benchmarkJsonPath);
    }

    public void setupShutdownHook(JsonCreator jsonCreator) {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> jsonCreator.createJson()));
    }

    public void parseBenchmarks(Parser parser, String outputPath) {
        parseBenchmarks(parser, 0, benchmarkMap.size() - 1, outputPath);
    }

    /**
     * Parses the unit tests that the benchmarks run.
     *
     * @param parser The parser to parse with.
     * @param firstBenchmarkIndex Index of the first benchmark to parse
     * @param lastBenchmarkIndex Index of the last benchmark to parse.
     * @param outputPath Where to output the parsed benchmarks.
     *
     * @return Success rate of the parsing as a percentage (0.0 - 100.0) .
     */
    public double parseBenchmarks(Parser parser, int firstBenchmarkIndex, int lastBenchmarkIndex, String outputPath) {
        // Check so indexes are in range, correct them if they are not
        if (firstBenchmarkIndex < 0) firstBenchmarkIndex = 0;
        if (lastBenchmarkIndex > benchmarkMap.size() - 1) lastBenchmarkIndex = benchmarkMap.size() - 1;
        if (lastBenchmarkIndex - firstBenchmarkIndex < 0) throw new IllegalArgumentException("Illegal index range.");

        DecimalFormat decimalFormat = new DecimalFormat("0.00");    // Used to format success rate
        JsonCreator jsonCreator = new JsonCreator(outputPath);
        setupShutdownHook(jsonCreator);
        int successfulIndex = 0;
        int iterationIndex = 0;
        double successRate = 0;
        double successRatePercentage = 0;

        System.out.println("\nSTARTING PARSING... \n");

        try {
            for (int i = firstBenchmarkIndex; i <= lastBenchmarkIndex; i++) {
                iterationIndex += 1;

                String benchmark = benchmarkMap.get(i).getKey();
                String method = benchmark.split("_Benchmark.benchmark_")[1];

                int secondLastDotIndex = benchmark.lastIndexOf('.', benchmark.lastIndexOf('.') - 1);
                // Get the class path, each directory is currently separated by dots so have to use paths library to resolve path
                String classPathDots = benchmark.substring(0, secondLastDotIndex);
                String[] parts = classPathDots.split("\\.");
                Path classPath = Paths.get("", parts);

                Path base = Paths.get(baseTestPath);
                Path resolvedPath = base.resolve(classPath);                     // Concatenate base path with class path
                Path benchmarkPath = Paths.get(resolvedPath + ".java");     // Add .java to file extension

                System.out.println(benchmarkPath);
                System.out.println(method);
                System.out.println("PARSING INDEX: " + (iterationIndex) + "/" + (lastBenchmarkIndex - firstBenchmarkIndex + 1));

                ParsedMethod parsedMethod = parser.parseMethod(benchmarkPath.toString(), method);
                if (parsedMethod == null) {
                    System.out.println("*** PARSING EXCEPTION! SKIPPING BENCHMARK! ***");
                    System.out.println("SUCCESSFUL PARSINGS: " + successfulIndex + "/" + iterationIndex +
                            ", SUCCESS RATE: " + successRatePercentage + "% \n");
                    continue;
                }

                JsonMethodItem jsonMethodItem = new JsonMethodItem(parsedMethod);
                Double stabilityMetricValue = benchmarkMap.get(i).getValue();
                jsonMethodItem.setStabilityMetricValue(stabilityMetricValue);
                // jsonMethodItem.setCodeCoverageMetricValue(codeCoverageMetricValue);
                jsonCreator.add(jsonMethodItem);

                successfulIndex += 1;
                successRate = ((double) successfulIndex) / iterationIndex;
                successRatePercentage = Math.round(successRate * 10000.0) / 100.0;
                System.out.println("SUCCESSFUL PARSINGS: " + successfulIndex + "/" + iterationIndex +
                                    ", SUCCESS RATE: " + successRatePercentage + "% \n");
            }

            System.out.println("PARSING COMPLETE! " +
                            "SUCCESS RATE: " + successfulIndex + "/" + iterationIndex +
                            ", " + successRatePercentage + "% \n");
            return successRatePercentage;
        }
        finally {
            // Create json file even if there's an exception
            jsonCreator.createJson();
        }
    }

    /**
     * Reads the given JSON file and converts it into a list of SimpleEntry<String, Double> objects,
     * which is essentially a list of pairs.
     *
     * @return List of SimpleEntry<String, Double> objects.
     */
    private List<SimpleEntry<String, Double>> readJson(String jsonPath) {
        Gson gson = new Gson();
        List<SimpleEntry<String, Double>> results = new ArrayList<>();

        Type listType = new TypeToken<List<List<Object>>>(){}.getType();

        try (FileReader reader = new FileReader(new File(jsonPath).getAbsoluteFile())) {
            List<List<Object>> rawData = gson.fromJson(reader, listType);

            for (List<Object> entry : rawData) {
                String methodName = (String) entry.get(0);
                double value = ((Number) entry.get(1)).doubleValue();
                results.add(new SimpleEntry<>(methodName, value));
            }

        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
        return results;
    }
}
