package day10;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
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
import java.util.stream.IntStream;

public class Day10Puzzle1 {
	public record Bracket(char open, char close, int points) {}
	
	public static void main(String[] args) throws IOException, URISyntaxException {
		List<String> inputLines = Files
				.readAllLines(Paths.get(Day10Puzzle1.class.getResource("input.txt").toURI()));

		int score = 0;
		Map<Character, Bracket> brackets = new HashMap<>();
		brackets.put(')', new Bracket('(', ')', 3));
		brackets.put(']', new Bracket('[', ']', 57));
		brackets.put('}', new Bracket('{', '}', 1197));
		brackets.put('>', new Bracket('<', '>', 25137));
		
		String openingBrackets = "([{<";
		for (String line : inputLines) {
			Stack<Character> stack = new Stack<>();
			
			for (char c : line.toCharArray()) {
				if (openingBrackets.contains("" + c)) {
					stack.add(c);
				} else {
					Bracket closingBracket = brackets.get(c);
					char lastOpenBracket = stack.pop();
					
					if (closingBracket.open != lastOpenBracket) {
						score += closingBracket.points;
						break;
					}
				}
			}
		}
		
		System.out.println(score);
	}
}
