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
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Allthoseterritories extends JPanel {

	private ArrayList<Territory> list;

	public static void main(String[] args) {

		JFrame frame = new JFrame("All Those Territories");

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setContentPane(new GameBoard(args[0]));
		frame.setSize(1250, 650);
		frame.setVisible(true);
	}

}
