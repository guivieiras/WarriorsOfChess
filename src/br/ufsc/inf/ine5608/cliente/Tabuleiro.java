/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufsc.inf.ine5608.cliente;

import br.ufsc.inf.ine5608.rede.AtorNetGames;
import br.ufsc.inf.ine5608.rede.Jogador;
import br.ufsc.inf.ine5608.rede.Lance;
import br.ufsc.inf.ine5608.rede.LanceValido;
import br.ufsc.inf.ine5608.rede.Personagem;
import br.ufsc.inf.ine5608.rede.Posicao;
import br.ufsc.inf.ine5608.rede.TipoDeJogada;
import br.ufsc.inf.ine5608.rede.TipoGuerreiro;
import java.awt.Image;
import javax.swing.ImageIcon;

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

    private AtorJogador tela;
    
     public static final int LINHAS = 13;
     public static final int COLUNAS = 10;
    
    public Tabuleiro(AtorJogador tela) {
        this.tela = tela;
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
       

        Personagem p = new Personagem(0, 0, TipoGuerreiro.Mage, jogador1);
        jogador1.setPersonagen(0, p);
        personagens[0] = p;

        p = new Personagem(0, 1, TipoGuerreiro.Warrior, jogador1);
        jogador1.setPersonagen(1, p);
        personagens[1] = p;

        p = new Personagem(0, 2, TipoGuerreiro.Ranger, jogador1);
        jogador1.setPersonagen(2, p);
        personagens[2] = p;

        p = new Personagem(12, 0, TipoGuerreiro.Mage, jogador2);
        jogador2.setPersonagen(0, p);
        personagens[3] = p;

        p = new Personagem(12, 1, TipoGuerreiro.Warrior, jogador2);
        jogador2.setPersonagen(1, p);
        personagens[4] = p;

        p = new Personagem(12, 2, TipoGuerreiro.Ranger, jogador2);
        jogador2.setPersonagen(2, p);
        personagens[5] = p;

        return personagens;
    }

    public void iniciaPartida() {
        partidaEmAndamento = true;
    }
    
    LanceValido validarLanceLocal(Lance lance){
        return validarLance(lance, local, remoto);
    }
    LanceValido validarLanceRemoto(Lance lance) {
        return validarLance(lance, remoto, local);
    }

    //Retorna nulo caso invalido
    LanceValido validarLance(Lance lance, Jogador jogando, Jogador adversario) {
        //Se está fora do tabuleiro
        if (!isDentroDoTabuleiro(lance.getPosFinal(), lance.getPosInicial()))
        {
            tela.notificaErro("Erro, selecione um personagem seu.");
            return null;
        }
        
        Personagem atuante = null;  
        //Pega personagem atuante
        for (Personagem p : jogando.getPersonagens()) {
            if (p.getPosicao().equals(lance.getPosInicial())) {
                atuante = p;
                break;
            }
        }
        if (atuante == null){
            tela.notificaErro("Erro, selecione um personagem seu.");
            return null;
        }
        
        //Pega personagem alvo
        Personagem alvo = null;
        Personagem[] personagensDoAdversario = adversario.getPersonagens();
        for (Personagem p : personagensDoAdversario) {
            if (p.getPosicao().equals(lance.getPosFinal())) {
                alvo = p;
                break;
            }
        }
        
        //Verifica se o alvo é aliado
        boolean isAlvoAliado = false;
        Personagem[] personagensAliados = jogando.getPersonagens();
        for (Personagem p : personagensAliados) {
            if (p.getPosicao().equals(lance.getPosFinal())) {
                isAlvoAliado = true;
                break;
            }
        }
        if (isAlvoAliado){
            tela.notificaErro("Erro, personagem alvo não pode ser aliado.");
            return null;
        }
        
        //Movimentação
        if (alvo == null){
            if (atuante.podeMovimentar(lance.getPosFinal())){
                LanceValido lv = new LanceValido();
                lv.setPosicaoFinal(lance.getPosFinal());
                lv.setAtuante(atuante);
                lv.setTipo(TipoDeJogada.Movimentacao);
                lv.setLance(lance);
                return lv;
            }
            else{
                tela.notificaErro("Erro, personagem não pode se movimentar para esta posição.");
                return null;
            }
        }
        //Ataque
        else {
            if (atuante.podeAtacar(lance.getPosFinal())){
                LanceValido lv = new LanceValido();
                lv.setAlvo(alvo);
                lv.setAtuante(atuante);
                lv.setTipo(TipoDeJogada.Ataque);  
                lv.setLance(lance);
                return lv;
            }
            else{
                tela.notificaErro("Erro, personagem não pode atacar personagem nesta posição.");
                return null;
            }
            
        }
    }

    void atualizarTabuleiro(LanceValido lance) {
        
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

    private boolean isDentroDoTabuleiro(Posicao posFinal, Posicao posInicial) {
        if (posFinal.getX() >= 0 && posFinal.getX() < LINHAS &&
           posInicial.getX() >= 0 && posInicial.getX() < LINHAS &&
           posFinal.getY() >= 0 && posFinal.getY() < COLUNAS &&
           posInicial.getY() >= 0 && posInicial.getY() < COLUNAS)
            return true;
      
        else
            return false;
    }


}
