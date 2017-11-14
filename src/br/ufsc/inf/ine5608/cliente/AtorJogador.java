/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufsc.inf.ine5608.cliente;

import br.ufsc.inf.ine5608.rede.AtorNetGames;
import br.ufsc.inf.ine5608.rede.Jogador;
import br.ufsc.inf.ine5608.rede.Lance;
import br.ufsc.inf.ine5608.rede.Personagem;
import br.ufsc.inf.ine5608.rede.Posicao;
import br.ufsc.inf.ine5608.rede.TipoGuerreiro;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Image;
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
        tabuleiro = new Tabuleiro();

        showMenu();
    }

    public void iniciarPartida() {
        atorNetGames.iniciarPartidaRede();
    }

    public void receberLance(Lance lance) {
        label_vezDeQuem.setText(nomeJogador);
        tabuleiro.atualizarTabuleiro(lance);
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
    private final JButton[][] posicoesBotoes = new JButton[13][10];
    private Posicao posInicial = null;
    private Posicao posFinal = null;

    private void gerarVisualizacaoTabuleiro() {
        panel_tabuleiro.removeAll();
        for (int i = 0; i < Tabuleiro.LINHAS; i++) {
            for (int j = 0; j < Tabuleiro.COLUNAS; j++) {

                JButton jb = new JButton(); //

                jb.setBackground(Color.WHITE);
                jb.setName(i + "," + j);
                posicoesBotoes[i][j] = jb;

                jb.addActionListener((e) -> {
                    JButton source = (JButton) e.getSource();
                    System.out.println(source.getName());
                    int x = Integer.valueOf(source.getName().split(",")[0]);
                    int y = Integer.valueOf(source.getName().split(",")[1]);
                    if (posInicial == null) {
                        posInicial = new Posicao(x, y);
                    } else {
                        posFinal = new Posicao(x, y);

                        Lance lance = new Lance();
                        lance.setPosFinal(posFinal);
                        lance.setPosInicial(posInicial);

                        posFinal = null;
                        posInicial = null;

                        //Se lance foi enviado para o servidor com sucesso
                        if (enviarLance(lance)) {
                            label_vezDeQuem.setText(tabuleiro.getJogadorRemoto().getNome());
                            tabuleiro.atualizarTabuleiro(lance);
                        }

                    }
                });

                panel_tabuleiro.add(jb);
            }
        }
        pack();

    }

    private void posicionaPersonagens(Personagem[] personagens) {
        try {
            Image warrior = ImageIO.read(new File("C:\\Users\\Guilherme\\Pictures\\aps\\warrior.png")).getScaledInstance(48, 48, Image.SCALE_AREA_AVERAGING);
            Image mage = ImageIO.read(new File("C:\\Users\\Guilherme\\Pictures\\aps\\mage.png")).getScaledInstance(48, 48, Image.SCALE_AREA_AVERAGING);
            Image ranger = ImageIO.read(new File("C:\\Users\\Guilherme\\Pictures\\aps\\ranger.png")).getScaledInstance(48, 48, Image.SCALE_AREA_AVERAGING);

            for (Personagem p : personagens) {
                Image icon = null;
                if (p.getTipoGuerreiro() == TipoGuerreiro.Mage) {
                    icon = mage;
                }
                if (p.getTipoGuerreiro() == TipoGuerreiro.Ranger) {
                    icon = ranger;
                }
                if (p.getTipoGuerreiro() == TipoGuerreiro.Warrior) {
                    icon = warrior;
                }

                posicoesBotoes[p.getPosicao().getX()][p.getPosicao().getY()].setIcon(new ImageIcon(icon));
            }

        } catch (IOException ex) {
            Logger.getLogger(AtorJogador.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public boolean enviarLance(Lance lance) {
        if (atorNetGames.isVezDoJogadorLocal()) {
            boolean lanceValido = tabuleiro.validarLance(lance);
            if (lanceValido) {
                return atorNetGames.enviarLance(lance);
            }
        }
        return false;
    }

    public void conexaoPerdida() {
        if (tabuleiro.IsPartidaEmAndamento()) {
            tabuleiro.encerraPartida();
        }
        showMenu();
        enableConnect();
    }

    public void encerraPartida(String message) {
        JOptionPane.showMessageDialog(this, message);

        if (tabuleiro.IsPartidaEmAndamento()) {
            tabuleiro.encerraPartida();
            showMenu();
        } else {
            JOptionPane.showMessageDialog(this, "Erro, partida foi encerrada mas não estava em andamento");
        }

        if (!atorNetGames.isConectado()) {
            enableConnect();
        }

    }

    private void enableConnect() {
        btn_conectar.setText("Conectar");
        btn_conectar.setEnabled(true);
    }

    private void showMenu() {
        panel_menu.setVisible(true);
        panel_jogo.setVisible(false);
    }

    private void showGame() {
        panel_menu.setVisible(false);
        panel_jogo.setVisible(true);
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
        btn_enviar_lance = new javax.swing.JButton();
        label_vezDeQuem = new javax.swing.JLabel();
        panel_tabuleiro = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        panel_menu = new javax.swing.JPanel();
        btn_conectar = new javax.swing.JButton();
        txt_field_player_name = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        btn_regras = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(500, 400));
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        panel_jogo.setPreferredSize(new java.awt.Dimension(900, 900));

        btn_enviar_lance.setText("Enviar");
        btn_enviar_lance.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_enviar_lanceActionPerformed(evt);
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
                .addGap(0, 419, Short.MAX_VALUE)
                .addComponent(jLabel2)
                .addGap(18, 18, 18)
                .addComponent(label_vezDeQuem)
                .addGap(82, 82, 82)
                .addComponent(btn_enviar_lance)
                .addGap(216, 216, 216))
            .addGroup(panel_jogoLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panel_tabuleiro, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        panel_jogoLayout.setVerticalGroup(
            panel_jogoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_jogoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panel_jogoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(label_vezDeQuem)
                    .addComponent(jLabel2)
                    .addComponent(btn_enviar_lance))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panel_tabuleiro, javax.swing.GroupLayout.DEFAULT_SIZE, 849, Short.MAX_VALUE)
                .addContainerGap())
        );

        getContentPane().add(panel_jogo, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

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

        javax.swing.GroupLayout panel_menuLayout = new javax.swing.GroupLayout(panel_menu);
        panel_menu.setLayout(panel_menuLayout);
        panel_menuLayout.setHorizontalGroup(
            panel_menuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_menuLayout.createSequentialGroup()
                .addGap(157, 157, 157)
                .addGroup(panel_menuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btn_regras)
                    .addComponent(btn_conectar)
                    .addGroup(panel_menuLayout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(18, 18, 18)
                        .addComponent(txt_field_player_name, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(602, Short.MAX_VALUE))
        );
        panel_menuLayout.setVerticalGroup(
            panel_menuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_menuLayout.createSequentialGroup()
                .addGap(115, 115, 115)
                .addGroup(panel_menuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_field_player_name, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addGap(29, 29, 29)
                .addComponent(btn_conectar)
                .addGap(45, 45, 45)
                .addComponent(btn_regras)
                .addContainerGap(645, Short.MAX_VALUE))
        );

        getContentPane().add(panel_menu, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btn_conectarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_conectarActionPerformed
        String nomeJogador = txt_field_player_name.getText();
        if (!atorNetGames.isConectado() && atorNetGames.conectar("127.0.0.1", nomeJogador)) {
            atorNetGames.iniciarPartidaRede();
            this.nomeJogador = nomeJogador;
            btn_conectar.setText("Procurando Adversário");
            btn_conectar.setEnabled(false);
        } else {
            JOptionPane.showMessageDialog(this, "Erro ao conectar-se ao servidor");
        }
    }//GEN-LAST:event_btn_conectarActionPerformed

    private void btn_enviar_lanceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_enviar_lanceActionPerformed

    }//GEN-LAST:event_btn_enviar_lanceActionPerformed

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
    private javax.swing.JButton btn_enviar_lance;
    private javax.swing.JButton btn_regras;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel label_vezDeQuem;
    private javax.swing.JPanel panel_jogo;
    private javax.swing.JPanel panel_menu;
    private javax.swing.JPanel panel_tabuleiro;
    private javax.swing.JTextField txt_field_player_name;
    // End of variables declaration//GEN-END:variables

    void notificaErro(String erro_selecione_um_personagem_seu) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
