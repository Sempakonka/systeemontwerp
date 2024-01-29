package nl.saxion.Models.Printers;

import nl.saxion.Models.FilamentType;
import nl.saxion.Models.PrintTask;
import nl.saxion.Models.interfaces.SingleSpoolPrinter;

/* Standard cartesian FDM printer */
public class StandardFDMPrinter extends SingleSpoolPrinter   {

    public StandardFDMPrinter(String id, String printerName, String manufacturer, int maxX, int maxY, int maxZ, Integer spool) {
        super(id, printerName, manufacturer, maxX, maxY, maxZ, spool);
    }

    @Override
    public boolean canAcceptTask(PrintTask task) {
        return task.getFilamentType() != FilamentType.ABS && task.getColors().size() == 1;
    }
}
