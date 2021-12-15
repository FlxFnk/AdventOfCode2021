package day12;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
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
import day5.Day5Puzzle1.Line;

public class Day12Puzzle2 {
	public static void main(String[] args) throws IOException, URISyntaxException {
		List<String> inputLines = Files
				.readAllLines(Paths.get(Day12Puzzle1.class.getResource("input.txt").toURI()));

		Map<String, List<String>> caves = new HashMap<>();
		for (String line : inputLines) {
			String[] l = line.split("-");

			List<String> cave1 = caves.get(l[0]);
			if (cave1 == null) {
				cave1 = new LinkedList<>();
				caves.put(l[0], cave1);
			}
			cave1.add(l[1]);

			List<String> cave2 = caves.get(l[1]);
			if (cave2 == null) {
				cave2 = new LinkedList<>();
				caves.put(l[1], cave2);
			}

			cave2.add(l[0]);
		}

		System.out.println(caves);

		List<String> routes = findPaths("start", "start", caves);

		System.out.println(routes);
		System.out.println(routes.size());
	}

	private static boolean isBigCave(String cave) {
		boolean isUpperCase = true;
		for (char c : cave.toCharArray()) {
			isUpperCase &= Character.isUpperCase(c);
		}

		return isUpperCase;
	}

	private static boolean valid(String path) {
		Map<String, Integer> caveCount = new HashMap<>();
		String[] caves = path.split(",");
		for (String cave : caves) {
			if (!isBigCave(cave)) {
				Integer count = caveCount.get(cave);
				if (count == null) {
					count = 0;
				} else if (count == 2) {
					return false;
				}
				caveCount.put(cave, count + 1);
			}
		}

		return caveCount.values().stream().filter(c -> c == 2).count() <= 1;
	}

	private static List<String> findPaths(String currentCave, String currentPath, Map<String, List<String>> caves) {
//		System.out.println(currentCave + " - " + currentPath);

		if (currentCave.equals("end")) {
			return Collections.singletonList(currentPath);
		} else {
			List<String> paths = new LinkedList<>();
			List<String> exits = caves.get(currentCave);
			if (exits != null) {
				for (String exit : exits) {
					String nextPath = currentPath + "," + exit;
					if ((!exit.equals("start")) && ((isBigCave(exit)) || (valid(nextPath)))) {
						paths.addAll(findPaths(exit, nextPath, caves));
					}
				}
			}

			return paths;
		}
	}
}
