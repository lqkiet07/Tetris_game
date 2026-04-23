/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package main;

import javax.swing.JFrame;

/**
 *
 * @author smth
 */ 
public class Main {

    public static void main(String[] args) {
        JFrame window = new JFrame("Tetris game");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //close completely a program
        window.setResizable(false); //can't resize
        
        //add gamePanel to window
        GamePanel gp = new GamePanel();
        window.add(gp);
        window.pack();//size of gp become size of window
        
        window.setLocationRelativeTo(null); // put program to center
        window.setVisible(true); // show window
        gp.launchGame();
    }
}
