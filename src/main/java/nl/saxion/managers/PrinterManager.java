package nl.saxion.managers;

import nl.saxion.Models.Printer;
import nl.saxion.factory.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class PrinterManager {

    private static PrinterManager instance;
    private PrinterFactoryRegistry factoryRegistry;

    private PrinterManager() {
        factoryRegistry = new PrinterFactoryRegistry();
    }

    public static synchronized PrinterManager getInstance() {
        if (instance == null) {
            instance = new PrinterManager();
        }
        return instance;
    }
    private List<Printer> printers = new ArrayList<>();

    public void addPrinter(String id, int printerType, String printerName, String manufacturer, int maxX, int maxY, int maxZ, int maxColors) {
        PrinterFactory factory = factoryRegistry.getFactory(printerType);
        if (factory != null) {
            Printer printer = factory.createPrinter(id, printerName, manufacturer, maxX, maxY, maxZ, maxColors);
            printers.add(printer);
        } else {
            System.out.println("Invalid printer type specified. Printer type " + printerType + " not found.");
        }
    }

    public void registerFactory(int printerType, PrinterFactory factory) {
        if (factory != null) {
           factoryRegistry.registerFactory(printerType, factory);
        } else {
            System.out.println("Invalid factory specified. Must implement PrinterFactory interface.");
        }
    }

    public Printer getPrinterById(String id) {
        for (Printer printer : printers) {
            if (printer.getId() == id) {
                return printer;
            }
        }
        return null;
    }

    public void assignTaskToPrinter(String printerId) {
        boolean suitablePrinterFound = false;
        for (Printer printer : printers) {
            if (Objects.equals(printer.getId(), printerId)) {
                printer.setCurrentTaskId(printerId);
            }
        }
        if (!suitablePrinterFound) {
            System.out.println("No free printer found with ID " + printerId);
        }
    }

    public String getPrinterCurrentTaskId(String printerId) {
        for (Printer printer : printers) {
            if (Objects.equals(printer.getId(), printerId)) {
                return printer.getCurrentTaskId();
            }
        }
        return null;
    }

    public void reduceResourcesForPrinter(String printerId, Map<Integer, Double> reductionMap) {
        Printer printer = getPrinterById(printerId);
        if (printer != null) {
           printer.reduceSpoolLength(reductionMap);
        } else {
            System.out.println("No printer found with ID " + printerId);
        }
    }

    // get printers with tasks
    public List<String> getPrintersWithTasks() {
        List<String> printersWithTasks = new ArrayList<>();
        for (Printer printer : printers) {
            if (printer.getCurrentTaskId() != null) {
                printersWithTasks.add(printer.getId());
            }
        }
        return printersWithTasks;
    }

    public List<String> getPrinterInfo() {
        List<String> printerInfo = new ArrayList<>();
        for (Printer printer : printers) {
            printerInfo.add(printer.toString());
        }
        return printerInfo;
    }
}
