package nl.saxion.Models.Printers;

import nl.saxion.Models.PrintTask;
import nl.saxion.Models.Spool;
import nl.saxion.Models.interfaces.SingleSpoolPrinter;

import java.util.Map;

/* Printer capable of printing ABS */
public class HousedPrinter extends SingleSpoolPrinter   {

    public HousedPrinter(String id, String printerName, String manufacturer, int maxX, int maxY, int maxZ, Integer spool) {
        super(id, printerName, manufacturer, maxX, maxY, maxZ, spool);
    }

    @Override
    public boolean canAcceptTask(PrintTask task) {
        return task.getColors().size() == 1;
    }
}
