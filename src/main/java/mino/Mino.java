/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mino;

import java.awt.Color;
import java.awt.Graphics2D;
import main.GamePanel;
import main.KeyHandler;
import main.PlayManager;

/**
 *
 * @author smth
 */
public class Mino {
    public Block b[] = new Block[4];
    public Block tempB[] = new Block[4];//to handle collision, when it happend we can restore the og pos
    int autoDropCounter = 0;
    public int direction = 1;// there are 4 directions (1/2/3/4)
    boolean leftCollision, rightCollision, bottomCollision;
    public boolean active = true;
    public boolean deactivating;
    int deactivateCouter = 0;
    
    public void create(Color c){
        b[0] = new Block(c);
        b[1] = new Block(c);
        b[2] = new Block(c);
        b[3] = new Block(c);
        tempB[0] = new Block(c);
        tempB[1] = new Block(c);
        tempB[2] = new Block(c);
        tempB[3] = new Block(c);     
    }
    
    public void setXY(int x, int y){}
    public void updateXY(int direction){
        checkRotationCollision();// check the tempB before rotate if it's not collision then rotate
        if(rightCollision == false && leftCollision == false && bottomCollision == false){
            this.direction = direction;
            b[0].x = tempB[0].x;
            b[0].y = tempB[0].y;
            b[1].x = tempB[1].x;
            b[1].y = tempB[1].y;
            b[2].x = tempB[2].x;
            b[2].y = tempB[2].y;
            b[3].x = tempB[3].x;
            b[3].y = tempB[3].y;
        }
    }
    
    public void getDirection1() {}
    public void getDircetion2() {}
    public void getDircetion3() {}
    public void getDircetion4() {}
    
    private void checkMovementCollision(){
        
        leftCollision = false;
        rightCollision = false;
        bottomCollision = false;
        
        checkStaticBlockCollision();
        
        //check frame collision
        //left wall
        for(int i = 0; i < b.length; i++){
            if(b[i].x == PlayManager.left_x){ // dont add the block size cause the block has created from the left alr
                leftCollision = true;
            }
        }
        //right wall
        for(int i = 0; i < b.length; i++){
            if(b[i].x + Block.SIZE == PlayManager.right_x){
                rightCollision = true;
            }
        }
        //bottom floor
        for(int i = 0; i < b.length; i++){
            if(b[i].y + Block.SIZE == PlayManager.bottom_y){
                bottomCollision = true;
            }
        }
    }
    private void checkRotationCollision(){
        
        leftCollision = false;
        rightCollision = false;
        bottomCollision = false;
        
        //check static block collision for rotation
        for(int i = 0 ; i < PlayManager.staticBlocks.size(); i++){
            int targetX = PlayManager.staticBlocks.get(i).x;
            int targetY = PlayManager.staticBlocks.get(i).y;
            
            for(int ii = 0; ii < tempB.length; ii++){
                if(tempB[ii].x == targetX && tempB[ii].y == targetY){
                    bottomCollision = true;
                }
            }
        }
        
        //check frame collision
        //left wall
        for(int i = 0; i < b.length; i++){
            if(tempB[i].x < PlayManager.left_x){ // dont add the block size cause the block has created from the left alr
                leftCollision = true;
            }
        }
        //right wall
        for(int i = 0; i < b.length; i++){
            if(tempB[i].x + Block.SIZE > PlayManager.right_x){
                rightCollision = true;
            }
        }
        //bottom floor
        for(int i = 0; i < b.length; i++){
            if(tempB[i].y + Block.SIZE > PlayManager.bottom_y){
                bottomCollision = true;
            }
        }
    }
    private void checkStaticBlockCollision(){
        //all the x,y and targetX,Y is the top left most of the block
        //get one by one block unactive and then compare to all the block in current mino
        for(int i = 0 ; i < PlayManager.staticBlocks.size(); i++){
            int targetX = PlayManager.staticBlocks.get(i).x;
            int targetY = PlayManager.staticBlocks.get(i).y;
            
            //check down
            for(int ii = 0; ii < b.length; ii++){
                if(b[ii].y + Block.SIZE == targetY && b[ii].x == targetX){
                    bottomCollision = true;
                }
            }
            //check left
            for(int ii = 0; ii < b.length; ii++){
                if(b[ii].x - Block.SIZE == targetX && b[ii].y == targetY){
                    leftCollision = true;
                }
            }
            //check right
            for(int ii = 0; ii < b.length; ii++){
                if(b[ii].x + Block.SIZE == targetX && b[ii].y == targetY){
                    rightCollision = true;
                }
            }
        }
    }
    
    public void update(){
        
        // every frame will check this
        if(deactivating){
            deactivating();
        }
        
        //Move the mino
        if(KeyHandler.upPressed){
            switch(direction){
                case 1: getDircetion2(); break;
                case 2: getDircetion3(); break;
                case 3: getDircetion4(); break;
                case 4: getDirection1(); break;
            }
            KeyHandler.upPressed = false;
            GamePanel.se.play(1,false);
        }
        
        if (KeyHandler.spacePressed) {
            // Hard drop logic (same distance calculation as Ghost Piece)
            int minDrop = Integer.MAX_VALUE;
            for (int i = 0; i < b.length; i++) {
                int drop = PlayManager.bottom_y - (b[i].y + Block.SIZE);
                for (Block sb : PlayManager.staticBlocks) {
                    if (sb.x == b[i].x && sb.y >= b[i].y + Block.SIZE) {
                        int dist = sb.y - (b[i].y + Block.SIZE);
                        if (dist < drop) {
                            drop = dist;
                        }
                    }
                }
                if (drop < minDrop) {
                    minDrop = drop;
                }
            }
            
            // Drop it instantly
            b[0].y += minDrop;
            b[1].y += minDrop;
            b[2].y += minDrop;
            b[3].y += minDrop;
            
            // Instantly deactivate and play sound
            active = false;
            GamePanel.se.play(3, false);
            KeyHandler.spacePressed = false;
            return; // Skip the rest of the update
        }
        
        checkMovementCollision();
        
        if(KeyHandler.downPressed){
            //if the mino bottom is not hitting, can go down
            if(bottomCollision == false){
                b[0].y += Block.SIZE;
                b[1].y += Block.SIZE;
                b[2].y += Block.SIZE;
                b[3].y += Block.SIZE;

                autoDropCounter = 0;//when move down, reset the autoDrop
            }
            KeyHandler.downPressed = false;
        }
        if(KeyHandler.leftPressed){
            // if the mino left is not hitting, can go left
            if(leftCollision == false){
                b[0].x -= Block.SIZE;
                b[1].x -= Block.SIZE;
                b[2].x -= Block.SIZE;
                b[3].x -= Block.SIZE;
            }
            KeyHandler.leftPressed = false;
        }
        if(KeyHandler.rightPressed){
            // if the mino right is not hitting, can go right
            if(rightCollision == false){
                b[0].x += Block.SIZE;
                b[1].x += Block.SIZE;
                b[2].x += Block.SIZE;   
                b[3].x += Block.SIZE;
            }
            KeyHandler.rightPressed = false;
        }
        if(bottomCollision){
            if(deactivating == false){
                GamePanel.se.play(3,false);
            }
            deactivating = true;
        }else{
            if (deactivating) {
                // Reset deactivating state if piece slides off a ledge
                deactivating = false;
                deactivateCouter = 0;
            }
            autoDropCounter++; // the counter increases in every frame
            if(autoDropCounter == PlayManager.dropInterval && bottomCollision == false){
                b[0].y += Block.SIZE;
                b[1].y += Block.SIZE;
                b[2].y += Block.SIZE;
                b[3].y += Block.SIZE;
                autoDropCounter = 0;
            }
        }
    }
    
    public void deactivating() {
        
        deactivateCouter++;
        //wait for 45 frame until deactivate
        if(deactivateCouter == 45){
            
            deactivateCouter = 0;
            checkMovementCollision(); // check if the bottom is still hitting
            
            // if the bottom still hitting after 45 frame, deactivate the mino
            if(bottomCollision){
                active = false;
            }
        }
    }
    public void draw(Graphics2D g2){
        
        int margin = 2;//seperate the block
        g2.setColor(b[0].c);
        g2.fillRect(b[0].x+margin, b[0].y+margin, Block.SIZE-(margin*2), Block.SIZE-(margin*2));
        g2.fillRect(b[1].x+margin, b[1].y+margin, Block.SIZE-(margin*2), Block.SIZE-(margin*2));
        g2.fillRect(b[2].x+margin, b[2].y+margin, Block.SIZE-(margin*2), Block.SIZE-(margin*2));
        g2.fillRect(b[3].x+margin, b[3].y+margin, Block.SIZE-(margin*2), Block.SIZE-(margin*2));
        
    }
}
