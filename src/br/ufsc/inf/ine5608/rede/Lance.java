/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufsc.inf.ine5608.rede;

import br.ufsc.inf.ine5608.cliente.TipoDeJogada;
import br.ufsc.inf.leobr.cliente.Jogada;

/**
 *
 * @author Guilherme
 */
public class Lance implements Jogada {
    private Posicao posInicial;
    private Posicao posFinal;
    public TipoDeJogada tipoDeJogada;

    public Posicao getPosInicial() {
        return posInicial;
    }

    public void setPosInicial(Posicao posInicial) {
        this.posInicial = posInicial;
    }

    public Posicao getPosFinal() {
        return posFinal;
    }

    public void setPosFinal(Posicao posFinal) {
        this.posFinal = posFinal;
    }

    public TipoDeJogada getTipoDeJogada() {
        return tipoDeJogada;
    }

    public void setTipoDeJogada(TipoDeJogada tipoDeJogada) {
        this.tipoDeJogada = tipoDeJogada;
    }
    
}
