package day13;

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
import java.util.Queue;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import day11.Day11Puzzle1.Position;
import day13.Day13Puzzle1.Dot;
import day13.Day13Puzzle1.Fold;
import day5.Day5Puzzle1.Line;

public class Day13Puzzle2 {
	public record Dot(int x, int y) {
	}

	public record Fold(String direction, int position) {
	}

	public static void printDots(Collection<Dot> dots) {
		int width = dots.stream().mapToInt(Dot::x).max().getAsInt();
		int height = dots.stream().mapToInt(Dot::y).max().getAsInt();

		boolean map[][] = new boolean[height + 1][width + 1];
		for (Dot dot : dots) {
			map[dot.y][dot.x] = true;
		}

		for (int y = 0; y < map.length; y++) {
			for (int x = 0; x < map[y].length; x++) {
				System.out.print(map[y][x] ? '#' : '.');
			}
			System.out.println();
		}
		System.out.println();
	}

	public static void main(String[] args) throws IOException, URISyntaxException {
		List<String> inputLines = Files.readAllLines(Paths.get(Day13Puzzle1.class.getResource("input.txt").toURI()));

		Set<Dot> dots = new HashSet<>();
		int row = 0;
		for (String line : inputLines) {
			System.out.println(line);
			if (line.isEmpty()) {
				break;
			}
			String l[] = line.split(",");
			dots.add(new Dot(Integer.parseInt(l[0]), Integer.parseInt(l[1])));
		}

		List<Fold> folds = new LinkedList<>();
		Pattern pattern = Pattern.compile(".+([x,y])=(\\d+)");
		for (int i = row; i < inputLines.size(); i++) {
			Matcher matcher = pattern.matcher(inputLines.get(i));
			if (matcher.matches()) {
				folds.add(new Fold(matcher.group(1), Integer.parseInt(matcher.group(2))));
			}
		}

		System.out.println(dots);
		System.out.println(folds);

//		printDots(dots);

		int width = 0;
		int height = 0;
		for (Fold fold : folds) {
			Set<Dot> foldedDots = new HashSet<>();
			for (Dot dot : dots) {
				int x, y;
				if (fold.direction.equals("x")) {
					if (dot.x > fold.position) {
						x = fold.position - (dot.x - fold.position);
					} else {
						x = dot.x;
					}
					y = dot.y;
				} else {
					if (dot.y > fold.position) {
						y = fold.position - (dot.y - fold.position);
					} else {
						y = dot.y;
					}
					x = dot.x;
				}
				foldedDots.add(new Dot(x, y));

//				System.out.println(dot.x + "," + dot.y + " -> " + x + "," + y);
				if (x > width) {
					width = x;
				}
				if (y > height) {
					height = y;
				}
			}

//			printDots(foldedDots);
			dots = foldedDots;

			System.out.println(dots);
			System.out.println(dots.size());
		}
		
		printDots(dots);
	}
}
