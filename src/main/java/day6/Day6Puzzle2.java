package day6;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import day5.Day5Puzzle1.Line;

public class Day6Puzzle2 {
	public static void main(String[] args) throws IOException, URISyntaxException {
		List<String> inputLines = Files.readAllLines(Paths.get(Day6Puzzle1.class.getResource("input.txt").toURI()));

		long[] fishes = new long[10];
		List<Integer> inputFishes = Arrays.stream(inputLines.get(0).split(",")).map(Integer::parseInt).collect(Collectors.toList());
		for (int f : inputFishes) {
			fishes[f]++;
		}

		for (int day = 0; day <= 256; day++) {
			long sum = 0;
			for (int i = 0; i < fishes.length; i++) {
				sum += fishes[i];
			}
			System.out.println("Day: " + day + "  Fishes: " + Arrays.toString(fishes) + "  Sum: " + sum);

			fishes[7] += fishes[0];
			fishes[9] += fishes[0];
			long[] newFishes = new long[10];
			for (int i = 1; i < fishes.length; i++) {
				newFishes[i-1] = fishes[i];
			}
			
			fishes = newFishes;
		}
	}
}
