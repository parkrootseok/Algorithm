import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * SWEA_1767_프로세서연결하기
 * @author parkrootseok
 * 
 * - 셀 조건
 *  - 1개 core 혹은 1개의 전선만 존재
 * - 맵 테두리에는 전원이 존재
 * - core 조건
 *  - 가장 자리에 존재하는 core는 이미 연결(행 시작, 끝 / 열 시작, 끝에 존재)
 *  - 전원을 연결하기 위한 전선은 직선 방향으로만 설치 가능
 *  - 단, 다른 core의 전선이 이미 존재하는 경우 설치 불가능
 * - 최대한 많은 core를 연결했을 때 길이의 합을 구해라
 *  - 여러 방법이 있을 때 최소를 구해라(즉, 길이의 합을 구해서 최소인 것을 찾아라)
 * 
 * 1. 테스트 케이스 횟수 입력.
 * 2. 맵 사이즈를 받아 맵 정보를 초기화
 * 3. 코어 연결을 진행한다.
 *  3-1. 4가지 방향으로 전선을 내린다.
 *    3-1-1. 현재 방향으로 전선을 연결할 수 있는지 확인
 *    3-1-2. 연결한 상태로 다음 코어로 이동
 *    3-1-3. 연결했던 전선을 모두 해제
 *  3-2. 코어를 사용하지 않은 경우
 *  3-3. 앞으로 연결할 코어수를 모두 연결해도 최대 개수를 넘지 않을 경우 종료
 *  3-4. 모든 코어가 전선을 내렸을 경우 종료
 *   3-4-1. 현재 사용한 코어수가 최대값보다 클 경우 최대 코어 개수와 최소 전선 갯수를 초기화
 *   3-4-2. 코어 개수가 같은 경우 전선의 갯수가 더 작다면 갱신
 **/

public class Solution {

	static final int TOTAL_CORE_NUMBER = 12;
	
	static class Core {

		int row;
		int col;

		Core(int row, int col) {
			this.row = row;
			this.col = col;
		}

	}

	static int[] dx = {-1, 1, 0, 0};
	static int[] dy = {0, 0, -1, 1};

	static final int EMPTY = 0;
	static final int WIRE = -1;

	static BufferedReader br;
	static BufferedWriter bw;
	static StringBuilder sb;
	static String[] inputs;
	static String input;

	static int testNumber;

	static int[][] map;
	static int size;

	static Core[] cores;
	static int totalCoreNumber;
	static int maxCoreCount;
	static int minWireCount;

	public static void main(String[] args) throws NumberFormatException, IOException {

		br = new BufferedReader(new InputStreamReader(System.in));
		bw = new BufferedWriter(new OutputStreamWriter(System.out));
		sb = new StringBuilder();

		// 1. 테스트 케이스 횟수 입력.
		testNumber = Integer.parseInt(br.readLine().trim());

		for (int curTest = 1; curTest <= testNumber; curTest++) {

			//2. 맵 사이즈를 받아 맵 정보를 초기화
			size = Integer.parseInt(br.readLine().trim());

			map = new int[size][size];
			totalCoreNumber = 0;
			cores = new Core[TOTAL_CORE_NUMBER];
			for (int row = 0; row < size; row++) {

				inputs = br.readLine().trim().split(" ");

				for (int col = 0; col < size; col++) {

					map[row][col] = Integer.parseInt(inputs[col]);

					// 2-1. 코어라면 리스트에 추가한다.
					if (map[row][col] != EMPTY) {

						if (row == 0 || col == 0 || row == size - 1 || col == size - 1) {
							// 가장자리에 있는 코어는 제외한다
							continue;
						}

						cores[totalCoreNumber++] = new Core(row, col); 

					}

				}

			}

			// 3. 코어 연결을 진행한다.
			maxCoreCount = 0;
			minWireCount = Integer.MAX_VALUE;
			powerOn(0, 0, 0);

			sb.append("#").append(curTest).append(" ").append(minWireCount).append("\n");

		}

		bw.write(sb.toString());
		bw.close();

	}

	public static boolean inRange(int row, int col) {

		if (0 <= row && row < size && 0 <= col && col < size) {
			return true;
		}

		return false;

	}

	public static boolean isConnectalbe(int row, int col, int dx, int dy) {

		// 1. 현재 위치에서 전선을 설치할 수 있는지 확인
		int nextRow = row;
		int nextCol = col;

		while (true) {

			nextRow += dx;
			nextCol += dy;

			if (!inRange(nextRow, nextCol)) {
				break;
			}

			// 진행 방향에 코어가 있거나 전선이 존재하면 불가
			if (map[nextRow][nextCol] != EMPTY) {
				return false;
			}

		}

		return true;

	}

	public static int connectWire(int row, int col, int dx, int dy) {

		int nextRow = row;
		int nextCol = col;

		// 1. 설치 후 설치된 전선의 수를 리턴
		int wireCount = 0;

		nextRow = row;
		nextCol = col;

		while (true) {

			nextRow += dx;
			nextCol += dy;

			if (!inRange(nextRow, nextCol)) {
				break;
			}

			map[nextRow][nextCol] = WIRE;
			wireCount++;

		}

		return wireCount;

	}

	public static void unconnectWire(int row, int col, int dx, int dy) {

		int nextRow = row;
		int nextCol = col;

		// 1. 연결했던 전선들을 다시 복구
		while (true) {

			nextRow += dx;
			nextCol += dy;

			if (!inRange(nextRow, nextCol)) {
				break;
			}

			map[nextRow][nextCol] = EMPTY;

		}

	}

	public static void powerOn(int coreNumber, int wireCount, int coreCount) {

		// 3-3. 앞으로 연결할 코어수를 모두 연결해도 최대 개수를 넘지 않을 경우 종료
		if ((totalCoreNumber - coreCount + coreCount) < maxCoreCount) {
			return;
		}

		// 3-4. 모든 코어가 전선을 내렸을 경우 종료
		if (coreNumber == totalCoreNumber) {

			// 3-4-1. 현재 사용한 코어수가 최대값보다 클 경우 최대 코어 개수와 최소 전선 갯수를 초기화
			if (maxCoreCount < coreCount) {
				maxCoreCount = coreCount;
				minWireCount = wireCount;
			}

			// 3-4-2. 코어 개수가 같은 경우 전선의 갯수가 더 작다면 갱신
			else if (coreCount == maxCoreCount) {
				minWireCount = Math.min(minWireCount, wireCount);
			}

			return;

		}

		// 3-1. 4가지 방향으로 전선을 내린다.
		Core core = cores[coreNumber];
		for (int dir = 0; dir < dx.length; dir++) {

			/**
			 * 전처리 로직
			 * 1. 현재 위치에서 특정 방향으로 연결할 수 있는지 확인 후 연결
			 */
			// 3-1-1. 현재 방향으로 전선을 연결할 수 있는지 확인
			if (!isConnectalbe(core.row, core.col, dx[dir], dy[dir])) {
				continue;
			}

			// 3-1-2. 연결한 상태로 다음 코어로 이동
			powerOn(coreNumber + 1, wireCount + connectWire(core.row, core.col, dx[dir], dy[dir]), coreCount + 1);

			/**
			 * 후처리 로직
			 * 1. 현재 위치에서 특정 방향으로 연결한 전선 해제
			 */
			// 3-1-3. 연결했던 전선을 모두 해제
			unconnectWire(core.row, core.col, dx[dir], dy[dir]);

		}

		// 3-2. 코어를 사용하지 않은 경우
		powerOn(coreNumber + 1, wireCount, coreCount);

	}

}