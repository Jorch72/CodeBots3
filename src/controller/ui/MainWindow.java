package controller.ui;

import controller.Game;
import controller.bot.CodeBot;

import javax.swing.*;
import javax.swing.event.MouseInputAdapter;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class MainWindow extends JFrame {
    private List<BotWindow> windows;
    private final MainGrid mainGrid;
    private final JMenuBar menu;
    private final JButton start;
    private final JButton stop;
    private final JButton step;
    private final JButton quit;
    private final JButton finish;
    private final JTextArea turnNumber;
    private BotRunner runner;
    private final Game game;
    public MainWindow(Game game){
        super();
        this.game = game;
        this.setLayout(new GridLayout());
        mainGrid = new MainGrid(game);
        this.add(mainGrid);
        menu = new JMenuBar();
        this.setJMenuBar(menu);
        start = new JButton("Start");
        stop = new JButton("Stop");
        step = new JButton("Step");
        quit = new JButton("Quit");
        finish = new JButton("End of Games");
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
                MainWindow.this.game.step();
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
                CodeBot botUnder = MainWindow.this.game.getMap().getBot(position);
                if (botUnder != null) {
                    final BotWindow bw = new BotWindow(botUnder, MainWindow.this.game);
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
            while(!game.isFinished()) {
                this.publish(0);
                Thread.sleep(0);
                game.step();
            }
            return 0;
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
            Point position = game.getMap().getBotPosition(window.bot);
            mainGrid.addHighlight(position.x, position.y, Color.red);
        }
        turnNumber.setText("Game " + game.getScoreboard().getGameCount() + " Turn " + game.getTurnCounter());
        if (game.isFinished()){
            new ScoreWindow(game.getScoreboard()).setVisible(true);
        }
    }

}
