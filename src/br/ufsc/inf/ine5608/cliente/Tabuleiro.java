/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufsc.inf.ine5608.cliente;

import br.ufsc.inf.ine5608.rede.AtorNetGames;
import br.ufsc.inf.ine5608.rede.Lance;
import br.ufsc.inf.ine5608.rede.Posicao;
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

    public static final int LINHAS = 11;
    public static final int COLUNAS = 9;

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

        Personagem p = new Personagem(0, 2, TipoGuerreiro.Mage, jogador1);
        jogador1.setPersonagen(0, p);
        personagens[0] = p;

        p = new Personagem(0, 4, TipoGuerreiro.Warrior, jogador1);
        jogador1.setPersonagen(1, p);
        personagens[1] = p;

        p = new Personagem(0, 6, TipoGuerreiro.Ranger, jogador1);
        jogador1.setPersonagen(2, p);
        personagens[2] = p;

        p = new Personagem(10, 2, TipoGuerreiro.Mage, jogador2);
        jogador2.setPersonagen(0, p);
        personagens[3] = p;

        p = new Personagem(10, 4, TipoGuerreiro.Warrior, jogador2);
        jogador2.setPersonagen(1, p);
        personagens[4] = p;

        p = new Personagem(10, 6, TipoGuerreiro.Ranger, jogador2);
        jogador2.setPersonagen(2, p);
        personagens[5] = p;

        return personagens;
    }

    public void iniciaPartida() {
        partidaEmAndamento = true;
    }

    Personagem getPersonagemLocal(Posicao posicao) {
        return getPersonagem(posicao, local);
    }

    Personagem getPersonagemRemoto(Posicao posicao) {
        return getPersonagem(posicao, remoto);
    }

    Personagem getPersonagem(Posicao posicao, Jogador jogador) {
        for (Personagem p : jogador.getPersonagens()) {
            if (p.getPosicao().equals(posicao)) {
                return p;
            }
        }
        return null;
    }

    LanceValido validarLanceLocal(Lance lance) {
        return validarLance(lance, local, remoto);
    }

    LanceValido validarLanceRemoto(Lance lance) {
        return validarLance(lance, remoto, local);
    }

    //Retorna nulo caso invalido
    LanceValido validarLance(Lance lance, Jogador jogando, Jogador adversario) {
        Personagem atuante = null;
        //Pega personagem atuante
        Personagem[] personagensAliados = jogando.getPersonagens();
        for (Personagem p : personagensAliados) {
            Posicao posPersonagem = p.getPosicao();
            Posicao posInicial = lance.getPosInicial();
            boolean clicouPersonagem = posPersonagem.equals(posInicial);
            if (clicouPersonagem) {
                atuante = p;
                break;
            }
        }
        if (atuante == null) {
            tela.notificaErro("Erro, selecione um personagem seu.");
            return null;
        }

        //Pega personagem alvo
        Personagem alvo = null;
        Personagem[] personagensDoAdversario = adversario.getPersonagens();
        for (Personagem p : personagensDoAdversario) {
            Posicao posPersonagem = p.getPosicao();
            Posicao posFinal = lance.getPosFinal();
            boolean clicouPersonagem = posPersonagem.equals(posFinal);
            if (clicouPersonagem) {
                alvo = p;
                break;
            }
        }

        //Verifica se o alvo é aliado
        boolean isAlvoAliado = false;
        for (Personagem p : personagensAliados) {
            Posicao posPersonagem = p.getPosicao();
            Posicao posFinal = lance.getPosFinal();
            boolean clicouAliado = posPersonagem.equals(posFinal);
            if (clicouAliado) {
                isAlvoAliado = true;
                break;
            }
        }
        if (isAlvoAliado) {
            tela.notificaErro("Erro, personagem alvo não pode ser aliado.");
            return null;
        }

        //Movimentação
        if (alvo == null) {
            Posicao posFinal = lance.getPosFinal();
            if (atuante.podeMovimentar(posFinal)) {
                LanceValido lv = new LanceValido();
                lv.setAtuante(atuante);
                lance.setTipoDeJogada(TipoDeJogada.Movimentacao);
                lv.setLance(lance);
                return lv;
            } else {
                tela.notificaErro("Erro, personagem não pode se movimentar para esta posição.");
                return null;
            }
        } //Ataque
        else {
            Posicao posFinal = lance.getPosFinal();
            if (atuante.podeAtacar(posFinal)) {
                LanceValido lv = new LanceValido();
                lv.setAlvo(alvo);
                lv.setAtuante(atuante);
                lance.setTipoDeJogada(TipoDeJogada.Ataque);
                lv.setLance(lance);
                return lv;
            } else {
                tela.notificaErro("Erro, personagem não pode atacar personagem nesta posição.");
                return null;
            }

        }
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

    //Retonar o jogador vencedor, caso nenhum ganhe, retorna nulo
    Jogador testaFimDeJogo() {
        boolean jogador2Ganhou = jogador1.todosPersonagensMortos();
        if (jogador2Ganhou)
            return jogador2;
        
        boolean jogador1Ganhou = jogador2.todosPersonagensMortos();
        if (jogador1Ganhou)
            return jogador1;
        
        return null;
    }

}
