import java.util.ArrayList;

public class Continent {

	private ArrayList<Territory> list;
	private String name;
	private int bonus;
	
	public Continent(String name,int bonus){
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

	public int getBonus() {
		return bonus;
	}

	public void addTerritory(Territory ter){
		list.add(ter);
		ter.setMotherContinent(this);
	}
}
