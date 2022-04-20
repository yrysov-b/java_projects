package help01;


import javax.imageio.*;
import java.awt.image.*;
import java.io.*;


public class Images {

    BufferedImage right;
    BufferedImage left;
    BufferedImage up;
    BufferedImage down;
    BufferedImage wall;
    BufferedImage ground;
    BufferedImage goal;
    BufferedImage BlueBox;
    BufferedImage RedBox;

    public Images() throws IOException {
        up = ImageIO.read(new File("images/RobotU.png"));
        down = ImageIO.read(new File("images/RobotD.png"));
        left = ImageIO.read(new File("images/RobotL.png"));
        right = ImageIO.read(new File("images/RobotR.png"));
        wall = ImageIO.read(new File("images/Wall.png"));
        ground = ImageIO.read(new File("images/Ground.png"));
        goal = ImageIO.read(new File("images/Goal.png"));
        BlueBox = ImageIO.read(new File("images/BoxBlue.png"));
        RedBox = ImageIO.read(new File("images/BoxRed.png"));


    }


}
