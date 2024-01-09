package nl.saxion;

import nl.saxion.Models.*;
import nl.saxion.managers.PrintManager;
import nl.saxion.managers.PrintTaskManager;
import nl.saxion.managers.PrinterManager;
import nl.saxion.managers.SpoolManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class PrinterFacade {
    private PrinterManager printerManager;
    private PrintManager printManager;
    private SpoolManager spoolManager;
    private PrintTaskManager printTaskManager;

    public PrinterFacade() {
        printerManager = new PrinterManager();
        printManager = new PrintManager();
        spoolManager = new SpoolManager();
        printTaskManager = new PrintTaskManager();
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

    public void addPrintTask(String printName, List<String> colors, String filamentTypeAsString) {
        FilamentType type = FilamentType.valueOf(filamentTypeAsString);
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

    public void addSpool(int id, String color, String fillamentTypeAsString, double length) {
        FilamentType type = FilamentType.valueOf(fillamentTypeAsString);
        Spool spool = new Spool(id, color, type, length);
        spoolManager.addSpool(spool);
    }

    public List<String> getAvailablePrints() {
        List<Print> prints = printManager.getPrints();
        List<String> printNames = new ArrayList<>();
        for (Print print : prints) {
            printNames.add(print.getName());
        }
        return printNames;
    }

    public List<String> getAvailableFilamentTypes() {
        return Arrays.stream(FilamentType.values())
                .map(Enum::name)
                .collect(Collectors.toList());
    }

    public List<String> getAvailableColorsForFilamentType(String fillamentTypeAsString) {
        FilamentType type = FilamentType.valueOf(fillamentTypeAsString);
        List<Spool> spools = spoolManager.getSpools();
        return spools.stream()
                .filter(spool -> spool.getFilamentType() == type)
                .map(Spool::getColor)
                .distinct()
                .collect(Collectors.toList());
    }


    public List<String> getRunningPrintersWithTasks() {
        List<String> runningPrinters = new ArrayList<>();
        for (Printer printer : printerManager.getPrinters()) {
            PrintTask currentTask = printTaskManager.getPrinterCurrentTask(printer);
            if (currentTask != null) {
                runningPrinters.add(printer.getId() + ": " + printer.getName() + " - " + currentTask);
            }
        }
        return runningPrinters;
    }

    public List<String> getFormattedSpools() {
        List<String> formattedSpools = new ArrayList<>();
        for (Spool spool : spoolManager.getSpools()) {
            formattedSpools.add(formatSpool(spool));
        }
        return formattedSpools;
    }

    private String formatSpool(Spool spool) {
        return "Color: " + spool.getColor() + ", Type: " + spool.getFilamentType() + ", Remaining: " + spool.getRemainingLength();
        // Add more spool details as needed
    }

    public List<String> getFormattedPrints() {
        List<String> formattedPrints = new ArrayList<>();
        for (Print print : printManager.getPrints()) {
            formattedPrints.add(formatPrint(print));
        }
        return formattedPrints;
    }

    private String formatPrint(Print print) {
        return "Print Name: " + print.getName() + ", Dimensions: " + print.getWidth() + "x" + print.getHeight() + "x" + print.getLength();
        // Add more print details as needed
    }

    public List<String> getFormattedPrinterInfo() {
        List<String> formattedPrinterInfo = new ArrayList<>();
        for (Printer printer : printerManager.getPrinters()) {
            String printerInfo = formatPrinterInfo(printer);
            formattedPrinterInfo.add(printerInfo);
        }
        return formattedPrinterInfo;
    }

    private String formatPrinterInfo(Printer printer) {
        StringBuilder info = new StringBuilder(printer.toString());
        PrintTask currentTask = printTaskManager.getPrinterCurrentTask(printer);
        if (currentTask != null) {
            info.append(System.lineSeparator())
                    .append("Current Print Task: ")
                    .append(currentTask)
                    .append(System.lineSeparator())
                    .append("--------");
        }
        return info.toString();
    }


    public List<String> getFormattedPendingPrintTasks() {
        List<String> formattedTasks = new ArrayList<>();
        for (PrintTask task : printTaskManager.getPendingPrintTasks()) {
            formattedTasks.add(formatPrintTask(task));
        }
        return formattedTasks;
    }

    private String formatPrintTask(PrintTask task) {
        // Format the task information as a string
        // Example: "Task ID: [ID], Print Name: [PrintName], Status: [Status]"
        return "Task ID: " + task.getId() + ", Print Name: " + task.getPrint().getName();
        // Add more details as necessary
    }
}
