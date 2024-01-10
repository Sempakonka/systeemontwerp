package nl.saxion.factory;

import nl.saxion.Models.HousedPrinter;
import nl.saxion.Models.Printer;

public class HousedPrinterFactory implements PrinterFactory {
    @Override
    public Printer createPrinter(int id, String printerName, String manufacturer, int maxX, int maxY, int maxZ, int maxColors) {
        return new HousedPrinter(id, printerName, manufacturer, maxX, maxY, maxZ, null);
    }
}
