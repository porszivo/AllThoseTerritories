package de.allthoseterritories;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

import javax.swing.JComponent;

public class Territory extends JComponent {
	private String name;
	private ArrayList<Territory> neighbours;
	private int[] capital;
	private int armyValue;
	private String owner;
	private ArrayList<int[]> polyXCoords;
	private ArrayList<int[]> polyYCoords;

	public Territory(String name, int[] coords) {
		this.name = name;
		this.owner = null;
		this.polyXCoords = new ArrayList<int[]>();
		this.polyYCoords = new ArrayList<int[]>();
		addPatch(coords);
		this.armyValue = 0;
		this.neighbours = new ArrayList<Territory>();
	}

	public void setCapital(int[] coords) {
		this.capital = coords;
	}

	public String getName() {
		return name;
	}


	public void addPatch(int[] patch) {
		int[] polyXCoordsSingle = new int[patch.length / 2];
		int[] polyYCoordsSingle = new int[patch.length / 2];
		int j = 0;
		for (int i = 0; i < patch.length; i++) {
			if (i % 2 == 0) {
				polyXCoordsSingle[j] = patch[i];
			} else {
				polyYCoordsSingle[j] = patch[i];
				j++;
			}
		}
		polyXCoords.add(polyXCoordsSingle);
		polyYCoords.add(polyYCoordsSingle);
	}

	public ArrayList<int[]> getPolyYCoords() {
		return polyYCoords;
	}

	public int getCapitalX() {
		return capital[0];
	}

	public int getCapitalY() {
		return capital[1];
	}

	public void setNeighbours(ArrayList<Territory> neighbours) {
		this.neighbours = neighbours;
	}

	public ArrayList<Territory> getNeighbours() {
		return neighbours;
	}

	public String getArmyValue() {
		return Integer.toString(armyValue);
	}

	public String getOwner() {
		return owner;
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setColor(Color.blue);
		if (owner == null)
			g.setColor(Color.blue);
		else if (owner.equals("CPU"))
			g.setColor(Color.red);
		else if (owner.equals("HUMAN"))
			g.setColor(Color.blue);

		for (int i = 0; i < polyXCoords.size(); i++) {
			g.fillPolygon(polyXCoords.get(i), polyYCoords.get(i),
					polyXCoords.get(i).length);
		}

		g.setColor(Color.black);
		g.drawString(Integer.toString(armyValue), capital[0], capital[1]);

	

}

}
