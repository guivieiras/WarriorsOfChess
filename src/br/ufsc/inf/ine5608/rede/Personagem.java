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
public class Personagem {
    private int[][] matrizDeAtaque;
    private int[][] matrizDeMovimentacao;
    private Posicao posicao;
    private TipoGuerreiro tipoGuerreiro;
    
  
    
    public Personagem(int x, int y, TipoGuerreiro tipoGuerreiro){
        posicao = new Posicao(x, y);
        this.tipoGuerreiro = tipoGuerreiro;
    }

    public TipoGuerreiro getTipoGuerreiro() {
        return tipoGuerreiro;
    }

    public Posicao getPosicao() {
        return posicao;
    }
    
    public int[][] getMatrizDeAtaque() {
        return matrizDeAtaque;
    }

    public void setMatrizDeAtaque(int[][] matrizDeAtaque) {
        this.matrizDeAtaque = matrizDeAtaque;
    }

    public int[][] getMatrizDeMovimentacao() {
        return matrizDeMovimentacao;
    }

    public void setMatrizDeMovimentacao(int[][] matrizDeMovimentacao) {
        this.matrizDeMovimentacao = matrizDeMovimentacao;
    }

    public boolean podeMovimentar(Posicao posFinal) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public boolean podeAtacar(Posicao posFinal) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
}
