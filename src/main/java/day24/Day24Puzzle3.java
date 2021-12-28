package day24;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
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

public class Day24Puzzle3 {
	//                  0   1   2    3   4   5   6   7   8   9   10   11  12  13
	//                 13   12  3    2  11  10   7   6   9   8    5    4   1   0 
	static int W[] = {  3,  6,  9,   6,  9,  7,  8,  3,  5,  3,   9,   1,  9,  9 };
	static int X[] = { 11, 11, 15, -11, 15, 15, 14, -7, 12, -6, -10, -15, -9,  0 };
	static int Y[] = {  6, 12,  8,   7,  7, 12,  2, 15,  4,  5,  12,  11, 13,  7 };
	static int Z[] = {  1,  1,  1,  26,  1,  1,  1, 26,  1,  26, 26,  26, 26, 26 };

	static int[][] pairs = {{8,9}, {6,7}, {2,3}, {5,10}, {4,11}, {1,12}, {0,13} };
	
	public static void main(String[] args) {

		long w, x, y, z;

		for (int[] pair : pairs) {
			boolean found = false;
			for (int w1 = 1; w1 <= 9 && !found; w1++) {
				for (int w2 = 1; w2 <= 9 && !found; w2++) {
					W[pair[0]] = w1;
					W[pair[1]] = w2;
					long[] t = new long[14];
					z = 0;
					for (int i= 0; i < 14; i++) {
						w = W[i];
						
						x = z % 26;
						z = z / Z[i];
						
						x = x + X[i];
						x = (x == w) ? 1 : 0;
						x = (x == 0) ? 1 : 0;
						
						z = z * (25 * x + 1);
						z = z + (w + Y[i]) * x;
						
//						System.out.println( i + " -> " + "W: " + W[i] + "  Z:" + Z[i] + "  x:" + x + " z:" + z);
						t[i] = x;
					}
					
					if (t[pair[1]] == 0) {
						found = true;
					}
				}
			}
		}
				
//		if (z == 0) {
			Arrays.stream(W).forEach(System.out::print);
//		}
	}
}
