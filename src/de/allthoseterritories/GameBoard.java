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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JComponent;

public class GameBoard extends JComponent {
	public ArrayList<Territory> list;
	public String initMap;

	public GameBoard(String initMap) {
		this.initMap = "Maps" + File.separator + "world.map";
		list = new ArrayList<Territory>();
		parseFile();

	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setColor(Color.black);
		for (Territory element : list) {
			for (int i = 0; i < element.getPolyXCoords().size(); i++) {
				g.drawPolygon(element.getPolyXCoords().get(i), element.getPolyYCoords().get(i),
						element.getPolyXCoords().get(i).length);
			}
			g.setColor(Color.red);
			// g.drawOval(element.getCapitalX(), element.getCapitalY(), 3,
			// 3);

			g.drawString(element.getArmyValue(), element.getCapitalX(), element.getCapitalY());
			g.setColor(Color.black);
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
				}
				if (line.startsWith("capital-of ")) {
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

}
