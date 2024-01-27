package nl.saxion.Models.interfaces;

import nl.saxion.Models.Printer;
import nl.saxion.Models.Spool;

import java.util.List;

public abstract class SingleSpoolPrinter extends Printer {
    Integer spoolId;

    public SingleSpoolPrinter(String id, String printerName, String manufacturer, int maxX, int maxY, int maxZ, Integer spoolId) {
        super(id, printerName, manufacturer, maxX, maxY, maxZ);
        this.spoolId = spoolId;
    }

    @Override
    public Integer[] getCurrentSpools() {
        if (spoolId == null) {
            return null;
        }
        return new Integer[]{spoolId};
    }


    @Override
    public void setCurrentSpools(Integer[] spools) {
        if (spools == null){
            spoolId = null;
            return;
        }
        spoolId = spools[0];
    }
}
