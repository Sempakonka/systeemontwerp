package nl.saxion.managers;

import nl.saxion.Models.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nl.saxion.Models.interfaces.MultiSpoolPrinter;
import nl.saxion.Models.interfaces.SingleSpoolPrinter;
import nl.saxion.managers.PrinterManager;

public class PrintTaskManager{
    private List<PrintTask> pendingPrintTasks = new ArrayList<>();
    private Map<Printer, PrintTask> runningPrintTasks = new HashMap();



    // get running tasks
    public Map<Printer, PrintTask> getRunningPrintTasks() {
        return runningPrintTasks;
    }

    public void addPrintTask(String printName, List<String> colors, FilamentType type, PrintManager printManager){
        Print print = printManager.findPrint(printName);

        if (print == null) {
            printError("Could not find print with name " + printName);
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

    public void selectPrintTask(Printer printer, PrinterManager printerManager, SpoolManager spoolManager) {
        // Getting spools based on printer type
        Spool[] spools;
        if (printer instanceof MultiSpoolPrinter multiSpoolPrinter) {
            spools = multiSpoolPrinter.getCurrentSpools();
        } else if (printer instanceof SingleSpoolPrinter singleSpoolPrinter) {
            Spool currentSpool = singleSpoolPrinter.getCurrentSpool();
            spools = new Spool[]{currentSpool};
        } else {
            throw new UnsupportedOperationException("Unsupported printer type: " + printer.getClass());
        }

        PrintTask chosenTask = null;

        // First we look for a task that matches the current spool on the printer.
        if (spools[0] != null) {
            for (PrintTask printTask : pendingPrintTasks) {
                if (printer.printFits(printTask.getPrint())) {
              /*
               The printer handles the task based on its instance type, through polymorphism.
               i.e., It'll call the method of its own class (StandardFDM, HousedPrinter, etc.)
              */
                    printer.handlePrintTask(printTask, this, spoolManager);

                    // Update chosenTask only if the handling was successful.
                    if (runningPrintTasks.get(printer) != null) {
                        chosenTask = printTask;
                        break;
                    }
                }
            }
        }

        // The already implemented part of your method.
        if (chosenTask != null) {
            pendingPrintTasks.remove(chosenTask);
            System.out.println("- Started task: " + chosenTask + " on printer " + printer.getName());
        } else {
            // Similar logic if you cannot find print task for current spools.
            // If we didn't find a print for the current spool we search for a print with the free spools.
            for (PrintTask printTask : pendingPrintTasks) {
                if (printer.printFits(printTask.getPrint()) && getPrinterCurrentTask(printer) == null) {
                    printer.handlePrintTask(printTask, this, spoolManager);
                    if (runningPrintTasks.get(printer) != null) {
                        chosenTask = printTask;
                        break;
                    }
                }
            }

            if(chosenTask != null) {
                pendingPrintTasks.remove(chosenTask);
                System.out.println("- Started task: " + chosenTask + " on printer " + printer.getName());
            }
        }
    }


    public List<PrintTask> getPendingPrintTasks() {
        return pendingPrintTasks;
    }

    public PrintTask getPrinterCurrentTask(Printer printer) {
        if(!runningPrintTasks.containsKey(printer)) {
            return null;
        }
        return runningPrintTasks.get(printer);
    }

    private void printError(String s) {
        System.out.println("---------- Error Message ----------");
        System.out.println("Error: "+s);
        System.out.println("--------------------------------------");
    }

    public void startInitialQueue(PrinterManager printerManager, SpoolManager spoolManager, PrintManager printManager) {
        for(Printer printer: printerManager.getPrinters()) {
            selectPrintTask(printer, printerManager, spoolManager);
        }
    }

    // in PrintTaskManager
    public void markTaskAsComplete(int printerId) {
        // Code to find and remove the print task.
        PrintTask task = runningPrintTasks.get(printerId);
        if (task == null) {
            printError("cannot find a running task on printer with ID " + printerId);
            return;
        }

        runningPrintTasks.remove(printerId);
        System.out.println("Task " + task + " removed from printer with ID " + printerId);
    }


    public void handlePrinterFailure(int printerId, PrinterManager printerManager, SpoolManager spoolManager) {
        Printer printer = printerManager. getPrinterById(printerId); // implemented in PrinterManager
        if (printer == null) {
            printError("Cannot find a printer with ID " + printerId); // this method prints the error message
            return;
        }

        // Removing and reading the task from running tasks to pending tasks
        PrintTask task = runningPrintTasks.get(printer);
        if (task == null) {
            printError("Cannot find a running task on printer with ID " + printerId);
            return;
        }

        runningPrintTasks.remove(printer);
        pendingPrintTasks.add(task);

        System.out.println("Task " + task + " removed from printer " + printer.getName() + ". Added back to pending tasks");
    }

}
