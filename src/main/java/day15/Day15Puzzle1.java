package day15;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IntSummaryStatistics;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import day11.Day11Puzzle1.Position;

public class Day15Puzzle1 {
	public static class Tile {
		final int x;
		final int y;
		final int level;
		boolean visited = false;
		int totalRisk = Integer.MAX_VALUE;
		Tile previous = null;

		public Tile(int x, int y, int level) {
			this.x = x;
			this.y = y;
			this.level = level;
		}

		@Override
		public String toString() {
			return "Tile [x=" + x + ", y=" + y + ", level=" + level + ", visited=" + visited + ", totalRisk="
					+ totalRisk + ", previous=" + previous + "]";
		}
	}

	static Tile map[][];

	public static void update(Tile t, int x, int y, Queue<Tile> tiles) {
		if ((y >= 0) && (y < map.length) && (x >= 0) && (x < map[y].length)) {
			Tile n = map[y][x];
			if (tiles.contains(n)) {
				int risk = t.totalRisk + n.level;
				if (risk < n.totalRisk) {
					n.totalRisk = risk;
					n.previous = t;
					tiles.remove(n);
					tiles.add(n);
					System.out.println(n);
				}
			}
		}
	}

	public static void main(String[] args) throws IOException, URISyntaxException {
		List<String> inputLines = Files
				.readAllLines(Paths.get(Day15Puzzle1.class.getResource("input.txt").toURI()));

		map = new Tile[inputLines.size()][inputLines.get(0).length()];
		for (int row = 0; row < inputLines.size(); row++) {
			for (int col = 0; col < inputLines.get(row).length(); col++) {
				int level = Integer.parseInt("" + inputLines.get(row).charAt(col));
				map[row][col] = new Tile(col, row, level);
			}
		}
		map[0][0].totalRisk = 0;

		Queue<Tile> tiles = new PriorityQueue<>((a, b) -> a.totalRisk - b.totalRisk);
		for (int row = 0; row < inputLines.size(); row++) {
			for (int col = 0; col < inputLines.get(row).length(); col++) {
				System.out.print(map[row][col].level);
				tiles.add(map[row][col]);
			}
			System.out.println();
		}
		System.out.println();

		while (!tiles.isEmpty()) {
			Tile t = tiles.poll();

			update(t, t.x, t.y - 1, tiles);
			update(t, t.x, t.y + 1, tiles);
			update(t, t.x - 1, t.y, tiles);
			update(t, t.x + 1, t.y, tiles);

			if ((t.y == map.length - 1) && (t.x == map[t.y].length - 1)) {
				break;
			}
		}

		System.out.println(map[map.length - 1][map[map.length - 1].length - 1].totalRisk);
	}
}
