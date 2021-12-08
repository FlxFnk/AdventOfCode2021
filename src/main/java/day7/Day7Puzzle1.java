package day7;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.IntSummaryStatistics;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Queue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day7Puzzle1 {
	public static void main(String[] args) throws IOException, URISyntaxException {
		List<String> inputLines = Files
				.readAllLines(Paths.get(Day7Puzzle1.class.getResource("input.txt").toURI()));

		List<Integer> crabs = Arrays.stream(inputLines.get(0).split(",")).map(Integer::parseInt)
				.collect(Collectors.toList());
		IntSummaryStatistics statistics = crabs.stream().mapToInt(Integer::intValue).summaryStatistics();

		int max = statistics.getMax();
		int min = statistics.getMin();

		int minFuel = Integer.MAX_VALUE;
		for (int i = min; i <= max; i++) {
			int y = i;
			int fuel =crabs.stream().mapToInt(Integer::intValue).map(p -> Math.abs(y - p)).sum(); 
			System.out.println("Position: " + i + "  Fuel: " + fuel);
			
			if (fuel < minFuel) {
				minFuel = fuel;
			}
		}
		
		System.out.println(minFuel);
	}
}
