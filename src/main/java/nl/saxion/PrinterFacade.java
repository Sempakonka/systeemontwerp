package nl.saxion;

import nl.saxion.Models.FilamentType;
import nl.saxion.factory.FactoryTypes.HousedMultiColorPrinterFactory;
import nl.saxion.factory.FactoryTypes.HousedPrinterFactory;
import nl.saxion.factory.FactoryTypes.MultiColorPrinterFactory;
import nl.saxion.factory.FactoryTypes.StandardFDMPrinterFactory;
import nl.saxion.factory.PrinterFactory;
import nl.saxion.factory.PrinterType;
import nl.saxion.managers.PrintManager;
import nl.saxion.managers.PrintTaskManager;
import nl.saxion.managers.PrinterManager;
import nl.saxion.managers.SpoolManager;
import nl.saxion.observer.Observer;
import nl.saxion.strategy.EfficientSpoolUsageStrategy;
import nl.saxion.strategy.LessSpoolChangesStrategy;
import nl.saxion.strategy.PrintStrategy;

import java.util.ArrayList;
import java.util.List;

public class PrinterFacade {
    private PrinterManager printerManager;
    private PrintManager printManager;
    private SpoolManager spoolManager;
    private PrintTaskManager printTaskManager;

    private PrintStrategy printStrategy;

    private List<Observer> observers = new ArrayList<>();

    public PrinterFacade() {
        this(PrinterManager.getInstance(),
                PrintManager.getInstance(),
                SpoolManager.getInstance(),
                PrintTaskManager.getInstance());
    }


    // Overloaded constructor for testing
    public PrinterFacade(PrinterManager printerManager,
                         PrintManager printManager,
                         SpoolManager spoolManager,
                         PrintTaskManager printTaskManager) {
        this.printerManager = printerManager;
        this.printManager = printManager;
        this.spoolManager = spoolManager;
        this.printTaskManager = printTaskManager;
    }


    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    private void notifyObservers(String message) {
        for (Observer observer : observers) {
            observer.update(message);
        }
    }

    public void changePrintStrategy(int strategyChoice) {
        switch (strategyChoice) {
            case 1:
                printStrategy = new EfficientSpoolUsageStrategy();
                break;
            case 2:
                printStrategy = new LessSpoolChangesStrategy();
                break;
            default:
                System.out.println("Invalid strategy choice");
        }
    }

    public void addPrinter(String id, int printerType, String printerName,
                           String manufacturer, int maxX, int maxY, int maxZ, int maxColors) {
        printerManager.addPrinter(id, printerType, printerName, manufacturer, maxX, maxY, maxZ, maxColors);
    }

    public void registerFactory(int printerType, PrinterFactory factory) {
        printerManager.registerFactory(printerType, factory);
    }

    public boolean selectPrintTask(String printerId) {
        String chosenTaskId = printTaskManager.findTaskForPrinter(printerManager.getPrinterById(printerId)::canAcceptTask);

        List<String> taskColors = printTaskManager.getTaskColors(chosenTaskId);

        if (chosenTaskId != null) {
            // Check and manage spools if necessary
            if (spoolManager.areSpoolsAvailableForTask(taskColors, printTaskManager.getTaskFilamentType(chosenTaskId)) != null) {
                List<Integer> spoolIds = spoolManager.areSpoolsAvailableForTask(taskColors, printTaskManager.getTaskFilamentType(chosenTaskId));

                spoolManager.assignSpoolsToTask(spoolIds, chosenTaskId, printTaskManager.getPrintIdFromPrintTask(chosenTaskId));
                printerManager.assignTaskToPrinter(printerId, chosenTaskId);
                printTaskManager.assignTaskToPrinter(printerId, chosenTaskId);
                printerManager.assignSpoolsToPrinter(printerId, spoolIds);

                System.out.println("Task " + chosenTaskId + " assigned to printer " + printerId);

                return true;
            } else {
                System.out.println("No suitable spools found for task " + chosenTaskId + " while searching for work for printer: " + printerId);
            }
        } else {
            System.out.println("No suitable task found for printer " + printerId);
        }

        return false;
    }

    public void startInitialQueue() {
        // Get all printer IDs
        List<String> printerIds = printerManager.getAllPrinterIds();

        // For each printer, select a print task and start it
        for (String printerId : printerIds) {
            selectPrintTask(printerId);
        }
    }

    public void addPrint(String name, int height, int width, int length,
                         ArrayList<Double> filamentLength, int printTime) {
        printManager.addPrint(name, height, width, length, filamentLength, printTime);
    }

    public String addPrintTask(String printId, List<String> colors, int filamentType, String taskId) {
        return printTaskManager.addPrintTask(printId, colors, filamentType, taskId);
    }

    public void registerPrinterFailure(String printerId) {
        String taskId = printerManager.getPrinterCurrentTaskId(printerId);

        if (taskId != null) {
            printTaskManager.removeRunningTask(taskId);
            printerManager.decoupleSpoolsFromPrinter(printerId);
            spoolManager.reduceSpoolResources(printManager.getFilamentLengths(printerId), printerId);
            spoolManager.freeSpoolsFromTask(taskId);
        } else {
            System.out.println("No task running on printer " + printerId);
        }
    }

    /**
     * Registers the completion of a print task for a specific printer.
     * Also starts a new print task for the printer if one is available.
     *
     * @param printerId The unique identifier of the printer.
     */
    public void registerCompletion(String printerId) {
        System.out.println("Registering completion for printer " + printerId);
        String taskId = printerManager.getPrinterCurrentTaskId(printerId);

        if (taskId != null) {
            printTaskManager.removeRunningTask(taskId);
            printerManager.decoupleSpoolsFromPrinter(printerId);
            spoolManager.reduceSpoolResources(printManager.getFilamentLengths(printerId), printerId);
            spoolManager.freeSpoolsFromTask(taskId);
            printTaskManager.registerTaskDone(taskId);
        } else {
            System.out.println("No task running on printer " + printerId);
        }
    }

    public Integer addSpool(int id, String color, FilamentType filamentType, double length) {
        return spoolManager.addSpool(id, color, filamentType, length);
    }

    public List<String> getAvailablePrints() {
        return printManager.getPrints();
    }

    public List<String> getAvailableFilamentTypes() {
        return spoolManager.getFilamentTypes();
    }

    public List<String> getAvailableColorsForFilamentType(String fillamentTypeAsString) {
        return spoolManager.getAvailableColorsForFilamentType(fillamentTypeAsString);
    }


    public List<String> getRunningPrintersWithTasks() {
        return printerManager.getPrintersWithTasks();
    }

    public List<String> getFormattedSpools() {
        return spoolManager.getSpools();
    }


    public List<String> getFormattedPrints() {
        return printManager.getPrints();
    }

    public List<String> getFormattedPrinterInfo() {
        return printerManager.getPrinterInfo();
    }

    public List<String> getPendingPrintTasks() {
        return printTaskManager.getPendingPrintTasks();
    }

    public int getColorAmountfromPrint(String printId) {
        return printManager.getAmountOfColors(printId);
    }

    /**
     * Retrieves the current print task for the specified printer.
     *
     * @param printerId The unique identifier [id] of the printer.
     * @return The ID of the current print task for the printer, or null if no task is found.
     */
    public String getCurrentPrintTask(String printerId) {
        return printTaskManager.getCurrentPrintTask(printerId);
    }

    /**
     * Retrieves the spools from a specified printer.
     *
     * @param printerId The unique identifier of the printer.
     * @return An array of Integers representing the spools of the printer. Returns null if no printer is found with the specified ID.
     */
    public Integer[] getSpoolsFromPrinter(String printerId) {
        return printerManager.getSpoolsFromPrinter(printerId);
    }
}
