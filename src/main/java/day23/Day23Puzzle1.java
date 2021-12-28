package day23;

import static java.lang.Math.max;
import static java.lang.Math.min;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day23Puzzle1 {
	static final int[] MOVEMENT_COSTS = { 1, 10, 100, 1000 };

	record Position(int x, int y) {
	}
	
	record Pod(int id, char letter, int x, int y, long usedEnergy) {
		Pod move(int nx, int ny) {
			return new Pod(id, letter, nx, ny,
					usedEnergy + (Math.abs(nx - x) + Math.abs(ny - y)) * MOVEMENT_COSTS[letter - 'A']);
		}
		
		boolean isFinished() {
			return y > 0 && x == ((letter - 'A') * 2 + 2);
		}
	}
	
	record Movement(int id, char letter, int x, int y) {
	}

	static char[][] createMap(Set<Pod> pods) {
		char map[][] = { "...........".toCharArray(), "  a b c d  ".toCharArray(), "  a b c d  ".toCharArray() };

		for (Pod pod : pods) {
			map[pod.y][pod.x] = pod.letter;
		}

		return map;
	}

	static void drawMap(char[][] map) {
		for (int y = 0; y < map.length; y++) {
			for (int x = 0; x < map[y].length; x++) {
				System.out.print(map[y][x]);
			}
			System.out.println();
		}
	}

	static char checkMap(int x, int y, int dx, int dy, char[][] map) {
		return map[y + dy][x + dx];
	}

	static boolean checkWay(int startX, int startY, int endX, int endY, char letter, char[][] map) {
//		System.out.println("  Checking " + startX + "," + startY + " -> " + endX + "," + endY);
		int x = startX;
		int y = startY;

		// first move up
		while (y > endY) {
			char c = checkMap(x, y, 0, -1, map);
			if ((Character.isLetter(c) && (Character.isUpperCase(c)))) {
				return false;
			}
			y--;
		}

		// move left/right
		int dx = (int) Math.signum(endX - x);
		while (x != endX) {
			char c = checkMap(x, y, dx, 0, map);
			if (Character.isLetter(c)) {
				return false;
			}

			x += dx;
		}

		// move down
		while (y < endY) {
			char c = checkMap(x, y, 0, 1, map);
			if (Character.isLetter(c)) {
				if (Character.isUpperCase(c)) {
					return false;
				} else if (Character.toUpperCase(c) != letter) {
					return false;
				}
			}
			y++;
		}

		return true;
	}

	static Set<Position> getAvailablePositions(Pod pod, Set<Pod> pods) {
		int targetPositions[][] = { { 0, 0 }, { 1, 0 }, { 2, 1 }, { 2, 2 }, { 3, 0 }, { 4, 1 }, { 4, 2 }, { 5, 0 },
				{ 6, 1 }, { 6, 2 }, { 7, 0 }, { 8, 1 }, { 8, 2 }, { 9, 0 }, { 10, 0 } };

		char[][] map = createMap(pods);

		Set<Position> positions = new HashSet<>();
		for (int[] position : targetPositions) {
			// no move
			if ((position[0] == pod.x) && (position[1] == pod.y)) {
				continue;
			}

			// if in hallway only move out
			if ((pod.y == 0) && (position[1] == 0)) {
				continue;
			}
			
			// if in room only move into hallway
			if ((pod.y > 0) && (position[1] > 0)) {
				continue;
			}

			Position p = new Position(position[0], position[1]);
			if (checkWay(pod.x, pod.y, p.x, p.y, pod.letter, map)) {
				positions.add(p);
			}
		}

		return positions;
	}

	static Set<Pod> movePod(Pod pod, int newX, int newY, Set<Pod> pods) {
		Set<Pod> newPods = new HashSet<>(pods);
		newPods.remove(pod);
		newPods.add(pod.move(newX, newY));
		return newPods;
	}
	
	static long calculateEnergy(Set<Pod> pods) {
		return pods.stream().mapToLong(Pod::usedEnergy).sum();
	}
	
	static Set<Pod> iterate(Set<Pod> initialPods) {
		Queue<Set<Pod>> q = new LinkedList<>();
		q.add(initialPods);

		Map<Set<Pod>, Long> finished = new HashMap<>();
		Set<Set<Pod>> visited = new HashSet<>();
		while (!q.isEmpty()) {
			System.out.println(q.size());
			System.out.println(visited.size());
			Set<Pod> pods = q.poll();
			

			if (pods.stream().allMatch(Pod::isFinished)) {
				System.out.println(pods);
				System.exit(0);
				
				finished.put(pods, calculateEnergy(pods));
			}
			
			drawMap(createMap(pods));
			
			for (Pod pod : pods) {
				System.out.println("Pod: " + pod.letter + pod.id);
				Set<Position> positions = getAvailablePositions(pod, pods);
				System.out.println("Positions for " + pod.letter + pod.id + ": " + positions);
				if (!positions.isEmpty()) {
					for (Position position : positions) {
//						System.out.println("Moving " + pod.letter + pod.id + " to " + position.x + "," + position.y);
						Set<Pod> movedPods = movePod(pod, position.x, position.y, pods);
						if (!visited.contains(movedPods)) {
							q.add(movedPods);
							visited.add(movedPods);
						}
					}
				} 
			}
			
		}
		
		return null;
	}

	public static void main(String[] args) throws IOException, URISyntaxException {
		List<String> inputLines = Files
				.readAllLines(Paths.get(Day23Puzzle1.class.getResource("input_test.txt").toURI()));

		Set<Pod> pods = new HashSet<>();
		int podId = 0;
		for (int y = 0; y < inputLines.size(); y++) {
			String s = inputLines.get(y);
			for (int x = 0; x < s.length(); x++) {
				if (Character.isLetter(s.charAt(x))) {
					pods.add(new Pod(podId++, s.charAt(x), x - 1, y - 1, 0));
				}
			}
		}

		System.out.println(pods);
		
		System.out.println(iterate(pods));
	}
}
