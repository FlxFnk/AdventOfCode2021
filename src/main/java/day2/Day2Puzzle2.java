package day2;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Day2Puzzle2 {
	public static void main(String[] args) throws IOException, URISyntaxException {
		List<String> lines = Files.readAllLines(Paths.get(Day2Puzzle2.class.getResource("input.txt").toURI()));

		int x = 0;
		int d = 0;
		for (String line : lines)  {
			String[] step = line.split(" ");
			int v = Integer.parseInt(step[1]);
			switch (step[0]) {
			case "forward":
				x += v;
				break;
			case "up":
				d -= v;
				break;
			case "down":
				d += v;
				break;
			}
		}
		
		System.out.println("x: " + x + ", d: " + d + ", result: " + (x * d));
	}
}
