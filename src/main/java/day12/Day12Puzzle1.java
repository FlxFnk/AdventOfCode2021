package day12;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
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
import java.util.stream.IntStream;

import day11.Day11Puzzle1.Position;

public class Day12Puzzle1 {
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

			if (!((l[0].equals("start") || (l[1].equals("end"))))) {
				List<String> cave2 = caves.get(l[1]);
				if (cave2 == null) {
					cave2 = new LinkedList<>();
					caves.put(l[1], cave2);
				}

				cave2.add(l[0]);
			}
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

	private static List<String> findPaths(String currentCave, String currentPath, Map<String, List<String>> caves) {
		System.out.println(currentCave + " - " + currentPath);

		if (currentCave.equals("end")) {
			return Collections.singletonList(currentPath);
		} else {
			List<String> paths = new LinkedList<>();
			List<String> exits = caves.get(currentCave);
			if (exits != null) {
				for (String exit : exits) {
					if ((isBigCave(exit)) || (!currentPath.contains(exit))) {
						paths.addAll(findPaths(exit, currentPath + "," + exit, caves));
					}
				}
			}

			return paths;
		}
	}

}
