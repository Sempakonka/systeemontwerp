package nl.saxion.Models.interfaces;

import nl.saxion.Models.Printer;
import nl.saxion.Models.Spool;

public abstract class MultiSpoolPrinter extends Printer {
    Spool[] spools;

    public MultiSpoolPrinter(String id, String printerName, String manufacturer, int maxX, int maxY, int maxZ, Spool[] spools) {
        super(id, printerName, manufacturer, maxX, maxY, maxZ);
        this.spools = spools;
    }

    void setCurrentSpools(Spool[] spools){
        this.spools = spools;
    };
    public Spool[] getCurrentSpools(){
        return spools;
    };
}
