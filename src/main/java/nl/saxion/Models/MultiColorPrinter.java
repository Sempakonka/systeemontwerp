package nl.saxion.Models;

import nl.saxion.Models.interfaces.MultiSpoolPrinter;

import java.util.List;

/* Printer capable of printing multiple colors. */
public class MultiColorPrinter extends MultiSpoolPrinter  {
    private int maxColors;

    public MultiColorPrinter(int id, String printerName, String manufacturer, int maxX, int maxY, int maxZ, int maxColors, Spool[] spools) {
        super(id, printerName, manufacturer, maxX, maxY, maxZ, spools);
        this.maxColors = maxColors;
    }

    @Override
    public PrintTask selectTask(List<PrintTask> pendingTasks, List<Spool> freeSpools) {
        return null;
    }

    @Override
    public void freeResources() {
        for (Spool spool : getCurrentSpools()) {
            if(spool != null) {
                spool.emptySpool();
            }
        }
    }

    @Override
    public void reduceSpoolLength(PrintTask task) {
        for (int i = 0; i < task.getPrint().getFilamentLength().size(); i++) {
            getCurrentSpools()[i].reduceLength(task.getPrint().getFilamentLength().get(i));
        }
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
    public boolean canAcceptTask(PrintTask task) {
        return task.getFilamentType() != FilamentType.ABS && task.getColors().size() <= getMaxColors();
    }

    public int getMaxColors() {
        return maxColors;
    }
}
