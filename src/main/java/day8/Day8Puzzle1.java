package day8;

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

public class Day8Puzzle1 {
	private record Line(String[] signals, String[] outputs) {
	}
	
	public static void main(String[] args) throws IOException, URISyntaxException {
		List<String> inputLines = Files
				.readAllLines(Paths.get(Day8Puzzle1.class.getResource("input.txt").toURI()));

		List<Line> lines = new LinkedList<>();
		for (String inputLine : inputLines) {
			String[] s = inputLine.split("\\|");
			String[] signals = s[0].trim().split(" ");
			String[] outputs = s[1].trim().split(" ");
			lines.add(new Line(signals, outputs));
		}
		
		int count = 0;
		for (Line line : lines) {
			for (int i = 0; i < line.outputs.length; i++) {
				int l = line.outputs[i].length();
				if ((l == 2) || (l == 4) || (l == 3) || (l == 7)) {
					count++;
				}
			}
		}
		
		System.out.println(count);
	}
}
