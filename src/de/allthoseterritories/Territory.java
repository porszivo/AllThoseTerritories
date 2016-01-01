package de.allthoseterritories;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.JComponent;

public class Territory extends JComponent implements MouseListener{
	private String name;
	private ArrayList<Territory> neighbours;
	private int[] capital;
	private int armyValue;
	private String owner;
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
		this.addMouseListener(this);
	}

	public void setCapital(int[] coords) {
		this.capital = coords;
		this.setBounds(capital[0]-40, capital[1]-40, 80, 80); //This is where the Magic happens, hier wird der bereich festgelegt, der Clickbar ist
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
		g.setColor(Color.BLUE);
		if (owner == null)
			g.setColor(Color.GRAY);
		else if (owner.equals("CPU"))
			g.setColor(Color.RED);
		else if (owner.equals("HUMAN"))
			g.setColor(Color.BLUE);

		for (int i = 0; i < polyXCoords.size(); i++) {
			g.fillPolygon(polyXCoords.get(i), polyYCoords.get(i),
					polyXCoords.get(i).length);
		}

		g.setColor(Color.BLACK);
		for (int i = 0; i < polyXCoords.size(); i++) {
			g.drawPolygon(polyXCoords.get(i), polyYCoords.get(i), polyXCoords.get(i).length);
		}

		g.drawString(Integer.toString(armyValue), capital[0], capital[1]);

	}

	public void changeOwner(String ow) {
		if(this.getOwner()==null) {
            this.owner=ow;
            this.repaint();
        }
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		System.out.println("Clicked!!!!" + name);
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}
