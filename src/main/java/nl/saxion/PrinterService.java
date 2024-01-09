package nl.saxion;

import nl.saxion.Models.*;
import nl.saxion.managers.PrintManager;
import nl.saxion.managers.PrintTaskManager;
import nl.saxion.managers.PrinterManager;
import nl.saxion.managers.SpoolManager;

import java.util.ArrayList;
import java.util.List;

public class PrinterService {
    private PrinterManager printerManager;
    private PrintManager printManager;
    private SpoolManager spoolManager;
    private PrintTaskManager printTaskManager;

    public PrinterService(PrinterManager printerManager, PrintManager printManager,
                          SpoolManager spoolManager, PrintTaskManager printTaskManager) {
        this.printerManager = printerManager;
        this.printManager = printManager;
        this.spoolManager = spoolManager;
        this.printTaskManager = printTaskManager;
    }

    public void addPrinter(int id, int printerType, String printerName,
                           String manufacturer, int maxX, int maxY, int maxZ, int maxColors) {
        printerManager.addPrinter(id, printerType, printerName, manufacturer, maxX, maxY, maxZ, maxColors);
    }

    public void selectPrintTask(Printer printer) {
        // Delegate to PrintTaskManager to select an appropriate task for the printer
        PrintTask chosenTask = printTaskManager.findTaskForPrinter(printer);

        if (chosenTask != null) {
            // Check and manage spools if necessary
            if (spoolManager.areSpoolsAvailableForTask(chosenTask)) {
                // Assign the task to the printer

                // Update  states
                spoolManager.assignSpoolsToTask(chosenTask);
                printerManager.assignTaskToPrinter(printer, chosenTask);
                printTaskManager.assignTaskToPrinter(printer, chosenTask);
                printTaskManager.removePendingTask(chosenTask);

                System.out.println("- Started task: " + chosenTask + " on printer " + printer.getName());
            } else {
                System.out.println("No suitable spools available for task on printer " + printer.getName());
            }
        } else {
            System.out.println("No suitable task found for printer " + printer.getName());
        }
    }

    public void startInitialQueue() {
        for (Printer printer : printerManager.getPrinters()) {
            selectPrintTask(printer);
        }
    }

    public void addPrint(String name, int height, int width, int length,
                         ArrayList<Double> filamentLength, int printTime) {
        printManager.addPrint(name, height, width, length, filamentLength, printTime);
    }

    public List<Print> getPrints() {
        return printManager.getPrints();
    }

    public List<Printer> getPrinters() {
        return printerManager.getPrinters();
    }

    public PrintTask getPrinterCurrentTask(Printer printer) {
        return printTaskManager.getPrinterCurrentTask(printer);
    }

    public void addPrintTask(String printName, List<String> colors, FilamentType type) {
        Print print = printManager.findPrint(printName);
        printTaskManager.addPrintTask(print, colors, type);
    }

    public void registerPrinterFailure(int printerId) {
        Printer printer = printerManager.getPrinterById(printerId);

        PrintTask task = printTaskManager.getPrinterCurrentTask(printer);

        if (task != null) {
            // Update states
            printTaskManager.removeRunningTask(printer);
            printTaskManager.addPendingTask(task);
            spoolManager.reduceResourcesForPrinter(printer, task);
        } else {
            System.out.println("No task running on printer " + printer.getName());
        }
        selectPrintTask(printer);
    }

    public void registerCompletion(int printerId) {
        Printer printer = printerManager.getPrinterById(printerId);

        PrintTask task = printTaskManager.getPrinterCurrentTask(printer);

        if (task != null) {
            // Update states
            printTaskManager.removeRunningTask(printer);
            spoolManager.reduceResourcesForPrinter(printer, task);
        } else {
            System.out.println("No task running on printer " + printer.getName());
        }
        selectPrintTask(printer);
    }

    public Print findPrint(int i) {
        return printManager.findPrint(i);
    }

    public List<Spool> getSpools() {
        return spoolManager.getSpools();
    }

    public List<PrintTask> getPendingPrintTasks() {
        return printTaskManager.getPendingPrintTasks();
    }

    public void addSpool(Spool spool) {
        spoolManager.addSpool(spool);
    }
}
