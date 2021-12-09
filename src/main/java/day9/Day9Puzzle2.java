package day9;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.IntSummaryStatistics;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Queue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import day5.Day5Puzzle1.Line;

public class Day9Puzzle2 {
	private static int getBasinSize(int y, int x, int map[][]) {
		int value = 1;
		map[y][x] = -1;
	
		if ((map[y-1][x] >= 0) && (map[y-1][x] < 9)) {
			value += getBasinSize(y-1, x, map);
		} 
		if ((map[y+1][x] >= 0) && (map[y+1][x] < 9)) {
			value += getBasinSize(y+1, x, map);
		} 
		if ((map[y][x-1] >= 0) && (map[y][x-1] < 9)) {
			value += getBasinSize(y, x-1, map);
		} 
		if ((map[y][x+1] >= 0) && (map[y][x+1] < 9)) {
			value += getBasinSize(y, x+1, map);
		}
		
		return value;
	}
	
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
			
		List<Integer> sizes = new LinkedList<>();
		for (int row = 1; row < map.length-1; row++) {
			for (int col = 1; col < map[row].length-1; col++) {
				int value = map[row][col];
				if ((value < map[row-1][col]) && (value < map[row+1][col]) && (value < map[row][col-1]) && (value < map[row][col+1])) {
					int size = getBasinSize(row, col, map); 
					System.out.println("Low point: " + col + "/" + row + " -> " + value + "  size: " + size);
					sizes.add(size);
				}
			}
		}
		
		Collections.sort(sizes);
		Collections.reverse(sizes);
		
		System.out.println(sizes.get(0) * sizes.get(1) * sizes.get(2));
	}
}
