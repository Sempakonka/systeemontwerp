package nl.saxion.Models;

import java.util.List;

public class PrintTask {
    private String printId;
    private List<String> colors;
    private FilamentType filamentType;
    /**
     * The ID of the printer currently printing this task.
     */
    private String printerId;

    private String id;
    boolean isDone = false;


    public PrintTask(String printId, List<String> colors, FilamentType filamentType, String id) {
        this.printId = printId;
        this.colors = colors;
        this.filamentType = filamentType;
        this.id = id;
    }

    public void setDone(boolean isDone) {
        this.isDone = isDone;
    }

    public boolean getDone() {
        return isDone;
    }


    /**
     * Retrieves the ID of the printer currently printing this task.
     *
     * @return The ID of the printer.
     */
    public String getPrinterId() {
        return printerId;
    }

    /**
     * Sets the ID of the printer currently printing this task.
     *
     * @param printerId The ID of the printer.
     */
    public void setPrinterId(String printerId) {
        this.printerId = printerId;
    }


    public List<String> getColors() {
        return colors;
    }

    public FilamentType getFilamentType() {
        return filamentType;
    }

    public String getPrint() {
        return printId;
    }


    public String getId() {
        return id;
    }


    @Override
    public String toString() {
        return "PrintTask{" +
                "printId='" + printId + "', " +
                "colors=" + colors + ", " +
                "filamentType=" + filamentType + ", " +
                "printerId='" + printerId + "', " +
                "id='" + id + "'" +
                '}';
    }
}
