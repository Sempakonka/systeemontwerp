package nl.saxion.Models;

import nl.saxion.Models.interfaces.MultiSpoolPrinter;

import java.util.Map;

/* Printer capable of printing multiple colors. */
public class MultiColorPrinter extends MultiSpoolPrinter  {
    private int maxColors;

    public MultiColorPrinter(String id, String printerName, String manufacturer, int maxX, int maxY, int maxZ, int maxColors, Spool[] spools) {
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
        return task.getFilamentType() != FilamentType.ABS && task.getColors().size() <= getMaxColors();
    }

    public int getMaxColors() {
        return maxColors;
    }
}
