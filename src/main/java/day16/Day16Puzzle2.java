package day16;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;
import java.util.stream.LongStream;

public class Day16Puzzle2 {
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
		final int typeId;

		public Packet(int version, int typeId) {
			this.version = version;
			this.typeId = typeId;
		}

		public int getVersion() {
			return version;
		}

		public int getTypeId() {
			return typeId;
		}

		public abstract long getValue();
	}

	public static class LiteralPacket extends Packet {
		final long value;

		public LiteralPacket(int version, int typeId, long value) {
			super(version, typeId);
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

		public OperatorPacket(int version, int typeId, int lengthTypeId, Integer lengthValue) {
			super(version, typeId);
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

		public long getValue() {
			return switch (getTypeId()) {
			case 0:
				yield subPackets.stream().mapToLong(Packet::getValue).sum();
			case 1:
				yield operatorMultiply(subPackets.stream().mapToLong(Packet::getValue));
			case 2:
				yield subPackets.stream().mapToLong(Packet::getValue).min().getAsLong();
			case 3:
				yield subPackets.stream().mapToLong(Packet::getValue).max().getAsLong();
			case 5:
				yield operatorGreaterThan(subPackets);
			case 6:
				yield operatorLessThan(subPackets);
			case 7:
				yield operatorEquals(subPackets);
			default:
				throw new IllegalArgumentException("Unknown typeId");
			};
		}

		private long operatorGreaterThan(List<Packet> subPackets) {
			if (subPackets.size() != 2) {
				throw new IllegalStateException("Too many subpackets for GreaterThan. (packet=" + this + ")");
			}

			long v0 = subPackets.get(0).getValue();
			long v1 = subPackets.get(1).getValue();

			return (v0 > v1) ? 1L : 0L;
		}

		private long operatorLessThan(List<Packet> subPackets) {
			if (subPackets.size() != 2) {
				throw new IllegalStateException("Too many subpackets for LessThan. (packet=" + this + ")");
			}
			
			long v0 = subPackets.get(0).getValue();
			long v1 = subPackets.get(1).getValue();
			
			return (v0 < v1) ? 1L : 0L;
		}
		
		private long operatorEquals(List<Packet> subPackets) {
			if (subPackets.size() != 2) {
				throw new IllegalStateException("Too many subpackets for Equals. (packet=" + this + ")");
			}
			
			long v0 = subPackets.get(0).getValue();
			long v1 = subPackets.get(1).getValue();
			
			return (v0 == v1) ? 1L : 0L;
		}
		
		private long operatorMultiply(LongStream s) {
			AtomicLong result = new AtomicLong(1);
			s.forEach(l -> result.set(result.get() * l));
			return result.get();
		}

		@Override
		public String toString() {
			return "OperatorPacket [version=" + version + ", lengthTypeId=" + lengthTypeId + ", lengthValue="
					+ lengthValue + ", subPackets=" + subPackets + "]";
		}

	}

	public static Packet parseLiteralPacket(int version, int typeId, StringBuffer b) {
		String digitBinary = "";
		while (b.available()) {
			String indicator = b.take(1);

			digitBinary += b.take(4);

			if (indicator.equals("0")) {
				break;
			}
		}

		long digit = Long.parseLong(digitBinary, 2);
		return new LiteralPacket(version, typeId, digit);
	}

	public static Packet parseOperatorPacket(int version, int typeId, StringBuffer b) {
		String lengthTypeId = b.take(1);

		if (lengthTypeId.equals("0")) {
			String totalLengthBinary = b.take(15);
			System.out.println(totalLengthBinary);
			OperatorPacket packet = new OperatorPacket(version, typeId, 0, Integer.parseInt(totalLengthBinary, 2));

			StringBuffer subBuffer = new StringBuffer(b.take(packet.getLengthValue()));
			while (subBuffer.available()) {
				Packet p = parsePacket(subBuffer);
				packet.getSubPackets().add(p);
			}
			return packet;
		} else {
			String numberOfPackagesBinary = b.take(11);
			System.out.println(numberOfPackagesBinary);
			OperatorPacket packet = new OperatorPacket(version, typeId, 1, Integer.parseInt(numberOfPackagesBinary, 2));
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
			return parseLiteralPacket(version, typeId, b);
		} else {
			return parseOperatorPacket(version, typeId, b);
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
//		String input = "C200B40A82";
//		String input = "04005AC33890";
//		String input = "880086C3E88112";
//		String input = "CE00C43D881120";
//		String input = "9C0141080250320F1802104A08";
		
		String binaryInput = makeBinary(input);

		System.out.println(input);
		System.out.println(binaryInput);
		Packet packet = parsePacket(new StringBuffer(binaryInput));

		System.out.println(packet);

		System.out.println(packet.getValue());
	}
}
