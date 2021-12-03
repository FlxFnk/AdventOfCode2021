package day3;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Day1Puzzle1 {
	public static void main(String[] args) throws IOException, URISyntaxException {
		List<String> lines = Files.readAllLines(Paths.get(Day1Puzzle1.class.getResource("input.txt").toURI()));
		
		int ones[] = new int[lines.get(0).length()];
		Arrays.fill(ones, 0);
		
		for (String line : lines) {
			for (int i = 0; i < line.length(); i++) {
				if (line.charAt(i) == '1') {
					ones[i]++;
				}
			}
		}

		String gamma = "";
		String epsilon = "";
		for (int count : ones) {
			if (count > lines.size() / 2) {
				gamma += "1";
				epsilon += "0";
			} else {
				gamma += "0";
				epsilon += "1";
			}
		}
		
		int powerConsumption = Integer.parseInt(gamma, 2) * Integer.parseInt(epsilon, 2);
		
		System.out.println(gamma + " * " + epsilon + " = " + powerConsumption);
	}
}
