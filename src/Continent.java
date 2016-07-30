import java.util.ArrayList;

public class Continent {

	private ArrayList<Territory> list;
	private String name;
	private int bonus;
	private ControlCentre controlCentre;
	
	public Continent(String name, int bonus, ControlCentre controlCentre) {
		this.controlCentre = controlCentre;
		this.name = name;
		this.bonus = bonus;
		list = new ArrayList<Territory>();
	}

	public boolean completeOwner(String player) {

		for(Territory elem : list) {
			if(!elem.getOwner().equals(player)) return false;
		}

		return true;

	}

	public int getAmountOfTerritory() {
		return list.size();
	}

	public ArrayList<Territory> getTerritoryList() {
		return list;
	}

	public int getBonus() {
		return bonus;
	}

	public void addTerritory(Territory ter){
		list.add(ter);
		ter.setMotherContinent(this);
	}
	
	public double getContinentWeight(String player) {
		return AIHelper.calcContinentWeight(this,player);
	}

	public String getName() {
		return this.name;
	}

	public boolean isOccupied() {
		boolean ret = true;
		for(Territory terr : list) {
			if(terr.getOwner() == null) return false;
		}
		return ret;
	}

}
