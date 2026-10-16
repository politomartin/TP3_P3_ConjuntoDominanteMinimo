package auxiliares;

import javax.swing.*;
import java.awt.*;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Graphics_2D extends JPanel {

    private Map<Integer, Point> posicionesVertices;
    private Map<Integer, Set<Integer>> adyacencias;
    private Set<Integer> conjuntoDominante;
    private Integer verticeSeleccionado;
    
    // Datos de la última ejecución para mostrar en el lienzo
    private String infoAlgoritmo = "";
    private String infoTiempo = "";

    private static final int DIAMETRO = 40;
    private static final int RADIO = DIAMETRO / 2;

    public Graphics_2D() {
        this.posicionesVertices = new HashMap<>();
        this.adyacencias = new HashMap<>();
        this.conjuntoDominante = new HashSet<>();
        this.verticeSeleccionado = null;
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
    }

    public void registrarVertice(int id, int x, int y) {
        posicionesVertices.put(id, new Point(x, y));
        repaint();
    }

    public void moverVertice(int id, int nuevoX, int nuevoY) {
        if (posicionesVertices.containsKey(id)) {
            // Limitar para que no se salga de los bordes del panel
            int x = Math.max(RADIO + 5, Math.min(getWidth() - RADIO - 5, nuevoX));
            int y = Math.max(RADIO + 5, Math.min(getHeight() - RADIO - 5, nuevoY));
            posicionesVertices.put(id, new Point(x, y));
            repaint();
        }
    }

    public void removerVertice(int id) {
        posicionesVertices.remove(id);
        conjuntoDominante.remove(id);
        if (verticeSeleccionado != null && verticeSeleccionado == id) {
            verticeSeleccionado = null;
        }
        repaint();
    }

    public void actualizarGrafo(Map<Integer, Set<Integer>> nuevasAdyacencias) {
        this.adyacencias = nuevasAdyacencias;
        repaint();
    }

    public void setVerticeSeleccionado(Integer id) {
        this.verticeSeleccionado = id;
        repaint();
    }

    public void mostrarResultadoCDM(Set<Integer> cdm, String nombreAlgoritmo, double tiempoMs) {
        this.conjuntoDominante = (cdm != null) ? new HashSet<>(cdm) : Collections.emptySet();
        this.infoAlgoritmo = nombreAlgoritmo + " | CDM: " + this.conjuntoDominante + " (Tamaño: " + this.conjuntoDominante.size() + ")";
        this.infoTiempo = String.format("Tiempo de ejecución: %.3f ms", tiempoMs);
        repaint();
    }

    public void limpiarResultadoCDM() {
        this.conjuntoDominante.clear();
        this.infoAlgoritmo = "";
        this.infoTiempo = "";
        repaint();
    }

    public void limpiarLienzo() {
        posicionesVertices.clear();
        adyacencias.clear();
        limpiarResultadoCDM();
        verticeSeleccionado = null;
        repaint();
    }

    /**
     * Distribuye los vértices con un radio dinámico: si hay pocos vértices quedan
     * cerca del centro; si hay muchos se expande gradualmente.
     */
    public void distribuirVerticesEnCirculo(Set<Integer> vertices) {
        posicionesVertices.clear();
        int n = vertices.size();
        if (n == 0) {
            repaint();
            return;
        }

        int centroX = getWidth() / 2;
        int centroY = getHeight() / 2;

        // Radio adaptable: para 6 vértices usa ~130px en lugar de 270px
        int radioMaximo = Math.min(centroX, centroY) - 60;
        int radioIdeal = Math.max(95, n * 22);
        int radioCirculo = (n == 1) ? 0 : Math.min(radioIdeal, radioMaximo);

        int i = 0;
        for (Integer id : vertices) {
            double angulo = 2 * Math.PI * i / n - (Math.PI / 2);
            int x = (int) (centroX + radioCirculo * Math.cos(angulo));
            int y = (int) (centroY + radioCirculo * Math.sin(angulo));
            posicionesVertices.put(id, new Point(x, y));
            i++;
        }
        repaint();
    }

    public Integer obtenerVerticeEnCoordenadas(int x, int y) {
        for (Map.Entry<Integer, Point> entrada : posicionesVertices.entrySet()) {
            Point centro = entrada.getValue();
            if (centro.distance(x, y) <= RADIO) {
                return entrada.getKey();
            }
        }
        return null;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // 1. Dibujar las aristas
        g2d.setStroke(new BasicStroke(2.2f));
        g2d.setColor(new Color(90, 90, 90));
        for (Map.Entry<Integer, Set<Integer>> entrada : adyacencias.entrySet()) {
            int origen = entrada.getKey();
            Point pOrigen = posicionesVertices.get(origen);

            if (pOrigen != null) {
                for (int destino : entrada.getValue()) {
                    if (origen < destino) {
                        Point pDestino = posicionesVertices.get(destino);
                        if (pDestino != null) {
                            g2d.drawLine(pOrigen.x, pOrigen.y, pDestino.x, pDestino.y);
                        }
                    }
                }
            }
        }

        // 2. Dibujar los vértices
        g2d.setFont(new Font("SansSerif", Font.BOLD, 14));
        FontMetrics fm = g2d.getFontMetrics();

        for (Map.Entry<Integer, Point> entrada : posicionesVertices.entrySet()) {
            int id = entrada.getKey();
            Point p = entrada.getValue();

            int xTopLeft = p.x - RADIO;
            int yTopLeft = p.y - RADIO;

            // Color según estado: Naranja (seleccionado), Verde (es parte del CDM), Celeste (normal)
            if (verticeSeleccionado != null && verticeSeleccionado == id) {
                g2d.setColor(new Color(255, 195, 100));
            } else if (conjuntoDominante.contains(id)) {
                g2d.setColor(new Color(102, 217, 138)); // Verde CDM
            } else {
                g2d.setColor(new Color(173, 216, 230)); // Celeste estándar
            }
            g2d.fillOval(xTopLeft, yTopLeft, DIAMETRO, DIAMETRO);

            g2d.setColor(Color.DARK_GRAY);
            g2d.setStroke(new BasicStroke(2f));
            g2d.drawOval(xTopLeft, yTopLeft, DIAMETRO, DIAMETRO);

            g2d.setColor(Color.BLACK);
            String texto = String.valueOf(id);
            int textoAncho = fm.stringWidth(texto);
            int textoAlto = fm.getAscent();
            g2d.drawString(texto, p.x - (textoAncho / 2), p.y + (textoAlto / 3));
        }

        // 3. Dibujar recuadro con resultados y tiempo de ejecución si existe
        if (!infoAlgoritmo.isEmpty()) {
            g2d.setFont(new Font("SansSerif", Font.BOLD, 13));
            FontMetrics fmInfo = g2d.getFontMetrics();
            int anchoCaja = Math.max(fmInfo.stringWidth(infoAlgoritmo), fmInfo.stringWidth(infoTiempo)) + 24;
            int altoCaja = 52;
            int xCaja = 15;
            int yCaja = getHeight() - altoCaja - 15;

            g2d.setColor(new Color(245, 248, 250, 230));
            g2d.fillRoundRect(xCaja, yCaja, anchoCaja, altoCaja, 10, 10);
            g2d.setColor(new Color(100, 140, 180));
            g2d.setStroke(new BasicStroke(1.5f));
            g2d.drawRoundRect(xCaja, yCaja, anchoCaja, altoCaja, 10, 10);

            g2d.setColor(new Color(30, 30, 30));
            g2d.drawString(infoAlgoritmo, xCaja + 12, yCaja + 22);
            g2d.setColor(new Color(0, 110, 60));
            g2d.drawString(infoTiempo, xCaja + 12, yCaja + 42);
        }
    }
}