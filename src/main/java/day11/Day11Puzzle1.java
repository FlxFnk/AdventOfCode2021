package day11;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.IntSummaryStatistics;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Queue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import day11.Day11Puzzle1.Position;

public class Day11Puzzle1 {
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

		long numberOfFlashes = 0;
		for (int steps = 0; steps < 100; steps++) {
			LinkedList<Position> flashes = new LinkedList<>();
			for (int row = 0; row < inputLines.size(); row++) {
				for (int col = 0; col < inputLines.get(row).length(); col++) {
					map[row][col][0]++;
					if (map[row][col][0] > 9) {
						map[row][col][1] = 1;
						flashes.add(new Position(row, col));
						numberOfFlashes++;
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

				numberOfFlashes += newFlashes.size();
				flashes.addAll(newFlashes);

			}

			for (int row = 0; row < inputLines.size(); row++) {
				for (int col = 0; col < inputLines.get(row).length(); col++) {
					if (map[row][col][0] > 9) {
						map[row][col][0] = 0;
						map[row][col][1] = 0;
					}
					System.out.print(map[row][col][0]);
				}
				System.out.println();
			}
			System.out.println();
		}
		
		System.out.println("Flashes: " + numberOfFlashes);

	}
}
