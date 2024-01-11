package nl.saxion.factory;

import nl.saxion.Models.Printer;
import nl.saxion.Models.Printers.StandardFDMPrinter;

public class StandardFDMPrinterFactory implements PrinterFactory {
    @Override
    public Printer createPrinter(String id, String printerName, String manufacturer, int maxX, int maxY, int maxZ, int maxColors) {
        return new StandardFDMPrinter(id, printerName, manufacturer, maxX, maxY, maxZ, null);
    }
}
