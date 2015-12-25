package de.allthoseterritories;

import java.awt.Color;
import java.awt.Graphics;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JComponent;

public class GameBoard extends JComponent {
	public ArrayList<Territory> list;
	public String initMap;
	public ArrayList<Continent> continentList;

	public GameBoard(String initMap) {
		this.initMap = initMap;
		list = new ArrayList<Territory>();
		continentList = new ArrayList<Continent>();
		parseFile();

	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		for (Territory element : list) {
			g.setColor(Color.red);
			for (Territory neighbour : element.getNeighbours()) {
				//Entfernung der Beiden Punkte kleiner bei Verbindung au�erhalb der Karte
				if (overEdge(neighbour.getCapitalX(), element.getCapitalX())) {
					if (element.getCapitalX() > neighbour.getCapitalX()) {
						g.drawLine(neighbour.getCapitalX() + 1250, neighbour.getCapitalY(), element.getCapitalX(),
								element.getCapitalY());
						g.drawLine(neighbour.getCapitalX(), neighbour.getCapitalY(), element.getCapitalX() - 1250,
								element.getCapitalY());
					}

					else {
						g.drawLine(neighbour.getCapitalX() - 1250, neighbour.getCapitalY(), element.getCapitalX(),
								element.getCapitalY());
						g.drawLine(neighbour.getCapitalX(), neighbour.getCapitalY(), element.getCapitalX() + 1250,
								element.getCapitalY());
					}
				} else {
					g.drawLine(neighbour.getCapitalX(), neighbour.getCapitalY(), element.getCapitalX(),
							element.getCapitalY());
				}
				//Ende Verbindung au�erhalb der Karte
			}
			g.setColor(Color.gray);
			for (int i = 0; i < element.getPolyXCoords().size(); i++) {
				g.fillPolygon(element.getPolyXCoords().get(i), element.getPolyYCoords().get(i),
						element.getPolyXCoords().get(i).length);
			}

			g.setColor(Color.green);
			g.drawString(element.getArmyValue(), element.getCapitalX(), element.getCapitalY());

		}

	}

	public ArrayList<Territory> getList() {
		return list;
	}

	public void parseFile() {
		String line;
		InputStream fis;
		try

		{
			fis = new FileInputStream(this.initMap);
			InputStreamReader isr = new InputStreamReader(fis);
			BufferedReader br = new BufferedReader(isr);

			while ((line = br.readLine()) != null) {
				if (line.startsWith("patch-of ")) {
					line = line.replace("patch-of ", "");
					Matcher m = Pattern.compile("\\d").matcher(line);
					int position = m.find() ? m.start() + 1 : 0;
					String name = line.substring(0, position - 2);
					boolean isNew = true;
					for (Territory element : list) {
						if (element.getName().equals(name)) {
							isNew = false;
							String[] coordsTemp = line.substring(position - 1).split(" ");
							int[] coords = new int[coordsTemp.length];
							for (int i = 0; i < coordsTemp.length; i++) {
								coords[i] = Integer.parseInt(coordsTemp[i]);
							}
							element.addPatch(coords);

						}
					}
					if (isNew) {
						String[] coordsTemp = line.substring(position - 1).split(" ");
						int[] coords = new int[coordsTemp.length];
						for (int i = 0; i < coordsTemp.length; i++) {
							coords[i] = Integer.parseInt(coordsTemp[i]);
						}
						Territory ter = new Territory(name, coords);
						list.add(ter);
					}
				} else if (line.startsWith("capital-of ")) {
					line = line.replace("capital-of ", "");
					Matcher m = Pattern.compile("\\d").matcher(line);
					int position = m.find() ? m.start() + 1 : 0;
					String name = line.substring(0, position - 2);
					String[] coordsTemp = line.substring(position - 1).split(" ");
					int[] coords = new int[coordsTemp.length];
					for (int i = 0; i < coordsTemp.length; i++) {
						coords[i] = Integer.parseInt(coordsTemp[i]);
					}
					for (Territory element : list) {
						if (element.getName().equals(name))
							element.setCapital(coords);
					}
				}

				else if (line.startsWith("neighbors-of ")) {
					line = line.replace("neighbors-of ", "");
					String[] split = line.split(" : ");
					String name = split[0];
					List<String> neighbours = Arrays.asList(split[1].split(" - "));
					ArrayList<Territory> tempNeighbour = new ArrayList<Territory>();
					Territory owner = null;
					for (Territory element : list) {
						if (element.getName().equals(name))
							owner = element;
						for (String item : neighbours) {
							if (element.getName().equals(item))
								tempNeighbour.add(element);
						}
					}
					owner.setNeighbours(tempNeighbour);
				}

				else if (line.startsWith("continent ")) {
					line = line.replace("continent ", "");
					Matcher m = Pattern.compile("\\d").matcher(line);
					int position = m.find() ? m.start() + 1 : 0;
					String name = line.substring(0, position - 2);
					line = line.replace(name + " ", "");
					String[] split = line.split(" : ");
					int bonus = Integer.parseInt(split[0]);
					Continent continent = new Continent(name, bonus);
					String[] territories = split[1].split(" - ");
					for (Territory element : list) {
						for (String ter : territories) {
							if (element.getName().equals(ter))
								continent.addTerritory(element);
						}
					}

					continentList.add(continent);
				}

			}
			System.out.println(list.size());
		} catch (

		FileNotFoundException e)

		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (

		IOException e)

		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public boolean overEdge(int x1, int x2) {
		if (x1 > x2) {
			int temp = x1;
			x1 = x2;
			x2 = temp;
		}
		if (x2 - x1 > 625)
			return true;
		else
			return false;
	}

}
