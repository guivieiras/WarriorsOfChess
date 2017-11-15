/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufsc.inf.ine5608.rede;

import br.ufsc.inf.leobr.cliente.Jogada;
import java.awt.Image;
import javax.swing.ImageIcon;

/**
 *
 * @author Guilherme
 */ 
public class Personagem {
    private int[][] matrizDeAtaque;
    private int[][] matrizDeMovimentacao;
    private Posicao posicao;
    private TipoGuerreiro tipoGuerreiro;
    private boolean isVivo = true;
    private ImageIcon icon;
    private Jogador owner;

    static int[][] matrix1 = {{0, 1, 0},
        {1, 0, 1},
        {0, 1, 0}};

     static   int[][] matrix2 = {{0, 1, 0},
        {1, 0, 1},
        {0, 1, 0}};

      static  int[][] matrix3 = {
            {0, 0, 1, 0 ,0},
            {0, 0, 1, 0, 0},
            {1, 1, 0, 1, 1},
            {0, 0, 1, 0, 0},
            {0, 0, 1, 0, 0}};
  
       static ImageIcon warrior = new ImageIcon( new javax.swing.ImageIcon(Personagem.class.getResource("/resources/warrior.png")).getImage().getScaledInstance(48, 48, Image.SCALE_AREA_AVERAGING));
      static  ImageIcon mage = new ImageIcon(new javax.swing.ImageIcon(Personagem.class.getResource("/resources/mage.png")).getImage().getScaledInstance(48, 48, Image.SCALE_AREA_AVERAGING));
static        ImageIcon ranger = new ImageIcon( new javax.swing.ImageIcon(Personagem.class.getResource("/resources/ranger.png")).getImage().getScaledInstance(48, 48, Image.SCALE_AREA_AVERAGING));
        
    public Personagem(int x, int y, TipoGuerreiro tipoGuerreiro, Jogador owner){
        posicao = new Posicao(x, y);
        this.tipoGuerreiro = tipoGuerreiro;
        setOwner(owner);
        
        switch(tipoGuerreiro){
            case Mage:
                setMatrizDeAtaque(matrix1);
                setMatrizDeMovimentacao(matrix2);
                setIcon(mage);
                break;
            case Ranger:
                setMatrizDeAtaque(matrix3);
                setMatrizDeMovimentacao(matrix3);
                setIcon(ranger);
                break;
            case Warrior:
                setMatrizDeAtaque(matrix3);
                setMatrizDeMovimentacao(matrix1);
                setIcon(warrior);
                break;
        }
    }

    public Jogador getOwner() {
        return owner;
    }

    public void setOwner(Jogador owner) {
        this.owner = owner;
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

    public void matar() {
        isVivo = false;
        posicao = new Posicao(-1,-1);
    }

    public void setPosicao(Posicao posicao) {
        this.posicao = posicao;
    }

    public void setIcon(ImageIcon icon) {
        this.icon = icon;
    }    
    
    public ImageIcon getIcon() {
        return icon;
    }

    public boolean isVivo() {
        return isVivo;
    }
    

   
    
    
}
