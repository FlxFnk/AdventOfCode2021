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

import day11.Day11Puzzle1.Position;
import day13.Day13Puzzle1.Dot;
import day13.Day13Puzzle1.Fold;
import day15.Day15Puzzle1.Tile;
import day17.Day17Puzzle2.Vector;
import day5.Day5Puzzle1.Line;

public class Day17Puzzle2 {
	public record Vector(int x, int y) {
	}
	
	public record Rect(int x0, int y0, int x1, int y1) {
	}

	public static boolean validateShot(Vector v, Rect target) {
		int x = 0;
		int y = 0;
		int vx = v.x;
		int vy = v.y;
		
		while ((x < target.x1) && (y > target.y1)) {
			System.out.println("x: " + x + "  y: " + y + "  vx: " + vx + "  vy:" + vy);
			
			x+=vx;
			y+=vy;
			vx = Math.max(0, vx-1);
			vy--;
			
			if ((x >= target.x0) && (x <= target.x1) && (y <= target.y0) && (y >= target.y1)) {
				return true;
			}
		}
		System.out.println("x: " + x + "  y: " + y + "  vx: " + vx + "  vy:" + vy);
		
		return false;
	}

	public static void main(String[] args) throws IOException, URISyntaxException {
		List<String> inputLines = Files
				.readAllLines(Paths.get(Day17Puzzle1.class.getResource("input.txt").toURI()));

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
		
		Rect target = new Rect(xmin, ymin, xmax, ymax);

		System.out.println(xmin + " " + xmax + " " + ymin + " " + ymax);

		int vymax = -(ymax + 1);
		int vymin = ymax;
		int vxmin = (int) Math.floor(-0.5 + Math.sqrt(0.25 + 2 * (xmin + 1)));
		int vxmax = xmax; 
		
		Set<Vector> trajectories = new HashSet<>();
		for (int vy = vymin; vy <= vymax; vy++) {
			for (int vx = vxmin; vx <= vxmax; vx++) {
				Vector v = new Vector(vx, vy);
				if (validateShot(v, target)) {
					trajectories.add(v);
				}
			}
		}

		System.out.println(trajectories);
		System.out.println(trajectories.size());
	}
}
