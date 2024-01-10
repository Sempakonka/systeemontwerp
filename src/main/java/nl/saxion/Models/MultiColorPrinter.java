package nl.saxion.Models;

import nl.saxion.Models.interfaces.MultiSpoolPrinter;

/* Printer capable of printing multiple colors. */
public class MultiColorPrinter extends MultiSpoolPrinter  {
    private int maxColors;

    public MultiColorPrinter(int id, String printerName, String manufacturer, int maxX, int maxY, int maxZ, int maxColors, Spool[] spools) {
        super(id, printerName, manufacturer, maxX, maxY, maxZ, spools);
        this.maxColors = maxColors;
    }

    @Override
    public void reduceSpoolLength(PrintTask task) {
        for (int i = 0; i < task.getPrint().getFilamentLength().size(); i++) {
            getCurrentSpools()[i].reduceLength(task.getPrint().getFilamentLength().get(i));
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
