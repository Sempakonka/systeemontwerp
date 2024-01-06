package nl.saxion.Models.interfaces;

import nl.saxion.Models.Printer;
import nl.saxion.Models.Spool;

public abstract class SingleSpoolPrinter extends Printer {
     Spool spool;

     public SingleSpoolPrinter(int id, String printerName, String manufacturer, int maxX, int maxY, int maxZ, Spool spool) {
          super(id, printerName, manufacturer, maxX, maxY, maxZ);
            this.spool = spool;
     }

     void setCurrentSpool(Spool spool){
            this.spool = spool;
     };
     public Spool getCurrentSpool(){
            return spool;
     };
}
