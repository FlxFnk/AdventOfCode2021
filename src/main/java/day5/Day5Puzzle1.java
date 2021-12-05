package day5;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Day5Puzzle1 {
	public record Line(int x0, int y0, int x1, int y1) {
	}

	public static void drawLine(Line line, int[][] map) {
		int dx = Math.abs(line.x1 - line.x0);
		int sx = line.x0 < line.x1 ? 1 : -1;
		int dy = -Math.abs(line.y1 - line.y0);
		int sy = line.y0 < line.y1 ? 1 : -1;
		int err = dx+dy;

		System.out.println("Drawing: " + line);
		int x = line.x0;
		int y = line.y0;
		while (true) {
			map[y][x]++;
			
			if ((x == line.x1) && (y == line.y1)) {
				break;
			}
			
			int e = 2 * err;
			if (e > dy) {
				err += dy; 
				x += sx;
			}
			if (e < dx) {
				err += dx;
				y += sy;
			}
		}
	}

	public static void main(String[] args) throws IOException, URISyntaxException {
		List<String> inputLines = Files
				.readAllLines(Paths.get(Day5Puzzle1.class.getResource("input.txt").toURI()));

		List<Line> lines = new ArrayList<>(inputLines.size());
		Pattern pattern = Pattern.compile("(\\d+),(\\d+) -> (\\d+),(\\d+)");
		int width = 0;
		int height = 0;
		for (String line : inputLines) {
			Matcher matcher = pattern.matcher(line);
			if (matcher.matches()) {
				int x0 = Integer.parseInt(matcher.group(1));
				int y0 = Integer.parseInt(matcher.group(2));
				int x1 = Integer.parseInt(matcher.group(3));
				int y1 = Integer.parseInt(matcher.group(4));
				lines.add(new Line(x0, y0, x1, y1));

				if (x0 > width) {
					width = x0;
				}
				if (x1 > width) {
					width = x1;
				}
				if (y0 > height) {
					height = y0;
				}
				if (y1 > height) {
					height = y1;
				}
			}
		}

		System.out.println("Width: " + width + ", Height: " + height);
		int[][] map = new int[height + 1][width + 1];
		lines.forEach(System.out::println);

		lines.stream() //
				.filter(l -> (l.x0 == l.x1) || (l.y0 == l.y1)) //
				.forEach(l -> drawLine(l, map));

		int result = 0;
		for (int row = 0; row < map.length; row++) {
			for (int col = 0; col < map[row].length; col++) {
				int v = map[row][col];
				if (v== 0) {
					System.out.print(" . ");
				} else {
					System.out.print(" " + v + " ");
					
					if (v > 1) {
						result++;
					}
				}
			}

			System.out.println();
		}
		
		System.out.println("Result: " + result);
	}
}
