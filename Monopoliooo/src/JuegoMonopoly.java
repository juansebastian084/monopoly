import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class JuegoMonopoly extends JFrame {
    private final int NUMERO_CASILLAS = 40;
    private final int RADIO_TABLERO = 300;
    private final Color[] COLORES_JUGADORES = {Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW, Color.ORANGE, Color.PINK};
    private final int DINERO_INICIAL = 1500;
    private final int ANCHO_VENTANA = 800;
    private final int ALTO_VENTANA = 800;
    private final int MARGEN_TABLERO = 100; 

    private JButton btnLanzarDado;
    private int[] posicionesJugadores;
    private int[] dineroJugadores;
    private int jugadorActual;
    private boolean[] casillasCompradas;
    private int[] propietarioCasillas; 
    private final int ALQUILER = 100; 
    private int numeroJugadores;

    public JuegoMonopoly() {
        setTitle("Juego Monopoly");
        setSize(ANCHO_VENTANA, ALTO_VENTANA);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        numeroJugadores = seleccionarNumeroJugadores();

        posicionesJugadores = new int[numeroJugadores];
        dineroJugadores = new int[numeroJugadores];
        casillasCompradas = new boolean[NUMERO_CASILLAS];
        propietarioCasillas = new int[NUMERO_CASILLAS]; 

        for (int i = 0; i < numeroJugadores; i++) {
            posicionesJugadores[i] = 0;
            dineroJugadores[i] = DINERO_INICIAL;
        }
        for (int i = 0; i < NUMERO_CASILLAS; i++) {
            propietarioCasillas[i] = -1; 
        }
        jugadorActual = 0;

        // Botón para lanzar el dado
        btnLanzarDado = new JButton("Lanzar Dado");
        btnLanzarDado.setBounds(ANCHO_VENTANA / 2 - 10, ALTO_VENTANA / 2 - 10, 150, 150); 
        btnLanzarDado.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                lanzarDado();
            }
        });
        setLayout(null);
        add(btnLanzarDado);

        setVisible(true);
    }

    private int seleccionarNumeroJugadores() {
        String[] opciones = {"2", "3", "4", "5", "6"};
        String seleccion = (String) JOptionPane.showInputDialog(null, "Selecciona el número de jugadores:", "Número de Jugadores", JOptionPane.QUESTION_MESSAGE, null, opciones, opciones[0]);
        return Integer.parseInt(seleccion);
    }

    private void lanzarDado() {
        int dado = (int) (Math.random() * 6) + 1;
        moverJugador(dado);
    }

    private void moverJugador(int pasos) {
        posicionesJugadores[jugadorActual] += pasos;
        posicionesJugadores[jugadorActual] %= NUMERO_CASILLAS;

        int casillaActual = posicionesJugadores[jugadorActual];
        if (!casillasCompradas[casillaActual]) {
            int opcion = JOptionPane.showConfirmDialog(null, "¿Deseas comprar esta casilla por $100?", "Compra de casilla", JOptionPane.YES_NO_OPTION);
            if (opcion == JOptionPane.YES_OPTION && dineroJugadores[jugadorActual] >= 100) {
                casillasCompradas[casillaActual] = true;
                propietarioCasillas[casillaActual] = jugadorActual; 
                dineroJugadores[jugadorActual] -= 100;
                JOptionPane.showMessageDialog(null, "¡Has comprado la casilla!");
            }
        } else if (propietarioCasillas[casillaActual] != jugadorActual) {
            int propietario = propietarioCasillas[casillaActual];
            dineroJugadores[jugadorActual] -= ALQUILER;
            dineroJugadores[propietario] += ALQUILER;
            JOptionPane.showMessageDialog(null, "Has pagado $" + ALQUILER + " de alquiler al jugador " + (propietario + 1));
        }

        verificarPerdida();
        jugadorActual = (jugadorActual + 1) % numeroJugadores;
        repaint();
    }

    private void verificarPerdida() {
        for (int i = 0; i < numeroJugadores; i++) {
            if (dineroJugadores[i] <= -500) {
                JOptionPane.showMessageDialog(null, "El jugador " + (i + 1) + " ha perdido el juego.");
                System.exit(0); 
            }
        }
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;
        double angulo = 0;
        double incrementoAngulo = 2 * Math.PI / NUMERO_CASILLAS;
        for (int i = 0; i < NUMERO_CASILLAS; i++) {
            int x = (int) (centerX + RADIO_TABLERO * Math.cos(angulo));
            int y = (int) (centerY + RADIO_TABLERO * Math.sin(angulo));
            g.setColor(Color.WHITE);
            g.fillOval(x - 25, y - 25, 50, 50);
            g.setColor(Color.BLACK);
            g.drawOval(x - 25, y - 25, 50, 50);
            angulo += incrementoAngulo;
        }
        for (int i = 0; i < numeroJugadores; i++) {
            angulo = 2 * Math.PI * posicionesJugadores[i] / NUMERO_CASILLAS;
            int x = (int) (centerX + RADIO_TABLERO * Math.cos(angulo));
            int y = (int) (centerY + RADIO_TABLERO * Math.sin(angulo));
            g.setColor(COLORES_JUGADORES[i]);
            g.fillOval(x - 15, y - 15, 30, 30);
        }

        
        for (int i = 0; i < numeroJugadores; i++) {
            g.setColor(Color.BLACK);
            g.drawString("Jugador " + (i + 1) + ": $" + dineroJugadores[i], 20, 40 + i * 20);
        }
    }

    public static void main(String[] args) {
        new JuegoMonopoly();
    }
}









