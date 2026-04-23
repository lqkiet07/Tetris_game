/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import mino.Block;
import mino.Mino;
import mino.Mino_I;
import mino.Mino_L1;
import mino.Mino_L2;
import mino.Mino_Square;
import mino.Mino_T;
import mino.Mino_Z1;
import mino.Mino_Z2;

/**
 *
 * @author smth
 */
public class PlayManager {

    // Main Play Area
    final int WIDTH = 300;
    final int HEIGHT = 600;
    public static int left_x;
    public static int right_x;
    public static int top_y;
    public static int bottom_y;

    // Mino
    Mino currentMino;
    final int MINO_START_X;
    final int MINO_START_Y;
    Mino nextMino;
    final int NEXTMINO_X;
    final int NEXTMINO_Y;
    public static ArrayList<Block> staticBlocks = new ArrayList<>();

    public static int dropInterval = 60; // mino drops in every 60 frames or 1 sec
    boolean gameOver;

    // score
    int level = 1;
    int lines;
    int score;
    private java.util.List<String[]> leaderboard = new java.util.ArrayList<>();

    public PlayManager() {

        // Main Play Area Frame
        left_x = (GamePanel.WIDTH / 2) - (WIDTH / 2);
        right_x = left_x + WIDTH;
        top_y = 50;
        bottom_y = top_y + HEIGHT;

        // pos for the mino
        MINO_START_X = left_x + (WIDTH / 2) - Block.SIZE; // center
        MINO_START_Y = top_y + Block.SIZE;

        NEXTMINO_X = right_x + 180;
        NEXTMINO_Y = top_y + 500;

        // set the starting Mino
        currentMino = pickMino();
        currentMino.setXY(MINO_START_X, MINO_START_Y);
        nextMino = pickMino();
        nextMino.setXY(NEXTMINO_X, NEXTMINO_Y);

        refreshLeaderboard();
    }

    private final ArrayList<Mino> minoBag = new ArrayList<>();

    private Mino pickMino() {
        // pick a random mino
        if (minoBag.isEmpty()) {
            minoBag.add(new Mino_I());
            minoBag.add(new Mino_L1());
            minoBag.add(new Mino_L2());
            minoBag.add(new Mino_Square());
            minoBag.add(new Mino_T());
            minoBag.add(new Mino_Z1());
            minoBag.add(new Mino_Z2());
            Collections.shuffle(minoBag);
        }
        return minoBag.remove(0);
    }

    public void update() {
        // check if the currentMino is active
        if (currentMino.active == false) {
            // if the mino is not active, put it into the staticBlocks
            staticBlocks.add(currentMino.b[0]);
            staticBlocks.add(currentMino.b[1]);
            staticBlocks.add(currentMino.b[2]);
            staticBlocks.add(currentMino.b[3]);

            currentMino.deactivating = false;

            // when a mino becomes inactive, check if line(s) can be deleted
            checkDelete();

            // replace the currentMino by the nextMino
            currentMino = nextMino;
            currentMino.setXY(MINO_START_X, MINO_START_Y);
            nextMino = pickMino();
            nextMino.setXY(NEXTMINO_X, NEXTMINO_Y);

            // check if the game is over: new mino collides with existing blocks right at
            // spawn
            boolean hitOnSpawn = false;
            for (int i = 0; i < currentMino.b.length; i++) {
                // get the current mino's block and compare it to all the static blocks
                for (Block sb : staticBlocks) {
                    if (currentMino.b[i].x == sb.x && currentMino.b[i].y == sb.y) {
                        hitOnSpawn = true;
                        break;
                    }
                }
            }

            if (hitOnSpawn) {
                gameOver = true;
                GamePanel.music.stop();
                GamePanel.se.play(2, false);

                // show dialog for user enter user's name
                String name = javax.swing.JOptionPane.showInputDialog(null,
                        "Game over! Enter your name to save the score: ", "Save Score",
                        javax.swing.JOptionPane.PLAIN_MESSAGE);

                // obj for save score only when player entered a valid name
                if (name != null && !name.trim().isEmpty()) {
                    DatabaseManager.saveScore(name.trim(), score, level, lines);
                    refreshLeaderboard();
                }
            }
        } else {
            currentMino.update();
        }
    }

    public void checkDelete() {

        int x = left_x;
        int y = top_y;
        int blockCount = 0;
        int lineCount = 0;

        while (x < right_x && y < bottom_y) {

            for (int i = 0; i < staticBlocks.size(); i++) {
                if (staticBlocks.get(i).x == x && staticBlocks.get(i).y == y) {
                    blockCount++;
                }
            }

            x += Block.SIZE;

            if (x == right_x) {

                // if the blockCount == 10, that means the current line y is all filled with
                // blocks, so we can delete them
                if (blockCount == 10) {

                    for (int i = staticBlocks.size() - 1; i > -1; i--) {
                        // remove all the block in the current y line
                        if (staticBlocks.get(i).y == y) {
                            staticBlocks.remove(i);
                        }

                    }

                    lineCount++;
                    lines++;
                    // Drop speed
                    // if the line score hits a certain number, increase the drop speed
                    // 1 is the fastest
                    if (lines % 10 == 0 && dropInterval > 1) {

                        level++;
                        if (dropInterval > 10) {
                            dropInterval -= 10;
                        } else {
                            dropInterval -= 1;
                        }
                    }

                    // a line has been deleted so need to slide down blocks that are above it
                    for (int i = 0; i < staticBlocks.size(); i++) {
                        // if a block is above the current y line, move it down by the block size
                        if (staticBlocks.get(i).y < y) {
                            staticBlocks.get(i).y += Block.SIZE;
                        }
                    }
                }

                blockCount = 0;
                x = left_x;
                y += Block.SIZE;
            }
        }
        // add score
        if (lineCount > 0) {
            GamePanel.se.play(0, false);
            int[] scoreTable = { 0, 100, 300, 500, 800 };
            int bonus = (lineCount < 4) ? scoreTable[lineCount] : 800;
            score += bonus * level;
        }
    }

    public void refreshLeaderboard() {
        leaderboard = DatabaseManager.getLeaderboard();
    }

    public void draw(Graphics2D g2) {

        // Draw Play Area Frame
        g2.setColor(Color.white);
        g2.setStroke(new BasicStroke(4f)); // thickness of the line
        g2.drawRect(left_x - 4, top_y - 4, WIDTH + 8, HEIGHT + 8);

        // Draw Next Mino Frame
        int x = right_x + 100;
        int y = bottom_y - 200;
        g2.drawRect(x, y, 200, 200);
        g2.setFont(new Font("Arial", Font.PLAIN, 30));
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);// turn on the
                                                                                                          // anti-aliasing
        g2.drawString("NEXT", x + 60, y + 60);

        // draw Score frame
        g2.drawRect(x, top_y, 250, 300);
        x += 40;
        y = top_y + 90;
        g2.drawString("LEVEL: " + level, x, y);
        y += 70;
        g2.drawString("LINES: " + lines, x, y);
        y += 70;
        g2.drawString("SCORE: " + score, x, y);

        // draw leaderboard
        g2.setColor(Color.white);
        g2.drawRect(left_x - 400, top_y - 4, WIDTH + 8, HEIGHT + 8);
        g2.drawString("LEADERBOARD", left_x - 360, top_y + 40);
        // obj for fetch and render top 10 rows
        List<String[]> board = leaderboard;
        int rankY = top_y + 80;
        g2.setFont(new Font("Arial", Font.PLAIN, 18));
        for (int i = 0; i < board.size(); i++) {
            String[] row = board.get(i);
            String line = (i + 1) + ". " + row[0] + "  " + row[1] + " pts";
            g2.drawString(line, left_x - 380, rankY);
            rankY += 50;
        }

        // Draw Ghost Mino
        if (currentMino != null && currentMino.active) {
            int minDrop = Integer.MAX_VALUE;
            for (int i = 0; i < currentMino.b.length; i++) {
                // Calculate distance to the floor
                int drop = bottom_y - (currentMino.b[i].y + Block.SIZE);
                // Check against all static blocks directly below this specific block
                for (Block sb : staticBlocks) {
                    if (sb.x == currentMino.b[i].x && sb.y >= currentMino.b[i].y + Block.SIZE) {
                        int dist = sb.y - (currentMino.b[i].y + Block.SIZE);
                        if (dist < drop) {
                            drop = dist;
                        }
                    }
                }
                if (drop < minDrop) {
                    minDrop = drop;
                }
            }
            // Draw the ghost with 80/255 transparency
            Color c = currentMino.b[0].c;
            g2.setColor(new Color(c.getRed(), c.getGreen(), c.getBlue(), 80));// ghost
            for (int i = 0; i < currentMino.b.length; i++) {
                g2.fillRect(currentMino.b[i].x + 2, currentMino.b[i].y + minDrop + 2, Block.SIZE - 4, Block.SIZE - 4);
            }
        }

        // Draw the currentMino
        if (currentMino != null) {
            currentMino.draw(g2);
        }

        // draw next mino
        nextMino.draw(g2);

        // draw static blocks
        for (int i = 0; i < staticBlocks.size(); i++) {
            staticBlocks.get(i).draw(g2);
        }

        // draw pause or game over
        g2.setColor(Color.YELLOW);
        g2.setFont(g2.getFont().deriveFont(40f));
        if (gameOver == true) {
            x = left_x + 25;
            y = top_y + 320;
            g2.drawString("GAME OVER", x, y);
        } else if (KeyHandler.pausePressed) {
            x = left_x + 70;
            y = top_y + 320;
            g2.drawString("PAUSED", x, y);
        }
    }
}