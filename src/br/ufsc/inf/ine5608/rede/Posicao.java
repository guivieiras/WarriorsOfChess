/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufsc.inf.ine5608.rede;

import br.ufsc.inf.leobr.cliente.Jogada;


/**
 *
 * @author Guilherme
 */
public class Posicao implements Jogada {
    private int x, y;
    public Posicao(int x, int y){
        this.x = x;
        this.y = y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 17 * hash + this.x;
        hash = 17 * hash + this.y;
        return hash;
    }
    
    
    @Override
    public boolean equals(Object obj){
        if (obj != null){
            if (obj.getClass() == Posicao.class)
            {
                Posicao pos = (Posicao) obj;
                return pos.getX() == this.getX() && pos.getY() == this.getY();
            }
        }
        return false;
    }
    
    
}
