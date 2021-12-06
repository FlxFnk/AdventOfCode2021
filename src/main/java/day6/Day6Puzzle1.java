package day6;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Queue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Day6Puzzle1 {
	public static void main(String[] args) throws IOException, URISyntaxException {
		List<String> inputLines = Files.readAllLines(Paths.get(Day6Puzzle1.class.getResource("input.txt").toURI()));
		
		Queue<Integer> fishes = new LinkedList<>();
		Arrays.stream(inputLines.get(0).split(",")).map(Integer::parseInt).forEach(fishes::add);
		
		for (int i = 0; i < 80; i++) {
			Queue<Integer> newFishes = new LinkedList<>();
			
			while (!fishes.isEmpty()) {
				int fish = fishes.poll() - 1;
				
				if (fish < 0) {
					newFishes.add(6);
					newFishes.add(8);
				} else {
					newFishes.add(fish);
				}
			}
			
			fishes = newFishes;
//			System.out.println(fishes);
		}
		
		System.out.println(fishes.size());
	}
}
