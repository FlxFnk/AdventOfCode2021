package day10;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.IntSummaryStatistics;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Queue;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import day10.Day10Puzzle1.Bracket;
import day5.Day5Puzzle1.Line;

public class Day10Puzzle2 {
	public record Bracket(char open, char close, int points) {
	}

	public static void main(String[] args) throws IOException, URISyntaxException {
		List<String> inputLines = Files.readAllLines(Paths.get(Day10Puzzle1.class.getResource("input.txt").toURI()));

		List<Long> scores = new LinkedList<>();
		Map<Character, Bracket> brackets = new HashMap<>();
		brackets.put(')', new Bracket('(', ')', 3));
		brackets.put(']', new Bracket('[', ']', 57));
		brackets.put('}', new Bracket('{', '}', 1197));
		brackets.put('>', new Bracket('<', '>', 25137));
		Map<Character, Bracket> brackets2 = new HashMap<>();
		brackets2.put('(', new Bracket('(', ')', 1));
		brackets2.put('[', new Bracket('[', ']', 2));
		brackets2.put('{', new Bracket('{', '}', 3));
		brackets2.put('<', new Bracket('<', '>', 4));

		String openingBrackets = "([{<";
		for (String line : inputLines) {
			Stack<Character> stack = new Stack<>();

			boolean corrupted = false;
			for (char c : line.toCharArray()) {
				if (openingBrackets.contains("" + c)) {
					stack.add(c);
				} else {
					Bracket closingBracket = brackets.get(c);
					char lastOpenBracket = stack.pop();

					if (closingBracket.open != lastOpenBracket) {
						corrupted = true;
						break;
					}
				}
			}
			
			if (!corrupted) {
				long score = 0;
				while (! stack.isEmpty()) {
					char c = stack.pop();
					score = score * 5 + brackets2.get(c).points;
				}
				
				System.out.println(score);
				scores.add(score);
			}
		}

		Collections.sort(scores);
		System.out.println(scores);
		System.out.println(scores.get(scores.size()/2));
	}
}
