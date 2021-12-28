package day23;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

public class Day23Puzzle2 {
	static final int[] roomPositions = { 2, 4, 6, 8 };
	static final int[] hallPositions = { 0, 1, 3, 5, 7, 9, 10 };
	static final int[] movementCosts = { 1, 10, 100, 1000 };

	static Map<State, Long> costCache = new HashMap<>();

	record State(char[][] rooms, char[] hall) {
		State copy() {
			char[][] newRooms = new char[4][rooms[0].length];
			char[] newHall = new char[hall.length];

			for (int i = 0; i < 4; i++) {
				System.arraycopy(rooms[i], 0, newRooms[i], 0, rooms[i].length);
			}
			System.arraycopy(hall, 0, newHall, 0, hall.length);
		
			return new State(newRooms, newHall);
		}
		
		void print() {
			for (int i = 0; i < hall.length; i++) {
				System.out.print(hall[i]);
			}
			System.out.println();

			for (int row = 0; row < 4; row++) {
				String s = "  ";
				for (int room = 0; room < 4; room++) {
					s += rooms[room][row] + " ";
				}
				System.out.println(s);
			}
			System.out.println();
		}

		boolean isDone() {
			for (int roomIndex = 0; roomIndex < rooms.length; roomIndex++) {
				char podType = roomIndexToPod(roomIndex);
				for (int position = 0; position < rooms[roomIndex].length; position++) {
					if (rooms[roomIndex][position] != podType) {
						return false;
					}
				}
			}

			return true;
		}
		
		boolean containsAlienPod(int roomIndex) {
			char podType = roomIndexToPod(roomIndex);
			for (int position = 0; position < rooms[roomIndex].length; position++) {
				char pod = rooms[roomIndex][position]; 
				if ((pod != '.') && (pod != podType)) {
					return true;
				}
			}
			
			return false;
		}

		boolean isEmpty(int roomIndex) {
			for (int position = 0; position < rooms[roomIndex].length; position++) {
				if (rooms[roomIndex][position] != '.') { 
					return false;
				}
			}
			
			return true;
		}
		
		int roomPosition(int roomIndex) {
			for (int i = 0; i < rooms[roomIndex].length; i++) {
				if (rooms[roomIndex][i] != '.') {
					return i;
				}
			}

			return -1;
		}

		long moveToHall(int roomIndex, int hallIndex) {
			int roomPosition = roomPosition(roomIndex);
			if (roomPosition < 0) {
				throw new IllegalStateException("Room " + roomIndex + " is empty.");
			}

			char pod = rooms[roomIndex][roomPosition];

			hall[hallIndex] = pod;
			rooms[roomIndex][roomPosition] = '.';

			return cost(pod) * (roomPosition + 1 + Math.abs(hallIndex - roomPositions[roomIndex]));
		}
		
		long moveToRoom(int hallIndex, int roomIndex) {
			int roomPosition = roomPosition(roomIndex);
			if (roomPosition == 0) {
				throw new IllegalArgumentException("Room " + roomIndex + " is full."); 
			}
			if (roomPosition <= 0) {
				roomPosition = rooms[roomIndex].length;
			}
			
			char pod = hall[hallIndex];
			
			rooms[roomIndex][roomPosition-1] = pod;
			hall[hallIndex] = '.';
			
			return cost(pod) * (roomPosition + Math.abs(hallIndex - roomPositions[roomIndex]));
		}
		
		boolean pathFromRoom(int roomIndex, int hallIndex) {
			int start = roomPositions[roomIndex];
			int dx = (int) Math.signum(hallIndex - start);

			for (int i = start; i != hallIndex; i += dx) {
				if (hall[i] != '.') {
					return false;
				}
			}

			return hall[hallIndex] == '.';
		}
		
		boolean pathFromHall(int hallIndex, int roomIndex) {
			int target = roomPositions[roomIndex];
			int dx = (int) Math.signum(target - hallIndex);

			for (int i = hallIndex + dx; i != target; i += dx) {
				if (hall[i] != '.') {
					return false;
				}
			}

			return hall[target] == '.';
		}
	}

	static int podToRoomIndex(char pod) {
		return pod - 'A';
	}

	static char roomIndexToPod(int roomIndex) {
		return (char) ('A' + roomIndex);
	}

	static int cost(char c) {
		return movementCosts[c - 'A'];
	}

	static long calculate(State state) {
//		System.out.println("State");
//		state.print();
//		System.out.println(costCache.size());
		
		if (state.isDone()) {
			return 0;
		}

		if (costCache.containsKey(state)) {
			return costCache.get(state);
		}

		// move back to room
		for (int hallIndex : hallPositions) {
			char pod = state.hall[hallIndex] ; 
			if (pod != '.') {
				int roomIndex = podToRoomIndex(pod);
				
				if ((!state.containsAlienPod(roomIndex)) && (state.pathFromHall(hallIndex, roomIndex))){
					State newState = state.copy();
					long cost = newState.moveToRoom(hallIndex, roomIndex);
//					newState.print();
//					System.out.println("H" + hallIndex + " -> R" + roomIndex + ": " + cost);
					return cost + calculate(newState);
				}
			}
		}
		
		long minCost = Integer.MAX_VALUE;
		// move to hallway
		for (int roomIndex = 0; roomIndex < 4; roomIndex++) {
			boolean empty = state.isEmpty(roomIndex);
			boolean containsAlienPod = state.containsAlienPod(roomIndex);
			if ((! empty) && (containsAlienPod)) {
				for (int hallIndex : hallPositions) {
					if (state.pathFromRoom(roomIndex, hallIndex)) {
						State newState = state.copy();
						long cost = newState.moveToHall(roomIndex, hallIndex);
//						newState.print();
//						System.out.println("R" + roomIndex + " -> H" + hallIndex + ": " + cost);
						cost += calculate(newState);
						if (cost < minCost) {
							minCost = cost;
						}
					}
				}
			}
		}

		costCache.put(state, minCost);

		return minCost;
	}

	public static void main(String[] args) throws IOException, URISyntaxException {
		State initial = new State(
				new char[][] { { 'C', 'D', 'D', 'B' }, new char[] { 'B', 'C', 'B', 'C' },
						new char[] { 'D', 'B', 'A', 'A' }, new char[] { 'D', 'A', 'C', 'A' } },
				new char[] { '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.' });
//		State initial = new State(
//				new char[][] { { '.', '.', '.', 'A' }, new char[] { 'B', 'C', 'B', 'C' },
//					new char[] { 'D', 'B', 'A', 'A' }, new char[] { 'D', 'A', 'C', 'A' } },
//				new char[] { 'A', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.' });

		System.out.println(calculate(initial));
	}
}
