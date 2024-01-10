package nl.saxion.adaptor;

import nl.saxion.Models.Print;
import nl.saxion.Models.Printer;
import nl.saxion.Models.Spool;

import java.util.List;

public interface FileDataReader {
    List<Print> readPrints(String filename);
    List<Printer> readPrinters(String filename);
    List<Spool> readSpools(String filename);
}

