/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufsc.inf.ine5608.rede;

/**
 *
 * @author Guilherme
 */
public class LanceValido {
    private Lance lance;
    private Personagem atuante;
    private Posicao posicaoFinal;
    private Personagem alvo;
    
    private TipoDeJogada tipo;

    public Lance getLance() {
        return lance;
    }

    public void setLance(Lance lance) {
        this.lance = lance;
    }

    public Personagem getAtuante() {
        return atuante;
    }

    public void setAtuante(Personagem atuante) {
        this.atuante = atuante;
    }

    public Posicao getPosicaoFinal() {
        return posicaoFinal;
    }

    public void setPosicaoFinal(Posicao posicaoFinal) {
        this.posicaoFinal = posicaoFinal;
    }

    public Personagem getAlvo() {
        return alvo;
    }

    public void setAlvo(Personagem alvo) {
        this.alvo = alvo;
    }

    public TipoDeJogada getTipo() {
        return tipo;
    }

    public void setTipo(TipoDeJogada tipo) {
        this.tipo = tipo;
    }
    
    
}
