package game;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.border.LineBorder;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.RTextScrollPane;

public class CodeCombatGame extends JFrame
{
    RSyntaxTextArea codeArea;
    public static Board gameBoard;
    JMenuBar menubar;
    JLabel statusbar;
    
    public CodeCombatGame()
    {
        codeArea = new RSyntaxTextArea(20, 60);
        codeArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVA);
        codeArea.setCodeFoldingEnabled(true);
        codeArea.setAutoIndentEnabled(true);
        codeArea.setCloseCurlyBraces(true);
        
        RTextScrollPane codePane = new RTextScrollPane(codeArea);
        codePane.setFoldIndicatorEnabled(true);
        codePane.createVerticalScrollBar();
        
        gameBoard = new Board();
        
        menubar = new JMenuBar();
        JMenu file = new JMenu("File");
        JMenuItem run = new JMenuItem("Run");
        JMenuItem quit = new JMenuItem("Quit");
        run.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                runCode();
            }
        });
        
        quit.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                System.exit(0);
            }
        });
        file.add(run);
        file.add(quit);
        menubar.add(file);
        setJMenuBar(menubar);
        
        JToolBar toolbar = new JToolBar();
        toolbar.setFloatable(false);
        
        JToolBar vertical = new JToolBar(JToolBar.VERTICAL);
        vertical.setFloatable(false);
        vertical.setMargin(new Insets(10, 5, 5, 5));
        
        statusbar = new JLabel(" Idle");
        statusbar.setPreferredSize(new Dimension(-1, 22));
        statusbar.setBorder(LineBorder.createGrayLineBorder());
        
        // menubar shortcuts
        run.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, KeyEvent.CTRL_MASK));
        quit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, KeyEvent.CTRL_MASK));
        
        // add components to game
        add(gameBoard, BorderLayout.CENTER);
        add(toolbar, BorderLayout.NORTH);
        add(codePane, BorderLayout.WEST);
        
        add(statusbar, BorderLayout.SOUTH);
        
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int weight = screenSize.width;
        int height = screenSize.height - 40;
        setSize(new Dimension(weight, height));
        setTitle("Code Combat");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }
    
    private void setupFocus()
    {
        gameBoard.setFocusable(true);
        gameBoard.addMouseListener(new MouseAdapter()
        {
            public void mouseClicked(MouseEvent event)
            {
                if (!gameBoard.isFocusOwner())
                {
                    gameBoard.requestFocus();
                }
            }
        });
        AbstractAction toggleFocus = new AbstractAction()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                toggleFocus();
            }
        };
        KeyStroke fireKeyStroke = KeyStroke.getKeyStroke("control TAB");
        gameBoard.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(fireKeyStroke, "control TAB");
        gameBoard.getActionMap().put("control TAB", toggleFocus);
    }
    
    public class CodeRunnable implements Runnable
    {
        public void run()
        {
            statusbar.setText(" Running code...");
            gameBoard.requestFocusInWindow();
            String code = codeArea.getText();
            String[] commands = code.split("\\s");
            for (String command : commands)
            {
                if (command.equals("left"))
                {
                    gameBoard.moveCraft("LEFT");
                }
                else if (command.equals("right"))
                {
                    gameBoard.moveCraft("RIGHT");
                }
                else if (command.equals("fire"))
                {
                    gameBoard.craftFire();
                }
                try
                {
                    Thread.sleep(50);
                }
                catch (InterruptedException e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            statusbar.setText(" Idle");
        }
    }
    
    private void runCode()
    {
        
        Thread codeExecution = new Thread(new CodeRunnable());
        codeExecution.start();
    }
    
    private void toggleFocus()
    {
        if (gameBoard.isFocusOwner())
        {
            codeArea.requestFocusInWindow();
        }
        else
        {
            gameBoard.requestFocusInWindow();
        }
    }
    
    public static class GameBoardRunnable implements Runnable
    {
        public void run()
        {
            gameBoard = new Board();
            while (true)
            {
                gameBoard.repaint();
                try
                {
                    Thread.sleep(16);
                }
                catch (InterruptedException e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }
    
    public static void main(String[] args)
    {
        // Thread gameBoardUpdate = new Thread(new GameBoardRunnable());
        // gameBoardUpdate.start();
        SwingUtilities.invokeLater(new Runnable()
        {
            public void run()
            {
                CodeCombatGame game = new CodeCombatGame();
                game.setVisible(true);
                game.setupFocus();
            }
        });
        
    }
}
