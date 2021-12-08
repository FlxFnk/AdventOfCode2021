package day8;

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
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import day5.Day5Puzzle1.Line;

public class Day8Puzzle2 {

	private static String sort(String s) {
		char[] a = s.toCharArray();
		Arrays.sort(a);
		return new String(a);
	}

	private static String and(String s1, String s2) {
		String result = "";
		for (char c = 'a'; c <= 'g'; c++) {
			if ((s1.indexOf(c) >= 0) && (s2.indexOf(c) >= 0)) {
				result += c;
			}
		}

		return result;
	}
	
	private static String or(String s1, String s2) {
		String result = "";
		for (char c = 'a'; c <= 'g'; c++) {
			if ((s1.indexOf(c) >= 0) || (s2.indexOf(c) >= 0)) {
				result += c;
			}
		}
		
		return result;
	}

	private record Line(List<String> signals, List<String> outputs) {
	}

	public static void main(String[] args) throws IOException, URISyntaxException {
		List<String> inputLines = Files
				.readAllLines(Paths.get(Day8Puzzle1.class.getResource("input.txt").toURI()));

		List<Line> lines = new LinkedList<>();
		for (String inputLine : inputLines) {
			String[] s = inputLine.split("\\|");
			List<String> signals = Arrays.stream(s[0].trim().split(" ")).map(Day8Puzzle2::sort)
					.collect(Collectors.toList());
			List<String> outputs = Arrays.stream(s[1].trim().split(" ")).map(Day8Puzzle2::sort)
					.collect(Collectors.toList());
			lines.add(new Line(signals, outputs));
		}

		System.out.println(lines);

		long sum = 0;
		for (Line line : lines) {
			String digit[] = new String[10];
			digit[8] = "abcdefg";

			digit[1] = line.signals.stream().filter(s -> s.length() == 2).findFirst().get();
			digit[4]= line.signals.stream().filter(s -> s.length() == 4).findFirst().get();
			digit[7]= line.signals.stream().filter(s -> s.length() == 3).findFirst().get();
			
			List<String> signalsFives = line.signals.stream().filter(s -> s.length() == 5).collect(Collectors.toList());
			List<String> signalsSixs = line.signals.stream().filter(s -> s.length() == 6).collect(Collectors.toList());
			
			// determine 3
			for (String signalsFive : signalsFives) {
				if (and(digit[1], signalsFive).length() == 2) {
					digit[3] = signalsFive;
					signalsFives.remove(signalsFive);
					break;
				}
			}
			
			if (signalsFives.size() == 3) {
				System.err.println("No 3");
			}
			
			// determine 9
			digit[9] = or(digit[3], digit[4]);
			signalsSixs.remove(digit[9]);
			if (line.signals.stream().noneMatch(s -> s.equals(digit[9]))) {
				System.err.println("No 9");
			}
			
			// determine 5
			for (String signalsFive : signalsFives) {
				if (or(digit[4], signalsFive).length() == 6) {
					digit[5] = signalsFive;
					signalsFives.remove(signalsFive);
					break;
				}
			}

			if (signalsFives.size() == 2) {
				System.err.println("No 5");
			}
			// get 2
			digit[2] = signalsFives.get(0);

			// determine 0 and 6
			for (String signalSix : signalsSixs) {
				if (and(signalSix, digit[1]).length() == 2) {
					digit[0] = signalSix;
					signalsSixs.remove(signalSix);
					break;
				}
			}
			if (and(signalsSixs.get(0), digit[1]).length() == 1) {
				digit[6] = signalsSixs.get(0);
			} else {
				System.err.println("No 6");
			}

			Map<String, String> digits = new HashMap<>();
			for (int i = 0; i < digit.length; i++) {
				digits.put(digit[i], String.valueOf(i));
			}
			
			int output = Integer.parseInt(line.outputs.stream().map(digits::get).collect(Collectors.joining()));
			
			System.out.println(output);
			System.out.println(Arrays.toString(digit));
			
			sum += output;
		}
		
		System.out.println("Sum: " + sum);
	}
}
