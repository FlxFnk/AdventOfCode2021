package day9;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.IntSummaryStatistics;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Queue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day9Puzzle1 {
	public static void main(String[] args) throws IOException, URISyntaxException {
		List<String> inputLines = Files
				.readAllLines(Paths.get(Day9Puzzle1.class.getResource("input.txt").toURI()));

		int map[][] = new int[inputLines.size()+2][inputLines.get(0).length()+2];
		
		Arrays.fill(map[0], 10);
		Arrays.fill(map[map.length-1], 10);
		for (int row = 0; row < inputLines.size(); row++) {
			Arrays.fill(map[row+1], 10);
			for (int col = 0; col < inputLines.get(row).length(); col++) {
				map[row+1][col+1] = Integer.parseInt("" + inputLines.get(row).charAt(col));
			}
		}
			
		int sum = 0;
		for (int row = 1; row < map.length-1; row++) {
			for (int col = 1; col < map[row].length-1; col++) {
				int value = map[row][col];
				if ((value < map[row-1][col]) && (value < map[row+1][col]) && (value < map[row][col-1]) && (value < map[row][col+1])) {
					System.out.println("Low point: " + col + "/" + row + " -> " + value);
					sum += value+1;
				}
			}
		}
		
		System.out.println(sum);
	}
}
