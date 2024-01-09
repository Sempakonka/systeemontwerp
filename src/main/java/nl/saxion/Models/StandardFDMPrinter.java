package nl.saxion.Models;

import nl.saxion.Models.interfaces.SingleSpoolPrinter;
import nl.saxion.Models.interfaces.IStandardFDM;
import nl.saxion.managers.PrintTaskManager;
import nl.saxion.managers.SpoolManager;

import java.util.List;

/* Standard cartesian FDM printer */
public class StandardFDMPrinter extends SingleSpoolPrinter implements IStandardFDM {

    public StandardFDMPrinter(int id, String printerName, String manufacturer, int maxX, int maxY, int maxZ, Spool spool) {
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
        return print.getHeight() <= maxZ && print.getWidth() <= maxX && print.getLength() <= maxY;
    }

    @Override
    public boolean canAcceptTask(PrintTask task) {
        return task.getFilamentType() != FilamentType.ABS && task.getColors().size() == 1;
    }

    public int CalculatePrintTime(String filename) {
        return 0;
    }

    @Override
    public String toString() {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
