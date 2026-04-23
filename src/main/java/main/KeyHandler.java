/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 *
 * @author smth
 */
public class KeyHandler implements KeyListener{

    public static boolean upPressed, downPressed, leftPressed, rightPressed, pausePressed, spacePressed;
    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();
        
        if(code == KeyEvent.VK_W || code == KeyEvent.VK_UP){
            upPressed = true;
        }
        if(code == KeyEvent.VK_A || code == KeyEvent.VK_LEFT){
            leftPressed = true;
        }
        if(code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN){
            downPressed = true;
        }
        if(code == KeyEvent.VK_D || code == KeyEvent.VK_RIGHT){
            rightPressed = true;
        }
        if(code == KeyEvent.VK_SPACE){
            spacePressed = true;
        }
        if(code == KeyEvent.VK_ESCAPE || code == KeyEvent.VK_P){
            if(pausePressed){
                pausePressed = false;
                GamePanel.music.play(4, true);
                GamePanel.music.loop();
            }else{
                pausePressed = true;
                GamePanel.music.stop();
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}
    
}
