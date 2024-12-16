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
    private List<Object> processors = new ArrayList<>();
    private List<String> data = new ArrayList<>();
    private ExecutorService executorService = Executors.newFixedThreadPool(3);

    // Регистрация обработчиков
    public void registerDataProcessor(Object processor) {
        processors.add(processor);
    }

    // Загрузка данных из файла
    public void loadData(String source) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(source))) {
            String line;
            while ((line = reader.readLine()) != null) {
                data.add(line);
            }
        }
    }

    // Обработка данных
    public void processData() {
        data.forEach(line -> {
            executorService.submit(() -> { // Отправляем задачу в пул потоков
                // Перебираем каждый процессор
                processors.forEach(processor -> {
                    // stream api для фильтрации
                    Arrays.stream(processor.getClass().getMethods())
                        .filter(method -> method.isAnnotationPresent(DataProcessor.class)) // Оставляем только те, что помечены @DataProcessor
                        .forEach(method -> {
                            try {
                                method.invoke(processor, line); // Вызываем метод с передачей строки
                            } catch (Exception e) {
                                System.out.println("ошибка" + e.getMessage());
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