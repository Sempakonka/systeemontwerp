package nl.saxion.factory;

import nl.saxion.Models.Printer;

public interface PrinterFactory {
    Printer createPrinter(String id, String printerName, String manufacturer, int maxX, int maxY, int maxZ, int maxColors);
}


