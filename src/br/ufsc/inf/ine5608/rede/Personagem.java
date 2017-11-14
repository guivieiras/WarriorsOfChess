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
        for (int[] linha : matrizDeAtaque)
            if (linha.length % 2 == 0)
                throw new RuntimeException("Aceita apenas Matriz com dimensões impares");   
        
        this.matrizDeAtaque = matrizDeAtaque;
    }

    public int[][] getMatrizDeMovimentacao() {
        return matrizDeMovimentacao;
    }

    public void setMatrizDeMovimentacao(int[][] matrizDeMovimentacao) {
        for (int[] linha : matrizDeMovimentacao)
            if (linha.length % 2 == 0)
                throw new RuntimeException("Aceita apenas Matriz com dimensões impares");
        
        this.matrizDeMovimentacao = matrizDeMovimentacao;
    }

    public boolean podeMovimentar(Posicao posFinal) {          
        int indiceX = centerMovimento() + posFinal.getX() - posicao.getX();
        int indiceY = centerMovimento() + posFinal.getY() - posicao.getY();
        
        if (indiceX >= 0 && indiceX < matrizDeMovimentacao.length &&
         indiceY >= 0 && indiceY < matrizDeMovimentacao.length)
            return matrizDeMovimentacao[indiceX][indiceY] == 1;
        else 
            return false;
    }
    
    public boolean podeAtacar(Posicao posFinal) {
        
        int indiceX = centerAtaque() + posFinal.getX() - posicao.getX();
        int indiceY = centerAtaque() + posFinal.getY() - posicao.getY();
        
        if (indiceX >= 0 && indiceX < matrizDeAtaque.length &&
         indiceY >= 0 && indiceY < matrizDeAtaque.length)
            return matrizDeAtaque[indiceX][indiceY] == 1;
        else 
            return false;
    }
     
    private int centerAtaque(){
        return (matrizDeAtaque.length - 1) / 2;
    }
    
    private int centerMovimento(){
        return (matrizDeMovimentacao.length - 1) / 2;
    }

   
    
    
}
