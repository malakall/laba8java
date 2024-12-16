import java.util.ArrayList;
import java.util.List;

public class DataProcessors {
    private final List<String> processedData = new ArrayList<>(); // список для  обработанных данных

    @DataProcessor
    public void filterData(String line) {
        if (line.contains("Текст")) { 
            processedData.add(line);
            System.out.println("Filtered: " + line);
        }
    }

    @DataProcessor
    public void transformData(String line) {
        String transformed = line.toUpperCase();
        processedData.add(transformed);
        System.out.println("Transformed: " + transformed);
    }

    @DataProcessor
    public void aggregateData(String line) {
        int length = line.length();
        processedData.add("Aggregated (length): " + length);
        System.out.println("Aggregated (length): " + length);
    }
    

    public List<String> getProcessedData() { // Метод для получения обработанных данных
        return processedData;
    }
}
