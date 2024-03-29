/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufsc.inf.ine5608.rede;

import br.ufsc.inf.ine5608.cliente.AtorJogador;
import br.ufsc.inf.ine5608.cliente.Tabuleiro;
import br.ufsc.inf.leobr.cliente.*;
import br.ufsc.inf.leobr.cliente.exception.ArquivoMultiplayerException;
import br.ufsc.inf.leobr.cliente.exception.JahConectadoException;
import br.ufsc.inf.leobr.cliente.exception.NaoConectadoException;
import br.ufsc.inf.leobr.cliente.exception.NaoJogandoException;
import br.ufsc.inf.leobr.cliente.exception.NaoPossivelConectarException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Guilherme
 */
public class AtorNetGames implements br.ufsc.inf.leobr.cliente.OuvidorProxy {

    private Proxy proxy;
    private AtorJogador atorJogador;
    private boolean vezDoJogadorLocal = false;
    private boolean isConectado = false;
    private boolean isPartidaEmAndamento = false;
    
    public AtorNetGames(AtorJogador atorJogador){
        super();
        this.atorJogador = atorJogador;
        proxy = Proxy.getInstance();
        proxy.addOuvinte(this);
    }
    
    public boolean conectar(String servidor, String nome){
        try {
            proxy.conectar(servidor, nome);
            isConectado = true;
            return true;
        } catch (JahConectadoException ex) {
            Logger.getLogger(AtorNetGames.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NaoPossivelConectarException ex) {
            Logger.getLogger(AtorNetGames.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ArquivoMultiplayerException ex) {
            Logger.getLogger(AtorNetGames.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
    public boolean iniciarPartidaRede(){
        try {
            if (isConectado)
            {
                proxy.iniciarPartida(2);
                return true;
            }
        } catch (NaoConectadoException ex) {
            Logger.getLogger(AtorNetGames.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
    public boolean enviarLance(Lance lance){
        try {
            proxy.enviaJogada(lance);
            vezDoJogadorLocal = false;
            return true;
        } catch (NaoJogandoException ex) {
            Logger.getLogger(AtorNetGames.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
    public void finalizarPartida(){
        try {
            proxy.finalizarPartida();
            isPartidaEmAndamento = false;            
        } catch (NaoConectadoException ex) {
            Logger.getLogger(AtorNetGames.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NaoJogandoException ex) {
            Logger.getLogger(AtorNetGames.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public boolean desconectar(){
        try {
            if (isConectado){
                proxy.desconectar();
                isConectado = false;
                isPartidaEmAndamento = false;
                return true;
            }
        } catch (NaoConectadoException ex) {
            Logger.getLogger(AtorNetGames.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
   
    //Receber solicitação de partida
    @Override
    public void iniciarNovaPartida(Integer posicao) {
        System.out.println("Partida iniciada: " + posicao);
        if (posicao == 1){
            vezDoJogadorLocal = true;
        } else if (posicao ==  2){
            vezDoJogadorLocal = false;
        }
        
        isPartidaEmAndamento = true;
        atorJogador.iniciarPartidaRede(vezDoJogadorLocal);
    }

    @Override
    public void finalizarPartidaComErro(String message) {
        isPartidaEmAndamento = false;
        atorJogador.encerraPartida(message);
    }

    @Override
    public void receberMensagem(String msg) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void receberJogada(Jogada jogada) {
        Lance lance = (Lance) jogada;
        atorJogador.receberLance(lance);
        vezDoJogadorLocal = true;    
    }
   
    @Override
    public void tratarConexaoPerdida() {
        isConectado = false;
        atorJogador.conexaoPerdida();
    }

    @Override
    public void tratarPartidaNaoIniciada(String message) {
        atorJogador.notificaMensagem(message);
    }
    
    public String obterNomeAdversario(){
        if (vezDoJogadorLocal)
            return proxy.obterNomeAdversario(2);
        else
            return proxy.obterNomeAdversario(1);      
    }

    public boolean isVezDoJogadorLocal() {
        return vezDoJogadorLocal;
    }

    public boolean isConectado() {
        return isConectado;
    }

    public void reiniciarPartida() {
        try {
            proxy.reiniciarPartida();
        } catch (NaoConectadoException ex) {
            Logger.getLogger(AtorNetGames.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NaoJogandoException ex) {
            Logger.getLogger(AtorNetGames.class.getName()).log(Level.SEVERE, null, ex);
        }
    } 
}
