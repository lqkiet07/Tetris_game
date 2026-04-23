/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main;

import java.awt.Color;
//obj for width and height
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;

/**
 *
 * @author smth
 */
public class GamePanel extends JPanel implements Runnable {
    // reuse
    public static final int WIDTH = 1280; // size of window
    public static final int HEIGHT = 720;
    final int FPS = 60;
    Thread gameThread;// to run game loop
    PlayManager pm;
    public static Sound music = new Sound();
    public static Sound se = new Sound();

    public GamePanel() {

        // panel settings
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT)); // set size of window
        this.setBackground(Color.black);
        this.setLayout(null);// can costomize the layout as we want
        // Implement KeyListener
        this.addKeyListener(new KeyHandler());// whenever press a key when window is focused so can get the key input
        this.setFocusable(true);

        pm = new PlayManager();
    }

    // launchgame by activating gameThread
    public void launchGame() {
        gameThread = new Thread(this);
        gameThread.start(); // start a thread will call the run method
        
        music.play(4, true);
        music.loop();
    }

    // implements Runnable
    @Override
    public void run() {
        // game loop
        double drawInterval = 1000000000 / FPS;// 16.6ms to draw a new frame
        double delta = 0;// decide when draw a new frame
        long lastTime = System.nanoTime();
        long currentTime;

        while (gameThread != null) {
            currentTime = System.nanoTime();

            delta += (currentTime - lastTime) / drawInterval;
            lastTime = currentTime;

            if (delta >= 1) {
                update();
                repaint();// call the paintComponent method
                delta--;
            }
        }
    }

    private void update() {
        if (KeyHandler.pausePressed == false && pm.gameOver == false) {
            pm.update();
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;
        pm.draw(g2);
    }
}
