package console;

public class Rules {

    public static final String BEGINNER = "beginner";
    public static final String INTERMEDIATE = "intermediate";
    public static final String EXPERT = "expert";
    public static final String CUSTOM = "custom";
    private final int MINE = -1;
    private final int FLAG = -2;
    private boolean isMined = false;
    private int height;
    private int width;
    private int maxMines;
    private int flags = 0, moves;
    public String field;

    // Board
    private char[][] charBoard;
    private int[][] realBoard;
    private int[][] minesLocation;

    // Nearby cells
    private final int[] xs = {1, -1, 0, 1, -1, 0, -1, 1};
    private final int[] ys = {1, -1, 1, 0, 0, -1, 1, -1};

    // Levels
    public Rules(String field) {
        switch (field) {
            case Rules.BEGINNER:
                height = width = 9;
                maxMines = 9;
                break;
            case Rules.INTERMEDIATE:
                height = width = 16;
                maxMines = 40;
                break;
            case Rules.EXPERT:
                height = 16;
                width = 30;
                maxMines = 99;
                break;
            default:
                usage("Unknown level: " + field);
        }
        this.field = field;

        setUp();
    }

    // Constructor for custom Minesweeper Game
    public Rules(int height, int width, int mines) {
        this.field = CUSTOM;

        if (height < 1) {
            throw new IllegalArgumentException("Height cannot be less than 1: " + height);
        }
        if (width < 1) {
            throw new IllegalArgumentException("Width cannot be less than 1: " + width);
        }
        if (mines < 1 || mines >= height * width) {
            throw new IllegalArgumentException("Incorrect number of mines: " + mines);
        }
        this.height = height;
        this.width = width;
        this.maxMines = mines;

        //usage("Incorrect mode: mines >= width * height");
        // Setup the game critical data
        setUp();
    }

    // If col and row are valid
    public boolean isValid(int row, int col) {
        return row >= 0 && col >= 0 && row < height && col < width;
    }

    // Check if it is a mine
    public boolean isMine(int x, int y) {
        return realBoard[x][y] == MINE;
    }

    // Count the number of mines around row and col
    public int countAdjacentMines(int row, int col) {
        if (!isValid(row, col))
            return 0;

        int count = 0; // Counter
        for (int k = 0; k < 8; ++k)
            if (isValid(xs[k] + row, ys[k] + col)) {
                if (isMine(xs[k] + row, ys[k] + col)) {
                    ++count;
                }
            }
        ;
        return count;
    }

    private void placeMines(int userX, int userY) {
        for (int i = 0; i < maxMines; ++i) {

            int x = (int) (Math.random() * height);
            int y = (int) (Math.random() * width);

            if (realBoard[x][y] == MINE || userX == x && userY == y)

                minesLocation[i][0] = x;
            minesLocation[i][1] = y;

            realBoard[x][y] = MINE;
        }
    }

    public void setUp() {
        realBoard = new int[height][width];
        charBoard = new char[height][width];
        minesLocation = new int[maxMines][2];
        moves = width * height - maxMines;
        initialize();
    }

    public void initialize() {
        for (int i = 0; i < height; i++)
            for (int j = 0; j < width; j++)
                charBoard[i][j] = '.';
    }

    public int getMoves() {
        return this.moves;
    }

    public int getFlags() {
        return flags;
    }
    public int getMaxMines() {
        return maxMines;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public char[][] getCharBoard() {
        return this.charBoard;
    }

    public int[][] getRealBoard() {
        return realBoard;
    }

    public boolean isMined() {
        return this.isMined;
    }

    public boolean move(int row, int col) {
        if (!isValid(row, col))
            throw new RuntimeException("Invalid parameters passed in move() function");

        // Place mines randomly after first move
        if (!isMined) {
            isMined = true;
            placeMines(row, col);
        } else {
            // Base case
            if (charBoard[row][col] != '.' && charBoard[row][col] != 'F')
                return false;

            // You opened a mine
            // You are going to lose
            if (isMine(row, col)) {
                charBoard[row][col] = '*';

                // Reveal all mines
                for (int i = 0; i < maxMines; i++) {
                    charBoard[minesLocation[i][0]][minesLocation[i][1]] = '*';
                }

                return true;
            } else {
                int count = countAdjacentMines(row, col);
                --moves;

                // Set counter or empty chart
                charBoard[row][col] = (count == 0) ? '#' : String.valueOf(count).charAt(0);
                realBoard[row][col] = count;

                // If count is zero
                if (count == 0) {
                    for (int k = 0; k < 8; ++k) {
                        if (isValid(xs[k] + row, ys[k] + col)) {
                            if (!isMine(xs[k] + row, ys[k] + col)) {
                                move(xs[k] + row, ys[k] + col);
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    // Right click at x, y
    public void flag(int row, int col) {
        if (!isValid(row, col))
            throw new RuntimeException("Invalid parameters passed in right() function");

        if (charBoard[row][col] != '.')
            return;

        if (charBoard[row][col] == 'F' && isMine(row, col)) {
            charBoard[row][col] = '.';
            --flags;
        }

        if (isValid(row, col)) {
            charBoard[row][col] = 'F';
            ++flags;
        }
    }

    // To string
    @Override
    public String toString() {
        String warning = "the game starts after the first move command";
        String header = String.format("Game(%s, width=%d, height=%d, mines=%d, flags=%d)\n", field.toUpperCase(), width, height, maxMines, flags);
        StringBuilder r = new StringBuilder();
        if (!isMined)
            r.append(warning).append("\n");

        r.append(header);

        for (int i = 0; i < height; ++i) {
            for (int j = 0; j < width; ++j) {
                r.append(charBoard[i][j]).append(" ");
            }
            r.append("\n");
        }

        return r.toString();
    }

    // Help info
    public static String help() {
        return "Help:" +
                "\nmove <row> <col>" +
                "\n\t- choose coordinates to move (row, col)" +
                "\nflag <row> <col>" +
                "\n\t- choose coordinates to place a flag (row, col)" +
                "\nquit" +
                "\n\t- quit the game" +
                "\nhelp\n\t- this text\n";
    }

    // Usage info
    public static void usage(String message) {
        System.out.println(message +
                "\nUsage: " +
                "\njava -jar Minesweeper.jar beginner" +
                "\n\t- game in the beginner mode: width=9, height=9, mines=10" +
                "\njava -jar Minesweeper.jar intermediate" +
                "\n\t- game in the intermediate mode: width=16, height=16, mines=40" +
                "\njava -jar Minesweeper.jar expert" +
                "\n\t- game in the expert mode: width=9, height=9, mines=10" +
                "\njava -jar Minesweeper.jar" +
                "\n\t- equivalent to \"java -jar Minesweeper.jar beginner\"" +
                "\njava -jar Minesweeper.jar <width> <height> <mines>" +
                "\n\t- game with the specified width, height and number of mines");
        throw new RuntimeException("");
    }
}






