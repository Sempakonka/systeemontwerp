package nl.saxion.managers;

import nl.saxion.Models.PrintTask;
import nl.saxion.Models.Printer;
import nl.saxion.Models.Spool;

import java.util.ArrayList;
import java.util.List;

public class SpoolManager {
    private List<Spool> spools = new ArrayList<>();
    private List<Spool> freeSpools = new ArrayList<>();

    public void addSpool(Spool spool) {
        spools.add(spool);
        freeSpools.add(spool);
    }

    public List<Spool> getSpools() {
        return spools;
    }

    public boolean areSpoolsAvailableForTask(PrintTask task) {
        for (Spool spool : freeSpools) {
            if (spool.spoolMatch(task.getColors().get(0), task.getFilamentType())) {
                return true;
            }
        }
        return false;
    }

    public void assignSpoolsToTask(PrintTask task) {
        for (Spool spool : freeSpools) {
            if (spool.spoolMatch(task.getColors().get(0), task.getFilamentType())) {
                freeSpools.remove(spool);
                break;
            }
        }
    }

    public void reduceResourcesForPrinter(Printer printer, PrintTask task) {
        printer.reduceSpoolLength(task);
    }
}
