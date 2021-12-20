package day17;

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

public class Day17Puzzle1 {
	public static void main(String[] args) throws IOException, URISyntaxException {
		List<String> inputLines = Files
				.readAllLines(Paths.get(Day17Puzzle1.class.getResource("input_test.txt").toURI()));

		Pattern pattern = Pattern.compile("target area: x=(\\d+)\\.\\.(\\d+), y=(-\\d+)\\.\\.(-\\d+)");
		Matcher matcher = pattern.matcher(inputLines.get(0));
		if (!matcher.matches()) {
			System.err.println("Invalid input");
			System.exit(0);
		}

		int xmin = Integer.parseInt(matcher.group(1));
		int xmax = Integer.parseInt(matcher.group(2));
		int ymin = Integer.parseInt(matcher.group(4));
		int ymax = Integer.parseInt(matcher.group(3));

		System.out.println(xmin + " " + xmax + " " + ymin + " " + ymax);

		double initialX = Math.floor(-0.5 + Math.sqrt(0.25 + 2 * (xmin + 1)));
		double initialY = -(ymax + 1);

		System.out.println(initialX + " " + initialY);
		
		int x = 0;
		int vx = (int) initialX;
		int y  =0;
		int vy = (int) initialY;
		
		int yMax = Integer.MIN_VALUE;
		while (y > ymin) {
			System.out.println("x: " + x + "  y: " + y + "  vx: " + vx + "  vy:" + vy);
			x += vx;
			y += vy;
			
			vx = Math.max(0, vx-1);
			vy--;
			
			if (y > yMax) {
				yMax = y;
			}
		}
		System.out.println("x: " + x + "  y: " + y + "  vx: " + vx + "  vy:" + vy);
		System.out.println(yMax);
	}
}

