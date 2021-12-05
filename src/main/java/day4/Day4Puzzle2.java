package day4;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class Day4Puzzle2 {
	private static class Board {
		String numbers[][] = new String[5][5];
		boolean state[][] = new boolean[5][5];

		public void setRow(int row, String line) {
			numbers[row] = line.trim().split("\\s+");	
		}

		public void draw(String drawnNumber) {
			for (int row = 0; row < 5; row++) {
				for (int column = 0; column < 5; column++) {
					if (numbers[row][column].equals(drawnNumber)) {
						state[row][column] = true;
					}
				}
			}
		}

		public boolean isWin() {
			boolean win = false;
			for (int row = 0; row < 5; row++) {
				win = true;
				for (int column = 0; column < 5; column++) {
					win &= state[row][column];
				}

				if (win) {
					return true;
				}
			}
			for (int column = 0; column < 5; column++) {
				win = true;
				for (int row = 0; row < 5; row++) {
					win &= state[row][column];
				}

				if (win) {
					return true;
				}
			}

			return false;
		}
		
		public int unmarkedSum() {
			int sum = 0;
			for (int row = 0; row < 5; row++) {
				for (int column = 0; column < 5; column++) {
					if (!state[row][column]) {
						sum += Integer.parseInt(numbers[row][column]);
					}
				}
			}
			
			return sum;
		}
		
		public String toString() {
			StringBuffer b = new StringBuffer();
			for (int row = 0; row < 5; row++) {
				for (int column = 0; column < 5; column++) {
					b.append(String.format("%s%3s%s", state[row][column] ? "(" : " ", numbers[row][column], state[row][column] ? ")" : " "));
				}
				b.append("\n");
			}
			
			return b.toString();
		}
	}

	public static void main(String[] args) throws IOException, URISyntaxException {
		List<String> lines = Files.readAllLines(Paths.get(Day4Puzzle2.class.getResource("input.txt").toURI()));

		List<String> numbers = Arrays.asList(lines.get(0).split(","));
		lines.remove(0);
		lines.remove(0);
		
		List<Board> boards = new LinkedList<>();
		Board board = new Board();
		int row = 0;
		for (String line : lines) {
			if (line.trim().isEmpty()) {
				boards.add(board);
				row = 0;
				board = new Board();
			} else {
				board.setRow(row++, line);
			}
		}
		
		boards.forEach(System.out::println);
		
		for (String number : numbers) {
			boards.forEach(b -> b.draw(number));
			
			System.out.println("Number: " + number + " left:" + boards.size());
			List<Board> winBoards = boards.stream().filter(Board::isWin).collect(Collectors.toList());
			for (Board winBoard : winBoards) {
				int unmarkedSum = winBoard.unmarkedSum();
				System.out.println("Sum: " + unmarkedSum + ", last draw: " + number + ", Result: " + (Integer.parseInt(number)*unmarkedSum));
				boards.remove(winBoard);
			}
		}
	}
}
