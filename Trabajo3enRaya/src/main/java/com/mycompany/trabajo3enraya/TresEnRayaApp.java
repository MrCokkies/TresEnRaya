package com.mycompany.trabajo3enraya;


import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class TresEnRayaApp implements MouseListener {
    private JFrame frame;
    private JPanel tableroPanel;
    private JLabel jugador1Label;
    private JLabel jugador2Label;
    private int[][] tablero;
    private int turno;
    private boolean partidaTerminada;
    private int puntajeJugador1;
    private int puntajeJugador2;

    public TresEnRayaApp() {
        tablero = new int[3][3];
        turno = 1;
        partidaTerminada = false;
        puntajeJugador1 = 0;
        puntajeJugador2 = 0;

        frame = new JFrame("Tres en Raya");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 500);
        frame.setLayout(new BorderLayout());

        tableroPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                dibujarTablero(g);
            }
        };
        tableroPanel.setPreferredSize(new Dimension(300, 300));
        tableroPanel.addMouseListener(this);

        JPanel puntajesPanel = new JPanel();
        puntajesPanel.setLayout(new BoxLayout(puntajesPanel, BoxLayout.Y_AXIS));
        jugador1Label = new JLabel("Jugador 1: " + puntajeJugador1);
        jugador2Label = new JLabel("Jugador 2: " + puntajeJugador2);
        jugador1Label.setAlignmentX(Component.CENTER_ALIGNMENT);
        jugador2Label.setAlignmentX(Component.CENTER_ALIGNMENT);
        puntajesPanel.add(jugador1Label);
        puntajesPanel.add(jugador2Label);

        frame.add(tableroPanel, BorderLayout.CENTER);
        frame.add(puntajesPanel, BorderLayout.SOUTH);

        JMenuBar menuBar = new JMenuBar();
        JMenu juegoMenu = new JMenu("Juego");
        JMenuItem nuevoJuegoItem = new JMenuItem("Nuevo juego");
        JMenuItem salirItem = new JMenuItem("Salir");
        juegoMenu.add(nuevoJuegoItem);
        juegoMenu.add(salirItem);
        menuBar.add(juegoMenu);

        nuevoJuegoItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                reiniciarJuego();
            }
        });

        salirItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        frame.setJMenuBar(menuBar);
        frame.setVisible(true);
    }

    private void dibujarTablero(Graphics g) {
        int width = tableroPanel.getWidth();
        int height = tableroPanel.getHeight();
        int cellWidth = width / 3;
        int cellHeight = height / 3;

        g.setColor(Color.BLACK);

        // Dibujar líneas verticales
        for (int col = 1; col < 3; col++) {
            g.drawLine(col * cellWidth, 0, col * cellWidth, height);
        }

        // Dibujar líneas horizontales
        for (int row = 1; row < 3; row++) {
            g.drawLine(0, row * cellHeight, width, row * cellHeight);
        }

        // Dibujar fichas
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                if (tablero[row][col] == 1) {
                    g.setColor(Color.RED);
                    g.drawLine(col * cellWidth + 5, row * cellHeight + 5, (col + 1) * cellWidth - 5, (row + 1) * cellHeight - 5);
                    g.drawLine(col * cellWidth + 5, (row + 1) * cellHeight - 5, (col + 1) * cellWidth - 5, row * cellHeight + 5);
                } else if (tablero[row][col] == 2) {
                    g.setColor(Color.BLUE);
                    g.drawOval(col * cellWidth + 5, row * cellHeight + 5, cellWidth - 10, cellHeight - 10);
                }
            }
        }
    }

    private void comprobarGanador() {
        int ganador = 0;

        // Comprobar filas
        for (int row = 0; row < 3; row++) {
            if (tablero[row][0] != 0 && tablero[row][0] == tablero[row][1] && tablero[row][1] == tablero[row][2]) {
                ganador = tablero[row][0];
                resaltarFichas(row, 0, row, 1, row, 2);
                break;
            }
        }

        // Comprobar columnas
        for (int col = 0; col < 3; col++) {
            if (tablero[0][col] != 0 && tablero[0][col] == tablero[1][col] && tablero[1][col] == tablero[2][col]) {
                ganador = tablero[0][col];
                resaltarFichas(0, col, 1, col, 2, col);
                break;
            }
        }

        // Comprobar diagonales
        if (tablero[0][0] != 0 && tablero[0][0] == tablero[1][1] && tablero[1][1] == tablero[2][2]) {
            ganador = tablero[0][0];
            resaltarFichas(0, 0, 1, 1, 2, 2);
        } else if (tablero[0][2] != 0 && tablero[0][2] == tablero[1][1] && tablero[1][1] == tablero[2][0]) {
            ganador = tablero[0][2];
            resaltarFichas(0, 2, 1, 1, 2, 0);
        }

        if (ganador != 0) {
            partidaTerminada = true;
            incrementarPuntaje(ganador);
            JOptionPane.showMessageDialog(frame, "¡Ha ganado el Jugador " + ganador + "!");
            reiniciarJuego();
        } else if (tableroCompleto()) {
            partidaTerminada = true;
            JOptionPane.showMessageDialog(frame, "La partida ha terminado sin ganador.");
            reiniciarJuego();
        }
    }

    private boolean tableroCompleto() {
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                if (tablero[row][col] == 0) {
                    return false;
                }
            }
        }
        return true;
    }

    private void reiniciarJuego() {
        tablero = new int[3][3];
        turno = 1;
        partidaTerminada = false;
        tableroPanel.repaint();
    }

    private void resaltarFichas(int row1, int col1, int row2, int col2, int row3, int col3) {
        Graphics g = tableroPanel.getGraphics();
        int width = tableroPanel.getWidth();
        int height = tableroPanel.getHeight();
        int cellWidth = width / 3;
        int cellHeight = height / 3;

        g.setColor(Color.GREEN);

        g.drawLine(col1 * cellWidth + 5, row1 * cellHeight + 5, (col1 + 1) * cellWidth - 5, (row1 + 1) * cellHeight - 5);
        g.drawLine(col1 * cellWidth + 5, (row1 + 1) * cellHeight - 5, (col1 + 1) * cellWidth - 5, row1 * cellHeight + 5);

        g.drawLine(col2 * cellWidth + 5, row2 * cellHeight + 5, (col2 + 1) * cellWidth - 5, (row2 + 1) * cellHeight - 5);
        g.drawLine(col2 * cellWidth + 5, (row2 + 1) * cellHeight - 5, (col2 + 1) * cellWidth - 5, row2 * cellHeight + 5);

        g.drawLine(col3 * cellWidth + 5, row3 * cellHeight + 5, (col3 + 1) * cellWidth - 5, (row3 + 1) * cellHeight - 5);
        g.drawLine(col3 * cellWidth + 5, (row3 + 1) * cellHeight - 5, (col3 + 1) * cellWidth - 5, row3 * cellHeight + 5);
    }

    private void incrementarPuntaje(int jugador) {
        if (jugador == 1) {
            puntajeJugador1++;
            jugador1Label.setText("Jugador 1: " + puntajeJugador1);
        } else if (jugador == 2) {
            puntajeJugador2++;
            jugador2Label.setText("Jugador 2: " + puntajeJugador2);
        }
    }

    private boolean esMovimientoValido(int row, int col) {
        return tablero[row][col] == 0;
    }

    private void cambiarTurno() {
        turno = (turno == 1) ? 2 : 1;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (partidaTerminada) {
            return;
        }

        int width = tableroPanel.getWidth();
        int height = tableroPanel.getHeight();
        int cellWidth = width / 3;
        int cellHeight = height / 3;

        int row = e.getY() / cellHeight;
        int col = e.getX() / cellWidth;

        if (esMovimientoValido(row, col)) {
            if (turno == 1) {
                tablero[row][col] = 1;
            } else {
                tablero[row][col] = 2;
            }
            tableroPanel.repaint();
            comprobarGanador();
            cambiarTurno();
        } else {
            Toolkit.getDefaultToolkit().beep();
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {}

    @Override
    public void mouseReleased(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new TresEnRayaApp();
            }
        });
    }
}
