package help01;


import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;


public class Main extends JFrame {

    public int totalMoves = 0;
    private JButton Reset = new JButton();
    private GameModel game = new GameModel();
    private Images images = new Images();
    private BufferedImage box = null;

    private CanvasPanel mainPanel;
    private JPanel controlPanel ;
    private JLabel levelLabel;
    private JLabel puzzleLabel;
    private JLabel movesLabel;

    private JPanel puzzlePanel;
    private JPanel levelPanel;
    private JPanel movesPanel;

    private BufferedImage curJimmy = images.up;

    Main() throws Exception {
        setLayout(new BorderLayout());

        mainPanel = new CanvasPanel();
        mainPanel.setFocusable(true);
        mainPanel.requestFocus();
        add(mainPanel, BorderLayout.CENTER);
        mainPanel.setBackground(Color.BLACK);

        controlPanel = new JPanel();
        add(controlPanel, BorderLayout.EAST);

         movesPanel = new JPanel();
         movesPanel.setSize(new Dimension(600, 20));
         movesPanel.setBorder(new TitledBorder("Moves"));
        movesPanel.setBackground(Color.WHITE);
        movesLabel = new JLabel(String.valueOf(getTotalMoves()));
        movesPanel.add(movesLabel);

        puzzlePanel = new JPanel();
        puzzlePanel.setSize(new Dimension(600, 20));
        puzzlePanel.setBorder(new TitledBorder("Puzzle"));
        puzzlePanel.setBackground(Color.WHITE);
        puzzleLabel = new JLabel(game.getCurLevel() + " from " + 3);
        puzzlePanel.add(puzzleLabel);

        levelPanel = new JPanel();
        levelPanel.setSize(new Dimension(600, 20));
        levelPanel.setBorder(new TitledBorder("Level"));
        levelPanel.setBackground(Color.WHITE);
        levelLabel = new JLabel("Minicosmos");
        levelPanel.add(levelLabel);

        controlPanel.add(movesPanel, BorderLayout.CENTER);
        controlPanel.add(puzzlePanel, BorderLayout.EAST);
        controlPanel.add(levelPanel, BorderLayout.EAST);

        Reset.setText("ESC");
        controlPanel.add(Reset);

        Reset.addActionListener(e ->{

            int NumOfBoxes = 0;
            int BoxIndex = 0;
            int ExitIndex = 0;
           for(int r = 0 ; r < game.getLevels().length; r++){
               for(int c = 0;  c< game.getLevels()[0].length;c++){
                   if (game.getLevels()[r][c] == 'R') {
                       game.maze.robotRow = r;
                       game.maze.robotCol = c;
                       game.maze. data[r][c] = ' ';
                       game.maze.ExitData[r][c] = ' ';

                   } else if ( game.getLevels()[r][c] == 'E') {
                       game.maze.ExitRow[ ExitIndex][0] = r;
                       game.maze.ExitCol[ ExitIndex][0] = c;
                       game.maze.data[r][c] = 'E';
                       game.maze.ExitData[r][c] = 'E';
                       ExitIndex++;
                   } else if (game.getLevels()[r][c] == '$') {
                       game.maze.BoxRow[ BoxIndex][0] = r;
                       game.maze.BoxCol[ BoxIndex][0] = c;
                       game.maze. data[r][c] = '$';
                        BoxIndex++;
                       NumOfBoxes++;

                   } else {
                       game.maze. data[r][c] = game.getLevels()[r][c];
                       game.maze. data[r][c] = game.getLevels()[r][c];

                   }

               }
           }
           totalMoves = 0;
           repaint();
           mainPanel.requestFocus();

        });


        mainPanel.addKeyListener(new CanvasPanelListener());
        setTitle("Maze");

        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);


    }


    public static void main(String[] args) {
        try {
            new Main();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }

    }

    public String getTotalMoves() {
        return Integer.toString(this.totalMoves);
    }


    class CanvasPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {


            int widthCell = images.wall.getWidth();
            int heightCell = images.wall.getHeight();

            int xLeftUpper = getWidth() / 2 - widthCell * game.maze.getWidth() / 2;
            int yLeftUpper = getHeight() / 2 - heightCell * game.maze.getHeight() / 2;


            for (int r = 0; r < game.maze.getHeight(); r++) {
                for (int c = 0; c < game.maze.getWidth(); c++) {

                    switch (game.maze.getCurElement(r, c)) {
                        case ' ':
                            drawImage(g, images.ground,
                                    xLeftUpper + c * widthCell, yLeftUpper + r * heightCell, widthCell, heightCell);
                            break;
                        case '#':
                            drawImage(g, images.wall,
                                    xLeftUpper + c * widthCell, yLeftUpper + r * heightCell, widthCell, heightCell);
                            break;

                        case 'E':
                            for (int i = 0; i < game.maze.ExitIndex; i++) {
                                drawImage(g, images.ground,
                                        xLeftUpper + game.maze.ExitCol[i][0] * widthCell,
                                        yLeftUpper + game.maze.ExitRow[i][0] * heightCell,
                                        widthCell, heightCell);

                                drawImage(g, images.goal,
                                        xLeftUpper + game.maze.ExitCol[i][0] * widthCell,
                                        yLeftUpper + game.maze.ExitRow[i][0] * heightCell,
                                        widthCell, heightCell);
                            }
                            break;

                    }
                }

            }
            //      --------DRAW BOX----------

            for (int i = 0; i < game.maze.NumOfBoxes; i++) {
                if (game.maze.data[game.maze.BoxRow[i][0]][game.maze.BoxCol[i][0]] == '*'    //Puts values of BoxRow and Col to check
                        && game.maze.data[game.maze.BoxRow[i][0]][game.maze.BoxCol[i][0]] == '*') {

                    box = images.RedBox;
                } else {
                    box = images.BlueBox;
                }
                drawImage(g, box,
                        xLeftUpper + game.maze.BoxCol[i][0] * widthCell,
                        yLeftUpper + game.maze.BoxRow[i][0] * heightCell,
                        widthCell, heightCell);

            }

            drawImage(g, curJimmy,
                    xLeftUpper + game.maze.getRobotCol() * widthCell,
                    yLeftUpper + game.maze.getRobotRow() * heightCell,
                    widthCell, heightCell);

            Main.this.movesLabel.setText("Moves " + Main.this.getTotalMoves());

            repaint();
        }

        private void drawImage(Graphics g, BufferedImage img, int x, int y, int w, int h) {
            int leftX = x + w / 2 - img.getWidth() / 2;
            int topY = y + h / 2 - img.getHeight() / 2;
            g.drawImage(img, leftX, topY, null);
        }
    }

    class CanvasPanelListener extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_H) {
                JOptionPane.showMessageDialog(null, "Move Jimmy using arrows!!");
            } else if (e.getKeyCode() == KeyEvent.VK_UP) {
                game.maze.Move(-1, 0);
                curJimmy = images.up;
                Main.this.totalMoves++;

            } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                game.maze.Move(1, 0);
                curJimmy = images.down;
                Main.this.totalMoves++;

            } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                game.maze.Move(0, -1);
                curJimmy = images.left;
                Main.this.totalMoves++;

            } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                game.maze.Move(0, 1);
                curJimmy = images.right;
                Main.this.totalMoves++;

            }

            repaint();
            if (game.maze.isWin()) {
                JOptionPane.showMessageDialog(Main.this, String.format("Solved!! %d Moves", Main.this.totalMoves++));
                game.nextLevel();
                totalMoves = 0;

            }


        }
    }
}
