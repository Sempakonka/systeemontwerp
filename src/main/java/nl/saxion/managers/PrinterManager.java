package nl.saxion.managers;

import nl.saxion.Models.*;

import java.util.ArrayList;
import java.util.List;

public class PrinterManager {
    private List<Printer> printers = new ArrayList<>();
    private List<Printer> freePrinters = new ArrayList<>();

    public void addPrinter(int id, int printerType, String printerName, String manufacturer, int maxX, int maxY, int maxZ, int maxColors) {
        if (printerType == 1) {
            StandardFDM printer = new StandardFDM(id, printerName, manufacturer, maxX, maxY, maxZ);
            printers.add(printer);
            freePrinters.add(printer);
        } else if (printerType == 2) {
            HousedPrinter printer = new HousedPrinter(id, printerName, manufacturer, maxX, maxY, maxZ);
            printers.add(printer);
            freePrinters.add(printer);
        } else if (printerType == 3) {
            MultiColor printer = new MultiColor(id, printerName, manufacturer, maxX, maxY, maxZ, maxColors);
            printers.add(printer);
            freePrinters.add(printer);
        }
    }

    public List<Printer> getPrinters() {
        return printers;
    }

    public List<Printer> getFreePrinters() {
        return freePrinters;
    }

    public Printer getPrinterById(int id) {
        for (Printer printer : printers) {
            if (printer.getId() == id) {
                return printer;
            }
        }
        return null;
    }

    // in PrinterManager
    public void freePrinterResources(int printerId, PrintTaskManager printTaskManager, PrinterManager printerManager, SpoolManager spoolManager) {
        // Assuming you have a way to retrieve the printer based on its ID
        Printer printer = getPrinterById(printerId);
        if (printer == null) {
            System.out.println("Cannot find a printer with ID " + printerId);
            return;
        }

        // Here, we need the task from the PrintTaskManager
        PrintTask task = printTaskManager.getPrinterCurrentTask(printer);

        if (task != null) {

            Spool[] spools = printer.getCurrentSpools();
            for (int i = 0; i < spools.length && i < task.getColors().size(); i++) {
                spools[i].reduceLength(task.getPrint().getFilamentLength().get(i));
            }

            // getPrinterCurrentTask method needs to be implemented in PrintTaskManager
            System.out.println("Task " + task.getPrint().getName() + " was completed by printer " + printer.getName());

            // Assign a new task to the printer
            printTaskManager.selectPrintTask(printer, printerManager, spoolManager);
        }
    }

    public void handlePrinterFailure(int printerId, PrintTaskManager printTaskManager, PrinterManager printerManager, SpoolManager spoolManager) {
        Printer printer =  getPrinterById(printerId);
        if (printer == null) {
            System.out.println("Cannot find a printer with ID " + printerId);
            return;
        }

        // Get the failed task
        PrintTask task = printTaskManager.getPrinterCurrentTask(printer);

        // Reduce the spool length
        Spool[] spools = printer.getCurrentSpools();
        for(int i=0; i<spools.length && i < task.getColors().size(); i++) {
            spools[i].reduceLength(task.getPrint().getFilamentLength().get(i));
        }

        // Attempt to execute a new task on the printer
        printTaskManager.selectPrintTask(printer, printerManager, spoolManager);
    }
    // Remove spools and their related methods from this class
}
