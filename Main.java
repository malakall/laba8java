import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        DataManager dataManager = new DataManager();
        dataManager.registerDataProcessor(new DataProcessors());

        try {
            dataManager.loadData("input.txt");
            dataManager.processData();
            dataManager.saveData("output.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
