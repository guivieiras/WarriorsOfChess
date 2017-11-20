/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufsc.inf.ine5608.cliente;

import br.ufsc.inf.ine5608.rede.AtorNetGames;
import br.ufsc.inf.ine5608.rede.Lance;
import br.ufsc.inf.ine5608.rede.Posicao;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;

/**
 *
 * @author Guilherme
 */
public class AtorJogador extends javax.swing.JFrame {

    private Tabuleiro tabuleiro;
    private AtorNetGames atorNetGames;
    private String nomeJogador;
    private Jogador jogador;

    private final JButton[][] posicoesBotoes = new JButton[13][10];
    private Posicao posInicial = null;
    private Posicao posFinal = null;

    private Personagem atuante = null;

    private Color enemyColor = new Color(236, 121, 121);
    private Color allyColor = new Color(105, 241, 105);

    /**
     * Creates new form InterfaceJogo
     */
    public AtorJogador() {
        initComponents();
        inicializar();
    }

    private void inicializar() {
        atorNetGames = new AtorNetGames(this);
        // this.setSize(260, 330);
        this.setContentPane(getContentPane());
        this.setTitle("Warriors Of Chess");
        tabuleiro = new Tabuleiro(this);

        showMenu();
    }

    public void iniciarPartida() {
        atorNetGames.iniciarPartidaRede();
    }

    public void receberLance(Lance lance) {
        LanceValido lanceValido = tabuleiro.validarLanceRemoto(lance);
        if (lanceValido != null) {
            Jogador local = tabuleiro.getJogadorLocal();
            String nome = local.getNome();
            label_vezDeQuem.setText(nome);
            atualizarTabuleiro(lanceValido);
        }
    }

    //Método é chamado quando é recebida a solicitação de inicio
    public void iniciarPartidaRede(boolean vezDoJogadorLocal) {
        //Trata a inicialização do tabuleiro e da interfaçe grafica
        showGame();

        String nomeAdversario = atorNetGames.obterNomeAdversario();
        String nomeJogadorLocal = nomeJogador;
        if (vezDoJogadorLocal) {
            tabuleiro.inicializarJogadores(nomeJogadorLocal, nomeAdversario, true);
            label_vezDeQuem.setText(nomeJogadorLocal);
            jogador = tabuleiro.getJogadorLocal();
        } else {
            tabuleiro.inicializarJogadores(nomeAdversario, nomeJogadorLocal, false);
            label_vezDeQuem.setText(nomeAdversario);
            jogador = tabuleiro.getJogadorLocal();
        }
        this.setTitle("Warriors Of Chess - " + jogador.getNome());

        gerarVisualizacaoTabuleiro();
        Personagem[] personagens = tabuleiro.inicializarPersonagens();

        posicionaPersonagens(personagens);

        tabuleiro.iniciaPartida();
    }

    private void gerarVisualizacaoTabuleiro() {
        panel_tabuleiro.setLayout(new java.awt.GridLayout(Tabuleiro.LINHAS, Tabuleiro.COLUNAS));

        panel_tabuleiro.removeAll();
        for (int i = 0; i < Tabuleiro.LINHAS; i++) {
            for (int j = 0; j < Tabuleiro.COLUNAS; j++) {

                JButton jb = new JButton(); //

                jb.setBackground(Color.WHITE);
                jb.setName(i + "," + j);
                posicoesBotoes[i][j] = jb;

                jb.addActionListener((e) -> {
                    posicao_click(e);
                });

                panel_tabuleiro.add(jb);
            }
        }
        pack();

    }

    private void posicao_click(ActionEvent e) {
        boolean isVezDoJogadorLocal = atorNetGames.isVezDoJogadorLocal();
        if (isVezDoJogadorLocal) {

            JButton source = (JButton) e.getSource();
            System.out.println(source.getName());
            //Pega as coordenadas do botão
            int x = Integer.valueOf(source.getName().split(",")[0]);
            int y = Integer.valueOf(source.getName().split(",")[1]);
            Personagem selected = tabuleiro.getPersonagemLocal(new Posicao(x, y));

            //Caso jogador tenha selecionado um personagem aliado ou não tenha selecionado nenhum
            if (atuante == null || (atuante != null && selected != null)) {

                if (atuante != null) {
                    limpaPossibilidades(atuante);
                    recolorirBackgrounds();
                }

                atuante = selected;

                mostraPossibilidades(atuante);

                posInicial = new Posicao(x, y);

            }
            //Caso o jogador tenha selecionado um aliado e efetuado uma jogada (clicando em uma posição ou alvo
            if (atuante != null && selected == null) {

                posFinal = new Posicao(x, y);

                Lance lance = new Lance();
                lance.setPosFinal(posFinal);
                lance.setPosInicial(posInicial);

                LanceValido lanceValido = tabuleiro.validarLanceLocal(lance);
                //Se lance for valido e enviado para o servidor com sucesso
                if (lanceValido != null && enviarLance(lanceValido)) {
                    Jogador remoto = tabuleiro.getJogadorRemoto();
                    String nome = remoto.getNome();
                    label_vezDeQuem.setText(nome);

                    limpaPossibilidades(atuante);
                    recolorirBackgrounds();

                    atualizarTabuleiro(lanceValido);

                    posFinal = null;
                    posInicial = null;
                    atuante = null;
                }
            }
        } else {
            notificaMensagem("Espere sua vez de jogar!");
        }
    }

    private void posicionaPersonagens(Personagem[] personagens) {
        for (Personagem p : personagens) {
            JButton btn = posicoesBotoes[p.getPosicao().getX()][p.getPosicao().getY()];
            btn.setIcon(p.getIcon());

            Jogador owner = p.getOwner();
            Jogador local = tabuleiro.getJogadorLocal();
            if (owner == local) {
                btn.setBackground(allyColor);
            } else {
                btn.setBackground(enemyColor);
            }
        }
    }

    public boolean enviarLance(LanceValido lanceValido) {
        boolean isVezDoJogadorLocal = atorNetGames.isVezDoJogadorLocal();
        if (isVezDoJogadorLocal) {
            Lance lance = lanceValido.getLance();
            return atorNetGames.enviarLance(lance);
        }
        return false;
    }

    public void conexaoPerdida() {
        notificaMensagem("Conexão com o servidor perdida!");
        boolean isPartidaEmAndamento = tabuleiro.IsPartidaEmAndamento();
        if (isPartidaEmAndamento) {
            tabuleiro.encerraPartida();
        }
        showMenu();
        enableConnect();
    }

    public void encerraPartida(String message) {
        JOptionPane.showMessageDialog(this, message);

        boolean isPartidaEmAndamento = tabuleiro.IsPartidaEmAndamento();
        if (isPartidaEmAndamento) {
            tabuleiro.encerraPartida();
        } else {
            JOptionPane.showMessageDialog(this, "Erro, partida foi encerrada mas não estava em andamento");
        }

        showMenu();
        boolean isConectado = atorNetGames.isConectado();
        if (!isConectado) {
            enableConnect();
        }

    }

    private void enableConnect() {
        btn_conectar.setText("Conectar");
        btn_conectar.setEnabled(true);
        btn_iniciarPartida.setEnabled(false);
    }

    private void disableConnect() {
        btn_conectar.setText("Procurando Adversário");
        btn_conectar.setEnabled(false);
        btn_iniciarPartida.setEnabled(true);
    }

    private void showMenu() {
        panel_menu.setVisible(true);
        panel_jogo.setVisible(false);

        boolean isConectado = atorNetGames.isConectado();
        if (isConectado) {
            disableConnect();
        } else {
            enableConnect();
        }
    }

    private void showGame() {
        panel_menu.setVisible(false);
        panel_jogo.setVisible(true);
    }

    public void notificaErro(String mensagem) {
        JOptionPane.showMessageDialog(this, mensagem, null, JOptionPane.ERROR_MESSAGE);
    }

    public void notificaMensagem(String mensagem) {
        JOptionPane.showMessageDialog(this, mensagem);

    }

    private void matarPersonagem(Personagem alvo) {
        Posicao posAlvo = alvo.getPosicao();
        posicoesBotoes[posAlvo.getX()][posAlvo.getY()].setIcon(null);
        posicoesBotoes[posAlvo.getX()][posAlvo.getY()].setBackground(Color.WHITE);
        alvo.matar();
    }

    private void moverPersonagem(Personagem atuante, Posicao posicao) {
        Posicao posAtuante = atuante.getPosicao();
        JButton btnInicial = posicoesBotoes[posAtuante.getX()][posAtuante.getY()];
        btnInicial.setBackground(Color.WHITE);
        btnInicial.setIcon(null);

        atuante.setPosicao(posicao);

        JButton btnFinal = posicoesBotoes[posicao.getX()][posicao.getY()];
        ImageIcon icon = atuante.getIcon();
        btnFinal.setIcon(icon);

        Jogador owner = atuante.getOwner();
        Jogador local = tabuleiro.getJogadorLocal();
        if (owner == local) {
            btnFinal.setBackground(allyColor);
        } else {
            btnFinal.setBackground(enemyColor);
        }
    }

    private void atualizarTabuleiro(LanceValido lanceValido) {
        Lance lance = lanceValido.getLance();
        TipoDeJogada tipoDeJogada = lance.getTipoDeJogada();
        if (tipoDeJogada == TipoDeJogada.Ataque) {
            Personagem alvo = lanceValido.getAlvo();
            matarPersonagem(alvo);
            Jogador vencedor = tabuleiro.testaFimDeJogo();
            Jogador local = tabuleiro.getJogadorLocal();
            Jogador remoto = tabuleiro.getJogadorRemoto();
            if (vencedor == local) {
                notificaMensagem("Você venceu! Ao clicar 'OK' a partida será reniciada");
                atorNetGames.reiniciarPartida();
            } else if (vencedor == remoto) {
                notificaMensagem("Você perdeu!");
            }

        } else {
            Personagem atuante = lanceValido.getAtuante();
            Posicao posicaoFinal = lance.getPosFinal();
            moverPersonagem(atuante, posicaoFinal);
        }
    }

    private void recolorirBackgrounds() {
        Jogador remoto = tabuleiro.getJogadorRemoto();
        Personagem[] personagensRemoto = remoto.getPersonagens();
        for (Personagem p : personagensRemoto) {
            boolean isVivo = p.isVivo();
            if (isVivo) {
                Posicao posicao = p.getPosicao();
                posicoesBotoes[posicao.getX()][posicao.getY()].setBackground(enemyColor);
            }
        }
        Jogador local = tabuleiro.getJogadorLocal();
        Personagem[] personagensLocal = local.getPersonagens();
        for (Personagem p : personagensLocal) {
            boolean isVivo = p.isVivo();
            if (isVivo) {
                Posicao posicao = p.getPosicao();
                posicoesBotoes[posicao.getX()][posicao.getY()].setBackground(allyColor);
            }
        }
    }

    private void mostraPossibilidades(Personagem atuante) {
        for (int i = 0; i < Tabuleiro.LINHAS; i++) {
            for (int j = 0; j < Tabuleiro.COLUNAS; j++) {
                Posicao posicao = new Posicao(i, j);
                if (atuante.podeAtacar(posicao)) {
                    posicoesBotoes[i][j].setBackground(Color.ORANGE);
                } else if (atuante.podeMovimentar(new Posicao(i, j))) {
                    posicoesBotoes[i][j].setBackground(Color.GREEN);
                }

            }
        }
    }

    private void limpaPossibilidades(Personagem atuante) {
        for (int i = 0; i < Tabuleiro.LINHAS; i++) {
            for (int j = 0; j < Tabuleiro.COLUNAS; j++) {
                Posicao posicao = new Posicao(i, j);
                if (atuante.podeAtacar(posicao) || atuante.podeMovimentar(posicao)) {
                    posicoesBotoes[i][j].setBackground(Color.WHITE);
                }
            }
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panel_jogo = new javax.swing.JPanel();
        btn_desconectar = new javax.swing.JButton();
        label_vezDeQuem = new javax.swing.JLabel();
        panel_tabuleiro = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        panel_menu = new javax.swing.JPanel();
        btn_conectar = new javax.swing.JButton();
        txt_field_player_name = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        btn_regras = new javax.swing.JButton();
        btn_iniciarPartida = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(500, 400));
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        panel_jogo.setPreferredSize(new java.awt.Dimension(900, 900));

        btn_desconectar.setText("Desconectar");
        btn_desconectar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_desconectarActionPerformed(evt);
            }
        });

        label_vezDeQuem.setText("VEZ DE QUEM");

        panel_tabuleiro.setLayout(new java.awt.GridLayout(13, 10));

        jLabel2.setText("Vez do:");

        javax.swing.GroupLayout panel_jogoLayout = new javax.swing.GroupLayout(panel_jogo);
        panel_jogo.setLayout(panel_jogoLayout);
        panel_jogoLayout.setHorizontalGroup(
            panel_jogoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panel_jogoLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel2)
                .addGap(18, 18, 18)
                .addComponent(label_vezDeQuem)
                .addGap(82, 82, 82)
                .addComponent(btn_desconectar)
                .addGap(216, 216, 216))
            .addGroup(panel_jogoLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panel_tabuleiro, javax.swing.GroupLayout.PREFERRED_SIZE, 685, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(25, Short.MAX_VALUE))
        );
        panel_jogoLayout.setVerticalGroup(
            panel_jogoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_jogoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panel_jogoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(label_vezDeQuem)
                    .addComponent(jLabel2)
                    .addComponent(btn_desconectar))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panel_tabuleiro, javax.swing.GroupLayout.PREFERRED_SIZE, 636, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(14, Short.MAX_VALUE))
        );

        getContentPane().add(panel_jogo, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 720, 690));

        panel_menu.setPreferredSize(new java.awt.Dimension(900, 900));

        btn_conectar.setText("Conectar");
        btn_conectar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_conectarActionPerformed(evt);
            }
        });

        txt_field_player_name.setText("Player1");

        jLabel1.setText("Nome");

        btn_regras.setText("Regras");

        btn_iniciarPartida.setText("Iniciar Partida");
        btn_iniciarPartida.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_iniciarPartidaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panel_menuLayout = new javax.swing.GroupLayout(panel_menu);
        panel_menu.setLayout(panel_menuLayout);
        panel_menuLayout.setHorizontalGroup(
            panel_menuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_menuLayout.createSequentialGroup()
                .addGap(217, 217, 217)
                .addGroup(panel_menuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btn_iniciarPartida)
                    .addComponent(btn_regras)
                    .addComponent(btn_conectar)
                    .addGroup(panel_menuLayout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(18, 18, 18)
                        .addComponent(txt_field_player_name, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(362, Short.MAX_VALUE))
        );
        panel_menuLayout.setVerticalGroup(
            panel_menuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panel_menuLayout.createSequentialGroup()
                .addContainerGap(212, Short.MAX_VALUE)
                .addGroup(panel_menuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txt_field_player_name, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(26, 26, 26)
                .addComponent(btn_conectar)
                .addGap(47, 47, 47)
                .addComponent(btn_iniciarPartida)
                .addGap(56, 56, 56)
                .addComponent(btn_regras)
                .addGap(260, 260, 260))
        );

        getContentPane().add(panel_menu, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 720, 690));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btn_conectarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_conectarActionPerformed
        String nomeJogador = txt_field_player_name.getText();
        boolean isConectado = atorNetGames.isConectado();
        if (!isConectado) {
            boolean conectou = atorNetGames.conectar("127.0.0.1", nomeJogador);
            if (conectou) {
                this.nomeJogador = nomeJogador;
                disableConnect();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Erro ao conectar-se ao servidor");
        }
    }//GEN-LAST:event_btn_conectarActionPerformed

    private void btn_desconectarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_desconectarActionPerformed
        boolean desconectado = atorNetGames.desconectar();
        if (!desconectado){
            notificaErro("Erro ao desconectar");
        }
    }//GEN-LAST:event_btn_desconectarActionPerformed

    private void btn_iniciarPartidaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_iniciarPartidaActionPerformed
        atorNetGames.iniciarPartidaRede();
    }//GEN-LAST:event_btn_iniciarPartidaActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(AtorJogador.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(AtorJogador.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(AtorJogador.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(AtorJogador.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new AtorJogador().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_conectar;
    private javax.swing.JButton btn_desconectar;
    private javax.swing.JButton btn_iniciarPartida;
    private javax.swing.JButton btn_regras;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel label_vezDeQuem;
    private javax.swing.JPanel panel_jogo;
    private javax.swing.JPanel panel_menu;
    private javax.swing.JPanel panel_tabuleiro;
    private javax.swing.JTextField txt_field_player_name;
    // End of variables declaration//GEN-END:variables

}
