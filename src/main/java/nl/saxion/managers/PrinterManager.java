package nl.saxion.managers;

import nl.saxion.Models.Printer;
import nl.saxion.Models.Spool;
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
            if (Objects.equals(printer.getId(), id)) {
                return printer;
            }
        }
        return null;
    }

    public void assignTaskToPrinter(String printerId, String taskId) {
        boolean suitablePrinterFound = false;
        for (Printer printer : printers) {
            if (Objects.equals(printer.getId(), printerId)) {
                if (printer.getCurrentTaskId() != null) {
                    System.out.println("Printer " + printerId + " is already assigned to a task");
                } else {
                    printer.setCurrentTaskId(taskId);
                    suitablePrinterFound = true;
                }
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

    public List<String> getAllPrinterIds() {
        List<String> printerIds = new ArrayList<>();
        for (Printer printer : printers) {
            printerIds.add(printer.getId());
        }
        return printerIds;
    }

    public void assignSpoolsToPrinter(String printerId, List<Integer> spoolIds) {
        Printer printer = getPrinterById(printerId);
        Integer[] spoolIdsArray = spoolIds.toArray(new Integer[0]);

        if (printer != null) {
            printer.setCurrentSpools(spoolIdsArray);
        }
    }

    public Integer[] getSpoolsFromPrinter(String printerId) {
        Printer printer = getPrinterById(printerId);
        if (printer != null) {
            return printer.getCurrentSpools();
        } else {
            System.out.println("No printer found with ID " + printerId);
            return null;
        }
    }

    /**
     * Decouples the spools from the printer with the specified ID.
     * Used when a printer fails or finished.
     * Is needed because spools are now free to go to another task.
     *
     * @param printerId The unique identifier of the printer.
     */
    public void decoupleSpoolsFromPrinter(String printerId) {
        Printer printer = getPrinterById(printerId);
        if (printer != null) {
            printer.setCurrentSpools(null);
        } else {
            System.out.println("No printer found with ID " + printerId);
        }
    }
}
