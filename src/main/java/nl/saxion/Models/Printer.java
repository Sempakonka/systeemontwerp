package nl.saxion.Models;

import nl.saxion.managers.PrintTaskManager;
import nl.saxion.managers.SpoolManager;

import java.util.ArrayList;

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

    public abstract void handlePrintTask(PrintTask printTask, PrintTaskManager printTaskManager, SpoolManager spoolManager);

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
}
