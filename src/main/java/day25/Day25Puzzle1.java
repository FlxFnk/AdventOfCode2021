package day25;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import day25.Day25Puzzle1.Cucumber;
import day25.Day25Puzzle1.Position;

public class Day25Puzzle1 {
	enum Direction {
		RIGHT, DOWN
	};

	record Position(int x, int y) {
	}

	static class Cucumber {
		final Direction direction;
		boolean move = false;

		public Cucumber(Direction direction) {
			this.direction = direction;
		}
	}

	static class Seafloor {
		final int width;
		final int height;
		Map<Position, Cucumber> cucumbers = new HashMap<>();

		public Seafloor(int width, int height) {
			this.width = width;
			this.height = height;
		}

		Cucumber at(int x, int y) {
			int xPos = x % width;
			int yPos = y % height;

			return cucumbers.get(new Position(xPos, yPos));
		}
	}

	static void drawMap(Seafloor map) {
		for (int y = 0; y < map.height; y++) {
			for (int x = 0; x < map.width; x++) {
				Cucumber cucumber = map.cucumbers.get(new Position(x, y));
				if (cucumber == null) {
					System.out.print('.');
				} else {
					if (cucumber.direction == Direction.DOWN) {
						System.out.print('v');
					} else {
						System.out.print('>');
					}
				}
			}
			System.out.println();
		}

		System.out.println();
	}

	static boolean checkNeighbor(Seafloor map, Position position, Direction direction) {
		if (direction == Direction.RIGHT) {
			return map.cucumbers.get(new Position((position.x + 1) % map.width, position.y)) == null;
		} else {
			return map.cucumbers.get(new Position(position.x, (position.y + 1) % map.height)) == null;
		}
	}

	static int move(Seafloor map, Direction direction) {
		Map<Position, Cucumber> newPositions = new HashMap<>();
		Set<Position> removedPositions = new HashSet<>();
		for (Entry<Position, Cucumber> entry : map.cucumbers.entrySet()) {
			Position position = entry.getKey();
			Cucumber cucumber = entry.getValue();
			if (cucumber.direction == direction) {
				if (checkNeighbor(map, position, cucumber.direction)) {
					cucumber.move = true;
					
					if (cucumber.direction == Direction.RIGHT) {
						newPositions.put(new Position((position.x+1)%map.width, position.y), cucumber);
					} else {
						newPositions.put(new Position(position.x, (position.y+1) % map.height), cucumber);
					}
					removedPositions.add(position);
				}
			}
		}

		removedPositions.forEach(map.cucumbers::remove);
		map.cucumbers.putAll(newPositions);

		return newPositions.size();
	}

	public static void main(String[] args) throws IOException, URISyntaxException {
		List<String> inputLines = Files
				.readAllLines(Paths.get(Day25Puzzle1.class.getResource("input.txt").toURI()));

		int width = inputLines.get(0).length();
		int height = inputLines.size();

		Seafloor map = new Seafloor(width, height);
		for (int y = 0; y < inputLines.size(); y++) {
			String s = inputLines.get(y);
			for (int x = 0; x < s.length(); x++) {
				char c = s.charAt(x);
				if (c == 'v') {
					map.cucumbers.put(new Position(x, y), new Cucumber(Direction.DOWN));
				} else if (c == '>') {
					map.cucumbers.put(new Position(x, y), new Cucumber(Direction.RIGHT));
				}
			}
		}

		int moved = Integer.MAX_VALUE;
		int step = 1;
		while (moved > 0) {
			moved = 0;
			System.out.println("Step: " + step);
//			drawMap(map);

			moved = move(map, Direction.RIGHT);
			moved += move(map, Direction.DOWN);
			System.out.println("Moved: " + moved);
			step++;
		}
	}
}
