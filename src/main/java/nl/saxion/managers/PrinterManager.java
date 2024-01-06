package nl.saxion.managers;

import nl.saxion.Models.*;
import nl.saxion.Models.interfaces.MultiSpoolPrinter;
import nl.saxion.Models.interfaces.SingleSpoolPrinter;

import java.util.ArrayList;
import java.util.List;

public class PrinterManager {
    private List<Printer> printers = new ArrayList<>();
    private List<Printer> freePrinters = new ArrayList<>();

    public void addPrinter(int id, int printerType, String printerName, String manufacturer, int maxX, int maxY, int maxZ, int maxColors) {
        if (printerType == 1) {
            StandardFDMPrinter printer = new StandardFDMPrinter(id, printerName, manufacturer, maxX, maxY, maxZ, null);
            printers.add(printer);
            freePrinters.add(printer);
        } else if (printerType == 2) {
            HousedPrinter printer = new HousedPrinter(id, printerName, manufacturer, maxX, maxY, maxZ, null);
            printers.add(printer);
            freePrinters.add(printer);
        } else if (printerType == 3) {
            MultiColorPrinter printer = new MultiColorPrinter(id, printerName, manufacturer, maxX, maxY, maxZ, maxColors, null);
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
    public void freePrinterResources(int printerId, PrintTaskManager printTaskManager,
                                     PrinterManager printerManager, SpoolManager spoolManager) {

        // Retrieve the printer based on its ID
        Printer printer = getPrinterById(printerId);

        // Error handling if printer is not found
        if (printer == null) {
            System.out.println("Cannot find a printer with ID " + printerId);
            return;
        }

        // Retrieve the current task from the PrintTaskManager
        PrintTask task = printTaskManager.getPrinterCurrentTask(printer);

        // If there's no task, there are no resources to free
        if (task == null) {
            return;
        }

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

        // Process all spools
        for (int i = 0; i < spools.length && i < task.getColors().size(); i++) {
            spools[i].reduceLength(task.getPrint().getFilamentLength().get(i));
        }

        // Log that task completion
        System.out.println("Task " + task.getPrint().getName() + " was completed by printer " + printer.getName());

        // Assign a new task to the printer
        printTaskManager.selectPrintTask(printer, printerManager, spoolManager);
    }


    public void handlePrinterFailure(int printerId, PrintTaskManager printTaskManager,
                                     PrinterManager printerManager, SpoolManager spoolManager) {

        // Retrieve the printer based on its ID
        Printer printer = getPrinterById(printerId);

        // Error handling if printer is not found
        if (printer == null) {
            System.out.println("Cannot find a printer with ID " + printerId);
            return;
        }

        // Retrieve the failed task from the PrintTaskManager
        PrintTask task = printTaskManager.getPrinterCurrentTask(printer);

        // If there's no task, there's nothing to manage
        if (task == null) {
            return;
        }

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

        // Reduce the spool length
        for (int i = 0; i < spools.length && i < task.getColors().size(); i++) {
            spools[i].reduceLength(task.getPrint().getFilamentLength().get(i));
        }

        // Attempt to execute a new task on the printer
        printTaskManager.selectPrintTask(printer, printerManager, spoolManager);
    }
    // Remove spools and their related methods from this class
}
