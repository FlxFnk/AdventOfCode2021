package day21;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

public class Day21Puzzle1 {
	public static void main(String[] args) throws IOException, URISyntaxException {
		List<String> inputLines = Files
				.readAllLines(Paths.get(Day21Puzzle1.class.getResource("input.txt").toURI()));

		int score[] = new int[2];
		int position[] = new int[2];
		position[0] = Integer.parseInt(inputLines.get(0).split(": ")[1]);
		position[1] = Integer.parseInt(inputLines.get(1).split(": ")[1]);

		int currentPlayer = 0;
		int die = 1;

		while ((score[0] < 1000) && (score[1] < 1000)) {
			int roll = die++ + die++ + die++;
			position[currentPlayer] = (position[currentPlayer] + roll-1) % 10 + 1;
			score[currentPlayer] += position[currentPlayer];
			System.out.println("Player " + (currentPlayer + 1) + ": " + score[currentPlayer]);
			currentPlayer = (currentPlayer + 1) % 2;
		}

		System.out.println("Player1: " + score[0]);
		System.out.println("Player2: " + score[1]);
		
		System.out.println("Rolled: " + (die-1));
		
		System.out.println("Result: " + (Math.min(score[0], score[1])*(die-1)));
	}
}
