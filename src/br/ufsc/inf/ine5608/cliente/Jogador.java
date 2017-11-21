/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufsc.inf.ine5608.cliente;

import br.ufsc.inf.leobr.cliente.Jogada;

/**
 *
 * @author Guilherme
 */
public class Jogador {

    private String nome;
    private Personagem[] personagens = new Personagem[3];
    
    public Jogador(String nome) {
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Personagem[] getPersonagens() {
        return personagens;
    }

    public void setPersonagen(int index, Personagem personagem) {
        personagens[index] = personagem;
    }
    
    public boolean todosPersonagensMortos(){
        int mortos = 0;
        for (Personagem p : personagens) {
            boolean estaMorto = !p.isVivo();
            if (estaMorto) {
                mortos++;
            }
        }
        if (mortos == 3) {
            return true;
        }
        return false;
    }
   
}
