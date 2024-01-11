package nl.saxion.factory;

import nl.saxion.Models.Printers.MultiColorPrinter;
import nl.saxion.Models.Printer;

public class MultiColorPrinterFactory implements PrinterFactory {
    @Override
    public Printer createPrinter(String id, String printerName, String manufacturer, int maxX, int maxY, int maxZ, int maxColors) {
        return new MultiColorPrinter(id, printerName, manufacturer, maxX, maxY, maxZ, maxColors, null);
    }
}
