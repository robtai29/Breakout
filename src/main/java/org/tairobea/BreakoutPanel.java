package org.tairobea;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;

public class BreakoutPanel extends JPanel implements KeyListener, ActionListener {

    class Block{
        int x;
        int y;
        int dy;
        int dx;
        int height;
        int width;
        boolean exist = true;

        Block(int x, int y){
            this.x = x;
            this.y = y;
            this.dy = 0;
            this.dx = 0;
            this.height = BLOCK_HEIGHT;
            this.width = BLOCK_WIDTH;
        }

        Block(int x, int y, int width, int height, int dx, int dy){
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.dx = dx;
            this.dy = dy;
        }
    }

    public static final int PANEL_WIDTH = 800, PANEL_HEIGHT = 600;
    public static final int BLOCK_HEIGHT = 5, BLOCK_WIDTH = 20;

    private final int blockCols = 8;
    private final int blockRows = 4;
    private Block player;
    private Block ball;
    private Timer gameLoop;
    private Block[] blocks;
    private Random random;
    private boolean play = true;
    private int score = 0;
    private int totalBricks = 0;

    private int brickX = 60;
    private int brickY = 40;
    private int brickWidth = 80;
    private int brickHeight = 20;

    private int playerX = 350;
    private int playerY = 450;

    private int playerWidth = 80;
    private int playerHeight = 10;

    private int playerVelocity = 20;
    private int ballPosX = 450;
    private int ballPosY = 350;
    private int ballVelocityX = -2;
    private int ballVelocityY = -2;

    private final int ballLength = 20;

    public BreakoutPanel(){
        setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
        setBackground(Color.BLACK);
        addKeyListener(this);
        setFocusTraversalKeysEnabled(false);
        setFocusable(true);
        gameLoop = new Timer(1000/60, this);
        blocks = new Block[blockCols * blockRows];
        createBlocks();
        totalBricks = blockCols * blockRows;
        player = new Block(playerX, playerY, playerWidth, playerHeight, 0, 0);
        ball = new Block(ballPosX, ballPosY, ballLength, ballLength, ballVelocityX, ballVelocityY);
        gameLoop.start();
    }

    private void createBlocks() {
        int size = blocks.length;
        int separationSpace = 5;
        int count = 0;
        for (int c = 0; c < blockCols && count < size; c++){
            for (int  r = 0; r < blockRows && count < size; r++){
                blocks[count] = new Block(brickX + c * (brickWidth + separationSpace), brickY + r * (brickHeight + separationSpace),
                                          brickWidth,
                                          ballLength,
                                      0,
                                      0
                                         );
                count++;
            }
        }
    }

    private void move(){
        ball.x += ballVelocityX;
        ball.y += ballVelocityY;

        checkWallCollision();
        checkCollisionPlayer();
        checkCollisionBricks();
        checkGameEnding();
    }

    private void checkGameEnding() {
        if (ball.y > PANEL_HEIGHT){
            play = false;
        }
    }


    private void checkCollisionBricks() {
        for (Block brick: blocks){
            if (brick.exist && checkCollision(brick, ball)){
                brick.exist = false;
                totalBricks--;
                score += 10;
                if (totalBricks == 0){
                    play = false;
                }

                if (ball.x < brick.x + brick.width && ball.x + ball.width > brick.x){
                    ballVelocityY *= -1;
                    ball.y += ballVelocityY;
                }

                if (ball.y < brick.y + brick.height && ball.y + ball.height > brick.y){
                    ballVelocityX *= -1;
                    ball.x += ballVelocityX;
                }
                    break;
            }
        }
    }

    private void checkWallCollision() {
        if (ball.x <= 0 || ball.x + ballLength >= PANEL_WIDTH){
            ballVelocityX *= -1;

            ball.x += 2 * ballVelocityX;
            ball.y += 2 * ballVelocityY;
        }

        if (ball.y <= 0){
            ballVelocityY *= -1;

            ball.x += 2 * ballVelocityX;
            ball.y += 2 * ballVelocityY;
        }

    }

    private boolean checkCollision(Block a, Block b) {
        return  a.x < b.x + b.width &&  //a's top left corner doesn't reach b's top right corner
                a.x + a.width > b.x &&  //a's top right corner passes b's top left corner
                a.y < b.y + b.height && //a's top left corner doesn't reach b's bottom left corner
                a.y + a.height > b.y;   //a's bottom left corner passes b's top left corner

    }

    private void checkCollisionPlayer(){
        if (checkCollision(ball, player)){
            ballVelocityY *= -1;
            ball.x += 2 * ballVelocityX;
            ball.y += 2 * ballVelocityY;
        }
    }



    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
        if (!play){
            gameLoop.stop();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_LEFT && player.x - playerVelocity >= 0) {
            player.x -= playerVelocity;
            System.out.println(player.x);
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT && player.x + playerVelocity + player.width <= PANEL_WIDTH) {
            player.x += playerVelocity;
            System.out.println("meow");
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }

    private void draw(Graphics g){
        drawPlayer(g);
        drawBall(g);
        drawBricks(g);
        drawScore(g);
    }

    private void drawScore(Graphics g){
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.PLAIN, 20));
        g.drawString("Score: " + String.valueOf((int) score), 10, 20);
        if (!play){
            g.setColor(Color.RED);
            g.drawString("game over, mijo.", 120, 20);
        }

        if (totalBricks == 0){
            g.setColor(Color.YELLOW);
            g.drawString("You won, good job!", 120, 20);
        }
    }


    private void drawPlayer(Graphics g){
        g.setColor(Color.PINK);
        g.fillRect(player.x, player.y, player.width, player.height);
    }

    private void drawBall(Graphics g){
        g.setColor(Color.LIGHT_GRAY);
        g.fillOval(ball.x, ball.y, ball.width, ball.height);
    }

    private void drawBricks(Graphics g){
        g.setColor(Color.WHITE);
        for (Block block: blocks){
            if (block.exist) {
                g.fillRect(block.x, block.y, block.width, block.height);
            }
        }

    }

}
