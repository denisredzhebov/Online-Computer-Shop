package onlineShop.models.products.components;

public class RandomAccessMemory extends BaseComponent{
    private static final double OVERALL_PERFORMANCE = 1.20;
    public RandomAccessMemory(int id, String manufacturer, String model, double price, double overallPerformance, int generation) {
        super(id, manufacturer, model, price, overallPerformance * OVERALL_PERFORMANCE, generation);
    }
}
