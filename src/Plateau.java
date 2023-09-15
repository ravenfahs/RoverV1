import javax.swing.*;

public class Plateau {
    public JPopupMenu panel;
    private int width;
    private int height;
    private int tamañoGrid;
    private int numeroDeObstaculos;
    private  int gridSize;
    private int cellXRover;
    private int cellYRover;
    private String sentido;

    public String getComandos() {
        return comandos;
    }

    public String setComandos(String comandos) {
        this.comandos = comandos;
        return comandos;
    }

    private String comandos;

    public String getSentido() {
        return sentido;
    }

    public void setSentido(String sentido) {
        this.sentido = sentido;
    }

    public int getCellXRover() {
        return cellXRover;
    }

    public void setCellXRover(int cellXRover) {
        this.cellXRover = cellXRover;
    }

    public int getCellYRover() {
        return cellYRover;
    }

    public void setCellYRover(int cellYRover) {
        this.cellYRover = cellYRover;
    }

    public Plateau() {
    }

    public int getGridSize() {
        return gridSize;
    }

    public void setGridSize(int gridSize) {
        this.gridSize = gridSize;
    }

    public int getNumeroDeObstaculos() {
        return numeroDeObstaculos;
    }

    public void setNumeroDeObstaculos(int numeroDeObstaculos) {
        this.numeroDeObstaculos = numeroDeObstaculos;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getTamañoGrid() {
        return tamañoGrid;
    }

    public void setTamañoGrid(int tamañoGrid) {
        this.tamañoGrid = tamañoGrid;
    }
}
