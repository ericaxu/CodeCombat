package game;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.AbstractAction;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.Timer;

public class Board extends JPanel implements ActionListener
{
    
    private Timer timer;
    private Spacecraft craft;
    
    private ArrayList<Enemy> enemies;
    private boolean ingame;
    private int B_WIDTH;
    private int B_HEIGHT;
    
    private int[][] pos = { { 700, 29 } };
    
    public Board()
    {
        setFocusable(true);
        setBackground(Color.BLACK);
        setDoubleBuffered(true);
        ingame = true;
        
        craft = new Spacecraft();
        initEnemies();
        
        timer = new Timer(5, this);
        timer.start();
        setupKeyBindings();
    }
    
    private void setupKeyBindings()
    {
        // move left
        AbstractAction moveLeft = new AbstractAction()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                craft.move("LEFT");
            }
        };
        KeyStroke moveLeftKeyStroke = KeyStroke.getKeyStroke("LEFT");
        this.getInputMap().put(moveLeftKeyStroke, "LEFT");
        this.getActionMap().put("LEFT", moveLeft);
        
        // move right
        AbstractAction moveRight = new AbstractAction()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                craft.move("RIGHT");
            }
        };
        KeyStroke moveRightKeyStroke = KeyStroke.getKeyStroke("RIGHT");
        this.getInputMap().put(moveRightKeyStroke, "RIGHT");
        this.getActionMap().put("RIGHT", moveRight);
        
        // fire
        AbstractAction fire = new AbstractAction()
        {
            
            @Override
            public void actionPerformed(ActionEvent e)
            {
                craft.fire();
            }
        };
        KeyStroke fireKeyStroke = KeyStroke.getKeyStroke("SPACE");
        this.getInputMap(WHEN_FOCUSED).put(fireKeyStroke, "SPACE");
        this.getActionMap().put("SPACE", fire);
    }
    
    public void addNotify()
    {
        super.addNotify();
        B_WIDTH = getWidth();
        B_HEIGHT = getHeight();
    }
    
    public void initEnemies()
    {
        enemies = new ArrayList<Enemy>();
        
        for (int i = 0; i < pos.length; i++)
        {
            enemies.add(new Enemy(pos[i][0], pos[i][1]));
        }
    }
    
    @Override
    public void paint(Graphics g)
    {
        super.paint(g);
        
        if (ingame)
        {
            Graphics2D g2d = (Graphics2D) g;
            
            if (craft.isVisible())
            {
                g2d.drawImage(craft.getImage(), craft.getX(), craft.getY(), this);
            }
            
            ArrayList<Missile> ms = craft.getMissiles();
            
            for (int i = 0; i < ms.size(); i++)
            {
                Missile m = (Missile) ms.get(i);
                g2d.drawImage(m.getImage(), m.getX(), m.getY(), this);
            }
            
            for (int i = 0; i < enemies.size(); i++)
            {
                Enemy a = (Enemy) enemies.get(i);
                if (a.isVisible())
                    g2d.drawImage(a.getImage(), a.getX(), a.getY(), this);
            }
            
            g2d.setColor(Color.WHITE);
            g2d.drawString("enemies left: " + enemies.size(), 5, 15);
            
        }
        else
        {
            String msg = "Game Over";
            Font small = new Font("Helvetica", Font.BOLD, 14);
            java.awt.FontMetrics metr = this.getFontMetrics(small);
            
            g.setColor(Color.white);
            g.setFont(small);
            g.drawString(msg, (B_WIDTH - metr.stringWidth(msg)) / 2, B_HEIGHT / 2);
        }
        
        Toolkit.getDefaultToolkit().sync();
        g.dispose();
    }
    
    public void actionPerformed(ActionEvent e)
    {
//        if (enemies.size() == 0)
//        {
//            ingame = false;
//        }
        
        ArrayList<Missile> ms = craft.getMissiles();
        
        for (int i = 0; i < ms.size(); i++)
        {
            Missile m = (Missile) ms.get(i);
            if (m.isVisible())
                m.move();
            else
                ms.remove(i);
        }
        
        for (int i = 0; i < enemies.size(); i++)
        {
            Enemy a = (Enemy) enemies.get(i);
            if (a.isVisible())
                a.move();
            else
                enemies.remove(i);
        }
        
        checkCollisions();
        repaint();
    }
    
    public void checkCollisions()
    {
        
        Rectangle r3 = craft.getBounds();
        
        for (int j = 0; j < enemies.size(); j++)
        {
            Enemy a = (Enemy) enemies.get(j);
            Rectangle r2 = a.getBounds();
            
            if (r3.intersects(r2))
            {
                craft.setVisible(false);
                a.setVisible(false);
                ingame = false;
            }
        }
        
        ArrayList<Missile> ms = craft.getMissiles();
        
        for (int i = 0; i < ms.size(); i++)
        {
            Missile m = (Missile) ms.get(i);
            
            Rectangle r1 = m.getBounds();
            
            for (int j = 0; j < enemies.size(); j++)
            {
                Enemy a = (Enemy) enemies.get(j);
                Rectangle r2 = a.getBounds();
                
                if (r1.intersects(r2))
                {
                    m.setVisible(false);
                    a.setVisible(false);
                }
            }
        }
    }
    
    public void moveCraft(String direction)
    {
        craft.move(direction);
    }
    
    public void craftFire()
    {
        craft.fire();
    }
}