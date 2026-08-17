/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.retrolaseroperation;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 *
 * @author gabri
 */
public class GamePanel extends JPanel implements ActionListener, KeyListener, MouseMotionListener, MouseListener{
    
    public static final int WIDTH = 800;
    public static final int HEIGHT = 600;
    public static final int FRAMES = 60;

    private enum STATES{
        PAUSED, PLAYING, OVER, MENU
    };
    
    private final Timer timer;
    
    private Robot robot;
    
    private int mouseX;
    private int mouseY;
    
    private boolean left;
    private boolean right;
    
    private boolean isShooting;
    
    private int score = 0;
    private int level = 1;
    private int energy = 100;
    private STATES state;
    
    
    public GamePanel(){
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        
        setBackground(Color.WHITE);
        
        setFocusable(true);
        
        addKeyListener(this);
        
        addMouseMotionListener(this);
        
        addMouseListener(this);
        
        state = STATES.MENU;
        
        robot = new Robot(WIDTH/5.0);
        
        timer = new Timer(Math.round(1000/FRAMES), this);
        
        timer.start();
    }
    
    private void updateGame(){
        double speed = 5;
        if(left){
            robot.x -= speed;
            robot.x = Math.max(robot.x, 0);
        }
        if(right){
            robot.x += speed;
            robot.x = Math.min(robot.x, WIDTH-100);
        }
        robot.recoil*=0.8;
        if(robot.recoil<0.1){
            robot.recoil = 0;
        }
    
}
    
    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        
        Graphics2D g2d = (Graphics2D) g.create();
        
        switch(state){
            case MENU -> drawMenu(g2d);
            case PAUSED -> drawPause(g2d);
            case PLAYING -> drawGame(g2d);
            case OVER -> drawOver(g2d);
            default -> drawMenu(g2d);
        }
        g2d.dispose();
    }
    
    private void drawMenu(Graphics2D g2d) {
        drawStringOnCenter(g2d, "Teste G2D", 0);
    }

    private void drawPause(Graphics2D g2d) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    private void drawGame(Graphics2D g2d) {
        robot.draw(g2d);
    }

    private void drawOver(Graphics2D g2d) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
    private void drawStringOnCenter(
            Graphics2D g2d,
            String text,
            int offSet
    ) {

        FontMetrics metrics =
                g2d.getFontMetrics();

        int textWidth =
                metrics.stringWidth(
                        text
                );
        
        int textHeight = metrics.getHeight();

        int x =
                (WIDTH - textWidth)
                / 2;

        int centerY = ((HEIGHT - textHeight)/2)+offSet;
        
        /*
         * Aqui centerY representa
         * aproximadamente a linha-base.
         */

        g2d.drawString(
                text,
                x,
                centerY
        );
    }

    @Override
    public void actionPerformed(java.awt.event.ActionEvent e) {
        updateGame();

        repaint();
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(state != STATES.PLAYING){
            state = STATES.PLAYING;
        }else{
            int key = e.getKeyCode();
            if(key == KeyEvent.VK_LEFT || key == KeyEvent.VK_A){
                left = true;
            }
            if(key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_D){
                right = true;
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
            if(key == KeyEvent.VK_LEFT || key == KeyEvent.VK_A){
                left = false;
            }
            if(key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_D){
                right = false;
            }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        robot.aimAt(e.getX(), e.getY());
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if(state == STATES.PLAYING){
            robot.shoot(e.getX(), e.getY());
        }
        
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }
    @Override
    public void mouseReleased(MouseEvent e) {
    }
    @Override
    public void mouseEntered(MouseEvent e) {
    }
    @Override
    public void mouseExited(MouseEvent e) {
    }
    
    
    
}
