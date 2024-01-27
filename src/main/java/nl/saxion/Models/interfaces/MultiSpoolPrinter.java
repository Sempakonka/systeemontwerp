package nl.saxion.Models.interfaces;

import nl.saxion.Models.Printer;
import nl.saxion.Models.Spool;

import java.util.List;
import java.util.stream.Stream;

public abstract class MultiSpoolPrinter extends Printer {
    Integer[] spoolsIds;

    public MultiSpoolPrinter(String id, String printerName, String manufacturer, int maxX, int maxY, int maxZ, Integer[] spools) {
        super(id, printerName, manufacturer, maxX, maxY, maxZ);
        this.spoolsIds = spools;
    }

    @Override
    public void setCurrentSpools(Integer[] spools){
        this.spoolsIds = spools;
    }
    @Override
    public Integer[] getCurrentSpools(){
        return spoolsIds;
    }
}
