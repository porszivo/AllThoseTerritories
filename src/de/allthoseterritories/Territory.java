package de.allthoseterritories;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.JComponent;

public class Territory extends JComponent {
	private String name;
	private ArrayList<Territory> neighbours;
	private int[] capital;
	private int armyValue;
	private String owner;
	private ArrayList<Polygon> polylist = new ArrayList<Polygon>();
	private ArrayList<int[]> polyXCoords;
	private ArrayList<int[]> polyYCoords;

	public Territory(String name, int[] coords) {
		super();
		this.name = name;
		this.owner = null;
		this.polyXCoords = new ArrayList<>();
		this.polyYCoords = new ArrayList<>();
		addPatch(coords);
		this.armyValue = 0;
		this.neighbours = new ArrayList<>();

	}

	public void setCapital(int[] coords) {
		this.capital = coords;
		this.setBounds(capital[0] - 7, capital[1] - 15, 20, 20);
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
		Polygon polytemp = new Polygon();
		for (int i = 0; i < polyXCoordsSingle.length; i++) {
			polytemp.addPoint(polyXCoordsSingle[i], polyYCoordsSingle[i]);
		}
		polylist.add(polytemp);

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
		for (Polygon polygon : polylist) {
			g.setColor(Color.GRAY);
			g.fillPolygon(polygon);
			g.setColor(Color.BLACK);
			g.drawPolygon(polygon);
		}

		g.setColor(Color.RED);
		g.drawRect(capital[0] - 7, capital[1] - 15, 20, 20);

		g.drawString(Integer.toString(armyValue), capital[0], capital[1]);

	}

	public void changeOwner(String ow) {
		if (this.getOwner() == null) {
			this.owner = ow;
			this.repaint();
		}
	}

}
