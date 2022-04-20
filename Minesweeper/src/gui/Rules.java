package gui;

import processing.core.PApplet;

public class Rules {

    // Variables
    public static final String BEGINNER = "beginner";
    public static final String INTERMEDIATE = "intermediate";
    public static final String EXPERT = "expert";
    public static final String CUSTOM = "custom";
    public static final char CELL = '.';
    public static final char MINE_CH = '*';
    public static final char FLAG_CH = 'F';
    public static final char EMPTY = '#';
    private final int MINE = -1;
    private final int FLAG = -2;
    private boolean isMined = false;
    private int height;
    private int width;
    private int maxMines;
    private int flags = 0, movesLeft;
    public String mode;

    // Matrices
    private char[][] charBoard;
    private int[][] realBoard;
    private int[][] minesLocation;

    // For checking all eight neighbours
    private final int[] xs = {1, -1, 0, 1, -1, 0, -1, 1};
    private final int[] ys = {1, -1, 1, 0, 0, -1, 1, -1};

    // Constructor for three modes of Minesweeper Game
    public Rules(String mode) {
        switch (mode) {
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
                usage("Unknown mode: " + mode);
        }
        this.mode = mode;

        // Setup the game critical data
        setUp();
    }

    // Constructor for custom Minesweeper Game
    public Rules(int height, int width, int mines) {
        this.mode = CUSTOM;

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
            count += (isValid(xs[k] + row, ys[k] + col) && isMine(xs[k] + row, ys[k] + col)) ? 1 : 0;
        return count;
    }

    // Place mines on the field
    private void placeMines(int userX, int userY) {
        for (int i = 0; i < maxMines; ++i) {
            // Random position of mines
            int x = (int) (Math.random() * height);
            int y = (int) (Math.random() * width);
            // Already mined, continue
            if (realBoard[x][y] == MINE || userX == x && userY == y)
                continue;
            // Save mine's position
            minesLocation[i][0] = x;
            minesLocation[i][1] = y;
            // Mine
            realBoard[x][y] = MINE;
        }
    }

    // Set up the Minesweeper Game boards
    public void setUp() {
        // Initialize variables and matrices
        realBoard = new int[height][width];
        charBoard = new char[height][width];
        minesLocation = new int[maxMines][2];
        movesLeft = width * height - maxMines;
        // Initialize
        initialize();
    }

    // Set up the game matrices
    public void initialize() {
        for (int i = 0; i < height; i++)
            for (int j = 0; j < width; j++)
                charBoard[i][j] = '.';
    }

    // Gets moves left
    public int getMovesLeft() {
        return this.movesLeft;
    }

    // Get flags count
    public int getFlags() {
        return flags;
    }

    // Get number of mines
    public int getMaxMines() {
        return maxMines;
    }

    // Get height
    public int getHeight() {
        return height;
    }

    // Get width
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

    // A recursive function to play the Minesweeper Game
    // Only works on left click at x y
    public boolean move(int row, int col) {
        if (!isValid(row, col))
            throw new RuntimeException("Invalid parameters passed in move() function");

        // Place mines randomly on first left click
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
                --movesLeft;

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
            throw new RuntimeException("Invalid parameters passed in flag() function");

        if (charBoard[row][col] != '.' && charBoard[row][col] != 'F')
            return;

        if (isMined) {
            if (isMine(row, col) && charBoard[row][col] == 'F' && flags > 0) {
                charBoard[row][col] = '.';
                --flags;
                return;
            }
            if (isValid(row, col) && charBoard[row][col] == '.' && flags < maxMines) {
                charBoard[row][col] = 'F';
                ++flags;
            }
        }
    }

    // To string
    @Override
    public String toString() {
        String warning = "the field will be mined after the first move command";
        String header = String.format("Game(%s, width=%d, height=%d, mines=%d, flags=%d)\n", mode.toUpperCase(), width, height, maxMines, flags);
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

    // Usage info
    public static void usage(String message) {
        System.out.println(message +
                "\nUsage: " +
                "\njava Minesweeper beginner" +
                "\n\t- game in the beginner mode: width=9, height=9, mines=10" +
                "\njava Minesweeper intermediate" +
                "\n\t- game in the intermediate mode: width=16, height=16, mines=40" +
                "\njava Minesweeper expert" +
                "\n\t- game in the expert mode: width=9, height=9, mines=10" +
                "\njava Minesweeper" +
                "\n\t- equivalent to \"java -jar Minesweeper.jar beginner\"" +
                "\njava Minesweeper.jar <width> <height> <mines>" +
                "\n\t- game with the specified width, height and number of mines");
        throw new RuntimeException("");
    }
}

class Timer {

    private int savedTime; // When Timer started
    private final int totalTime; // How long Timer should last
    private int begin;
    private int duration;
    private int time;
    private final PApplet canvas;

    public Timer(PApplet canvas, int tempTotalTime) {
        this.canvas = canvas;
        this.totalTime = tempTotalTime;
        this.begin = canvas.millis();
        this.duration = this.time = 0;
    }

    // Starting the timer
    void reset() {
        // When the timer starts it stores the current time in milliseconds.
        this.duration = this.time = 0;
        this.begin = canvas.millis();
    }

    // Get elapsed time
    public int getElapsedTime() {
        return duration + (canvas.millis() - begin) / 1000;
    }

    boolean isFinished() {
        // Check how much time has passed
        int passedTime = this.canvas.millis() - savedTime;
        return passedTime > totalTime;
    }

}

class Digit {
    private static final int N = 5;
    private final int size;
    private final int digit;
    private final float x;
    private final float y;
    private final float width;
    private final float height;
    private final PApplet canvas;

    public Digit(PApplet canvas, float x, float y, float width, float height, int size, int digit) {
        this.canvas = canvas;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.size = size;
        this.digit = digit;
    }

    void draw() {

        canvas.pushMatrix();
        canvas.pushStyle();
        canvas.translate(this.x, this.y);
        canvas.textAlign(canvas.CENTER);
        canvas.textSize(this.size);

        switch (this.digit) {
            case 1:
                canvas.fill(0, 0, 255);
                break;
            case 2:
                canvas.fill(255, 0, 0);
                break;
            case 3:
                canvas.fill(0, 128, 0);
                break;
            case 4:
                canvas.fill(255, 192, 203);
                break;
            case 5:
                canvas.fill(128, 0, 128);
                break;
            case 6:
                canvas.fill(255, 255, 0);
                break;
            case 7:
                canvas.fill(255, 165, 0);
                break;
            case 8:
                canvas.fill(244, 0, 161);
                break;
        }
        canvas.text(digit, width / 2f, height - height / 4f); // Adjacent numbers

        canvas.popStyle();
        canvas.popMatrix();
    }
}