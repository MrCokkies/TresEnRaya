import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class TresEnRaya implements MouseListener {
    private JFrame frame;
    private JPanel tableroPanel;
    private JPanel puntajesPanel;
    private JLabel jugador1Label;
    private JLabel jugador2Label;

    private int[][] tablero;
    private int turno;
    private boolean partidaTerminada;
    private int puntajeJugador1;
    private int puntajeJugador2;

    public TresEnRaya() {
        tablero = new int[3][3];
        turno = 1;
        partidaTerminada = false;
        puntajeJugador1 = 0;
        puntajeJugador2 = 0;

        frame = new JFrame("Tres en Raya");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        tableroPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawTablero(g);
                drawFichas(g);
            }
        };
        tableroPanel.setPreferredSize(new Dimension(400, 400));
        tableroPanel.addMouseListener(this);

        jugador1Label = new JLabel("Jugador 1: " + puntajeJugador1);
        jugador1Label.setHorizontalAlignment(JLabel.CENTER);
        jugador2Label = new JLabel("Jugador 2: " + puntajeJugador2);
        jugador2Label.setHorizontalAlignment(JLabel.CENTER);
        puntajesPanel = new JPanel();
        puntajesPanel.setLayout(new BoxLayout(puntajesPanel, BoxLayout.Y_AXIS));
        puntajesPanel.add(jugador1Label);
        puntajesPanel.add(jugador2Label);

        frame.add(tableroPanel, BorderLayout.CENTER);
        frame.add(puntajesPanel, BorderLayout.SOUTH);

        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Opciones");
        JMenuItem nuevoJuegoItem = new JMenuItem("Nuevo juego");
        nuevoJuegoItem.addActionListener((ActionEvent e) -> {
            reiniciarJuego();
        });
        JMenuItem salirItem = new JMenuItem("Salir");
        salirItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        menu.add(nuevoJuegoItem);
        menu.add(salirItem);
        menuBar.add(menu);
        frame.setJMenuBar(menuBar);

        frame.pack();
        frame.setVisible(true);
    }

    private void drawTablero(Graphics g) {
        int width = tableroPanel.getWidth();
        int height = tableroPanel.getHeight();
        int cellWidth = width / 3;
        int cellHeight = height / 3;

        g.setColor(Color.BLACK);

        for (int row = 1; row < 3; row++) {
            g.drawLine(0, row * cellHeight, width, row * cellHeight);
        }

        for (int col = 1; col < 3; col++) {
            g.drawLine(col * cellWidth, 0, col * cellWidth, height);
        }
    }

    private void drawFichas(Graphics g) {
        int width = tableroPanel.getWidth();
        int height = tableroPanel.getHeight();
        int cellWidth = width / 3;
        int cellHeight = height / 3;

        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                if (tablero[row][col] == 1) {
                    g.setColor(Color.RED);
                    g.drawLine(col * cellWidth + 5, row * cellHeight + 5, (col + 1) * cellWidth - 5, (row + 1) * cellHeight - 5);
                    g.drawLine((col + 1) * cellWidth - 5, row * cellHeight + 5, col * cellWidth + 5, (row + 1) * cellHeight - 5);
                } else if (tablero[row][col] == 2) {
                    g.setColor(Color.BLUE);
                    g.drawOval(col * cellWidth + 5, row * cellHeight + 5, cellWidth - 10, cellHeight - 10);
                }
            }
        }
    }

    private void reiniciarJuego() {
        tablero = new int[3][3];
        turno = 1;
        partidaTerminada = false;
        tableroPanel.repaint();
    }

    private void actualizarPuntajes() {
        jugador1Label.setText("Jugador 1: " + puntajeJugador1);
        jugador2Label.setText("Jugador 2: " + puntajeJugador2);
    }

    private void comprobarGanador() {
        // Comprobar filas
        for (int row = 0; row < 3; row++) {
            if (tablero[row][0] != 0 && tablero[row][0] == tablero[row][1] && tablero[row][0] == tablero[row][2]) {
                resaltarFichas(row, 0, row, 1, row, 2);
                incrementarPuntaje(tablero[row][0]);
                partidaTerminada = true;
                return;
            }
        }

        // Comprobar columnas
        for (int col = 0; col < 3; col++) {
            if (tablero[0][col] != 0 && tablero[0][col] == tablero[1][col] && tablero[0][col] == tablero[2][col]) {
                resaltarFichas(0, col, 1, col, 2, col);
                incrementarPuntaje(tablero[0][col]);
                partidaTerminada = true;
                return;
            }
        }

        // Comprobar diagonales
        if (tablero[0][0] != 0 && tablero[0][0] == tablero[1][1] && tablero[0][0] == tablero[2][2]) {
            resaltarFichas(0, 0, 1, 1, 2, 2);
            incrementarPuntaje(tablero[0][0]);
            partidaTerminada = true;
            return;
        }

        if (tablero[0][2] != 0 && tablero[0][2] == tablero[1][1] && tablero[0][2] == tablero[2][0]) {
            resaltarFichas(0, 2, 1, 1, 2, 0);
            incrementarPuntaje(tablero[0][2]);
            partidaTerminada = true;
            return;
        }

        // Comprobar empate
        boolean empate = true;
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                if (tablero[row][col] == 0) {
                    empate = false;
                    break;
                }
            }
            if (!empate) {
                break;
            }
        }

        if (empate) {
            partidaTerminada = true;
        }
    }

    private void resaltarFichas(int row1, int col1, int row2, int col2, int row3, int col3) {
        // Implementar el resaltado de las fichas en el tablero
    }

    private void incrementarPuntaje(int jugador) {
        if (jugador == 1) {
            puntajeJugador1++;
        } else if (jugador == 2) {
            puntajeJugador2++;
        }
        actualizarPuntajes();
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

        if (tablero[row][col] == 0) {
            tablero[row][col] = turno;
            tableroPanel.repaint();
            comprobarGanador();
            if (!partidaTerminada) {
                turno = (turno == 1) ? 2 : 1;
            }
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
                new TresEnRaya();
            }
        });
    }
}