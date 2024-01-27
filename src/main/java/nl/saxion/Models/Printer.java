package nl.saxion.Models;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class Printer {
    private String id;
    private String name;
    private String manufacturer;

    private String currentTaskId;

    protected final int maxX;
    protected int maxY;
    protected final int maxZ;

    public Printer(String id, String printerName, String manufacturer, int maxX, int maxY, int maxZ) {
        this.id = id;
        this.name = printerName;
        this.manufacturer = manufacturer;
        this.maxX = maxX;
        this.maxY = maxY;
        this.maxZ = maxZ;
    }

    public void setCurrentTaskId(String currentTaskId) {
        this.currentTaskId = currentTaskId;
    }

    public String getCurrentTaskId() {
        return currentTaskId;
    }

    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return  "--------" + System.lineSeparator() +
                "- ID: " + id + System.lineSeparator() +
                "- Name: " + name + System.lineSeparator() +
                "- Manufacturer: " + manufacturer + System.lineSeparator() +
                "- Current task ID: " + currentTaskId + System.lineSeparator() +
                "--------";
    }

    public String getName(){
        return name;
    }

    public abstract boolean canAcceptTask(PrintTask task);

    public abstract void setCurrentSpools(Integer[] spools);
    public abstract Integer[] getCurrentSpools();
}
