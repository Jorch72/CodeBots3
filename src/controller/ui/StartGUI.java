package controller.ui;

import controller.Game;

public class StartGUI {
    private MainWindow host;
    public StartGUI(Game game){
        host = new MainWindow(game);
    }
    public void run(){
        host.setVisible(true);
    }
}
