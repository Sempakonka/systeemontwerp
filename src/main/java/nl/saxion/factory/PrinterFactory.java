package nl.saxion.factory;

import nl.saxion.Models.PrintTask;
import nl.saxion.Models.Printer;

public interface PrinterFactory {
    Printer createPrinter(int id, String printerName, String manufacturer, int maxX, int maxY, int maxZ, int maxColors);
}


