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

    public void assignTaskToPrinter(Printer printer, PrintTask chosenTask) {
        freePrinters.remove(printer);
    }
}
