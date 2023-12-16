package nl.saxion.managers;

import nl.saxion.Models.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import nl.saxion.managers.PrinterManager;

public class PrintTaskManager{
    private List<PrintTask> pendingPrintTasks = new ArrayList<>();
    private Map<Printer, PrintTask> runningPrintTasks = new HashMap();

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
        Spool[] spools = printer.getCurrentSpools();
        PrintTask chosenTask = null;
        // First we look if there's a task that matches the current spool on the printer.
        if(spools[0] != null) {
            for (PrintTask printTask : pendingPrintTasks) {
                if (printer.printFits(printTask.getPrint())) {
                    if (printer instanceof StandardFDM && printTask.getFilamentType() != FilamentType.ABS && printTask.getColors().size() == 1) {
                        if (spools[0].spoolMatch(printTask.getColors().get(0), printTask.getFilamentType())) {
                            runningPrintTasks.put(printer, printTask);
                          printerManager.  getFreePrinters().remove(printer);
                            chosenTask = printTask;
                            break;
                        }
                        // The housed printer is the only one that can print ABS, but it can also print the others.
                    } else if (printer instanceof HousedPrinter && printTask.getColors().size() == 1) {
                        if (spools[0].spoolMatch(printTask.getColors().get(0), printTask.getFilamentType())) {
                            runningPrintTasks.put(printer, printTask);
                            printerManager.  getFreePrinters().remove(printer);
                            chosenTask = printTask;
                            break;
                        }
                        // For multicolor the order of spools does matter, so they have to match.
                    } else if (printer instanceof MultiColor && printTask.getFilamentType() != FilamentType.ABS && printTask.getColors().size() <= ((MultiColor) printer).getMaxColors()) {
                        boolean printWorks = true;
                        for (int i = 0; i < spools.length && i < printTask.getColors().size(); i++) {
                            if (!spools[i].spoolMatch(printTask.getColors().get(i), printTask.getFilamentType())) {
                                printWorks = false;
                            }
                        }
                        if (printWorks) {
                            runningPrintTasks.put(printer, printTask);
                            printerManager.  getFreePrinters().remove(printer);
                            chosenTask = printTask;
                            break;
                        }
                    }
                }
            }
        }
        if(chosenTask != null) {
            pendingPrintTasks.remove(chosenTask);
            System.out.println("- Started task: " + chosenTask + " on printer " + printer.getName());
        } else {
            // If we didn't find a print for the current spool we search for a print with the free spools.
            for(PrintTask printTask: pendingPrintTasks) {
                if(printer.printFits(printTask.getPrint()) && getPrinterCurrentTask(printer) == null) {
                    if (printer instanceof StandardFDM && printTask.getFilamentType() != FilamentType.ABS && printTask.getColors().size() == 1) {
                        Spool chosenSpool = null;
                        for (Spool spool :spoolManager. getFreeSpools()) {
                            if (spool.spoolMatch(printTask.getColors().get(0), printTask.getFilamentType())) {
                                chosenSpool = spool;
                            }
                        }
                        if (chosenSpool != null) {
                            runningPrintTasks.put(printer, printTask);
                            spoolManager. getFreeSpools().add(printer.getCurrentSpools()[0]);
                            System.out.println("- Spool change: Please place spool " + chosenSpool.getId() + " in printer " + printer.getName());
                            spoolManager. getFreeSpools().remove(chosenSpool);
                            ((StandardFDM) printer).setCurrentSpool(chosenSpool);
                            printerManager.  getFreePrinters().remove(printer);
                            chosenTask = printTask;
                        }
                    } else if (printer instanceof HousedPrinter && printTask.getColors().size() == 1) {
                        Spool chosenSpool = null;
                        for (Spool spool : spoolManager. getFreeSpools()) {
                            if (spool.spoolMatch(printTask.getColors().get(0), printTask.getFilamentType())) {
                                chosenSpool = spool;
                            }
                        }
                        if (chosenSpool != null) {
                            runningPrintTasks.put(printer, printTask);
                            spoolManager. getFreeSpools().add(printer.getCurrentSpools()[0]);
                            System.out.println("- Spool change: Please place spool " + chosenSpool.getId() + " in printer " + printer.getName());
                            spoolManager. getFreeSpools().remove(chosenSpool);
                            ((StandardFDM) printer).setCurrentSpool(chosenSpool);
                            printerManager.  getFreePrinters().remove(printer);
                            chosenTask = printTask;
                        }
                    } else if (printer instanceof MultiColor && printTask.getFilamentType() != FilamentType.ABS && printTask.getColors().size() <= ((MultiColor) printer).getMaxColors()) {
                        ArrayList<Spool> chosenSpools = new ArrayList<>();
                        for (int i = 0; i < printTask.getColors().size(); i++) {
                            for (Spool spool :            spoolManager. getFreeSpools()) {
                                if (spool.spoolMatch(printTask.getColors().get(i), printTask.getFilamentType()) && !spoolManager. containsSpool(chosenSpools, printTask.getColors().get(i))) {
                                    chosenSpools.add(spool);
                                }
                            }
                        }
                        // We assume that if they are the same length that there is a match.
                        if (chosenSpools.size() == printTask.getColors().size()) {
                            runningPrintTasks.put(printer, printTask);
                            for (Spool spool : printer.getCurrentSpools()) {
                                spoolManager. getFreeSpools().add(spool);
                            }
                            printer.setCurrentSpools(chosenSpools);
                            int position = 1;
                            for (Spool spool : chosenSpools) {
                                System.out.println("- Spool change: Please place spool " + spool.getId() + " in printer " + printer.getName() + " position " + position);
                                spoolManager. getFreeSpools().remove(spool);
                                position++;
                            }
                            printerManager.  getFreePrinters().remove(printer);
                            chosenTask = printTask;
                        }
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

        // Removing and readding the task from running tasks to pending tasks
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
