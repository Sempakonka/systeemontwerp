package nl.saxion.managers;

import nl.saxion.Models.*;
import nl.saxion.Models.interfaces.MultiSpoolPrinter;
import nl.saxion.Models.interfaces.PrinterFactory;
import nl.saxion.Models.interfaces.SingleSpoolPrinter;
import nl.saxion.factory.HousedPrinterFactory;
import nl.saxion.factory.MultiColorPrinterFactory;
import nl.saxion.factory.StandardFDMPrinterFactory;

import java.util.ArrayList;
import java.util.List;

public class PrinterManager {
    private List<Printer> printers = new ArrayList<>();
    private List<Printer> freePrinters = new ArrayList<>();

    public void addPrinter(int id, int printerType, String printerName, String manufacturer, int maxX, int maxY, int maxZ, int maxColors) {
        PrinterFactory factory = getPrinterFactory(printerType);
        if (factory != null) {
            Printer printer = factory.createPrinter(id, printerName, manufacturer, maxX, maxY, maxZ, maxColors);
            printers.add(printer);
            freePrinters.add(printer);
        } else {
            // Handle invalid printer type
            System.out.println("Invalid printer type specified.");
        }
    }

    private PrinterFactory getPrinterFactory(int printerType) {
        switch (printerType) {
            case 1: return new StandardFDMPrinterFactory();
            case 2: return new HousedPrinterFactory();
            case 3: return new MultiColorPrinterFactory();
            default: return null;
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
