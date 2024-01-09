package nl.saxion.factory;

import nl.saxion.Models.MultiColorPrinter;
import nl.saxion.Models.Printer;
import nl.saxion.Models.interfaces.PrinterFactory;

public class MultiColorPrinterFactory implements PrinterFactory {
    @Override
    public Printer createPrinter(int id, String printerName, String manufacturer, int maxX, int maxY, int maxZ, int maxColors) {
        return new MultiColorPrinter(id, printerName, manufacturer, maxX, maxY, maxZ, maxColors, null);
    }
}
