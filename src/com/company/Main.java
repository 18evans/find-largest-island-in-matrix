package com.company;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Main {

    private static final int COUNT_DIRECTION_CHECKS = 4;
    //define offsets for each of the available directions (see COUNT_DIRECTION_CHECKS) adjacent to
    // a matrix pos - ie. if COUNT_DIRECTION_CHECKS == 4 adjacent positions are -> north, east, south, west
    private static final int[] adjacentRowOffsets = new int[]{-1, 0, 1, 0};
    private static final int[] adjacentColOffsets = new int[]{0, 1, 0, -1};

    //matrix size
    private static int sizeX;
    private static int sizeY;

    /**
     * Eg. input:
     * 1)
     * 2 4 4
     * 0 0 0 1
     * 0 0 1 1
     * 0 0 0 2
     * 2 2 2 2
     * <p>
     * 2)
     * <p>
     * 2 4 4
     * 0 0 0 1
     * 1 1 1 1
     * 1 1 1 2
     * 2 2 2 2
     */
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);

        int nrOfIslands = in.nextInt();

        sizeX = in.nextInt();
        sizeY = in.nextInt();

        int[][] matrix = new int[sizeX][sizeY];

        for (int rowIndex = 0; rowIndex < sizeX; rowIndex++) {
            for (int colIndex = 0; colIndex < sizeY; colIndex++) {
                matrix[rowIndex][colIndex] = in.nextInt();
            }
        }

        Map.Entry<Integer, Integer> largestIslandWithSize = processLargestIsland(matrix, nrOfIslands);

        if (largestIslandWithSize == null)
            System.out.println("No largest isle.");
        else
            System.out.println("Largest isle is " + largestIslandWithSize.getKey() +
                    " with size " + largestIslandWithSize.getValue());
    }

    /**
     * @return pair where key is island id and value is island size.
     */
    public static Map.Entry<Integer, Integer> processLargestIsland(int[][] matrix, int maxNrIslands) {

        //create data structure to keep check of which islands in the matrix have been visited
        // Note: will increase size complexity cause allocates a default empty slot even for the pos nodes that aren't islands).
        // todo: use Hash impl?
        int[][] visited = new int[sizeX][sizeY];

        //keep track of all unique encountered islands in the matrix along with their size
        Map<Integer, Integer> mapIslandWithSize = new HashMap<>();

        //go through each pos in the matrix
        for (int rowIndex = 0; rowIndex < sizeX; rowIndex++) {
            for (int colIndex = 0; colIndex < sizeY; colIndex++) {
                //check if is island AND is not visited
                if (isIsland(matrix, rowIndex, colIndex) && !isVisited(visited, rowIndex, colIndex)) {
                    //if more island found than declared
                    if (mapIslandWithSize.size() > maxNrIslands)
                        throw new IllegalStateException("Found islands exceeds maximum declared island count");

                    int islandId = matrix[rowIndex][colIndex]; //unique number of the island
                    // if this island has previously been entered (ie. had completed DFS) and is encountered again
                    if (mapIslandWithSize.containsKey(islandId))
                        throw new IllegalStateException("Multiple islands with the same island id/number");

                    mapIslandWithSize.put(islandId,
                            dfs(matrix, rowIndex, colIndex, visited));
                }
            }
        }

        return getLargestIsland(mapIslandWithSize);
    }

    /**
     * DFS helper.
     * Recursively sums the count of all adjacent of this position nodes (see {@link #COUNT_DIRECTION_CHECKS})
     * that are part of the same island.
     *
     * @see StackOverflowError on recursion will be limited by Stack space
     */
    public static int dfs(int[][] matrix, int row, int col, int[][] visitedPos) {
        visitedPos[row][col] = 1; //mark as visited

        int size = 1; //count this island
        //loop through each adjacent position looking for islands
        for (int i = 0; i < COUNT_DIRECTION_CHECKS; i++) {
            int rowAdjacentPos = row + adjacentRowOffsets[i];
            int colAdjacentPos = col + adjacentColOffsets[i];

            //validate this adjacent pos whether is an unprocessed island
            if (isSafe(matrix, rowAdjacentPos, colAdjacentPos, visitedPos))
                //check if the same island
                if (matrix[row][col] == matrix[rowAdjacentPos][colAdjacentPos])
                    size += dfs(matrix, rowAdjacentPos, colAdjacentPos, visitedPos);
        }

        return size;
    }

    //conditions

    /**
     * @return {@code true} only if position is valid within given @param matrix,
     * is an actual island,
     * and has not been marked as visited (in @param visited).
     */
    public static boolean isSafe(int[][] matrix, int row, int col, int[][] visited) {
        return isValidPos(row, col) && isIsland(matrix, row, col) && !isVisited(visited, row, col);
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    //preferred code style when all conditions are positive
    private static boolean isVisited(int[][] visited, int row, int col) {
        return visited[row][col] != 0;
    }

    private static boolean isIsland(int[][] matrix, int row, int col) {
        return matrix[row][col] > 0;
    }

    private static boolean isValidPos(int row, int col) {
        return (row >= 0 && row < sizeX) && (col >= 0 && col < sizeY);
    }

    //util to the map

    /**
     * Finds the entry whose value is the highest and returns it.
     * <p>
     * Note on Order:
     * If there are multiple entries holding the highest value the first one in the map iteration order
     * is returned.
     *
     * @return If no islands are present in the passed map then null is returned.
     */
    private static Map.Entry<Integer, Integer> getLargestIsland(Map<Integer, Integer> mapIslandWithSize) {
        int largestIsland = 0;
        int largestIslandSize = 0;
        for (Map.Entry<Integer, Integer> entry : mapIslandWithSize.entrySet()) {
            //in case of equal size, first one in the iteration is winner
            if (entry.getValue() > largestIslandSize) {
                largestIsland = entry.getKey();
                largestIslandSize = entry.getValue();
            }
        }

        //if no islands, null, else return the largest island with its size
        return largestIsland == 0 ? null : new AbstractMap.SimpleEntry<>(largestIsland, largestIslandSize);
    }
}
