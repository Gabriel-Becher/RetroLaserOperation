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
import java.awt.Point;
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
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
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
    private List<Meteor> meteors;
    
    private int mouseX;
    private int mouseY;
    
    private boolean left;
    private boolean right;
    
    private boolean isShooting;
    
    private int meteorTimer = 0;
    
    private int laserTimer = 0;
    
    private int score = 0;
    private int level = 1;
    private int energy = 100;
    private STATES state;
    
    private int refreshEnergy = 0;
    
    List<Bresenham.Passo> lastShot = null;
    
    private Font menuFont;
    
    
    public GamePanel(){
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        
        setBackground(Color.WHITE);
        
        setFocusable(true);
        
        addKeyListener(this);
        
        addMouseListener(this);
        
        addMouseMotionListener(this);
        
        state = STATES.MENU;
        
        robot = new Robot(WIDTH/5.0);
        
        meteors = new ArrayList<Meteor>();
        
        loadFont();
        
        timer = new Timer(Math.round(1000/FRAMES), this);
        
        timer.start();
    }
    
    private void updateGame(){
        int newLevel = score / 1000 + 1;

        if (newLevel > level) {
            level = newLevel;
            energy =100;
            robot.canShoot = true;
        }
        if(++refreshEnergy>=120){
            energy = Math.min(energy+5,100);
            refreshEnergy = 0;
        }
        if(refreshEnergy >= 120 && energy == 100){
            refreshEnergy = 0;
        }
        
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
        
        meteorTimer++;

        if (meteorTimer >= 60) {

            meteors.add(
                    new Meteor(level)
            );

        meteorTimer = 0;
}
        for(Meteor meteor: meteors){
            meteor.update();
            if(meteor.hit()){
                state = STATES.OVER;
                break;
            }
        }
        if(state == STATES.OVER){
            meteors.clear();
        }
        meteors.removeIf(
            Meteor::isOutside
            );
        if(laserTimer>0){
            laserTimer--;
        }
    
    
    }
    
    private void resetGame(){
        meteors.clear();
        robot.x = WIDTH/2;
        level=1;
        energy=100;
        score = 0;
        robot.canShoot = true;
        robot.recoil = 0;
        meteorTimer = 0;
        left = false;
        right = false;
        laserTimer = 0;
    }
    
    private void loadFont(){
        try{
            menuFont = Font.createFont(Font.TRUETYPE_FONT, getClass().getResourceAsStream("/Barrio-Regular.ttf"));
            menuFont = menuFont.deriveFont(48f);
            
        }catch(Exception e){
            e.printStackTrace();
            
            menuFont = new Font("SansSerif",Font.BOLD, 48);
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
        g2d.setFont(menuFont);
        g2d.setColor(Color.black);
        g2d.fill(new Rectangle2D.Double(0, 0, WIDTH, HEIGHT));
        
        //sombra
        g2d.setColor(new Color(200,200,200,50));
        drawStringOnCenter(g2d, "RetroLaser", 5,5);
        GradientPaint titleGradient =
            new GradientPaint(
                    0, HEIGHT/3,
                    Color.YELLOW,
                    0, HEIGHT*2/3,
                    Color.RED
            );
        g2d.setPaint(titleGradient);
        
        drawStringOnCenter(g2d, "RetroLaser", 0,0);
        
        g2d.setFont(menuFont.deriveFont(20f));
        g2d.setColor(new Color(200,200,200,50));
        drawStringOnCenter(g2d,"Pressione qualquer tecla para iniciar", 55,5);
        g2d.setPaint(titleGradient);
        drawStringOnCenter(g2d,"Pressione qualquer tecla para iniciar", 50,0);
        
    }
    
    private void drawLaser(Graphics2D g2d){
        if(laserTimer <=0 || lastShot == null){
            return;
        }
        List<Bresenham.Passo> pontos = lastShot;
        g2d.setColor(Color.pink);
        for(Bresenham.Passo ponto: pontos){
            g2d.fillRect(ponto.x-1, ponto.y-1, 4, 4);
        }
        
        g2d.setColor(Color.red);
        for(Bresenham.Passo ponto: pontos){
            g2d.fillRect(ponto.x, ponto.y, 2, 2);
        }
    }

    private void drawPause(Graphics2D g2d) {

        drawGame(g2d);

        g2d.setColor(
                new Color(255, 255, 255, 180)
        );

        g2d.fillRect(
                0,
                0,
                WIDTH,
                HEIGHT
        );

        g2d.setColor(Color.BLACK);

        g2d.setFont(menuFont);

        drawStringOnCenter(g2d,"PAUSE",0,0);
        g2d.setFont(menuFont.deriveFont(24f));
        drawStringOnCenter(g2d,"Pressione ESC para continuar", 12,0);
}

    private void drawGame(Graphics2D g2d) {
        robot.draw(g2d);
        
        for(Meteor x: meteors){
            x.draw(g2d);
        }
        
        drawLaser(g2d);
        
        drawHud(g2d);
    }

    private void drawOver(Graphics2D g2d) {
        g2d.setFont(menuFont);
        g2d.setColor(Color.red);
        drawStringOnCenter(g2d, "GAME OVER", 0,0);
        g2d.setFont(menuFont.deriveFont(24f));
        drawStringOnCenter(g2d, "Pressione qualquer tecla para reiniciar", 12,0);
    }
    
    private void drawStringOnCenter(Graphics2D g2d,String text,int xOffSet, int yOffSet
    ) {

        FontMetrics metrics = g2d.getFontMetrics();

        int textWidth =metrics.stringWidth( text);
        
        int textHeight = metrics.getHeight();

        int x =(WIDTH - textWidth)/ 2+yOffSet;

        int centerY = ((HEIGHT - textHeight)/2)+xOffSet;
        
        g2d.drawString(text,x,centerY);
    }
    
    private void drawHud(Graphics2D g2d){
        
    Shape background = new Rectangle2D.Double(0,0,WIDTH, 50);
    
    g2d.setColor(Color.white);
    g2d.fill(background);

    g2d.draw(background);
    // linha separando o HUD do jogo
    Shape linha1 = new Line2D.Double(0,50, WIDTH,50);

    g2d.setColor(Color.BLACK);
    g2d.draw(linha1);

    g2d.setFont(menuFont.deriveFont(20f));

    g2d.setColor(Color.BLACK);

    String piloto = "PILOTO: GABRIEL";

    g2d.drawString(piloto, 20, 32);

    String scoreText = "SCORE: " + score;

    g2d.drawString( scoreText,250, 32);

    String levelText ="LEVEL: " + level;

    g2d.drawString(levelText,430,32);

    String energyText = "ENERGIA: " + energy + "%";

    g2d.drawString(energyText,590,32);
}

    @Override
    public void actionPerformed(java.awt.event.ActionEvent e) {
        if(state == STATES.PLAYING){
            updateGame();        
        }
        repaint();
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {

        int key = e.getKeyCode();

        if(state == STATES.MENU){
            state = STATES.PLAYING;
            return;
        }

        if(state == STATES.OVER){
            resetGame();
            state = STATES.PLAYING;
            return;
        }

        if(state == STATES.PAUSED){
            if(key == KeyEvent.VK_ESCAPE){
                state = STATES.PLAYING;
            }
            return;
        }

        if(key == KeyEvent.VK_ESCAPE){

            state = STATES.PAUSED;

            left = false;
            right = false;

            return;
        }

        if(key == KeyEvent.VK_LEFT|| key == KeyEvent.VK_A){
            left = true;
        }

        if(key == KeyEvent.VK_RIGHT|| key == KeyEvent.VK_D){
            right = true;
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
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if(state == STATES.PLAYING){
            if(!robot.canShoot && energy<10) return;
            
            lastShot = Bresenham.bresenhamAlgorithm(robot.getCannonTip(), new Point(e.getX(), e.getY()));
            robot.shoot(e.getX(), e.getY());
            laserTimer = 8;
            Iterator<Meteor> iterator = meteors.iterator();
            this.energy+=-10;
            this.energy = Math.max(this.energy, 0);
            if(this.energy == 0) robot.canShoot = false;
            List<Bresenham.Passo> pontos = lastShot;
            boolean spent = false;
            while(iterator.hasNext()){
                Meteor meteor = iterator.next();
                for(Bresenham.Passo ponto: pontos){
                    if(meteor.isShot(ponto.x, ponto.y)&&!spent){
                    iterator.remove();
                    score+=100;
                    spent = true;
                    break;
                    }
                }
                
            }
            
        }
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
