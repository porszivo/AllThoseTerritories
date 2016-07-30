package de.allthoseterritories;

// Class done

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

	public void addTerritory(Territory ter){
		list.add(ter);
	}
}
