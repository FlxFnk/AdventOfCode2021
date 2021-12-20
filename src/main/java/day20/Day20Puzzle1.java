package day20;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public class Day20Puzzle1 {
	static char getValue(int x, int y, boolean[][] map, boolean background) {
		if ((x < 0) || (y < 0) || (y > map.length - 1) || (x > map[y].length - 1)) {
			return background ? '1' : '0';
		} else {
			return map[y][x] ? '1' : '0';
		}
	}

	static boolean calc(int x, int y, boolean[][] map, boolean background, String lookup) {
		StringBuffer b = new StringBuffer();
		b.append(getValue(x - 1, y - 1, map, background));
		b.append(getValue(x, y - 1, map, background));
		b.append(getValue(x + 1, y - 1, map, background));
		b.append(getValue(x - 1, y, map, background));
		b.append(getValue(x, y, map, background));
		b.append(getValue(x + 1, y, map, background));
		b.append(getValue(x - 1, y + 1, map, background));
		b.append(getValue(x, y + 1, map, background));
		b.append(getValue(x + 1, y + 1, map, background));

		int index = Integer.parseInt(b.toString(), 2);
		return lookup.charAt(index) == '#';
	}

	static int print(boolean[][] map, boolean background) {
		int count = 0;
		for (int y = -2; y <= map.length + 1; y++) {
			for (int x = -2; x <= map[0].length + 1; x++) {
				if ((y < 0) || (x < 0) || (y > map.length - 1) || (x > map[y].length - 1)) {
					System.out.print(background ? '*' : '_');
				} else {
					if (map[y][x]) {
						count++;
					}
					System.out.print(map[y][x] ? '#' : '.');
				}
			}
			System.out.println();
		}

		return count;
	}

	public static void main(String[] args) throws IOException, URISyntaxException {
		List<String> inputLines = Files
				.readAllLines(Paths.get(Day20Puzzle1.class.getResource("input.txt").toURI()));

		String lookup = inputLines.get(0);

		int width = inputLines.get(2).length();
		int height = inputLines.size() - 2;

		boolean map[][] = new boolean[height + 10][width + 10];
		for (int y = 2; y < inputLines.size(); y++) {
			for (int x = 0; x < inputLines.get(y).length(); x++) {
				map[y + 3][x + 5] = inputLines.get(y).charAt(x) == '#';
			}
		}

		print(map, false);

		boolean background[][] = { { false } };
		for (int step = 0; step < 2; step++) {
			boolean newMap[][] = new boolean[map.length][map[0].length];
			for (int y = 0; y < map.length; y++) {
				for (int x = 0; x < map[y].length; x++) {
					newMap[y][x] = calc(x, y, map, background[0][0], lookup);
				}
			}

			background[0][0] = calc(0, 0, background, background[0][0], lookup);
			map = newMap;

			System.out.println("Lit: " + print(map, background[0][0]));
		}
	}
}
