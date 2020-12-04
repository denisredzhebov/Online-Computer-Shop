package onlineShop.core;

import onlineShop.core.interfaces.Controller;
import onlineShop.models.products.BaseProduct;
import onlineShop.models.products.Product;
import onlineShop.models.products.components.*;
import onlineShop.models.products.computers.Computer;
import onlineShop.models.products.computers.DesktopComputer;
import onlineShop.models.products.computers.Laptop;
import onlineShop.models.products.peripherals.*;

import java.util.ArrayList;
import java.util.List;

import static onlineShop.common.constants.ExceptionMessages.*;
import static onlineShop.common.constants.OutputMessages.*;

public class ControllerImpl implements Controller {
    private List<Computer> computers;
    private List<Component> components;
    private List<Peripheral> peripherals;

    public ControllerImpl() {
        this.computers = new ArrayList<>();
        this.components = new ArrayList<>();
        this.peripherals = new ArrayList<>();
    }

    private boolean checkIfComputerExists (int id) {
        for (Computer computer : computers) {
            if (computer.getId() == id) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String addComputer(String computerType, int id, String manufacturer, String model, double price) {
        Computer currentComputer;
        if (computerType.equals("Laptop")) {
            currentComputer = new Laptop(id, manufacturer, model, price);
        } else if (computerType.equals("DesktopComputer")) {
            currentComputer = new DesktopComputer(id, manufacturer, model, price);
        } else {
            throw new IllegalArgumentException(INVALID_COMPUTER_TYPE);
        }

        for (Computer computer : computers) {
            if (computer.getId() == id) {
                throw new IllegalArgumentException(EXISTING_COMPUTER_ID);
            }
        }
        this.computers.add(currentComputer);
        return String.format(ADDED_COMPUTER, id);
    }

    @Override
    public String addPeripheral(int computerId, int id, String peripheralType, String manufacturer, String model, double price, double overallPerformance, String connectionType) {
        if (!checkIfComputerExists(computerId)) {
            throw new IllegalArgumentException(NOT_EXISTING_COMPUTER_ID);
        }
        Peripheral currentPeriphal;
        switch (peripheralType) {
            case "Headset":
                currentPeriphal = new Headset(id, manufacturer, model, price, overallPerformance, connectionType);
                break;
            case "Keyboard":
                currentPeriphal = new Keyboard(id, manufacturer, model, price, overallPerformance, connectionType);
                break;
            case "Monitor":
                currentPeriphal = new Monitor(id, manufacturer, model, price, overallPerformance, connectionType);
                break;
            case "Mouse":
                currentPeriphal = new Mouse(id, manufacturer, model, price, overallPerformance, connectionType);
                break;
            default:
                throw new IllegalArgumentException(INVALID_PERIPHERAL_TYPE);
        }
        for (Peripheral peripheral : peripherals) {
            if (peripheral.getId() == id) {
                throw new IllegalArgumentException(EXISTING_PERIPHERAL_ID);
            }
        }
        this.peripherals.add(currentPeriphal);
        for (Computer computer : computers) {
            if (computer.getId() == computerId) {
                computer.addPeripheral(currentPeriphal);
                break;
            }
        }
        return String.format(ADDED_PERIPHERAL, peripheralType, id, computerId);
    }

    @Override
    public String removePeripheral(String peripheralType, int computerId) {
        if (!checkIfComputerExists(computerId)) {
            throw new IllegalArgumentException(NOT_EXISTING_COMPUTER_ID);
        }
        Peripheral currentPeripheral = this.peripherals.stream().filter(p -> p.getClass().getSimpleName().equals(peripheralType)).findFirst().orElse(null);
        for (Computer computer : computers) {
            if (computer.getId() == computerId) {
                computer.removePeripheral(peripheralType);
                return String.format(REMOVED_PERIPHERAL, peripheralType, currentPeripheral.getId());
            }
        }
        return null;
    }

    @Override
    public String addComponent(int computerId, int id, String componentType, String manufacturer, String model, double price, double overallPerformance, int generation) {
        if (!checkIfComputerExists(computerId)) {
            throw new IllegalArgumentException(NOT_EXISTING_COMPUTER_ID);
        }
        Component currentComponent;
        switch (componentType) {
            case "CentralProcessingUnit":
                currentComponent = new CentralProcessingUnit(id, manufacturer, model, price, overallPerformance, generation);
                break;
            case "Motherboard":
                currentComponent = new Motherboard(id, manufacturer, model, price, overallPerformance, generation);
                break;
            case "PowerSupply":
                currentComponent = new PowerSupply(id, manufacturer, model, price, overallPerformance, generation);
                break;
            case "RandomAccessMemory":
                currentComponent = new RandomAccessMemory(id, manufacturer, model, price, overallPerformance, generation);
                break;
            case "SolidStateDrive":
                currentComponent = new SolidStateDrive(id, manufacturer, model, price, overallPerformance, generation);
                break;
            case "VideoCard":
                currentComponent = new VideoCard(id, manufacturer, model, price, overallPerformance, generation);
                break;
            default:
                throw new IllegalArgumentException(INVALID_COMPONENT_TYPE);
        }
        for (Component component : components) {
            if (component.getId() == id) {
                throw new IllegalArgumentException(EXISTING_COMPONENT_ID);
            }
        }
        this.components.add(currentComponent);
        for (Computer computer : computers) {
            if (computer.getId() == computerId) {
                computer.addComponent(currentComponent);
                break;
            }
        }
        return String.format(ADDED_COMPONENT, componentType, id, computerId);
    }

    @Override
    public String removeComponent(String componentType, int computerId) {
        if (!checkIfComputerExists(computerId)) {
            throw new IllegalArgumentException(NOT_EXISTING_COMPUTER_ID);
        }
        Component currentComponent = this.components.stream().filter(p -> p.getClass().getSimpleName().equals(componentType)).findFirst().orElse(null);
        for (Computer computer : computers) {
            if (computer.getId() == computerId) {
                computer.removeComponent(componentType);
                return String.format(REMOVED_COMPONENT, componentType, currentComponent.getId());
            }
        }
        return null;
    }

    @Override
    public String buyComputer(int id) {
        if (!checkIfComputerExists(id)) {
            throw new IllegalArgumentException(NOT_EXISTING_COMPUTER_ID);
        }
        Computer currentComputer = this.computers.stream().filter(c -> c.getId() == id).findFirst().orElse(null);
        this.computers.remove(currentComputer);
        return currentComputer.toString().trim();
    }

    @Override
    public String BuyBestComputer(double budget) {
        List<Computer> computersCanBuy = new ArrayList<>();
        for (Computer computer : computers) {
            if (computer.getPrice() <= budget) {
                computersCanBuy.add(computer);
            }
        }
        if (computersCanBuy.isEmpty()) {
            throw new IllegalArgumentException(String.format(CAN_NOT_BUY_COMPUTER, budget));
        }
        double maxOverallPerformance = 0;
        for (Computer computer : computersCanBuy) {
            if (computer.getOverallPerformance() > maxOverallPerformance) {
                maxOverallPerformance = computer.getOverallPerformance();
            }
        }
        for (Computer computer : computersCanBuy) {
            if (computer.getOverallPerformance() == maxOverallPerformance) {
                this.computers.remove(computer);
                return computer.toString().trim();
            }
        }
        return null;
    }

    @Override
    public String getComputerData(int id) {
        if (!checkIfComputerExists(id)) {
            throw new IllegalArgumentException(NOT_EXISTING_COMPUTER_ID);
        }
        Computer computer = this.computers.stream().filter(c -> c.getId() == id).findFirst().orElse(null);
        return computer.toString().trim();
    }
}
