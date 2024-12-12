import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DataManager {
    private final List<Object> processors = new ArrayList<>();
    private final List<String> data = new ArrayList<>();

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

    // Обработка данных с использованием потоков и Stream API
    public void processData() {
        List<Thread> threads = new ArrayList<>(); // Список потоков

        // Перебираем строки данных
        data.forEach(line -> {
            // Создаём поток вручную для обработки текущей строки
            Thread thread = new Thread(() -> {
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

            threads.add(thread); // Добавляем поток в список
            thread.start(); // Запускаем поток
        });

        // Ожидание завершения всех потоков
        threads.forEach(thread -> {
            try {
                thread.join(); // Дожидаемся завершения каждого потока
            } catch (InterruptedException e) {
                e.printStackTrace(); // Обрабатываем исключения
            }
        });
    }

    // Сохранение данных в файл
    public void saveData(String destination) throws IOException {
        Files.write(Paths.get(destination), data);
    }
}
