package nl.saxion.managers;


import nl.saxion.Models.FilamentType;
import nl.saxion.Models.Print;
import nl.saxion.Models.PrintTask;
import java.util.function.Predicate;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PrintTaskManager {

    private static PrintTaskManager instance = null;

    private PrintTaskManager() {
    }

    public static PrintTaskManager getInstance() {
        if (instance == null) {
            instance = new PrintTaskManager();
        }
        return instance;
    }
    private List<PrintTask> printTasks = new ArrayList<>();


    public void addPrintTask(String printId, List<String> colors, int filamentType) {
        if (colors.size() == 0) {
            printError("Need at least one color, but none given");
            return;
        }

        PrintTask task = new PrintTask(printId, colors, filamentType);
        printTasks.add(task);
        System.out.println("Added task to queue");
    }

    public List<String> getPendingPrintTasks() {
        List<String> pendingTasks = new ArrayList<>();
        for (PrintTask task : printTasks) {
            if (task.getPrinterId() == null) {
                pendingTasks.add(task.getId());
            }
        }
        return pendingTasks;
    }

    /**
     * Retrieves the current print task associated with the given printer ID.
     *
     * @param printerId The ID of the printer to search for.
     * @return The current PrintTask object associated with the given printer ID, or null if no task is found.
     */
    public PrintTask getPrinterCurrentTask(String printerId) {
        // search for task with printerId
        for (PrintTask task : printTasks) {
            if (task.getPrinterId() != null && task.getPrinterId().equals(printerId)) {
                return task;
            }
        }
        return null;
    }

    private void printError(String s) {
        System.out.println("---------- Error Message ----------");
        System.out.println("Error: " + s);
        System.out.println("--------------------------------------");
    }



    public String findTaskForPrinter(Predicate<PrintTask> taskEvaluator) {
        for (PrintTask task : printTasks) {
            if (taskEvaluator.test(task)) {
                return task.getId();
            }
        }
        return null;
    }

    public void removePendingTask(String taskId) {
        printTasks.removeIf(task -> task.getId().equals(taskId));
    }

    /**
     * Assigns a print task to a printer.
     *
     * @param printerId   the ID of the printer to which the task is assigned
     *
     */
    public void assignTaskToPrinter(String printerId, String chosenTaskId) {
        for (PrintTask task : printTasks) {
            if (task.getId().equals(chosenTaskId)) {
                task.setPrinterId(printerId);
                break;
            }
        }
    }

    /**
     * Removes a running print task for the specified printer.
     *
     * @param printerId the ID of the printer for which to remove the running print task
     */
    public void removeRunningTask(String printerId) {
        for (PrintTask task : printTasks) {
            if (task.getPrinterId().equals(printerId)) {
                task.setPrinterId(null);
                break;
            }
        }
    }

    public void addPendingTask(String taskId) {
        for (PrintTask task : printTasks) {
            if (task.getId().equals(taskId)) {
                task.setPrinterId(null);
                break;
            }
        }
    }

    public String getTaskColors(String chosenTaskId) {
        // search
        return "";
    }

    public String getTaskFilamentType(String chosenTaskId) {
        for (PrintTask task : printTasks) {
            if (task.getId().equals(chosenTaskId)) {
                return FilamentType.values()[task.getFilamentType()].toString();
            }
        }
        return null;
    }
}
