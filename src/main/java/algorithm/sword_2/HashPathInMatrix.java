package algorithm.sword_2;

/**
 * Created by xinszhou on 17/12/2016.
 */
public class HashPathInMatrix {

    public static void main(String args[]) {
        char[] matrix = new String("ABCESFCSADEE").toCharArray();
        boolean rst = new HashPathInMatrix().hasPath(matrix, 3, 4, "ABCCED".toCharArray());
        System.out.println(rst);
    }
    
    
    public boolean hasPath(char[] matrix, int rows, int cols, char[] str) {
        boolean[][] visited = new boolean[rows][cols];
        for(int i = 0; i < rows; i ++) {
            for(int j = 0; j < cols; j ++ ) {
                visited[i][j] = false;
            }
        }

        for(int i = 0; i < rows; i ++) {
            for (int j = 0; j < cols; j ++) {
                if (dfs(matrix, rows, cols, str, visited, 0, i, j))
                    return true;
            }
        }
        return false;
    }

    private boolean dfs(char[] matrix, int rows, int cols, char[] str, boolean[][] visited, int reached, int cr, int cc) {
        final int[] px = new int[4];
        final int[] py = new int[4];
        // left down up right
        px[0] = 0; px[1] = 1; px[2] = -1; px[3] = 0;
        py[0] = -1; py[1] = 0; py[2] = 0; py[3] = 1;

        if (cr >= 0 && cr < rows && cc >= 0 && cc < cols && !visited[cr][cc]) {
            // valid location
            if(position(matrix, cr, cc, cols) == str[reached]) {
                reached += 1;

                visited[cr][cc] = true;

                if(reached == str.length) {
                    return true;
                } else {

                    for(int i = 0; i < 4; i ++) {
                        int newx = cr + px[i];
                        int newy = cc + py[i];
                        if(dfs(matrix, rows, cols, str, visited, reached, newx, newy)) {
                            return true;
                        }
                    }

                    // restore state
                    visited[cr][cc] = false;
                }
            }
        }

        return false;
    }

    private char position(char[] matrix, int row, int col, int cols) {
        int location = row * cols + col;
        return matrix[location];
    }

}
