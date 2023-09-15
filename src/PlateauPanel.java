import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.List;
import javax.swing.*;


public class PlateauPanel extends JPanel{

    private Plateau plateau;
    private List<GridElement>  elementos;

    public PlateauPanel(Plateau plateau, List<GridElement> elementos)  {
        this.plateau = plateau;
        this.elementos = elementos;
    }
    @Override
    protected void paintComponent(Graphics g) {

            super.paintComponent(g);
            // Dibuja la meseta, los obstáculos y el rover
            // Utiliza las coordenadas x, y. y la orientación del rover para dibujarlo
            // Dibujar la cuadrícula
            drawBox(g,plateau.getWidth(),plateau.getHeight(),plateau.getGridSize());
            // Dibujar una flecha hacia arriba en el centro de un cuadro específico (Posición inicial)
            // elementos.add(new GridElement(cellX,cellY,"W",GridElementType.FLECHA));
            //dibujar recorrido del rover
            for (GridElement elemento : elementos) {
                if (elemento.tipo == GridElementType.ROVER) {
                    drawArrow(g, elemento.x, elemento.y, elemento.sO);
                }
            }
            //dibujar obstáculos
            drawObs(g);

    }



    public void drawBox(Graphics g, int width, int height, int gridSize){
        g.setColor(Color.BLACK);

        for (int x = 1; x <= width+10; x += gridSize) {
            g.drawLine(x, 0, x, height+10);

            String label = Integer.toString(x / gridSize); // Calcula la etiqueta
            g.drawString(label, x + 5,  15); // Ajusta la posición y al gusto
        }
        for (int y = 1; y <= height+10; y += gridSize) {
            g.drawLine(0, y, width+10, y);
            String label = Integer.toString(y / gridSize); // Calcula la etiqueta
            g.drawString(label, 5, y + 15); // Ajusta la posición x y al gusto
        }

        String X = "--> X";
        String Y1 = "--> ";
        String Y = "Y";
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.BLACK);

        // Dibuja X
        g2d.drawString(X, 25, 15);

        // Rotación y dibujo de Y
        AffineTransform oldTransform = g2d.getTransform();
        AffineTransform at = new AffineTransform();
        at.rotate(Math.PI / 2, 10, 25); // Rotación de 90 grados en sentido contrario a las manecillas del reloj
        g2d.setTransform(at);
        g2d.drawString(Y1, 10, 25);
        g2d.setTransform(oldTransform);
        g2d.drawString(Y, 10, 60);
    }

    public void drawObs(Graphics g){
        g.setColor(Color.BLUE);
        int radio = 20; // Tamaño del círculo

        for (GridElement elemento : elementos) {
            if (elemento.tipo == GridElementType.OBSTACLE) {
                g.fillOval( elemento.x-radio/2, elemento.y-radio/2, radio, radio);
            }
        }
    }
    private void drawArrow(Graphics g,int x,int y,String sentidoDirrecion) {
        Graphics2D g2d = (Graphics2D) g;
        int arrowSize = 20;

        int[] xPoints;
        int[] yPoints;

        if (sentidoDirrecion.equals("N")) {
            xPoints = new int[] {x, x - arrowSize / 2, x + arrowSize / 2};
            yPoints = new int[] {y - arrowSize, y, y};
        } else if (sentidoDirrecion.equals("S")) {
            xPoints = new int[] {x, x - arrowSize / 2, x + arrowSize / 2};
            yPoints = new int[] {y + arrowSize, y, y};
        } else if (sentidoDirrecion.equals("W")) {
            xPoints = new int[] {x - arrowSize, x, x};
            yPoints = new int[] {y, y - arrowSize / 2, y + arrowSize / 2};
        } else if (sentidoDirrecion.equals("E")) {
            xPoints = new int[] {x + arrowSize, x, x};
            yPoints = new int[] {y, y - arrowSize / 2, y + arrowSize / 2};
        } else {
            // Dirección desconocida o no válida, no se dibuja nada
            return;
        }
        g2d.setColor(Color.RED);
        g2d.fillPolygon(xPoints, yPoints, 3);
    }

    public void updatePlateau(Plateau plateau, List<GridElement> elementos) {
        this.plateau = plateau;
        this.elementos = elementos;
        // Llamar a repaint para redibujar el panel con los nuevos datos
        repaint();
    }
}
