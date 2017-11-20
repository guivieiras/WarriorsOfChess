/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufsc.inf.ine5608.cliente;

import br.ufsc.inf.ine5608.rede.Lance;
import br.ufsc.inf.ine5608.rede.Posicao;

/**
 *
 * @author Guilherme
 */
public class LanceValido {
    private Lance lance;
    private Personagem atuante;
    private Personagem alvo;
    
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

    public Personagem getAlvo() {
        return alvo;
    }

    public void setAlvo(Personagem alvo) {
        this.alvo = alvo;
    }    
}
