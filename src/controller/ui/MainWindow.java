package controller.ui;

import controller.Game;
import controller.Scoreboard;
import controller.bot.CodeBot;

import javax.swing.*;
import javax.swing.event.MouseInputAdapter;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainWindow extends JFrame {
    private List<BotWindow> windows;
    private final MainGrid mainGrid;
    private final JTextArea turnNumber;
    private BotRunner runner;
    private final Random random;
    private Game currentGame;
    public MainWindow(Random random){
        super();
        currentGame = new Game(random);
        Scoreboard.gameStarted();
        this.random = random;
        this.setLayout(new GridLayout());
        mainGrid = new MainGrid(currentGame);
        this.add(mainGrid);
        JMenuBar menu = new JMenuBar();
        this.setJMenuBar(menu);
        JButton start = new JButton("Run");
        JButton stop = new JButton("Stop");
        JButton step = new JButton("Step");
        JButton quit = new JButton("Quit");
        menu.add("Start", start);
        menu.add("Stop", step);
        menu.add("Step", stop);
        menu.add("Quit", quit);
        turnNumber = new JTextArea();
        menu.add(turnNumber);
        windows = new ArrayList<BotWindow>();
        runner = new BotRunner();
        start.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (runner.isCancelled())
                    MainWindow.this.runner = new BotRunner();
                runner.execute();
            }
        });
        stop.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                runner.cancel(true);
            }
        });
        step.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed (ActionEvent e) {
                MainWindow.this.currentGame.step();
                runner.cancel(true);
                repaint();
            }
        });
        quit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        this.pack();
        mainGrid.addMouseListener(new MouseInputAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Point position = new Point(e.getX() / MainGrid.squareWidth, e.getY() / MainGrid.squareHeight);
                CodeBot botUnder = MainWindow.this.currentGame.getMap().getBot(position);
                if (botUnder != null) {
                    final BotWindow bw = new BotWindow(botUnder, MainWindow.this.currentGame);
                    bw.addWindowListener(new WindowAdapter() {
                        @Override
                        public void windowClosing(WindowEvent e) {
                            windows.remove(bw);
                        }
                    });
                    windows.add(bw);
                    bw.setVisible(true);
                }
            }
        });
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }


    class BotRunner extends SwingWorker<Integer, Integer> {

        @Override
        protected Integer doInBackground() throws Exception {
            while (true) {
                while (!currentGame.isFinished()) {
                    this.publish(0);
                    Thread.sleep(0);
                    currentGame.step();
                }
                Scoreboard.gameFinished(currentGame);
                if (Scoreboard.getGameCount() < Game.NUM_GAMES) {
                    currentGame = new Game(random);
                    Scoreboard.gameStarted();
                    mainGrid.setGame(currentGame);
                } else {
                    new ScoreWindow().setVisible(true);
                    return 0;
                }
            }
        }

        @Override
        protected void process(List<Integer> chunks) {
            repaint();
        }
    }

    @Override
    public void repaint() {
        super.repaint();
        for (BotWindow window: windows){
            window.repaint();
            Point position = currentGame.getMap().getBotPosition(window.bot);
            mainGrid.addHighlight(position.x, position.y, Color.red);
        }
        turnNumber.setText("Game " + Scoreboard.getGameCount() + " Turn " + currentGame.getTurnCounter());
    }

}
