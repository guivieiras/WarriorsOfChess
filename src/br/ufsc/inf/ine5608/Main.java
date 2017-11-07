/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufsc.inf.ine5608;

import br.ufsc.inf.ine5608.cliente.AtorJogador;
import br.ufsc.inf.ine5608.cliente.Tabuleiro;
import br.ufsc.inf.ine5608.rede.AtorNetGames;
import javax.swing.JFrame;
import org.apache.log4j.BasicConfigurator;

/**
 *
 * @author Guilherme
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        //BasicConfigurator.configure();

        AtorJogador janela;
	janela = new AtorJogador();
	janela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	janela.setVisible(true);
    }
    
}
