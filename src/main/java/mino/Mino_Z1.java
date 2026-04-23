/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mino;

import java.awt.Color;

/**
 *
 * @author smth
 */
public class Mino_Z1 extends Mino{
    public Mino_Z1(){
        create(Color.RED);
    }
    @Override
    public void setXY(int x, int y){
        //   o
        // o o
        // o
        b[0].x = x;
        b[0].y = y;
        b[1].x = b[0].x;
        b[1].y = b[0].y - Block.SIZE;
        b[2].x = b[0].x - Block.SIZE;
        b[2].y = b[0].y;
        b[3].x = b[0].x - Block.SIZE;
        b[3].y = b[0].y + Block.SIZE;
    }
    @Override
    public void getDirection1() {
        //   o
        // o o
        // o
        tempB[0].x = b[0].x;
        tempB[0].y = b[0].y;
        tempB[1].x = b[0].x;
        tempB[1].y = b[0].y - Block.SIZE;
        tempB[2].x = b[0].x - Block.SIZE;
        tempB[2].y = b[0].y;
        tempB[3].x = b[0].x - Block.SIZE;
        tempB[3].y = b[0].y + Block.SIZE;
        
        updateXY(1);
    }
    @Override
    public void getDircetion2() {
        // o o
        //   o o
        // 
        tempB[0].x = b[0].x;
        tempB[0].y = b[0].y;
        tempB[1].x = b[0].x + Block.SIZE;
        tempB[1].y = b[0].y;
        tempB[2].x = b[0].x;
        tempB[2].y = b[0].y - Block.SIZE;
        tempB[3].x = b[0].x - Block.SIZE;
        tempB[3].y = b[0].y - Block.SIZE;
        
        updateXY(2);
    }
    @Override
    public void getDircetion3() {
        getDirection1();
    }
    @Override
    public void getDircetion4() {
        getDircetion2();
    }
    
}
