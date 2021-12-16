package day16;

import java.io.IOException;
import java.io.StringReader;
import java.math.BigInteger;
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
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day16Puzzle1 {
	public static class StringBuffer {
		private final String s;
		private int position;

		public StringBuffer(String s) {
			this.s = s;
			position = 0;
		}

		public String take(int n) {
			int oldPosition = position;
			position = position + n;
			return s.substring(oldPosition, position);
		}

		public int takeInt(int n) {
			int oldPosition = position;
			position = position + n;
			return Integer.parseInt(s.substring(oldPosition, position));
		}

		public boolean available() {
			return position < s.length();
		}
	}

	public abstract static class Packet {
		final int version;

		public Packet(int version) {
			this.version = version;
		}

		public int getVersion() {
			return version;
		}
	}

	public static class LiteralPacket extends Packet {
		final long value;

		public LiteralPacket(int version, long value) {
			super(version);
			this.value = value;
		}

		public int getVersion() {
			return version;
		}

		public long getValue() {
			return value;
		}

		@Override
		public String toString() {
			return "LiteralPacket [version=" + version + ", value=" + value + "]";
		}
	}

	public static class OperatorPacket extends Packet {
		final int lengthTypeId;

		final Integer lengthValue;

		final List<Packet> subPackets = new LinkedList<>();

		public OperatorPacket(int version, int lengthTypeId, Integer lengthValue) {
			super(version);
			this.lengthTypeId = lengthTypeId;
			this.lengthValue = lengthValue;
		}

		public int getVersion() {
			return version;
		}

		public int getLengthTypeId() {
			return lengthTypeId;
		}

		public Integer getLengthValue() {
			return lengthValue;
		}

		public List<Packet> getSubPackets() {
			return subPackets;
		}

		@Override
		public String toString() {
			return "OperatorPacket [version=" + version + ", lengthTypeId=" + lengthTypeId + ", lengthValue="
					+ lengthValue + ", subPackets=" + subPackets + "]";
		}

	}

	public static Packet parseLiteralPacket(int version, StringBuffer b) {
		String digitBinary = "";
		while (b.available()) {
			String indicator = b.take(1);

			digitBinary += b.take(4);

			if (indicator.equals("0")) {
				break;
			}
		}

		long digit = Long.parseLong(digitBinary, 2);
		return new LiteralPacket(version, digit);
	}

	public static Packet parseOperatorPacket(int version, StringBuffer b) {
		String lengthTypeId = b.take(1);

		if (lengthTypeId.equals("0")) {
			String totalLengthBinary = b.take(15);
			System.out.println(totalLengthBinary);
			OperatorPacket packet = new OperatorPacket(version, 0, Integer.parseInt(totalLengthBinary, 2));

			StringBuffer subBuffer = new StringBuffer(b.take(packet.getLengthValue()));
			while (subBuffer.available()) {
				Packet p = parsePacket(subBuffer);
				packet.getSubPackets().add(p);
			}
			return packet;
		} else {
			String numberOfPackagesBinary = b.take(11);
			System.out.println(numberOfPackagesBinary);
			OperatorPacket packet = new OperatorPacket(version, 1, Integer.parseInt(numberOfPackagesBinary, 2));
			for (int i = 0; i < packet.getLengthValue(); i++) {
				Packet p = parsePacket(b);
				packet.getSubPackets().add(p);
			}
			return packet;
		}
	}

	public static Packet parsePacket(StringBuffer b) {
		String versionBinary = b.take(3);
		String typeIdBinary = b.take(3);

		int version = Integer.parseInt(versionBinary, 2);
		int typeId = Integer.parseInt(typeIdBinary, 2);
		System.out.println("version: " + version);
		System.out.println("typeId: " + typeId);

		if (typeId == 4) {
			return parseLiteralPacket(version, b);
		} else {
			return parseOperatorPacket(version, b);
		}
	}
	
	public static void visit(Packet packet, Consumer<Packet> c) {
		c.accept(packet);
		if (packet instanceof OperatorPacket o) {
			o.getSubPackets().forEach(p -> visit(p, c));
		}
	}

	public static String makeBinary(String hex) {
		String bits[] = { "0000", "0001", "0010", "0011", "0100", "0101", "0110", "0111", "1000", "1001", "1010",
				"1011", "1100", "1101", "1110", "1111" };

		StringBuilder b = new StringBuilder();
		for (char c : hex.toCharArray()) {
			b.append(bits[Integer.parseInt("" + c, 16)]);
		}

		return b.toString();
	}

	public static void main(String[] args) throws IOException, URISyntaxException {
		List<String> inputLines = Files.readAllLines(Paths.get(Day16Puzzle1.class.getResource("input.txt").toURI()));

		String input = inputLines.get(0);
//		String input = "D2FE28";
//		String input = "38006F45291200";
//		String input = "EE00D40C823060";
//		String input = "8A004A801A8002F478";
//		String input = "620080001611562C8802118E34";
//		String input = "C0015000016115A2E0802F182340";
//		String input = "A0016C880162017C3686B18A3D4780";
		String binaryInput = makeBinary(input);

		System.out.println(input);
		System.out.println(binaryInput);
		Packet packet = parsePacket(new StringBuffer(binaryInput));

		System.out.println(packet);
		
		AtomicInteger sum = new AtomicInteger();
		visit(packet, p -> sum.addAndGet(p.getVersion()));
		
		System.out.println(sum);
	}
}
