package nl.saxion.Models;

import nl.saxion.Models.interfaces.SingleSpoolPrinter;

import java.util.List;

/* Standard cartesian FDM printer */
public class StandardFDMPrinter extends SingleSpoolPrinter   {

    public StandardFDMPrinter(int id, String printerName, String manufacturer, int maxX, int maxY, int maxZ, Spool spool) {
        super(id, printerName, manufacturer, maxX, maxY, maxZ, spool);
    }

    @Override
    public void reduceSpoolLength(PrintTask task) {
        getCurrentSpool().reduceLength(task.getPrint().getFilamentLength().get(0));
    }

    @Override
    public boolean canAcceptTask(PrintTask task) {
        return task.getFilamentType() != FilamentType.ABS && task.getColors().size() == 1;
    }

    @Override
    public String toString() {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
