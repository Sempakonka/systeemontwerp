package nl.saxion.managers;

import nl.saxion.Models.FilamentType;

import nl.saxion.Models.Spool;

import java.util.ArrayList;
import java.util.List;

public class SpoolManager {

    private static SpoolManager instance = null;

    private SpoolManager() {
    }

    public static SpoolManager getInstance() {
        if (instance == null) {
            instance = new SpoolManager();
        }
        return instance;
    }

    private List<Spool> spools = new ArrayList<>();

    public void addSpool(int id, String color, String filamentType, double length) {
        FilamentType type = FilamentType.valueOf(filamentType);
        Spool spool = new Spool(id, color, type, length);
        spools.add(spool);
    }

    public List<String> getSpools() {
        // return all spools as list of strings
        List<String> spoolStrings = new ArrayList<>();
        for (Spool spool : spools) {
            spoolStrings.add(spool.getId() + " " + spool.getColor() + " " + spool.getFilamentType() + " " + spool.getLength());
        }
        return spoolStrings;
    }

    public boolean areSpoolsAvailableForTask(String colors, FilamentType filamentType) {
        for (Spool spool : spools) {
            if (spool.spoolMatch(colors, filamentType) && spool.getTaskId() == null) {
                return true;
            }
        }
        return false;
    }

    public void assignSpoolsToTask(String colors, FilamentType filamentType, String taskId) {
        boolean found = false;
        for (Spool spool : spools) {
            if (spool.spoolMatch(colors, filamentType) && spool.getTaskId() == null) {
                spool.setTaskId(taskId);
                found = true;
                break;
            }
        }

        if (!found) {
            System.out.println("Warning: No suitable spools found for task");
        }
    }

    // get all filemtntypes return as list of string
    public List<String> getFilamentTypes() {
        List<String> filamentTypes = new ArrayList<>();
        for (FilamentType filamentType : FilamentType.values()) {
            filamentTypes.add(filamentType.toString());
        }
        return filamentTypes;
    }


    /**
     * Retrieves the available colors for a given filament type.
     *
     * @param filamentType the filament type to retrieve the available colors for
     * @return a list of available colors for the given filament type
     */
    public List<String> getAvailableColorsForFilamentType(String filamentType) {
        List<String> colors = new ArrayList<>();
        for (Spool spool : spools) {
            if (spool.getFilamentType().toString().equals(filamentType) && !colors.contains(spool.getColor())) {
                colors.add(spool.getColor());
            }
        }
        return colors;
    }
}
