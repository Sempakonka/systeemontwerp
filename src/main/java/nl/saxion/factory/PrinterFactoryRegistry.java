package nl.saxion.factory;

import java.util.HashMap;
import java.util.Map;

public class PrinterFactoryRegistry {
    private Map<Integer, PrinterFactory> factoryMap = new HashMap<>();

    public void registerFactory(int printerType, PrinterFactory factory) {
        factoryMap.put(printerType, factory);
    }

    public PrinterFactory getFactory(int printerType) {
        return factoryMap.getOrDefault(printerType, null);
    }
}
