package day1;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class Day1Puzzle1 {
	public static void main(String[] args) throws IOException, URISyntaxException {
		List<String> lines = Files.readAllLines(Paths.get(Day1Puzzle1.class.getResource("input.txt").toURI()));
		List<Integer> depths = lines.stream().map(Integer::parseInt).collect(Collectors.toList());
		
		int lastDepth = depths.get(0);
		int count = 0;
		for (int depth : depths) {
			if (depth > lastDepth) {
				count++;
			}
			lastDepth = depth;
		}
		
		System.out.println(count);
	}

	
}
