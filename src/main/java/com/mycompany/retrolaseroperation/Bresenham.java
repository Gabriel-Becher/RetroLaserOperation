/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.retrolaseroperation;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author gabri
 */
public class Bresenham {
    public static class Passo{
        int x,y;
        
        public Passo(int x, int y){
            this.x = x;
            this.y = y;
        }
    }
 
    public static List<Passo> bresenhamAlgorithm(Point a, Point b){
        List<Passo> resultado = new ArrayList<>();
        
        //inicialização do algoritmo
        
        int x = a.x;
        int y = a.y;
        
        int dx = Math.abs(b.x-a.x);
        int dy = Math.abs(b.y-a.y);
        
        int sx = x < b.x ? 1 : -1;
        int sy = y< b.y ? 1 : -1;
                
        int decisao = dx - dy;
        
        while(true){
            resultado.add(new Passo(x, y));
        
                if( x== b.x && y == b.y){
                    break;
                }
                
                int dobroDecisao = 2 * decisao;
                                
                if( dobroDecisao > -dy ){
                    decisao -=dy;
                    x+=sx;
                }
                if( dobroDecisao <dx ){
                    decisao +=dx;
                    y+=sy;
                }
                
        }
        
        return resultado;
    }
    
    
    
    
}
