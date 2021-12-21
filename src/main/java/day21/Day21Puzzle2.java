package day21;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day21Puzzle2 {
	public record Configuration(int pos1, int pos2, int score1, int score2) {
	}
	
	static Map<Configuration, long[]> cache = new HashMap<>();
	
	static long[] calculateWins(int pos1, int pos2, int score1, int score2) {
		if (score1 >= 21) {
			return new long[] {1,0};
		}
		if (score2 >= 21) {
			return new long[] {0,1};
		}

		Configuration c = new Configuration(pos1, pos2, score1, score2);
		if (cache.containsKey(c)) {
			return cache.get(c);
		}

		long[] result = {0,0};
		for (int r1 : new int[]{1,2,3}) {
			for (int r2 : new int[]{1,2,3}) {
				for (int r3 : new int[]{1,2,3}) {
					int newPos1 = (pos1 + r1 + r2 + r3) % 10; 
					int newScore1 = score1 + newPos1 + 1;
			
					long[] l = calculateWins(pos2, newPos1, score2, newScore1);
					result[0] += l[1];
					result[1] += l[0];
				}
			}
		}
		
		cache.put(c, result);
		return result;
	}
	
	public static void main(String[] args) throws IOException, URISyntaxException {
		List<String> inputLines = Files.readAllLines(Paths.get(Day21Puzzle1.class.getResource("input.txt").toURI()));

		int pos1 = Integer.parseInt(inputLines.get(0).split(": ")[1])-1;
		int pos2 = Integer.parseInt(inputLines.get(1).split(": ")[1])-1;
		
		long[] wins = calculateWins(pos1, pos2, 0, 0);
		System.out.println("Wins 1: " + wins[0]);
		System.out.println("Wins 2: " + wins[1]);
	}

}
