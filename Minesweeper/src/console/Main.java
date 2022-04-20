package console;

import java.util.Arrays;
import java.util.Scanner;

public class Main {

    // Game rules
    private static Rules rules;

    public static void main(String[] args) {
        // Launch th game
        try {
            if(args.length==0) {
                rules = new Rules(Rules.BEGINNER);
            } else if(args.length==1) {
                rules = new Rules(args[0]);
            } else if(args.length==3) {
                rules = new Rules(Integer.parseInt(args[0]), Integer.parseInt(args[1]), Integer.parseInt(args[2]));
            } else {
                Rules.usage("Incorrect app args: " + Arrays.toString(args));
            }

            System.out.println(Rules.help());
            System.out.println(rules);

            launch();
        } catch(RuntimeException e) {
            System.out.println("\t"+e.getMessage());
        }
    }

    private static void launch() {
        Scanner scanner = new Scanner(System.in);
        boolean isGameOver = false;

        while(true) {
            System.out.print("command: ");
            Commands cmd = new Commands(scanner.nextLine());
            System.out.println();

            switch (cmd.getCommand()) {
                case Commands.MOVE:
                    isGameOver = rules.move(cmd.getRow(), cmd.getCol());
                    System.out.println(rules);
                    break;
                case Commands.FLAG:
                    rules.flag(cmd.getRow(), cmd.getCol());
                    System.out.println(rules);
                    break;
                case Commands.HELP:
                    System.out.println(Rules.help());
                    break;
                case Commands.QUIT:
                    System.out.println("Next time you will be better\nBye\n");
                    System.exit(0);
                    break;
            }
            // Game is not over and no moves left, you won!
            if(!isGameOver && rules.getMoves()==0) {
                System.out.println("\nCongratulations!\nBye\n");
                break;
            }
            // Game is over, you lost!
            if(isGameOver) {
                System.out.println("\nNext time you will be better\nBye\n");
                break;
            }
        }
    }
}