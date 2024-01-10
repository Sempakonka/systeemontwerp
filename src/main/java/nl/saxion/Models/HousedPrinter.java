package nl.saxion.Models;

import nl.saxion.Models.interfaces.SingleSpoolPrinter;

import java.util.List;

/* Printer capable of printing ABS */
public class HousedPrinter extends SingleSpoolPrinter   {

    public HousedPrinter(int id, String printerName, String manufacturer, int maxX, int maxY, int maxZ, Spool spool) {
        super(id, printerName, manufacturer, maxX, maxY, maxZ, spool);
    }

    @Override
    public PrintTask selectTask(List<PrintTask> pendingTasks, List<Spool> freeSpools) {
        return null;
    }

    @Override
    public void freeResources() {
        getCurrentSpool().emptySpool();
    }

    @Override
    public void reduceSpoolLength(PrintTask task) {
        getCurrentSpool().reduceLength(task.getPrint().getFilamentLength().get(0));
    }

    @Override
    public boolean printFits(Print print) {
        return false;
    }

    @Override
    public boolean canAcceptTask(PrintTask task) {
        return task.getPrint().getFilamentLength().size() == 1;
    }
}
