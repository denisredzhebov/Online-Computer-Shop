package onlineShop.models.products.computers;

import onlineShop.models.products.BaseProduct;
import onlineShop.models.products.components.Component;
import onlineShop.models.products.peripherals.Peripheral;

import java.util.ArrayList;
import java.util.List;

import static onlineShop.common.constants.ExceptionMessages.*;

public abstract class BaseComputer extends BaseProduct implements Computer {
    private List<Component> components;
    private List<Peripheral> peripherals;

    protected BaseComputer(int id, String manufacturer, String model, double price, double overallPerformance) {
        super(id, manufacturer, model, price, overallPerformance);
        this.components = new ArrayList<>();
        this.peripherals = new ArrayList<>();
    }

    @Override
    public double getOverallPerformance() {
        if (this.components.isEmpty()) {
            return super.getOverallPerformance();
        }
        return super.getOverallPerformance() + getAverageOverallPerformanceSumComponents();

    }

    @Override
    public double getPrice() {
        return super.getPrice() + getPriceSumComponents() + getSumPricePeripherals();
    }

    @Override
    public List<Component> getComponents() {
        return this.components;
    }

    @Override
    public List<Peripheral> getPeripherals() {
        return this.peripherals;
    }

    @Override
    public void addComponent(Component component) {
        if (this.components.contains(component)) {
            throw new IllegalArgumentException(String.format(EXISTING_COMPONENT, component.getClass().getSimpleName(), this.getClass().getSimpleName(), this.getId()));
        }
        this.components.add(component);
    }

    @Override
    public Component removeComponent(String componentType) {
        Component lookingComponent = this.components.stream().filter(a -> a.getClass().getSimpleName().equals(componentType)).findFirst().orElse(null);
        if (this.components.isEmpty() || !this.components.contains(lookingComponent)) {
            throw new IllegalArgumentException(String.format(NOT_EXISTING_COMPONENT, componentType, this.getClass().getSimpleName(), this.getId()));
        }
        this.components.remove(lookingComponent);
        return lookingComponent;
    }

    @Override
    public void addPeripheral(Peripheral peripheral) {
        if (this.peripherals.contains(peripheral)) {
            throw new IllegalArgumentException(String.format(EXISTING_PERIPHERAL, peripheral.getClass().getSimpleName(), this.getClass().getSimpleName(), this.getId()));
        }
        this.peripherals.add(peripheral);
    }

    @Override
    public Peripheral removePeripheral(String peripheralType) {
        Peripheral lookingPeripheral = this.peripherals.stream().filter(a -> a.getClass().getSimpleName().equals(peripheralType)).findFirst().orElse(null);
        if (this.peripherals.isEmpty() || !this.peripherals.contains(lookingPeripheral)) {
            throw new IllegalArgumentException(String.format(NOT_EXISTING_PERIPHERAL, peripheralType, this.getClass().getSimpleName(), this.getId()));
        }
        this.peripherals.remove(lookingPeripheral);
        return lookingPeripheral;
    }

    public double getAverageOverallPerformanceSumPeripherals() {
        double sum = getSumPeripherals();
        double result = sum / this.peripherals.size();
        if (this.peripherals.isEmpty()) {
            result = 0.00;
        }
        return result;
    }

    private double getSumPricePeripherals() {
        double sum = 0;
        for (Peripheral peripheral : peripherals) {
            sum += peripheral.getPrice();
        }
        return sum;
    }

    private double getSumPeripherals() {
        double sum = 0;
        for (Peripheral peripheral : peripherals) {
            sum += peripheral.getOverallPerformance();
        }
        return sum;
    }

    public double getAverageOverallPerformanceSumComponents() {
        double sum = getSumComponents();
        double result = sum / this.components.size();
        if (this.components.isEmpty()) {
            result = 0.00;
        }
        return result;
    }

    private double getSumComponents() {
        double sum = 0;
        for (Component component : components) {
            sum += component.getOverallPerformance();
        }
        return sum;
    }

    private double getPriceSumComponents() {
        double sum = 0;
        for (Component component : components) {
            sum += component.getPrice();
        }
        return sum;
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("Overall Performance: %.2f. Price: %.2f - %s: %s %s (Id: %d)",
                this.getOverallPerformance(), this.getPrice(), this.getClass().getSimpleName(), this.getManufacturer(), this.getModel(), this.getId()));
        sb.append(System.lineSeparator());
        sb.append(String.format(" Components (%d):", this.components.size())).append(System.lineSeparator());
        for (Component component : components) {
            sb.append(String.format("  %s", component)).append(System.lineSeparator());
        }
        sb.append(String.format(" Peripherals (%d); Average Overall Performance (%.2f):", this.peripherals.size(),
                getAverageOverallPerformanceSumPeripherals())).append(System.lineSeparator());

        for (Peripheral peripheral : peripherals) {
            sb.append(String.format("  %s", peripheral)).append(System.lineSeparator());
        }
        return sb.toString().trim();
    }
}
