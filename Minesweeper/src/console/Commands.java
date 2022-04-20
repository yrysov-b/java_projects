package console;

import java.util.Scanner;

class Commands {
    public static final String MOVE = "move";
    public static final String FLAG = "flag";
    public static final String HELP = "help";
    public static final String QUIT = "quit";

    private final String command;
    private int row, col;

    public Commands(String line) {
        line = line.trim();
        switch (line) {
            case HELP:
            case QUIT:
                command = line;
                return;
        }

        Scanner inpLine = new Scanner(line);
        if (!inpLine.hasNext()) {
            throw new RuntimeException("Wrong command: '" + line + "'");
        }

        String userCommand = inpLine.next();
        if (!userCommand.equals(MOVE) && !userCommand.equals(FLAG)) {
            throw new RuntimeException("Unknown command: '" + line + "'");
        }

        if (!inpLine.hasNextInt()) {
            throw new RuntimeException("No integer in command " + userCommand + ": '" + line + "'");
        }

        int row = inpLine.nextInt();
        if (row < 0) {
            throw new RuntimeException("Negative row in command " + userCommand + ": '" + line + "'");
        }

        if (!inpLine.hasNext()) {
            throw new RuntimeException("Not enough parameters in command " + userCommand + ": '" + line + "'");
        }

        int col = inpLine.nextInt();
        if (col < 0) {
            throw new RuntimeException("Negative col in command " + userCommand + ": '" + line + "'");
        }

        if (inpLine.hasNext()) {
            throw new RuntimeException("Too many parameters in command " + userCommand + ": '" + line + "'");
        }

        command = userCommand;
        this.row = row;
        this.col = col;
    }

    // Getter
    public String getCommand() {
        return command;
    }

    public int getRow() {
        if (!command.equals(MOVE) && !command.equals(FLAG)) {
            throw new RuntimeException("Undefined row or col: current command is not " + MOVE + " or " + FLAG);
        }
        return this.row;
    }

    public int getCol() {
        if (!command.equals(MOVE) && !command.equals(FLAG)) {
            throw new RuntimeException("Undefined row or col: current command is not " + MOVE + " or " + FLAG);
        }
        return this.col;
    }
}