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
import br.ufsc.inf.ine5608.rede.TipoGuerreiro;

/**
 *
 * @author Guilherme
 */
public class Tabuleiro {

    private Jogador jogador1;
    private Jogador jogador2;

    private Jogador vezDo;

    private boolean partidaEmAndamento;

    private Jogador local;
    private Jogador remoto;

    public Tabuleiro() {

    }

    public void inicializarJogadores(String nome1, String nome2, boolean localJogaPrimeiro) {
        jogador1 = new Jogador(nome1);
        jogador2 = new Jogador(nome2);

        vezDo = jogador1;

        if (localJogaPrimeiro) {
            local = jogador1;
            remoto = jogador2;
        } else {
            local = jogador2;
            remoto = jogador1;
        }
    }

    public Personagem[] inicializarPersonagens() {
        Personagem[] personagens = new Personagem[6];
        int[][] matrix1 = {{0, 1, 0},
        {1, 0, 1},
        {0, 1, 0}};

        int[][] matrix2 = {{0, 1, 0},
        {1, 0, 1},
        {0, 1, 0}};

        int[][] matrix3 = {{0, 1, 0},
        {1, 0, 1},
        {0, 1, 0}};

        Personagem p = new Personagem(0,0, TipoGuerreiro.Mage);
        p.setMatrizDeAtaque(matrix1);
        p.setMatrizDeMovimentacao(matrix2);
        jogador1.setPersonagen(0, p);
        personagens[0] = p;

        p = new Personagem(0,1, TipoGuerreiro.Warrior);
        p.setMatrizDeAtaque(matrix3);
        p.setMatrizDeMovimentacao(matrix1);
        jogador1.setPersonagen(1, p);
        personagens[1] = p;

        p = new Personagem(0,2, TipoGuerreiro.Ranger);
        p.setMatrizDeAtaque(matrix2);
        p.setMatrizDeMovimentacao(matrix3);
        jogador1.setPersonagen(2, p);
        personagens[2] = p;

        p = new Personagem(12,0, TipoGuerreiro.Mage);
        p.setMatrizDeAtaque(matrix1);
        p.setMatrizDeMovimentacao(matrix2);
        jogador2.setPersonagen(0, p);
        personagens[3] = p;

        p = new Personagem(12,1, TipoGuerreiro.Warrior);
        p.setMatrizDeAtaque(matrix3);
        p.setMatrizDeMovimentacao(matrix1);
        jogador2.setPersonagen(1, p);
        personagens[4] = p;

        p = new Personagem(12,2, TipoGuerreiro.Ranger);
        p.setMatrizDeAtaque(matrix2);
        p.setMatrizDeMovimentacao(matrix3);
        jogador2.setPersonagen(2, p);
        personagens[5] = p;
            
        return personagens;
    }

    public void iniciaPartida() {
        partidaEmAndamento = true;
    }

    boolean validarLance(Lance lance) {
        return true;
    }

    void atualizarTabuleiro(Lance lance) {

    }

    public boolean IsPartidaEmAndamento() {
        return partidaEmAndamento;
    }

    public Jogador getJogadorLocal() {
        return local;
    }

    public Jogador getJogadorRemoto() {
        return remoto;
    }

    void encerraPartida() {
        partidaEmAndamento = false;
    }
    
    

}
