package nl.saxion.managers;

import nl.saxion.Models.Print;

import java.util.ArrayList;
import java.util.List;

public class PrintManager {
    private List<Print> prints = new ArrayList<>();

    public void addPrint(String name, int height, int width, int length, ArrayList<Double> filamentLength, int printTime) {
        Print p = new Print(name, height, width, length, filamentLength, printTime);
        prints.add(p);
    }

    public List<Print> getPrints() {
        return prints;
    }

    public Print findPrint(String printName) {
        for (Print p : prints) {
            if (p.getName().equals(printName)) {
                return p;
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
}
