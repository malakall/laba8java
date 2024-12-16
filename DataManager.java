import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DataManager {
    private final List<Object> processors = new ArrayList<>();
    private final List<String> data = new ArrayList<>();
    private final ExecutorService executorService = Executors.newFixedThreadPool(3); // Используем фиксированный пул из 3 потоков

    // Регистрация обработчиков
    public void registerDataProcessor(Object processor) {
        processors.add(processor);
    }

    // Загрузка данных из файла
    public void loadData(String source) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(source))) {
            String line;
            while ((line = reader.readLine()) != null) {
                data.add(line); // Добавляем каждую считанную строку в список данных
            }
        }
    }

    // Обработка данных с использованием ExecutorService
    public void processData() {
        // Перебираем строки данных
        data.forEach(line -> {
            executorService.submit(() -> { // Отправляем задачу на выполнение в пул потоков
                // Перебираем каждый процессор
                processors.forEach(processor -> {
                    // Используем Stream API для фильтрации и вызова методов с аннотацией @DataProcessor
                    Arrays.stream(processor.getClass().getMethods()) // Получаем все методы класса
                        .filter(method -> method.isAnnotationPresent(DataProcessor.class)) // Оставляем только те, что помечены @DataProcessor
                        .forEach(method -> {
                            try {
                                method.invoke(processor, line); // Вызываем метод с передачей строки
                            } catch (Exception e) {
                                e.printStackTrace(); // Обрабатываем возможные исключения
                            }
                        });
                });
            });
        });
    }

    // Завершение работы ExecutorService
    public void shutdown() {
        executorService.shutdown(); // Завершаем работу пула потоков
        try {
            // Ждем завершения всех задач в пуле потоков (например, 60 секунд)
            if (!executorService.awaitTermination(60, java.util.concurrent.TimeUnit.SECONDS)) {
                executorService.shutdownNow(); // Принудительное завершение, если задачи не завершены
            }
        } catch (InterruptedException e) {
            System.out.println("ошибка" + e.getMessage());
        }
    }

    public void saveData(String destination, DataProcessors processor) throws IOException {
        List<String> processed = processor.getProcessedData(); // Получаем обработанные данные
        Files.write(Paths.get(destination), processed); // Записываем их в файл
    }
}