package nl.saxion.Models;

import nl.saxion.Models.interfaces.SingleSpoolPrinter;

import java.util.Map;

/* Standard cartesian FDM printer */
public class StandardFDMPrinter extends SingleSpoolPrinter   {

    public StandardFDMPrinter(String id, String printerName, String manufacturer, int maxX, int maxY, int maxZ, Spool spool) {
        super(id, printerName, manufacturer, maxX, maxY, maxZ, spool);
    }

    @Override
    public void reduceSpoolLength(Map<Integer, Double> reductionMap) {
        for (Map.Entry<Integer, Double> entry : reductionMap.entrySet()) {
            int index = entry.getKey();
            double length = entry.getValue();
            getCurrentSpool().reduceLength(length);
        }
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
