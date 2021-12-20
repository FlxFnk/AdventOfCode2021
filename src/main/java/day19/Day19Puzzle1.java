package day19;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import day19.Day19Puzzle1.Scanner.MatchResult;

public class Day19Puzzle1 {

	public static class Vector {

		final int x;
		final int y;
		final int z;

		public Vector(int x, int y, int z) {
			this.x = x;
			this.y = y;
			this.z = z;
		}

		Vector rotateXCW() {
			return new Vector(x, -z, y);
		}

		Vector rotateXCCW() {
			return new Vector(x, z, -y);
		}

		Vector rotateYCW() {
			return new Vector(-z, y, x);
		}

		Vector rotateYCCW() {
			return new Vector(z, y, -x);
		}

		Vector rotateZCW() {
			return new Vector(y, -x, z);
		}

		Vector rotateZCCW() {
			return new Vector(-y, x, z);
		}

		Vector translate(Vector v) {
			return new Vector(x + v.x, y + v.y, z + v.z);
		}

		Vector difference(Vector v) {
			return new Vector(x - v.x, y - v.y, z - v.z);
		}

		long distance(Vector v) {
			return (v.x - x) * (v.x - x) + (v.y - y) * (v.y - y) + (v.z - z) * (v.z - z);
		}

		@Override
		public String toString() {
			return "Vector [x=" + x + ", y=" + y + ", z=" + z + "]";
		}

		@Override
		public int hashCode() {
			return Objects.hash(x, y, z);
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Vector other = (Vector) obj;
			return x == other.x && y == other.y && z == other.z;
		}
	}

	public static class Scanner {
		record MatchResult(int rx, int ry, int rz, Vector t, int numberOfMatches, Set<Vector> beacons) {
		}

		Set<Vector> beacons = new HashSet<>();
		final int number;

		public Scanner(int number) {
			this.number = number;
		}
		
		public int getNumber() {
			return number;
		}

		@Override
		public String toString() {
			return "Scanner [number=" + number + ", beacons=" + beacons + "]";
		}

		Set<Vector> rotateBeacons(int rx, int ry, int rz, Set<Vector> beacons) {
			Set<Vector> rotatedBeacons = new HashSet<>();
			for (Vector b : beacons) {
				Vector v = b;
				for (int i = 0; i < rx; i++) {
					v = v.rotateXCW();
				}
				for (int i = 0; i < ry; i++) {
					v = v.rotateYCW();
				}
				for (int i = 0; i < rz; i++) {
					v = v.rotateZCW();
				}
				rotatedBeacons.add(v);
			}

			return rotatedBeacons;
		}

		Set<Vector> translateBeacons(Vector t, Set<Vector> beacons) {
			return beacons.stream().map(b -> b.translate(t)).collect(Collectors.toSet());

		}

		MatchResult getNumberOfMatches(Set<Vector> otherBeacons, int rx, int ry, int rz) {
			MatchResult bestMatch = new MatchResult(rx, ry, rz, new Vector(0, 0, 0), 0, Collections.emptySet());

			Vector n = new Vector(1, 1, 1);
			Set<Vector> rn = rotateBeacons(rx, ry, rz, Collections.singleton(n));
			Set<Vector> rotatedBeacons = rotateBeacons(rx, ry, rz, otherBeacons);
			for (Vector beacon : beacons) {
				for (Vector baseBeacon : rotatedBeacons) {
					Vector d = beacon.difference(baseBeacon);
					Set<Vector> translatedBeacons = translateBeacons(d, rotatedBeacons);
					
					long numberOfMatches = translatedBeacons.stream().filter(beacons::contains).count(); 

					if (numberOfMatches > bestMatch.numberOfMatches) {
						bestMatch = new MatchResult(rx, ry, rz, d, (int) numberOfMatches, translatedBeacons);
					}
				}
			}

			return bestMatch;
		}

		MatchResult matches(Scanner other) {
			int[][] rotations = { 
					{ 0, 0, 0 }, { 0, 0, 1 }, { 0, 0, 2 }, { 0, 0, 3 }, 
					{ 0, 1, 0 }, { 0, 1, 1 }, { 0, 1, 2 }, { 0, 1, 3 }, 
					{ 0, 2, 0 }, { 0, 2, 1 }, { 0, 2, 2 }, { 0, 2, 3 }, 
					{ 0, 3, 0 }, { 0, 3, 1 }, { 0, 3, 2 }, { 0, 3, 3 }, 
					{ 1, 0, 0 }, { 1, 0, 1 }, { 1, 0, 2 }, { 1, 0, 3 },
					{ 3, 0, 0 }, { 3, 0, 1 }, { 3, 0, 2 }, { 3, 0, 3 } };

			for (int i = 0; i < rotations.length; i++) {
				int rx = rotations[i][0];
				int ry = rotations[i][1];
				int rz = rotations[i][2];
				MatchResult matchResult = getNumberOfMatches(other.beacons, rx, ry, rz);
//				System.out.println("Trying " + number + " -> " + other.number + " " + rx + " " + ry + " " + rz + " -> "
//						+ matchResult.numberOfMatches);
				if (matchResult.numberOfMatches >= 12) {
					return matchResult;
				}
			}

			return null;
		}

		public void merge(Scanner otherScanner, MatchResult matchResult) {
			Set<Vector> rotatedBeacons = rotateBeacons(matchResult.rx, matchResult.ry, matchResult.rz,
					otherScanner.beacons);
			Set<Vector> translatedBeacons = translateBeacons(matchResult.t, rotatedBeacons);

			beacons.addAll(translatedBeacons);
		}
	}

	public static void main(String[] args) throws IOException, URISyntaxException {
		List<String> inputLines = Files
				.readAllLines(Paths.get(Day19Puzzle1.class.getResource("input.txt").toURI()));

		LinkedList<Scanner> scanners = new LinkedList<>();
		Scanner currentScanner = null;
		int scannerNumber = 0;
		for (String line : inputLines) {
			if (line.startsWith("---")) {
				if (currentScanner != null) {
					scanners.add(currentScanner);
				}
				currentScanner = new Scanner(scannerNumber++);
			} else if (!line.trim().isEmpty()) {
				String p[] = line.split(",");
				Vector v = new Vector(Integer.parseInt(p[0]), Integer.parseInt(p[1]), Integer.parseInt(p[2]));
				currentScanner.beacons.add(v);
			}
		}
		scanners.add(currentScanner);

		scanners.forEach(System.out::println);

		Scanner scanner = scanners.poll();
		while (!scanners.isEmpty()) {
			System.out.println(scanners.stream().map(s -> s.getNumber() + "(" + s.beacons.size() + ")").collect(Collectors.joining(",")));
			
			Scanner otherScanner = scanners.poll();
			MatchResult matchResult = scanner.matches(otherScanner);
			if (matchResult != null) {
				System.out.println("Merging " + scanner.number + " -> " + otherScanner.number);
				scanner.merge(otherScanner, matchResult);
			} else {
				scanners.add(otherScanner);
			}
		}

		System.out.println(scanner.beacons.size());
	}

}
