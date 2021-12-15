package day11;

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

import day11.Day11Puzzle1.Position;
import day5.Day5Puzzle1.Line;

public class Day11Puzzle2 {
	public record Position(int row, int col) {
	}

	public static List<Position> inc(int row, int col, int[][][] map) {
		List<Position> newFlashes = new LinkedList<>();
		if ((row >= 0) && (col >= 0) && (row < map.length) && (col < map[row].length)) {
			map[row][col][0]++;
			if ((map[row][col][0] > 9) && (map[row][col][1] == 0)) {
				newFlashes.add(new Position(row, col));
				map[row][col][1] = 1;
			}
		}

		return newFlashes;
	}

	public static void main(String[] args) throws IOException, URISyntaxException {
		List<String> inputLines = Files
				.readAllLines(Paths.get(Day11Puzzle1.class.getResource("input.txt").toURI()));

		int map[][][] = new int[inputLines.size()][inputLines.get(0).length()][2];
		for (int row = 0; row < inputLines.size(); row++) {
			for (int col = 0; col < inputLines.get(row).length(); col++) {
				map[row][col][0] = Integer.parseInt("" + inputLines.get(row).charAt(col));
				map[row][col][1] = 0;
			}
		}

		long step = 0;
		while (true) {
			step++;
			
			System.out.println("Step: " + step);
			LinkedList<Position> flashes = new LinkedList<>();
			for (int row = 0; row < inputLines.size(); row++) {
				for (int col = 0; col < inputLines.get(row).length(); col++) {
					map[row][col][0]++;
					if (map[row][col][0] > 9) {
						map[row][col][1] = 1;
						flashes.add(new Position(row, col));
					}
				}
			}

			while (!flashes.isEmpty()) {
				Position flash = flashes.poll();

				List<Position> newFlashes = new LinkedList<>();
				newFlashes.addAll(inc(flash.row - 1, flash.col - 1, map));
				newFlashes.addAll(inc(flash.row - 1, flash.col, map));
				newFlashes.addAll(inc(flash.row - 1, flash.col + 1, map));
				newFlashes.addAll(inc(flash.row, flash.col - 1, map));
				newFlashes.addAll(inc(flash.row, flash.col + 1, map));
				newFlashes.addAll(inc(flash.row + 1, flash.col - 1, map));
				newFlashes.addAll(inc(flash.row + 1, flash.col, map));
				newFlashes.addAll(inc(flash.row + 1, flash.col + 1, map));

				if ((newFlashes.isEmpty()) && (flashes.isEmpty())) {
					break;
				}

				flashes.addAll(newFlashes);

			}

			boolean allFlash = true;
			for (int row = 0; row < inputLines.size(); row++) {
				for (int col = 0; col < inputLines.get(row).length(); col++) {
					if (map[row][col][0] > 9) {
						map[row][col][0] = 0;
						map[row][col][1] = 0;
					} else {
						allFlash = false;
					}
					System.out.print(map[row][col][0]);
				}
				System.out.println();
			}
			System.out.println();
			
			if (allFlash) {
				break;
			}
		}
		
		System.out.println("Step: " + step);
	}
}
