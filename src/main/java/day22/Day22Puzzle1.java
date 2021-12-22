package day22;

import static java.lang.Math.max;
import static java.lang.Math.min;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

public class Day22Puzzle1 {
	record Cube(int x, int y, int z) {
	}

	record Rule(boolean on, int x1, int x2, int y1, int y2, int z1, int z2) {
		boolean contains(int x, int y, int z) {
			return ((x >= x1) && (x <= x2) && (y >= y1) && (y <= y2) && (z >= z1) && (z <= z2));
		}
	}

	public static void main(String[] args) throws IOException, URISyntaxException {
		List<String> inputLines = Files
				.readAllLines(Paths.get(Day22Puzzle1.class.getResource("input.txt").toURI()));

		List<Rule> rules = new LinkedList<>();

		Pattern pattern = Pattern
				.compile("(on|off) x=(-?\\d+)..(-?\\d+),y=(-?\\d+)..(-?\\d+),z=(-?\\d+)..(-?\\d+)");
		for (String line : inputLines) {
			Matcher matcher = pattern.matcher(line);
			if (matcher.matches()) {
				int x1 = Integer.parseInt(matcher.group(2));
				int x2 = Integer.parseInt(matcher.group(3));
				int y1 = Integer.parseInt(matcher.group(4));
				int y2 = Integer.parseInt(matcher.group(5));
				int z1 = Integer.parseInt(matcher.group(6));
				int z2 = Integer.parseInt(matcher.group(7));
				rules.add(new Rule(matcher.group(1).equals("on"), x1, x2, y1, y2, z1, z2));
			} else {
				System.out.println(line);
				System.exit(0);
			}
		}

		System.out.println(rules);

		Set<Cube> cubes = new HashSet<>();

		for (Rule rule : rules) {
			if ((rule.x1 < -50) || (rule.x2 > 50) || (rule.y1 < -50) || (rule.y2 > 50)) {
				System.out.println("ignoring: " + rule);
				continue;
			}

			System.out.println(rule);
			for (int z = rule.z1; z <= rule.z2; z++) {
				for (int y = rule.y1; y <= rule.y2; y++) {
					for (int x = rule.x1; x <= rule.x2; x++) {
						if (rule.on) {
							cubes.add(new Cube(x, y, z));
						} else {
							cubes.remove(new Cube(x, y, z));
						}
					}
				}
			}
		}

//		System.out.println(cubes);

		System.out.println(cubes.size());
	}
}
