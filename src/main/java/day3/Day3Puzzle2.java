package day3;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Day3Puzzle2 {
	private static int countOnes(List<String> lines, int index) {
		int count = 0;
		for (String line : lines) {
			if (line.charAt(index) == '1') {
				count++;
			}
		}

		return count;
	}

	public static void main(String[] args) throws IOException, URISyntaxException {
		List<String> lines = Files.readAllLines(Paths.get(Day3Puzzle1.class.getResource("input.txt").toURI()));

		List<String> oxygenLines = new ArrayList<>(lines);
		AtomicInteger index = new AtomicInteger(0);
		while (oxygenLines.size() > 1) {
			char v =  countOnes(oxygenLines, index.get()) >= oxygenLines.size() / 2.0f ? '1' : '0';
			oxygenLines.removeIf(l -> l.charAt(index.get()) != v);
			index.incrementAndGet();
		}

		List<String> co2Lines = new ArrayList<>(lines);
		index.set(0);
		while (co2Lines.size() > 1) {
			char v =  countOnes(co2Lines, index.get()) < co2Lines.size() / 2.0f ? '1' : '0';
			co2Lines.removeIf(l -> l.charAt(index.get()) != v);
			index.incrementAndGet();
		}

		int oxygenRating = Integer.parseInt(oxygenLines.get(0), 2);
		int co2Rating = Integer.parseInt(co2Lines.get(0), 2);
		System.out.println(oxygenLines + " -> " + oxygenRating);
		System.out.println(co2Lines + " -> " + co2Rating);
		System.out.println(oxygenRating *  co2Rating);
	}
}
