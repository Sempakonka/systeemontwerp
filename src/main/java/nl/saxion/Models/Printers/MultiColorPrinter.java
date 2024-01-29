package nl.saxion.Models.Printers;

import nl.saxion.Models.FilamentType;
import nl.saxion.Models.PrintTask;
import nl.saxion.Models.interfaces.MultiSpoolPrinter;

import java.util.Map;

/* Printer capable of printing multiple colors. */
public class MultiColorPrinter extends MultiSpoolPrinter  {
    private int maxColors;

    public MultiColorPrinter(String id, String printerName, String manufacturer, int maxX, int maxY, int maxZ, int maxColors, Integer[] spools) {
        super(id, printerName, manufacturer, maxX, maxY, maxZ, spools);
        this.maxColors = maxColors;
    }

    @Override
    public boolean canAcceptTask(PrintTask task) {
        return task.getFilamentType() != FilamentType.ABS && task.getColors().size() <= maxColors;
    }

    public int getMaxColors() {
        return maxColors;
    }
}
