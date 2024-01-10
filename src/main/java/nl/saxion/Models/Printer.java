package nl.saxion.Models;

import java.util.List;

public abstract class Printer {
    private int id;
    private String name;
    private String manufacturer;

    protected final int maxX;
    protected int maxY;
    protected final int maxZ;

    public Printer(int id, String printerName, String manufacturer, int maxX, int maxY, int maxZ) {
        this.id = id;
        this.name = printerName;
        this.manufacturer = manufacturer;
        this.maxX = maxX;
        this.maxY = maxY;
        this.maxZ = maxZ;
    }

    public abstract PrintTask selectTask(List<PrintTask> pendingTasks, List<Spool> freeSpools);

    public abstract void freeResources();

    public abstract void reduceSpoolLength(PrintTask task);

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return  "--------" + System.lineSeparator() +
                "- ID: " + id + System.lineSeparator() +
                "- Name: " + name + System.lineSeparator() +
                "- Manufacturer: " + manufacturer + System.lineSeparator() +
                "--------";
    }

    public String getName(){
        return name;
    }
    public abstract boolean printFits(Print print);

    public abstract boolean canAcceptTask(PrintTask task);
}
