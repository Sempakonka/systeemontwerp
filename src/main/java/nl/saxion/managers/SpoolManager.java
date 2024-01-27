package nl.saxion.managers;

import nl.saxion.Models.FilamentType;

import nl.saxion.Models.Spool;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

    public Integer addSpool(Integer id, String color, FilamentType filamentType, double length) {
        Spool spool = new Spool(id, color, filamentType, length);
        spools.add(spool);
        return spool.getId();
    }

    public List<String> getSpools() {
        // return all spools as list of strings
        List<String> spoolStrings = new ArrayList<>();
        for (Spool spool : spools) {
            spoolStrings.add(spool.getId() + " " + spool.getColor() + " " + spool.getFilamentType() + " " + spool.getLength());
        }
        return spoolStrings;
    }

    /**
     * Finds a spool id for all the needed colors.
     *
     * @param colors The list of colors to check for availability. SHOULD only contain colors belonging to one print
     * @param filamentType The filament type to check for availability.
     * @return A list of spool IDs that are available for the given colors and filament type.
     * Returns null if no spools are available OR founds spools amount does not equal given amount of colors
     * because that means that given print is not available to print. Printing a print partially is not wanted ofcourse.
     */
    public List<Integer> areSpoolsAvailableForTask(List<String> colors, FilamentType filamentType) {
        List<Integer> availableSpools = new ArrayList<>();
        for (String color : colors) {
            for (Spool spool : spools) {
                if (spool.spoolMatch(color, filamentType) && spool.getTaskId() == null) {
                    availableSpools.add(spool.getId());
                    break;
                }
            }
        }

        if (availableSpools.isEmpty() || availableSpools.size() != colors.size()) {
            return null;
        } else {
            return availableSpools;
        }
    }

    public void assignSpoolsToTask(List<Integer> spoolIds,  String taskId, String printId) {
        int iteration = 0;
        for (Spool spool : spools) {
            if (spoolIds.contains(spool.getId())) {
                spool.setTaskId(taskId);
                spool.setPrintId(printId);
                //TODO: For now, the filament index is the same as the spool index.
                // This should be changed later to what color the user wants to become what part.
                spool.setPrintFilamentIndex(iteration);
                iteration++;
            }
        }
    }

    /**
     * Frees the spools associated with a specific task. So spool no longer has that task and is available for other tasks.
     *
     * @param taskId The unique identifier of the task.
     */
    public void freeSpoolsFromTask(String taskId) {
        for (Spool spool : spools) {
                System.out.println("Freeing spool " + spool.getTaskId() + " from task " + taskId);
            if (spool.getTaskId() != null && spool.getTaskId().equals(taskId)) {
                spool.setTaskId(null);
                spool.setPrintId(null);
                spool.setPrintFilamentIndex(null);
            }
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


    /**
     * Reduces the length of the spools associated with a specific print ID.
     *
     * @param filamentLength The list of filament lengths.
     * @param printId        The unique identifier of the print.
     */
    public void reduceSpoolResources(ArrayList<Double> filamentLength, String printId) {
        // if spool printid is equal to param print id, then get the filament index of the spool and on that index in the filamentlength arraylist reduce the length of the spool
        for (Spool spool : spools) {
            if (spool.getPrintId() != null && spool.getPrintId().equals(printId)) {
                int index = spool.getPrintFilamentIndex();
                double length = filamentLength.get(index);
                spool.reduceLength(length);
            }
        }
    }
}
