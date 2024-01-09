package nl.saxion.managers;

import nl.saxion.Models.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PrintTaskManager {
    private List<PrintTask> pendingPrintTasks = new ArrayList<>();
    private Map<Printer, PrintTask> runningPrintTasks = new HashMap();

    public void addPrintTask(Print print, List<String> colors, FilamentType type) {

        if (print == null) {
            printError("Could not find print with name " + print.getName());
            return;
        }

        if (colors.size() == 0) {
            printError("Need at least one color, but none given");
            return;
        }

        // Check for color availability moved to SpoolManager class

        PrintTask task = new PrintTask(print, colors, type);
        pendingPrintTasks.add(task);
        System.out.println("Added task to queue");
    }

    public List<PrintTask> getPendingPrintTasks() {
        return pendingPrintTasks;
    }

    public PrintTask getPrinterCurrentTask(Printer printer) {
        if (!runningPrintTasks.containsKey(printer)) {
            return null;
        }
        return runningPrintTasks.get(printer);
    }

    private void printError(String s) {
        System.out.println("---------- Error Message ----------");
        System.out.println("Error: " + s);
        System.out.println("--------------------------------------");
    }

    public PrintTask findTaskForPrinter(Printer printer) {
        for (PrintTask task : pendingPrintTasks) {
            // Check if the task is compatible with the printer
            if (printer.canAcceptTask(task)) {
                // Optional: additional checks or logic before confirming the task
                return task;
            }
        }
        return null;  // No suitable task found
    }

    public void removePendingTask(PrintTask task) {
        pendingPrintTasks.remove(task);
    }

    public void assignTaskToPrinter(Printer printer, PrintTask chosenTask) {
        runningPrintTasks.put(printer, chosenTask);
    }

    public void removeRunningTask(Printer printer) {
        runningPrintTasks.remove(printer);
    }

    public void addPendingTask(PrintTask task) {
        pendingPrintTasks.add(task);
    }
}
