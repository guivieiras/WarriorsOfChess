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
    
    
}
