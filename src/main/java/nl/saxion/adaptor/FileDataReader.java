package nl.saxion.adaptor;

import nl.saxion.Models.Print;
import nl.saxion.Models.Printer;
import nl.saxion.Models.Spool;

import java.util.List;

public interface FileDataReader {
    void readPrints(String filename);
    void readPrinters(String filename);
    void readSpools(String filename);
}

