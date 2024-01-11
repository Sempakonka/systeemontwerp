package nl.saxion.factory.FactoryTypes;

import nl.saxion.Models.Printers.HousedPrinter;
import nl.saxion.Models.Printer;
import nl.saxion.factory.PrinterFactory;

public class HousedPrinterFactory implements PrinterFactory {
    @Override
    public Printer createPrinter(String id, String printerName, String manufacturer, int maxX, int maxY, int maxZ, int maxColors) {
        return new HousedPrinter(id, printerName, manufacturer, maxX, maxY, maxZ, null);
    }
}
