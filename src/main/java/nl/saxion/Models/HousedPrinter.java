package nl.saxion.Models;

import nl.saxion.Models.interfaces.IHousedPrinter;
import nl.saxion.Models.interfaces.SingleSpoolPrinter;
import nl.saxion.managers.PrintTaskManager;
import nl.saxion.managers.SpoolManager;

/* Printer capable of printing ABS */
public class HousedPrinter extends SingleSpoolPrinter implements IHousedPrinter {
    public HousedPrinter(int id, String printerName, String manufacturer, int maxX, int maxY, int maxZ, Spool spool) {
        super(id, printerName, manufacturer, maxX, maxY, maxZ, spool);
    }

    @Override
    public void handlePrintTask(PrintTask printTask, PrintTaskManager printTaskManager, SpoolManager spoolManager) {

    }

    @Override
    public boolean printFits(Print print) {
        return false;
    }
}
