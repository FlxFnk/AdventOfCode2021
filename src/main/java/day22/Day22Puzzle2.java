package day22;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Queue;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Day22Puzzle2 {
	record Region(boolean on, int x1, int y1, int z1, int x2, int y2, int z2) {
		boolean intersects(Region other) {
			return contains(other.x1, other.y1, other.z1) || contains(other.x1, other.y2, other.z1)
					|| contains(other.x2, other.y1, other.z1) || contains(other.x2, other.y2, other.z1)
					|| contains(other.x1, other.y1, other.z2) || contains(other.x1, other.y2, other.z2)
					|| contains(other.x2, other.y1, other.z2) || contains(other.x2, other.y2, other.z2)
					|| other.contains(x1, y1, z1) || other.contains(x1, y2, z1) || other.contains(x2, y1, z1)
					|| other.contains(x2, y2, z1) || other.contains(x1, y1, z2) || other.contains(x1, y2, z2)
					|| other.contains(x2, y1, z2) || other.contains(x2, y2, z2);
		}

		boolean contains(int x, int y, int z) {
			return ((x >= x1) && (x <= x2) && (y >= y1) && (y <= y2) && (z >= z1) && (z <= z2));
		}

		boolean contains(float x, float y, float z) {
			return ((x >= x1) && (x <= x2) && (y >= y1) && (y <= y2) && (z >= z1) && (z <= z2));
		}

		long volume() {
			return (x2 - x1+1) * (y2 - y1+1 ) * (z2 - z1+1 );
		}
	}

	static Set<Region> intersectRegions(Region r1, Region r2) {
		Set<Region> result = new HashSet<>();

		List<Integer> valuesX = new ArrayList<>(4);
		List<Integer> valuesY = new ArrayList<>(4);
		List<Integer> valuesZ = new ArrayList<>(4);

		valuesX.add(r1.x1);
		valuesX.add(r1.x2);
		valuesX.add(r2.x1);
		valuesX.add(r2.x2);
		valuesX = valuesX.stream().distinct().sorted().collect(Collectors.toList());
//		Collections.sort(valuesX);
		valuesY.add(r1.y1);
		valuesY.add(r1.y2);
		valuesY.add(r2.y1);
		valuesY.add(r2.y2);
		valuesY = valuesY.stream().distinct().sorted().collect(Collectors.toList());
//		Collections.sort(valuesY);
		valuesZ.add(r1.z1);
		valuesZ.add(r1.z2);
		valuesZ.add(r2.z1);
		valuesZ.add(r2.z2);
		valuesZ = valuesZ.stream().distinct().sorted().collect(Collectors.toList());
//		Collections.sort(valuesZ);

		for (int z = 0; z < valuesZ.size() - 1; z++) {
			for (int y = 0; y < valuesY.size() - 1; y++) {
				for (int x = 0; x < valuesX.size() - 1; x++) {
					int x1 = valuesX.get(x);
					int x2 = valuesX.get(x + 1);
					int y1 = valuesY.get(y);
					int y2 = valuesY.get(y + 1);
					int z1 = valuesZ.get(z);
					int z2 = valuesZ.get(z + 1);
					
//					if ((x1 == x2) || (y1 == y2) || (z1 == z2)) {
//						continue;
//					}

					float cx = (x1 + x2) / 2f;
					float cy = (y1 + y2) / 2f;
					float cz = (z1 + z2) / 2f;

					boolean inR1 = r1.contains(cx, cy, cz);
					boolean inR2 = r2.contains(cx, cy, cz);

					boolean state = false;
					if (inR1 && inR2) {
						state = r2.on;
					} else if (inR1) {
						state = r1.on;
					} else if (inR2) {
						state = r2.on;
					} else {
						state = false;
					}

//					System.out.println(cx + "/" + cy + "/" + cz + "=" + state);
//					if (state) {
					Region r = new Region(state, x1, y1, z1, x2, y2, z2);
					if (r.volume() == 0) {
						throw new RuntimeException("Volume 0");
					}
//					System.out.println("r: "+ r + "  " + r.volume());
						result.add(r);
//					}
				}
			}
		}

		return result;
	}

	public static void main(String[] args) throws IOException, URISyntaxException {
		List<String> inputLines = Files
				.readAllLines(Paths.get(Day22Puzzle1.class.getResource("input_test.txt").toURI()));

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

		System.out.println(regions);

		Set<Region> space = new HashSet<>();
		for (Region region : regions) {
			System.out.println("Space: " + space.size());
			if (space.isEmpty()) {
				space.add(region);
				continue;
			}
			
			Set<Region> newRegions = new HashSet<>();
			Set<Region> removedRegions = new HashSet<>();
			for (Region existingRegion : space) {
				if (existingRegion.intersects(region)) {
					removedRegions.add(existingRegion);
					System.out.println(region + " intersects " + existingRegion);
					Set<Region> intersectedRegions = intersectRegions(existingRegion, region);
					intersectedRegions.stream().filter(Region::on).forEach(newRegions::add);
				} else {
					newRegions.add(region);
				}
			}
			
			space.addAll(newRegions);
			space.removeAll(removedRegions);
		}

		System.out.println(space);
		System.out.println(space.size());
		System.out.println(space.stream().mapToLong(Region::volume).sum());
	}
}
