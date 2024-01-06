package nl.saxion.Models;

import nl.saxion.Models.interfaces.SingleSpoolPrinter;
import nl.saxion.Models.interfaces.IStandardFDM;
import nl.saxion.managers.PrintTaskManager;
import nl.saxion.managers.SpoolManager;

/* Standard cartesian FDM printer */
public class StandardFDMPrinter extends SingleSpoolPrinter implements IStandardFDM {
    private Spool currentSpool;

    public StandardFDMPrinter(int id, String printerName, String manufacturer, int maxX, int maxY, int maxZ, Spool spool) {
        super(id, printerName, manufacturer, maxX, maxY, maxZ, spool);
    }

    @Override
    public void handlePrintTask(PrintTask printTask, PrintTaskManager printTaskManager, SpoolManager spoolManager) {

    }

    @Override
    public boolean printFits(Print print) {
        return print.getHeight() <= maxZ && print.getWidth() <= maxX && print.getLength() <= maxY;
    }

    public int CalculatePrintTime(String filename) {
        return 0;
    }

    @Override
    public String toString() {
        String result = super.toString();
        String append = "- maxX: " + maxX + System.lineSeparator() +
                "- maxY: " + maxY + System.lineSeparator() +
                "- maxZ: " + maxZ + System.lineSeparator();
        if (currentSpool != null) {
            append += "- Spool(s): " + currentSpool.getId()+ System.lineSeparator();
        }
        append += "--------";
        result = result.replace("--------", append);
        return result;
    }
}
