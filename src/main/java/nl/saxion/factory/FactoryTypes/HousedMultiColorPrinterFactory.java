package nl.saxion.factory.FactoryTypes;

import nl.saxion.Models.Printer;
import nl.saxion.Models.Printers.HousedMultiColorPrinter;
import nl.saxion.factory.PrinterFactory;

public class HousedMultiColorPrinterFactory implements PrinterFactory {
    @Override
    public Printer createPrinter(String id, String printerName, String manufacturer, int maxX, int maxY, int maxZ, int maxColors) {
        return new HousedMultiColorPrinter(id, printerName, manufacturer, maxX, maxY, maxZ, maxColors, null);
    }
}
