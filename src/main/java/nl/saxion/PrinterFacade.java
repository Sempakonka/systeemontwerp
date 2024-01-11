package nl.saxion;

import nl.saxion.factory.PrinterFactory;
import nl.saxion.managers.PrintManager;
import nl.saxion.managers.PrintTaskManager;
import nl.saxion.managers.PrinterManager;
import nl.saxion.managers.SpoolManager;
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


    public PrinterFacade() {
        printerManager = PrinterManager.getInstance();
        printManager = PrintManager.getInstance();
        spoolManager = SpoolManager.getInstance();
        printTaskManager = PrintTaskManager.getInstance();
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

    public void selectPrintTask(String printerId) {
        // Delegate to PrintTaskManager to select an appropriate task for the printer
        String chosenTaskId = printTaskManager.findTaskForPrinter(printerManager.getPrinterById(printerId)::canAcceptTask);

        String taskColors = printTaskManager.getTaskColors(chosenTaskId);

        if (chosenTaskId != null) {
            // Check and manage spools if necessary
            if (spoolManager.areSpoolsAvailableForTask(taskColors, printTaskManager.getTaskFilamentType(chosenTaskId))) {

                // Update  states
                spoolManager.assignSpoolsToTask(taskColors, printTaskManager.getTaskFilamentType(chosenTaskId), chosenTaskId);
                printerManager.assignTaskToPrinter(printerId);
                printTaskManager.assignTaskToPrinter(printerId, chosenTaskId);
                printTaskManager.removePendingTask(chosenTaskId);

                System.out.println("Task " + chosenTaskId + " assigned to printer " + printerId);
            } else {
                System.out.println("No suitable spools found for task " + chosenTaskId);
            }
        } else {
            System.out.println("No suitable task found for printer " + printerId);
        }
    }

    public void startInitialQueue() {
        // TODO: Implement
    }

    public void addPrint(String name, int height, int width, int length,
                         ArrayList<Double> filamentLength, int printTime) {
        printManager.addPrint(name, height, width, length, filamentLength, printTime);
    }

    public void addPrintTask(String printId, List<String> colors, int filamentType) {
        printTaskManager.addPrintTask(printId, colors, filamentType);
    }

    public void registerPrinterFailure(String printerId) {
        String taskId = printerManager.getPrinterCurrentTaskId(printerId);

        if (taskId != null) {
            // Update states
            printTaskManager.removeRunningTask(taskId);
            printTaskManager.addPendingTask(taskId);
            printerManager.reduceResourcesForPrinter(printerId, printManager.getReductionMap(taskId));
        } else {
            System.out.println("No task running on printer " + printerId);
        }
        selectPrintTask(printerId);
    }

    public void registerCompletion(String printerId) {
        String taskId = printerManager.getPrinterCurrentTaskId(printerId);

        if (taskId != null) {
            // Update states
            printTaskManager.removeRunningTask(taskId);
            printerManager.reduceResourcesForPrinter(printerId, printManager.getReductionMap(taskId));
        } else {
            System.out.println("No task running on printer " + printerId);
        }
        selectPrintTask(printerId);
    }

    public void addSpool(int id, String color, String filamentType, double length) {
        // TODO: Check if fillament type exists
        spoolManager.addSpool(id, color, filamentType, length);
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

    public List<String> getFormattedPendingPrintTasks() {
        return printTaskManager.getPendingPrintTasks();
    }
}
