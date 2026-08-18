/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.retrolaseroperation;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.QuadCurve2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;

/**
 *
 * @author gabri
 */
public class Robot {
    protected double x;
    private final int y = 600;
    
    protected double cannonAngle=0;
    protected double recoil = 0;
    protected boolean canShoot = true;
    
    public Robot(double x){
        this.x = x;
    }
    
    public void aimAt(double mouseX, double mouseY){
        double cannonX = this.x+ 50;
        double cannonY = this.y- 80;
        
        double dx = mouseX-cannonX;
        double dy = cannonY-mouseY;
        
        if (mouseY > cannonY) {
            if (mouseX < cannonX) {
                cannonAngle = Math.toRadians(90);
            } else {
                cannonAngle = Math.toRadians(-90);
            }
            return;
        }
        
        cannonAngle = Math.atan2(-dx,dy);
        
                
    }
    
    public void shoot(double mouseX, double mouseY){
        if(canShoot){
            this.recoil =1;
        }
        
    }
    
    public Point getCannonTip() {

        double cannonLength = 35 * (1.0 - recoil * 0.5);

        double localX =50- Math.sin(cannonAngle)* cannonLength;
        double localY = 80+ Math.cos(cannonAngle)* cannonLength;

        int screenX =(int) (x + localX);
        int screenY =(int) (y - localY);

        return new Point(screenX,screenY);
    }
    
    private void drawSquare(Graphics2D g2d, double centerX, double centerY){

        AffineTransform original = g2d.getTransform();
        double angle = x * -0.05;

        g2d.translate(centerX,centerY);
        g2d.rotate(angle,7,7);

        Shape square = new Rectangle2D.Double(0,0,14,14);

        g2d.setColor(Color.BLUE);
        g2d.fill(square);
        g2d.setColor(Color.BLACK);
        g2d.draw(square);
        g2d.setTransform(original);
}
    
    private void drawCannon(Graphics2D g2d, double pivotX, double pivotY){
        AffineTransform original = g2d.getTransform();
        //40 70
        g2d.translate(pivotX, pivotY);
        double recoilScale = 1.0 - recoil * 0.5;
        
        g2d.rotate(cannonAngle);
        g2d.scale(1, recoilScale);
        
        Shape cannon = new Rectangle2D.Double(-5,0,10,35);
        g2d.setColor(Color.GREEN);
        g2d.fill(cannon);
        g2d.setColor(Color.BLACK);

        g2d.draw(cannon);
        
        g2d.setTransform(original);
        
    }
    
    private Shape createRobot(Graphics2D g2d){
        GeneralPath g = new GeneralPath();
        //esteiras
        Shape esteira = new RoundRectangle2D.Double(0,0, 100, 40, 10,10);
        g2d.setColor(Color.decode("#7f7f7f"));
        g2d.fill(esteira);
        g.append(esteira, false);
        //canhao
        drawCannon(g2d, 50,80);
        //corpo
        Shape corpo = new Rectangle2D.Double(30,40,40,40);
        g2d.setColor(Color.decode("#c3c3c3"));
        g2d.fill(corpo);
        g.append(corpo, false);
        
        //olhos
        Shape olhoEsquerdo = new Ellipse2D.Double(37,58,10,10);
        g2d.setColor(Color.CYAN);
        g2d.fill(olhoEsquerdo);
        g.append(olhoEsquerdo, false);
        
        Shape olhoDireito = new Ellipse2D.Double(53,58,10,10);
        g2d.setColor(Color.CYAN);
        g2d.fill(olhoDireito);
        g.append(olhoDireito, false);
        //sobrancelhas
        
        Shape sobEsquerda = new Line2D.Double(37,75,47,72);
        Shape sobDireita = new Line2D.Double(53,72,63,75);
        g.append(sobEsquerda, false);
        g.append(sobDireita, false);
        
        //boca
        Shape boca = new QuadCurve2D.Double(35,45,50,65,65,45);
        g.append(boca, false);
        
        //braços
        Shape bracoDireito = new QuadCurve2D.Double(70,50,80,50, 90, 60);
        g.append(bracoDireito, false);
        Shape bracoEsquerdo = new QuadCurve2D.Double(30,50,20,50, 10, 60);
        g.append(bracoEsquerdo, false);
        
        //mãos
        Shape maoDireita = new QuadCurve2D.Double(90,70,88,58, 100, 60);
        g.append(maoDireita, false);
        Shape maoEsquerda = new QuadCurve2D.Double(0,60,12,58, 10, 70);
        g.append(maoEsquerda, false);
        
        
        //base canhão
        Shape meiaLua = new Arc2D.Double(40,70 , 20, 20, 0, -180, Arc2D.CHORD);
        g2d.setColor(Color.black);
        g2d.fill(meiaLua);
        g.append(meiaLua, false);
        //canhao
       
        //triangulos da esteira
        drawSquare(g2d,20,15);
        drawSquare(g2d,45,15);
        drawSquare(g2d,70,15);
        
        
        
        return g;
    
    }
    
    public void draw(Graphics2D g2d){
        AffineTransform original = g2d.getTransform();
        
        g2d.translate(x, y);
        g2d.scale(1, -1);
        
        Shape robot = createRobot(g2d);
        
        g2d.setStroke(new BasicStroke(3f));
        g2d.setColor(Color.BLACK);
        g2d.draw(robot);
    
        g2d.setTransform(original);
        
    }
    
}
