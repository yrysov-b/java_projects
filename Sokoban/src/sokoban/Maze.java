package help01;


public class Maze {
    public int NumOfBoxes;
    public int BoxIndex;
    public int ExitIndex;

    protected int[][] ExitCol = new int[3][1];
    protected int[][] ExitRow = new int[3][1];
    protected int[][] BoxRow = new int[3][1];
    protected int[][] BoxCol = new int[3][1];

    char[][] data;
    char[][] ExitData;
    protected int robotRow;
    protected int robotCol;
    private int height;
    private int width;

    public Maze(char[][] level) {
        this.NumOfBoxes = 0;

        this.height = level.length;
        this.width = level[0].length;

        this.data = new char[height][width];
        this.ExitData = new char[height][width];

        this.BoxIndex = 0;
        this.ExitIndex = 0;
        for (int r = 0; r < height; r++) {

            for (int c = 0; c < width; c++) {
                if (level[r][c] == 'R') {
                    robotRow = r;
                    robotCol = c;
                    data[r][c] = ' ';
                    ExitData[r][c] = ' ';

                } else if (level[r][c] == 'E') {
                    ExitRow[ExitIndex][0] = r;
                    ExitCol[ExitIndex][0] = c;
                    data[r][c] = 'E';
                    ExitData[r][c] = 'E';
                    ExitIndex++;
                } else if (level[r][c] == '$') {
                    BoxRow[BoxIndex][0] = r;
                    BoxCol[BoxIndex][0] = c;
                    data[r][c] = '$';
                    BoxIndex++;
                    NumOfBoxes++;


                } else {
                    data[r][c] = level[r][c];
                    data[r][c] = level[r][c];

                }
            }

        }

    }

    public void Move(int dr, int dc) {
        int tRow = robotRow + dr;
        int tCol = robotCol + dc;

        if (data[tRow][tCol] == ' ' || data[tRow][tCol] == 'E') {
            robotRow = tRow;
            robotCol = tCol;
        } else if (data[tRow][tCol] == '$') {
            System.out.println("in $");

            int tBoxRow = tRow + dr;
            int tBoxCol = tCol + dc;

            if (data[tBoxRow][tBoxCol] == ' ' || ExitData[tBoxRow][tBoxCol] == 'E') {


                int ind = getIndex(BoxRow, BoxCol, tRow, tCol);


                BoxRow[ind][0] = tBoxRow;
                BoxCol[ind][0] = tBoxCol;

                robotRow = tRow;
                robotCol = tCol;

                if (ExitData[tBoxRow][tBoxCol] == 'E') {

                    data[tBoxRow][tBoxCol] = '*';
                } else {
                    data[tBoxRow][tBoxCol] = '$';
                }
                data[tRow][tCol] = ' ';

            }
        } else if (data[tRow][tCol] == '*') {
            int tBoxRow = tRow + dr;
            int tBoxCol = tCol + dc;


            if (data[tBoxRow][tBoxCol] == ' ') {
                System.out.println("in *");

                int ind = getIndex(BoxRow, BoxCol, tRow, tCol);

                BoxRow[ind][0] = tBoxRow;
                BoxCol[ind][0] = tBoxCol;


                robotRow = tRow;
                robotCol = tCol;

                data[tBoxRow][tBoxCol] = '$';


            }
        }
    }

    private int getIndex(int[][] boxRow, int[][] boxCol, int tRow, int tCol) {
        for (int i = 0; i < boxRow.length; i++)
            if (tRow == boxRow[i][0])
                for (int j = 0; j < boxCol.length; j++)
                    if (tCol == boxCol[j][0])
                        return i;


        return -1;
    }


    public boolean isWin() {

        int win = 0;
        int[][] tExitRow = new int[3][1];
        int[][] tExitCol = new int[3][1];


        for (int i = 0; i < ExitCol.length; i++) {
            tExitRow[i][0] = ExitRow[i][0];
            tExitCol[i][0] = ExitCol[i][0];
        }

        //Takes each Box and checks if it is inside of the Exit
        for (int i = 0; i < BoxCol.length; i++) {
            for (int j = 0; j < tExitCol.length; j++) {
                if (BoxRow[i][0] == tExitRow[j][0] && BoxCol[i][0] == tExitCol[j][0] && tExitRow[j][0] != -1 && tExitCol[j][0] != -1) {
                    win++;

                    tExitRow[j][0] = -1;
                    tExitCol[j][0] = -1;
                }
            }
        }
        return win == ExitCol.length;
    }


    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public char getCurElement(int r, int c) {
        return data[r][c];
    }

    public int getRobotCol() {
        return robotCol;
    }

    public int getRobotRow() {
        return robotRow;
    }


    public char getExitData(int r, int c) {
        return ExitData[r][c];
    }


}
