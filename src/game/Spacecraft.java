package game;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.util.ArrayList;

import javax.swing.ImageIcon;

public class Spacecraft
{
    private int x;
    private int y;
    private Image image;
    private int width;
    private int height;
    private boolean visible;
    private ArrayList<Missile> missiles;
    
    private final int CRAFT_SIZE = 90;
    private final int STEP_SIZE = 100;
    
    public Spacecraft()
    {
        ImageIcon ii = new ImageIcon(this.getClass().getResource("/player.png"));
        image = ii.getImage();
        width = image.getWidth(null);
        height = image.getHeight(null);
        visible = true;
        missiles = new ArrayList<Missile>();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenHeight = screenSize.height;
        x = 20;
        y = screenHeight - 225;
    }
    
    public void move(String direction)
    {
        if (direction.equals("LEFT"))
            x -= STEP_SIZE;
        else if (direction.equals("RIGHT"))
            x += STEP_SIZE;
    }
    
    public int getX()
    {
        return x;
    }
    
    public int getY()
    {
        return y;
    }
    
    public Image getImage()
    {
        return image;
    }
    
    public ArrayList<Missile> getMissiles()
    {
        return missiles;
    }
    
    public void setVisible(boolean visible)
    {
        this.visible = visible;
    }
    
    public boolean isVisible()
    {
        return visible;
    }
    
    public Rectangle getBounds()
    {
        return new Rectangle(x, y, width, height);
    }
    
    public void fire()
    {
        missiles.add(new Missile(x + CRAFT_SIZE / 2, y - CRAFT_SIZE / 4));
    }
    
}
