package nl.saxion.Models;

import nl.saxion.Models.interfaces.IMultiColor;
import nl.saxion.Models.interfaces.MultiSpoolPrinter;
import nl.saxion.managers.PrintTaskManager;
import nl.saxion.managers.SpoolManager;

/* Printer capable of printing multiple colors. */
public class MultiColorPrinter extends MultiSpoolPrinter implements IMultiColor {
    private int maxColors;

    public MultiColorPrinter(int id, String printerName, String manufacturer, int maxX, int maxY, int maxZ, int maxColors, Spool[] spools) {
        super(id, printerName, manufacturer, maxX, maxY, maxZ, spools);
        this.maxColors = maxColors;
    }
    @Override
    public void handlePrintTask(PrintTask printTask, PrintTaskManager printTaskManager, SpoolManager spoolManager) {

    }

    @Override
    public String toString() {
        String result = super.toString();
        String[] resultArray = result.split("- ");
        String spools = resultArray[resultArray.length-1];
        for (Spool spool : getCurrentSpools()) {
            if(spool != null) {
                spools = spools.replace(System.lineSeparator(), ", " + spool.getId() + System.lineSeparator());
            }
        }
        spools = spools.replace("--------", "- maxColors: " + maxColors + System.lineSeparator() +
                "--------");
        resultArray[resultArray.length-1] = spools;
        result = String.join("- ", resultArray);
        return result;
    }

    @Override
    public boolean printFits(Print print) {
        return false;
    }

    @Override
    public int getMaxColors() {
        return maxColors;
    }
}
