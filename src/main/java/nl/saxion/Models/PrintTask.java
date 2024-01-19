package nl.saxion.Models;

import java.util.List;
import java.util.UUID;

public class PrintTask {
    private String printId;
    private List<String> colors;
    private int filamentType;
    /**
     * The ID of the printer currently printing this task.
     */
    private String printerId;

    private String id;


    public PrintTask(String printId, List<String> colors, int filamentType) {
        this.printId = printId;
        this.colors = colors;
        this.filamentType = filamentType;
        this.id = UUID.randomUUID().toString();
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

    public int getFilamentType() {
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
