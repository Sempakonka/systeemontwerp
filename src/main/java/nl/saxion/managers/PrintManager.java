package nl.saxion.managers;

import nl.saxion.Models.Print;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PrintManager {

    private static PrintManager instance = null;

    private PrintManager() {
    }

    public static PrintManager getInstance() {
        if (instance == null) {
            instance = new PrintManager();
        }
        return instance;
    }

    private List<Print> prints = new ArrayList<>();

    public void addPrint(String name, int height, int width, int length, ArrayList<Double> filamentLength, int printTime) {
        Print p = new Print(name, height, width, length, filamentLength, printTime);
        prints.add(p);
    }

    public List<String> getPrints() {
        List<String> printNames = new ArrayList<>();
        for (Print p : prints) {
            printNames.add(p.getName());
        }
        return printNames;
    }

    public String findPrint(String printName) {
        for (Print p : prints) {
            if (p.getName().equals(printName)) {
                return p.getId();
            }
        }
        return null;
    }

    public Print findPrint(int index) {
        if(index > prints.size() -1) {
            return null;
        }
        return prints.get(index);
    }

    public ArrayList<Double> getFilamentLengths(String printId) {
        for (Print p : prints) {
            if (p.getName().equals(printId)) {
                return p.getFilamentLengths();
            }
        }
        return null;
    }


    public int getAmountOfColors(String printName) {
        for (Print p : prints) {
            if (p.getName().equals(printName)) {
                return p.getAmountOfColors();
            }
        }
        return 0;
    }
}
