import java.util.ArrayList;
import java.util.List;

public class DataProcessors {
    private final List<String> processedData = new ArrayList<>(); // Новый список для хранения обработанных данных

    @DataProcessor
    public void filterData(String line) {
        if (line.contains("Текст")) { // Условие фильтрации
            processedData.add(line); // Добавляем отфильтрованную строку в список
            System.out.println("Filtered: " + line);
        }
    }

    @DataProcessor
    public void transformData(String line) {
        String transformed = line.toUpperCase(); // Пример преобразования
        processedData.add(transformed); // Добавляем преобразованную строку в список
        System.out.println("Transformed: " + transformed);
    }

    @DataProcessor
    public void aggregateData(String line) {
        int length = line.length();
        processedData.add("Aggregated (length): " + length); // Добавляем длину строки в список
        System.out.println("Aggregated (length): " + length);
    }
    

    public List<String> getProcessedData() { // Метод для получения обработанных данных
        return processedData;
    }
}
