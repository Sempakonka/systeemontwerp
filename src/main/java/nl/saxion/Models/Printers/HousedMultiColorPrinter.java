package nl.saxion.Models.Printers;

import nl.saxion.Models.FilamentType;
import nl.saxion.Models.PrintTask;
import nl.saxion.Models.Spool;
import nl.saxion.Models.interfaces.MultiSpoolPrinter;

import java.util.Map;

public class HousedMultiColorPrinter extends MultiSpoolPrinter {
    private int maxColors;
    public HousedMultiColorPrinter(String id, String printerName, String manufacturer, int maxX, int maxY, int maxZ, int maxColors, Integer[] spools) {
        super(id, printerName, manufacturer, maxX, maxY, maxZ, spools);
        this.maxColors = maxColors;
    }

    @Override
    public boolean canAcceptTask(PrintTask task) {
        return task.getColors().size() <= maxColors;
    }
}
