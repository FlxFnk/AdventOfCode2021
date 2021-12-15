package day14;

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
import java.util.Queue;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import day11.Day11Puzzle1.Position;

public class Day14Puzzle1 {
	public static void main(String[] args) throws IOException, URISyntaxException {
		List<String> inputLines = Files
				.readAllLines(Paths.get(Day14Puzzle1.class.getResource("input.txt").toURI()));

		Map<String, Character> rules = new HashMap<>();
		for (int i = 2; i < inputLines.size(); i++) {
			String s[] = inputLines.get(i).trim().split(" -> ");
			rules.put(s[0], s[1].charAt(0));
		}

		System.out.println(rules);

		Map<Character, Long> letters = new HashMap<>();
		Map<String, Long> combinations = new HashMap<>();
		String input = inputLines.get(0);
		for (int i = 0; i < input.length(); i++) {
			char c = input.charAt(i);
			letters.merge(c, 1L, (o, n) -> o + 1);

			if (i + 1 < input.length()) {
				String combination = input.charAt(i) + "" + input.charAt(i + 1);
				combinations.merge(combination, 1L, (o, n) -> o + 1);
			}
		}

		for (int steps = 0; steps < 10; steps++) {
			Map<String, Long> newCombinations = new HashMap<>();

			for (String combination : combinations.keySet()) {
				long count = combinations.get(combination);
				Character letter = rules.get(combination);
				if (letter != null) {
					letters.merge(letter, 1L, (o, n) -> o + count);
				
					newCombinations.merge(combination.charAt(0) + "" + letter, count, (o, n) -> o + count);
					newCombinations.merge(letter + "" + combination.charAt(1), count, (o, n) -> o + count);
				}
			}
			
			combinations.clear();
			combinations.putAll(newCombinations);
			
			System.out.println(steps);
			System.out.println(letters);
			System.out.println(combinations);
		}

		long max = 0L;
		Character maxLetter = null;
		long min = Long.MAX_VALUE;
		Character minLetter = null;
		
		for (Map.Entry<Character, Long> e : letters.entrySet()) {
			if (e.getValue() > max) {
				maxLetter = e.getKey();
				max = e.getValue();
			}
			if (e.getValue() < min) {
				minLetter = e.getKey();
				min = e.getValue();
			}
		}
		
		System.out.println(maxLetter + ": " + max + "  " + minLetter + ": " + min + "  -> " + (max-min));
	}
}
