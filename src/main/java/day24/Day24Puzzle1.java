package day24;

import static java.lang.Math.max;
import static java.lang.Math.min;

import java.io.IOException;
import java.math.BigInteger;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day24Puzzle1 {
	record Instruction(String command, String param1, String param2) {
	}

	static BigInteger valueOf(String s, Map<String, BigInteger> variables) {
		try {
			int v = Integer.parseInt(s);
			return BigInteger.valueOf(v);
		} catch (NumberFormatException e) {
			return variables.get(s);
		}
	}

	static void inp(Instruction instruction, Map<String, BigInteger> variables, Integer input) {
		variables.put(instruction.param1, BigInteger.valueOf(input));
	}

	static void add(Instruction instruction, Map<String, BigInteger> variables) {
		BigInteger operant1 = variables.get(instruction.param1);
		BigInteger operant2 = valueOf(instruction.param2, variables);
		BigInteger result = operant1.add(operant2);

		variables.put(instruction.param1, result);
	}

	static void mul(Instruction instruction, Map<String, BigInteger> variables) {
		BigInteger operant1 = variables.get(instruction.param1);
		BigInteger operant2 = valueOf(instruction.param2, variables);
		BigInteger result = operant1.multiply(operant2);

		variables.put(instruction.param1, result);
	}

	static void div(Instruction instruction, Map<String, BigInteger> variables) {
		BigInteger operant1 = variables.get(instruction.param1);
		BigInteger operant2 = valueOf(instruction.param2, variables);
		BigInteger result = operant1.divide(operant2);

		variables.put(instruction.param1, result);
	}

	static void mod(Instruction instruction, Map<String, BigInteger> variables) {
		BigInteger operant1 = variables.get(instruction.param1);
		BigInteger operant2 = valueOf(instruction.param2, variables);
		BigInteger result = operant1.mod(operant2);

		variables.put(instruction.param1, result);
	}

	static void eql(Instruction instruction, Map<String, BigInteger> variables) {
		BigInteger operant1 = variables.get(instruction.param1);
		BigInteger operant2 = valueOf(instruction.param2, variables);
		BigInteger result = operant1.equals(operant2) ? BigInteger.ONE : BigInteger.ZERO;

		variables.put(instruction.param1, result);
	}

	static Map<String, BigInteger> run(int programNumber, int input, int z) throws IOException, URISyntaxException {
//		List<String> inputLines = Files
//				.readAllLines(Paths.get(Day24Puzzle1.class.getResource("input_test" + programNumber + ".txt").toURI()));
		List<String> inputLines = Files
				.readAllLines(Paths.get(Day24Puzzle1.class.getResource("input.txt").toURI()));

		List<Instruction> instructions = new LinkedList<>();
		for (String line : inputLines) {
			String s[] = line.split(" ");
			String cmd = s[0];
			String param1 = s[1];
			String param2 = s.length > 2 ? s[2] : null;
			instructions.add(new Instruction(cmd, param1, param2));
		}

		Map<String, BigInteger> variables = new HashMap<>();
		variables.put("w", BigInteger.ZERO);
		variables.put("x", BigInteger.ZERO);
		variables.put("y", BigInteger.ZERO);
		variables.put("z", BigInteger.valueOf(z));

		for (Instruction instruction : instructions) {
//			System.out.println(variables);
//			System.out.println(instruction);
			switch (instruction.command) {
			case "inp":
//				System.out.println("  " + variables);
				inp(instruction, variables, input);
				break;
			case "add":
				add(instruction, variables);
				break;
			case "mul":
				mul(instruction, variables);
				break;
			case "div":
				div(instruction, variables);
				break;
			case "mod":
				mod(instruction, variables);
				break;
			case "eql":
				eql(instruction, variables);
				break;
			default:
				throw new IllegalArgumentException("Unknown command: " + instruction);
			}
		}

		return variables;
	}

	public static void main(String[] args) throws IOException, URISyntaxException {
		for (int d = 0; d <= 0; d++) {
//			for (int z = 6; z < 15; z++) {
//				System.out.println(+ d + " - " + z);
				Map<String, BigInteger> vars = run(2, 9, 0);
				System.out.println(vars);
//			}
		}

	}
}
