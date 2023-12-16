package nl.saxion.managers;

import nl.saxion.Models.Spool;

import java.util.ArrayList;
import java.util.List;

public class SpoolManager {
    private List<Spool> spools = new ArrayList<>();
    private List<Spool> freeSpools = new ArrayList<>();

    public void addSpool(Spool spool) {
        spools.add(spool);
        freeSpools.add(spool);
    }

    public List<Spool> getSpools() {
        return spools;
    }

    public Spool getSpoolByID(int id) {
        for(Spool s: spools) {
            if(s.getId() == id) {
                return s;
            }
        }
        return null;
    }

    public boolean containsSpool(final List<Spool> list, final String name){
        return list.stream().anyMatch(o -> o.getColor().equals(name));
    }

    public List<Spool> getFreeSpools() {
        return freeSpools;
    }
}
