public class DataProcessors {

    @DataProcessor
    public void filterData(String line) {
        if (line.contains("filter")) {
            System.out.println("Filtered: " + line);
        }
    }

    @DataProcessor
    public void transformData(String line) {
        String transformed = line.toUpperCase();
        System.out.println("Transformed: " + transformed);
    }

    @DataProcessor
    public void aggregateData(String line) {
        int length = line.length();
        System.out.println("Aggregated (length): " + length);
    }
}
