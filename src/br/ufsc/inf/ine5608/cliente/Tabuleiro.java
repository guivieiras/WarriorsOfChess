/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufsc.inf.ine5608.cliente;

import br.ufsc.inf.ine5608.rede.AtorNetGames;
import br.ufsc.inf.ine5608.rede.Jogador;
import br.ufsc.inf.ine5608.rede.Lance;
import br.ufsc.inf.ine5608.rede.Personagem;

/**
 *
 * @author Guilherme
 */
public class Tabuleiro {

    private Jogador jogador1;
    private Jogador jogador2;
    
    private Jogador vezDo;
    
    private boolean partidaEmAndamento;
    
    
    public Tabuleiro() {
        
    }
    
    public void inicializarJogadores(String nome1, String nome2){
        jogador1 = new Jogador(nome1);
        jogador2 = new Jogador(nome2);
        
        vezDo = jogador1;
    }
    
    public void inicializarPersonagens(){
        int[][] matrix1 = {{ 0, 1, 0},
                           { 1, 0, 1},
                           { 0, 1, 0}};
        
        int[][] matrix2 = {{ 0, 1, 0},
                           { 1, 0, 1},
                           { 0, 1, 0}};
        
        int[][] matrix3 = {{ 0, 1, 0},
                           { 1, 0, 1},
                           { 0, 1, 0}};

        Personagem p = new Personagem();        
        p.setMatrizDeAtaque(matrix1);
        p.setMatrizDeMovimentacao(matrix2);
        jogador1.setPersonagen(0, p);
        
        p = new Personagem();        
        p.setMatrizDeAtaque(matrix3);
        p.setMatrizDeMovimentacao(matrix1);
        jogador1.setPersonagen(1, p);
        
        p = new Personagem();        
        p.setMatrizDeAtaque(matrix2);
        p.setMatrizDeMovimentacao(matrix3);
        jogador1.setPersonagen(2, p);
        
        p = new Personagem();        
        p.setMatrizDeAtaque(matrix1);
        p.setMatrizDeMovimentacao(matrix2);
        jogador2.setPersonagen(0, p);
        
        p = new Personagem();        
        p.setMatrizDeAtaque(matrix3);
        p.setMatrizDeMovimentacao(matrix1);
        jogador2.setPersonagen(1, p);
        
        p = new Personagem();        
        p.setMatrizDeAtaque(matrix2);
        p.setMatrizDeMovimentacao(matrix3);
        jogador2.setPersonagen(2, p);
    }
    
    public void iniciaPartida(){
        partidaEmAndamento = true;
    }

    boolean validarLance(Lance lance) {
        return true;
    }
    
    Jogador getJogadorLocal(boolean localJogaPrimeiro){
        if (localJogaPrimeiro)
            return jogador1;
        else return jogador2;
    }

    void atualizarTabuleiro(Lance lance) {
        
    }
    
    public boolean IsPartidaEmAndamento(){
        return partidaEmAndamento;
    }

    
}
