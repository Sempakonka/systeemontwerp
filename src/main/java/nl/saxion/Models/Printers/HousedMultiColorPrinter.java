package nl.saxion.Models.Printers;

import nl.saxion.Models.PrintTask;
import nl.saxion.Models.Spool;
import nl.saxion.Models.interfaces.MultiSpoolPrinter;

import java.util.Map;

public class HousedMultiColorPrinter extends MultiSpoolPrinter {
    private int maxColors;
    public HousedMultiColorPrinter(String id, String printerName, String manufacturer, int maxX, int maxY, int maxZ, int maxColors, Spool[] spools) {
        super(id, printerName, manufacturer, maxX, maxY, maxZ, spools);
        this.maxColors = maxColors;
    }

    @Override
    public void reduceSpoolLength(Map<Integer, Double> reductionMap) {
        for (Map.Entry<Integer, Double> entry : reductionMap.entrySet()) {
            int index = entry.getKey();
            double length = entry.getValue();
            getCurrentSpools()[index].reduceLength(length);
        }
    }

    @Override
    public boolean canAcceptTask(PrintTask task) {
        // can accept abs tasks and colors should be less than or equal to max colors
        return task.getFilamentType() == 0 && task.getColors().size() <= maxColors;
    }
}
