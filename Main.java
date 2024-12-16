import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        DataManager dataManager = new DataManager();
        DataProcessors dataProcessors = new DataProcessors();
        dataManager.registerDataProcessor(dataProcessors); // Сохраняем объект DataProcessors

        try {
            dataManager.loadData("input.txt");
            dataManager.processData();
            dataManager.shutdown(); // Ждем завершения всех задач
            dataManager.saveData("output.txt", dataProcessors); // Передаем dataProcessors для сохранения обработанных данных
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
