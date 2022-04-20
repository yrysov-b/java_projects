package gui;

import processing.core.*;

public class Main extends PApplet {

    // Globals
    private Rules game;
    private SmileButton resetButton;
    private String mode;
    private Timer timer;
    private final float cellSide = 35f;
    private float modeButtonX;
    private float modeButtonY;
    private float modeButtonW;
    private float modeButtonH;
    private float smileY;
    private float minesTextX;
    private float timerTextX;
    private float startButtonX;
    private float startButtonY;
    private float startButtonW;
    private float startButtonH;
    private boolean isGameOver = false;
    private boolean isGameWon = false;

    // Settings
    public void settings() {
        fullScreen();
    }

    // Setup
    public void setup() {
        frameRate(60);
        timer = new Timer(this, 3000);
        smileY = 150f;
        startButtonX = width/2f-35f;
        startButtonY = 115f;
        startButtonW = 70f;
        startButtonH = 70f;
        modeButtonX = 0f;
        modeButtonY = height/2f-70f;
        modeButtonW = 190f;
        modeButtonH = 40f;
        this.mode = Rules.BEGINNER;
        game = new Rules(this.mode);
    }

    // Draw
    public void draw() {
        background(0, 0, 0);
        // Draw the field
        updateField();
        // Adapt widgets
        calculatePosition();
        // Mode switch buttons
        addButtons();
        // Draw texts
        drawText();
        // Check game state
        checkGameState();
    }

    // Draw field
    public void updateField() {
        float x,y;

        for(int i=0; i<game.getWidth(); ++i) {
            for(int j=0; j<game.getHeight(); ++j) {
                x = i*cellSide+width/2f-cellSide*game.getWidth()/2f;
                y = j*cellSide+height/2f-cellSide*game.getHeight()/2f;
                drawElement(game.getCharBoard()[j][i], x, y, i, j, new Button(this, x, y, cellSide,cellSide, null));
            }
        }
    }

    // Draw digits
    public void drawElement(char cell, float x, float y, int col, int row, Button cellBtn) {
        if(cell== Rules.CELL || cell== Rules.FLAG_CH){
            if(cell== Rules.FLAG_CH)
                (new Flag(this, x, y, cellSide, cellSide)).draw();

            if(!isGameOver && !isGameWon) {
                cellBtn.setOnLeftClickListener(() -> {
                    isGameOver = game.move(row, col);
                    Mine.setExplodedRowCol(row, col);
                });
                cellBtn.setOnRightClickListener(() -> {
                    game.flag(row, col);
                    delay(100);
                });
            }
        } else if(Character.isDigit(cell)) {
            cellBtn.drawPressed();
            (new Digit(this, x,y,cellSide,cellSide, 25, cell - '0')).draw();
        } else if(cell== Rules.MINE_CH) {
            cellBtn.drawPressed();
            (new Mine(this, x, y, row, col, cellSide, cellSide, 10f)).draw();
        } else if(cell== Rules.EMPTY) {
            cellBtn.drawPressed();
        }
    }

    // Calculation coordinates of all widgets on screen
    public void calculatePosition() {
        modeButtonX = width/2f+cellSide*game.getWidth()/2f+30f;
        smileY = height/2f-cellSide*game.getHeight()/2f-40f;
        startButtonY = smileY-35f;
        minesTextX = width/2f-cellSide*game.getWidth()/2f;
        timerTextX = width/2f+cellSide*game.getWidth()/2f-110f;
    }

    // Update game state all the time to identify loss or win
    public void checkGameState() {
        // Game is won
        if(!isGameOver && game.getMovesLeft()==0) {
            timer.reset();
            isGameWon = true;
            resetButton.setVictorious(true);
        }
        // Game is lost
        if(isGameOver) {
            timer.reset();
            resetButton.setIsSad(true);
        }
    }

    // Create buttons and set listeners
    public void addButtons() {
        resetButton = new SmileButton(this, startButtonX, startButtonY, startButtonW,startButtonH);
        resetButton.setOnClickListener(() -> {
            timer.reset();
            setGame(this.mode);
        });
        Button beginnerMode = new Button(this, modeButtonX, modeButtonY, modeButtonW, modeButtonH, Rules.BEGINNER);
        beginnerMode.setOnClickListener(() -> setGame(Rules.BEGINNER));
        Button intermediateMode = new Button(this, modeButtonX, modeButtonY + 70f, modeButtonW, modeButtonH, Rules.INTERMEDIATE);
        intermediateMode.setOnClickListener(() -> setGame(Rules.INTERMEDIATE));
        Button expertMode = new Button(this, modeButtonX, modeButtonY + 140f, modeButtonW, modeButtonH, Rules.EXPERT);
        expertMode.setOnClickListener(() -> setGame(Rules.EXPERT));
    }

    // Set game mode, timer and states
    public void setGame(String mode) {
        timer.reset();
        this.mode = mode;
        isGameOver = isGameWon = false;
        game = new Rules(this.mode);
        delay(120);
    }

    // Draw some text
    public void drawText() {
        // Mines and timer
        textSize(60);
        fill(255, 0, 0);
        text(String.format("%03d",(game.getMaxMines())-game.getFlags()),minesTextX, smileY+15f); // Mines' number
        text(String.format("%03d", timer.getElapsedTime()), timerTextX, smileY+15f); // Timer is seconds
        // Credits
        pushStyle();
        textSize(16);
        fill(231,84,128);
        textAlign(CENTER);
        popStyle();
    }

    // Program driver
    public static void main(String[] args) {
        PApplet.main("gui.Main");
    }
}