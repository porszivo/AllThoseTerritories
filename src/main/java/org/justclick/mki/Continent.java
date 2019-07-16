package org.justclick.mki;

import java.util.ArrayList;
import java.util.List;

public class Continent {

    private List<Territory> list;
    private String name;
    private int bonus;

    public Continent(String name, int bonus) {
        this.name = name;
        this.bonus = bonus;
        list = new ArrayList<>();
    }

    public boolean completeOwner(String player) {

        for (Territory elem : list) {
            if (!elem.getOwner().equals(player)) return false;
        }

        return true;

    }

    public int getAmountOfTerritory() {
        return list.size();
    }

    public List<Territory> getTerritoryList() {
        return list;
    }

    public int getBonus() {
        return bonus;
    }

    public void addTerritory(Territory ter) {
        list.add(ter);
        ter.setMotherContinent(this);
    }

    public String getName() {
        return this.name;
    }

    public boolean isOccupied() {
        for (Territory terr : list) {
            if (terr.getOwner() == null) return false;
        }
        return true;
    }

}
