package day22;

import java.io.IOException;
import java.math.BigInteger;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Queue;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Day22Puzzle2V2 {
	record Region(boolean on, int x1, int y1, int z1, int x2, int y2, int z2) {
	}

	public static void main(String[] args) throws IOException, URISyntaxException {
		List<String> inputLines = Files.readAllLines(Paths.get(Day22Puzzle1.class.getResource("input.txt").toURI()));

		List<Region> regions = new LinkedList<>();

		Pattern pattern = Pattern.compile("(on|off) x=(-?\\d+)..(-?\\d+),y=(-?\\d+)..(-?\\d+),z=(-?\\d+)..(-?\\d+)");
		for (String line : inputLines) {
			Matcher matcher = pattern.matcher(line);
			if (matcher.matches()) {
				int x1 = Integer.parseInt(matcher.group(2));
				int x2 = Integer.parseInt(matcher.group(3));
				int y1 = Integer.parseInt(matcher.group(4));
				int y2 = Integer.parseInt(matcher.group(5));
				int z1 = Integer.parseInt(matcher.group(6));
				int z2 = Integer.parseInt(matcher.group(7));
				regions.add(new Region(matcher.group(1).equals("on"), x1, y1, z1, x2, y2, z2));
			} else {
				System.out.println(line);
				System.exit(0);
			}
		}

		List<Integer> pointsX = new ArrayList<>();
		List<Integer> pointsY = new ArrayList<>();
		List<Integer> pointsZ = new ArrayList<>();

		for (Region region : regions) {
			pointsX.add(region.x1);
			pointsX.add(region.x2 + 1);
			pointsY.add(region.y1);
			pointsY.add(region.y2 + 1);
			pointsZ.add(region.z1);
			pointsZ.add(region.z2 + 1);
		}

		pointsX = new ArrayList<>(pointsX.stream().distinct().sorted().collect(Collectors.toList()));
		pointsY = new ArrayList<>(pointsY.stream().distinct().sorted().collect(Collectors.toList()));
		pointsZ = new ArrayList<>(pointsZ.stream().distinct().sorted().collect(Collectors.toList()));

		System.out.println(pointsX.size());
		System.out.println(pointsY.size());
		System.out.println(pointsZ.size());

		BigInteger sum = BigInteger.valueOf(0);
		for (int z = 0; z < pointsZ.size() - 1; z++) {
			System.out.println(z);
			for (int y = 0; y < pointsY.size() - 1; y++) {
				for (int x = 0; x < pointsX.size() - 1; x++) {
					BigInteger dx = BigInteger.valueOf((pointsX.get(x + 1) - pointsX.get(x)));
					BigInteger dy = BigInteger.valueOf((pointsY.get(y + 1) - pointsY.get(y)));
					BigInteger dz = BigInteger.valueOf((pointsZ.get(z + 1) - pointsZ.get(z)));

					BigInteger size = dx.multiply(dy).multiply(dz);
					if (isOn(pointsX.get(x), pointsY.get(y), pointsZ.get(z), regions)) {
						sum = sum.add(size);
					}
				}
			}
		}

		System.out.println(sum);
	}

	private static boolean isOn(int x, int y, int z, Collection<Region> regions) {
		boolean result = false;
		for (Region region : regions) {
			if ((x < region.x1) || (x > region.x2) || (y < region.y1) || (y > region.y2) || (z < region.z1)
					|| (z > region.z2)) {
				continue;
			}

			result = region.on;
		}

		return result;
	}
}
