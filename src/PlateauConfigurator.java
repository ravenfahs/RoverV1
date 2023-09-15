import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PlateauConfigurator extends JFrame implements ActionListener {
    private final List<GridElement> elementos = new ArrayList<>();
    private final List<ListCommandElements> commandElementsList = new ArrayList<>();
    private final List<String> roverHistory = new ArrayList<>();
    DefaultListModel<String> listModel = new DefaultListModel<>();
    private final Plateau plateau = new Plateau();
    private final PlateauPanel roverPanel;
    private JTextField widthTextField;
    private JTextField heightTextField;
    private JTextField gridSizeTextField;
    private JTextField obstaclesTextField;
    private JTextField moverRoverXY;
    private JButton configureButton;
    private JButton moverRoverBtn;
    private JButton configureRoverButton;
    private JTextField xInicialTextField;
    private JTextField yInicialTextField;
    private JTextField sentidoITextField;
    private boolean plateauPanelCreated = false;
    GridElement ultimoRover = null;
    JPanel panelConfigPlateau = new JPanel();
    JPanel panelRoverInicial = new JPanel();
    private JFrame frame;
    private final JFrame frameP = new JFrame();
    private final JFrame framePla = new JFrame();


    public PlateauConfigurator() {
        initializeUI();
        roverPanel = new PlateauPanel(plateau, elementos); // Inicializa roverPanel en el constructor
    }

    private void initializeUI(){
        //Se configura el área de la meseta, el número de secciones que se dividirá para formar los espacios donde se podrá
        // mover el rover, número de posibles obstáculos que tendrá la meseta
        //JPanel panel = new JPanel();

        framePla.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        framePla.setSize( 400, 200);
        framePla.setLocationRelativeTo(null);

        panelConfigPlateau.setLayout(new GridLayout(5, 2));

        setTitle("..* plateau configuration * ...");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 200);
        setLocationRelativeTo(null);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.EAST;

        panelConfigPlateau.add(new JLabel("Width:", SwingConstants.RIGHT));
        widthTextField = new JTextField();
        panelConfigPlateau.add(widthTextField);

        panelConfigPlateau.add(new JLabel("Height:", SwingConstants.RIGHT));
        heightTextField = new JTextField();
        panelConfigPlateau.add(heightTextField);

        panelConfigPlateau.add(new JLabel("Grid Size:", SwingConstants.RIGHT));
        gridSizeTextField = new JTextField();
        panelConfigPlateau.add(gridSizeTextField);

        panelConfigPlateau.add(new JLabel("Number of Obstacles:", SwingConstants.RIGHT));
        obstaclesTextField = new JTextField();
        panelConfigPlateau.add(obstaclesTextField);

        configureButton = new JButton("Setup");
        configureButton.addActionListener(this);
        panelConfigPlateau.add(new JLabel());
        panelConfigPlateau.add(configureButton);
        add(panelConfigPlateau);
        framePla.add(panelConfigPlateau);
        framePla.setVisible(true);

    }


    public void posicionIncialRover(){

        //Frame frameP = new JFrame("Control Rover on the Plateau-P");
        frameP.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frameP.setSize( 400, 200);
        frameP.setLocationRelativeTo(null);
/*
        setTitle("..* Rover configuration * ...");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 200);
        setLocationRelativeTo(null);
*/
        panelRoverInicial.setLayout(new GridLayout(4, 2));

        GridBagConstraints gbc1 = new GridBagConstraints();
        gbc1.anchor = GridBagConstraints.EAST;


        // Agregar KeyListener para detectar "Enter" en Sentido inicial
        panelRoverInicial.add(new JLabel("Posición inicial en X:", SwingConstants.RIGHT));
        xInicialTextField = new JTextField(10);
        panelRoverInicial.add(xInicialTextField);

        panelRoverInicial.add(new JLabel("Posición inicial en Y:", SwingConstants.RIGHT));
        yInicialTextField = new JTextField(10);
        panelRoverInicial.add(yInicialTextField);

        panelRoverInicial.add(new JLabel("Sentido inicial:", SwingConstants.RIGHT));
        sentidoITextField = new JTextField(10);
        panelRoverInicial.add(sentidoITextField);

        configureRoverButton = new JButton("Sent");
        configureRoverButton.addActionListener(this);
        panelRoverInicial.add(new JLabel());
        panelRoverInicial.add(configureRoverButton);
        add(panelRoverInicial);
        panelRoverInicial.setVisible(true);
        frameP.add(panelRoverInicial);
        frameP.setVisible(true);

    }

    public void configureRover() {
        int contarRover = 0;

        for (GridElement rover : elementos){
            if(rover.tipo == GridElementType.ROVER){
                contarRover++;
            }
        }
        if(contarRover == 0) {
            boolean roverInicialOk = false;
            while (!roverInicialOk){
                try {
                    //Validar posición en X
                    int xRover = Integer.parseInt(xInicialTextField.getText());
                    if (xRover >= 0 && xRover < plateau.getWidth()) {
                        plateau.setCellXRover(xRover * plateau.getGridSize() + plateau.getGridSize() / 2);
                    } else {
                        JOptionPane.showMessageDialog(this, "X inicial esta fuera de la meseta.");
                        break;
                    }
                    //Validar posición en Y
                    int yRover = Integer.parseInt(yInicialTextField.getText());
                    if (yRover >= 0 && yRover < plateau.getHeight()) {
                        plateau.setCellYRover(yRover * plateau.getGridSize() + plateau.getGridSize() / 2);
                    } else {
                        JOptionPane.showMessageDialog(this, "Y inicial esta fuera de la meseta.");
                        break;
                    }
                    //Validar el sentido
                    String sentidoRover = sentidoITextField.getText();
                    if (sentidoRover.equals("N") || sentidoRover.equals("S") || sentidoRover.equals("E") || sentidoRover.equals("W")) {
                        plateau.setSentido(sentidoITextField.getText());
                    } else {
                        JOptionPane.showMessageDialog(this, "No es un sentido valido. Debe ser Norte N, Sur S, Oeste W, Este E");
                        break;
                    }
                    int XR = plateau.getCellXRover();
                    int YR = plateau.getCellYRover();
                    String sentidoInicial = plateau.getSentido();
                    if (XR >= 0 && XR < plateau.getWidth() && YR >= 0 && YR < plateau.getHeight() && sentidoInicial != null) {
                        if(isPositionOccupied(XR,YR)){
                            roverInicialOk = true;
                            elementos.add(new GridElement(XR, YR, sentidoInicial, GridElementType.ROVER));
                            launchPlateauPanel();
                            //panelRoverInicial.setVisible(false);
                        }else{
                            JOptionPane.showMessageDialog(this, "Opps cuidado, En posición inicial indicada " + xRover + "," + yRover + " existe un obstáculo, intenta con otra posición");
                            break;
                        }

                    } else {
                        JOptionPane.showMessageDialog(this, "El rover está fuera de la región. los valores de X o Y Deben ser menores que " + plateau.getTamañoGrid());
                        break;
                    }
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this, "Por favor, ingresa valores válidos.");
                    break;
                }
            }
        }else{
            panelRoverInicial.setVisible(true);
            panelRoverInicial.revalidate();
            panelRoverInicial.repaint();
        }
    }


    ///---------------------------------------------correcto-------------------------------------------------
    @Override
    //Configurar las rutas de los botones
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == configureButton) {
            configurePlateau();//obstacúlos
            posicionIncialRover();//rover
        }
        else if(e.getSource() == configureRoverButton){
            configureRover(); //validar la posición del rover
        }
        //Recibe las cadenas de comandos (Forward, Backward, Left, Right)
        else if (e.getSource() == moverRoverBtn) {
            String mover = plateau.setComandos(moverRoverXY.getText());
            System.out.println(mover);
            //validar si la cadena tiene los siguientes caracteres F, L, R
            for (int i = 0; i < mover.length(); i++) {
                char comando = mover.charAt(i);
                switch (comando) {
                    case 'F' -> {
                        commandElementsList.add(new ListCommandElements(CommandElements.F));
                        moverRover();
                    }
                    case 'L' -> {
                        commandElementsList.add(new ListCommandElements(CommandElements.L));
                        moverRover();
                    }
                    case 'R' -> {
                        commandElementsList.add(new ListCommandElements(CommandElements.R));
                        moverRover();
                    }
                    case 'B' -> {
                        commandElementsList.add(new ListCommandElements(CommandElements.B));
                        moverRover();
                    }
                    default -> {
                        JOptionPane.showMessageDialog(this,"Comando no es reconocido: " + mover, "Alerta",JOptionPane.WARNING_MESSAGE);
                        dispose();
                        i=mover.length();
                    }
                }
            }
        }
    }

    private void configurePlateau() {
        try {
            //Se valida en ancho
            int width = Integer.parseInt(widthTextField.getText());
            if(width<=0){
                JOptionPane.showMessageDialog(this,"El ancho de la meseta debe ser mayor que cero.");
            }
            else{
                plateau.setWidth(width);
            }
            //Se valida el alto
            int height = Integer.parseInt(heightTextField.getText());
            if(height<=0){
                JOptionPane.showMessageDialog(this,"El alto de la meseta debe ser mayor que cero." );
            }
            else {
                plateau.setHeight(height);
            }
            // Validar el tamaño de la cuadrícula de la sección
            int gridSize = Integer.parseInt(gridSizeTextField.getText());
            if (gridSize <= 0) {
                JOptionPane.showMessageDialog(this, "El tamaño de la cuadrícula debe ser mayor que cero.");
            } else {
                plateau.setTamañoGrid(gridSize);
            }
            // Validar el número de obstáculos
            int numObstacles = Integer.parseInt(obstaclesTextField.getText());
            if (numObstacles <= 0) {
                JOptionPane.showMessageDialog(this, "El número de obstáculos debe ser mayor que cero.");
            } else {
                plateau.setNumeroDeObstaculos(numObstacles);
            }
            plateau.setGridSize(Math.min(plateau.getWidth() / plateau.getTamañoGrid(), plateau.getHeight() / plateau.getTamañoGrid()));

            // Configurar las posiciónes de los obstáculos
            if (elementos.isEmpty()) {
                // Capturar las coordenadas de los obstáculos
                for (int i = 0; i < plateau.getNumeroDeObstaculos(); i++) {
                    boolean coordenadasCorrectas = false; // Bandera para verificar si las coordenadas son correctas
                    while (!coordenadasCorrectas) {
                        System.out.println("Coordenadas del obstáculo " + (i + 1));
                        String input = JOptionPane.showInputDialog("Ingresa las coordenadas X e Y del obstáculo " + (i + 1) + " de "+ i +" separadas por una coma (por ejemplo, 3,5):");
                        // Dividir la entrada en coordenadas X e Y
                        String[] parts = input.split(",");
                        if (parts.length == 2) {
                            try {
                                int columnPoint = Integer.parseInt(parts[0].trim());
                                int rowPoint = Integer.parseInt(parts[1].trim());
                                // Convertir coordenadas a píxeles
                                int x = columnPoint * plateau.getGridSize() + plateau.getGridSize() / 2;
                                int y = rowPoint * plateau.getGridSize() + plateau.getGridSize() / 2;
                                System.out.println("X: " + columnPoint + " Y: " + rowPoint);
                                if (x >= 0 && x < plateau.getWidth() && y >= 0 && y < plateau.getHeight()) {
                                    elementos.add(new GridElement(x, y, "0", GridElementType.OBSTACLE));
                                    panelConfigPlateau.setVisible(false);

                                    coordenadasCorrectas = true; // Las coordenadas son correctas, sal del bucle
                                } else {
                                    JOptionPane.showMessageDialog(this, "El obstáculo está fuera de la región. Los valores de X o Y Deben ser menores que "+ plateau.getTamañoGrid());
                                }
                            } catch (NumberFormatException ex) {
                                JOptionPane.showMessageDialog(this, "Ingresa números válidos para las coordenadas X e Y.");
                            }
                        } else {
                            JOptionPane.showMessageDialog(this, "Por favor, ingresa las coordenadas en el formato 'X,Y'.");
                        }
                    }
                }
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Por favor, ingresa valores válidos.");
        }
    }

    private void launchPlateauPanel() {
        //PlateauPanel roverPanel = new PlateauPanel(plateau, elementos);
        roverPanel.setSize(plateau.getWidth() + 250, plateau.getHeight() + 60);
        roverPanel.updatePlateau(plateau, elementos);

        if (!plateauPanelCreated) {
            frame = new JFrame("Control Move Rover on the Plateau");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(plateau.getWidth() + 300, plateau.getHeight() + 60);
            frame.setLocationRelativeTo(null);

            JPanel formPanel = new JPanel(new GridLayout(2, 1));
            formPanel.setPreferredSize(new Dimension(formPanel.getPreferredSize().width + 250, 60)); // Establece el tamaño preferido en 30 píxeles
            formPanel.setBackground(Color.LIGHT_GRAY);

            JPanel row1 = new JPanel();
            BoxLayout boxLayout = new BoxLayout(row1,BoxLayout.Y_AXIS);
            row1.setLayout(boxLayout);
            JLabel labelForward = new JLabel("F: Forward");
            labelForward.setAlignmentX(Component.LEFT_ALIGNMENT);
            JLabel labelBackward = new JLabel("B: Backward");
            labelBackward.setAlignmentX(Component.LEFT_ALIGNMENT);
            JLabel labelLeft = new JLabel("L: Lef");
            labelLeft.setAlignmentX(Component.LEFT_ALIGNMENT);
            JLabel labelRight = new JLabel("R: Right");
            labelRight.setAlignmentX(Component.LEFT_ALIGNMENT);
            JLabel labelE = new JLabel("Ejemplo: FFRFFLB");
            labelE.setAlignmentX(Component.LEFT_ALIGNMENT);
            JLabel labelRover = new JLabel("Comando rover: ");
            labelRover.setAlignmentX(Component.LEFT_ALIGNMENT);

            row1.add(labelForward);
            row1.add(labelBackward);
            row1.add(labelLeft);
            row1.add(labelRight);
            row1.add(labelE);
            row1.add(labelRover);

            moverRoverXY = new JTextField(10);
            moverRoverXY.setAlignmentX(Component.LEFT_ALIGNMENT);
            Dimension preferredSize = moverRoverXY.getPreferredSize();
            preferredSize.height = moverRoverXY.getPreferredSize().height; // Mantener la altura original
            moverRoverXY.setMaximumSize(preferredSize);
            moverRoverXY.setMinimumSize(preferredSize);
            row1.add(moverRoverXY);

            moverRoverBtn = new JButton("Send.");
            moverRoverBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
            moverRoverBtn.addActionListener(this);
            row1.add(moverRoverBtn);
            formPanel.add(row1);

            JList<String> historyList = new JList<>();
            historyList.setModel(listModel);
            formPanel.add(new JScrollPane(historyList), BorderLayout.CENTER);
            formPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));

            JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, roverPanel, formPanel);
            splitPane.revalidate();
            splitPane.repaint();
            splitPane.setResizeWeight(1); // Proporción de tamaño entre los paneles

            frame.add(splitPane);
            frame.setVisible(true);
            frame.revalidate();
            frame.repaint();
            plateauPanelCreated = true; // Marcar que la ventana se ha creado

        }
    }
    private void moverRover() {

        for (ListCommandElements c: commandElementsList) {

            // última posición del rover
            for (GridElement element : elementos) {
                if (element.tipo == GridElementType.ROVER) {
                    ultimoRover = element;
                    System.out.println("El ultimo: " + ultimoRover.sO + " numero de elementos" + elementos.size());
                }
            }
            // Actualiza solo el sentido del rover hacia la izquierda
            if(c.commandElements.equals(CommandElements.L)){
                switch (ultimoRover.sO) {
                    case "N" -> {
                        plateau.setSentido(ultimoRover.sO = "W");
                        agregrarRoverElemento();
                    }
                    case "W" -> {
                        plateau.setSentido(ultimoRover.sO = "S");
                        agregrarRoverElemento();
                    }
                    case "S" -> {
                        plateau.setSentido(ultimoRover.sO = "E");
                        agregrarRoverElemento();
                    }
                    case "E" -> {
                        plateau.setSentido(ultimoRover.sO = "N");
                        agregrarRoverElemento();
                    }
                    default -> {
                        // Dirección desconocida o no válida, no se cambia el sentido
                    }
                }
            }
            // Actualiza solo el sentido del rover hacia la derecha
            if(c.commandElements.equals(CommandElements.R)){
                switch (ultimoRover.sO) {
                    case "N" -> {
                        plateau.setSentido(ultimoRover.sO = "E");
                        agregrarRoverElemento();
                    }
                    case "E" -> {
                        plateau.setSentido(ultimoRover.sO = "S");
                        agregrarRoverElemento();
                    }
                    case "S" -> {
                        plateau.setSentido(ultimoRover.sO = "W");
                        agregrarRoverElemento();
                    }
                    case "W" -> {
                        plateau.setSentido(ultimoRover.sO = "N");
                        agregrarRoverElemento();
                    }
                    default -> {
                        // Dirección desconocida o no válida, no se cambia el sentido
                    }
                }
            }
            //Avanzar hacia delante
            if(c.commandElements.equals(CommandElements.F)) {
                int move = plateau.getGridSize() / 2;
                //Se guarda el ultimo elemento de la lista
                switch (Objects.requireNonNull(ultimoRover).sO) {
                    case "N" -> {
                        moverNorte(move);
                    }
                    case "S" -> {
                        moverSur(move);
                    }
                    case "W" -> {
                        moverOeste(move);
                    }
                    case "E" -> {
                        moverEste(move);
                    }
                    default -> {
                        // Dirección desconocida o no válida, no se dibuja nada
                        return;
                    }
                }
            }
            //Retroceder conservando su dirección
            if(c.commandElements.equals(CommandElements.B)) {
                int move = plateau.getGridSize() / 2;
                //Se guarda el ultimo elemento de la lista
                switch (Objects.requireNonNull(ultimoRover).sO) {
                    case "N" -> {
                        moverSur(move);
                    }
                    case "S" -> {
                        moverNorte(move);
                    }
                    case "W" -> {
                        moverEste(move);
                    }
                    case "E" -> {
                        moverOeste(move);
                    }
                    default -> {
                        // Dirección desconocida o no válida, no se dibuja nada
                        return;
                    }
                }
            }
            roverPanel.repaint();
        }
        commandElementsList.clear();
    }

     //Movimientos en las direcciones norte, sur, oriente, occidente, y establecer los límites de la meseta
    private void moverNorte(int move) {
        int auxY = (move + plateau.getGridSize() + plateau.getGridSize() / 2);
        if (plateau.getCellYRover() - auxY / 2 >= 0) {
            if(isPositionOccupied(plateau.getCellXRover(), plateau.getCellYRover() - auxY / 2)){
                plateau.setCellYRover(plateau.getCellYRover() - auxY / 2);
                agregrarRoverElemento();
            }else{
                JOptionPane.showMessageDialog(this,"La posición hacia el Norte está ocupada por un obstáculo", "Alerta",JOptionPane.WARNING_MESSAGE);
                dispose();
                System.out.println("La posición hacia el Norte está ocupada por un obstáculo.");
            }
        } else {
            Object[] options = {"Izquierda", "Derecha", "Cancelar"};
            int option = JOptionPane.showOptionDialog(this, "El rover no puede salir de la cuadrícula hacia el Norte. ¿Qué deseas hacer?", "Cambiar de Sentido",
                    JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[2]);
            if (option == JOptionPane.YES_OPTION) {
                // El usuario eligió "Izquierda"
                plateau.setSentido("W");
                agregrarRoverElemento();
                dispose();
            } else if (option == JOptionPane.NO_OPTION) {
                // El usuario eligió "Derecha"
                // Realiza la lógica para cambiar el sentido a la derecha
                plateau.setSentido("E");
                agregrarRoverElemento();
                dispose();
            } else if (option == JOptionPane.CANCEL_OPTION) {
                JOptionPane.getRootFrame().dispose();
                frame.setVisible(true);
            }
        }
    }

    private void agregrarRoverElemento() {
        elementos.add(new GridElement(plateau.getCellXRover(), plateau.getCellYRover(), plateau.getSentido(), GridElementType.ROVER));
        String roverPosition = "X: " + ((plateau.getCellXRover() - plateau.getGridSize() / 2) / plateau.getGridSize())
                + ", Y: " + ((plateau.getCellYRover() - plateau.getGridSize() / 2) / plateau.getGridSize()) + ", Sentido: " + plateau.getSentido() + ", Paso: " + plateau.getComandos();
        roverHistory.add(roverPosition);
        updateHistoryList();
    }

    private void moverSur(int move) {
        int auxY = (move + plateau.getGridSize() + plateau.getGridSize() / 2);
        if (plateau.getCellYRover() + auxY / 2 < plateau.getHeight()) {
            if(isPositionOccupied(plateau.getCellXRover(), plateau.getCellYRover() + auxY / 2)){
                plateau.setCellYRover(plateau.getCellYRover() + auxY / 2);
                agregrarRoverElemento();
            }else {
                JOptionPane.showMessageDialog(this,"La posición hacia el Sur está ocupada por un obstáculo", "Alerta",JOptionPane.WARNING_MESSAGE);
                dispose();
                System.out.println("La posición hacia el Sur está ocupada por un obstáculo.");
            }

        } else {
            Object[] options = {"Izquierda", "Derecha", "Cancelar"};
            int option = JOptionPane.showOptionDialog(this, "El rover no puede salir de la cuadrícula hacia el Sur. ¿Qué deseas hacer?", "Cambiar de Sentido",
                    JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[2]);
            if (option == JOptionPane.YES_OPTION) {
                // El usuario eligió "Izquierda"
                plateau.setSentido("E");
                agregrarRoverElemento();
                dispose();
            } else if (option == JOptionPane.NO_OPTION) {
                // El usuario eligió "Derecha"
                // Realiza la lógica para cambiar el sentido a la derecha
                plateau.setSentido("W");
                agregrarRoverElemento();
                dispose();
            } else if (option == JOptionPane.CANCEL_OPTION) {
                JOptionPane.getRootFrame().dispose();
                frame.setVisible(true);
            }
        }
    }

    private void moverOeste(int move) {
        int auxX = (move + plateau.getGridSize() + plateau.getGridSize() / 2);
        if (plateau.getCellXRover() - auxX / 2 >= 0) {
            if(isPositionOccupied(plateau.getCellXRover() - auxX / 2, plateau.getCellYRover())){
                plateau.setCellXRover(plateau.getCellXRover() - auxX / 2);
                agregrarRoverElemento();
            }else{
                JOptionPane.showMessageDialog(this,"La posición hacia el Oeste está ocupada por un obstáculo", "Alerta",JOptionPane.WARNING_MESSAGE);
                dispose();
                System.out.println("La posición hacia el Oeste está ocupada por un obstáculo.");
            }

        } else {
            Object[] options = {"Izquierda", "Derecha", "Cancelar"};
            int option = JOptionPane.showOptionDialog(this, "El rover no puede salir de la cuadrícula hacia el Oeste. ¿Qué deseas hacer?", "Cambiar de Sentido",
                    JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[2]);
            if (option == JOptionPane.YES_OPTION) {
                // El usuario eligió "Izquierda"
                plateau.setSentido("S");
                agregrarRoverElemento();
                dispose();
            } else if (option == JOptionPane.NO_OPTION) {
                // El usuario eligió "Derecha"
                // Realiza la lógica para cambiar el sentido a la derecha
                plateau.setSentido("N");
                agregrarRoverElemento();
                dispose();
            } else if (option == JOptionPane.CANCEL_OPTION) {
                JOptionPane.getRootFrame().dispose();
                frame.setVisible(true);
            }
        }
    }

    private void moverEste(int move) {
        int auxX = (move + plateau.getGridSize() + plateau.getGridSize() / 2);
        if (plateau.getCellXRover() + auxX / 2 < plateau.getWidth()) {
            if(isPositionOccupied(plateau.getCellXRover() + auxX / 2, plateau.getCellYRover())){
                plateau.setCellXRover(plateau.getCellXRover() + auxX / 2);
                agregrarRoverElemento();
            }else{
                JOptionPane.showMessageDialog(this,"La posición hacia el Este está ocupada por un obstáculo", "Alerta",JOptionPane.WARNING_MESSAGE);
                dispose();
                System.out.println("La posición hacia el Este está ocupada por un obstáculo.");
            }

        } else {
            Object[] options = {"Izquierda", "Derecha", "Cancelar"};
            int option = JOptionPane.showOptionDialog(this, "El rover no puede salir de la cuadrícula hacia el Este. ¿Qué deseas hacer?", "Cambiar de Sentido",
                    JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[2]);
            if (option == JOptionPane.YES_OPTION) {
                // El usuario eligió "Izquierda"
                plateau.setSentido("N");
                agregrarRoverElemento();
                dispose();
            } else if (option == JOptionPane.NO_OPTION) {
                // El usuario eligió "Derecha"
                // Realiza la lógica para cambiar el sentido a la derecha
                plateau.setSentido("S");
                agregrarRoverElemento();
                dispose();
            } else if (option == JOptionPane.CANCEL_OPTION) {
                JOptionPane.getRootFrame().dispose();
                frame.setVisible(true);
            }
        }
    }

    private void updateHistoryList() {
        listModel.clear();
        for (String position : roverHistory) {
            listModel.addElement(position);
        }
    }
    private boolean isPositionOccupied(int x,int y) {
        for (GridElement element : elementos) {
            if (element.tipo == GridElementType.OBSTACLE && element.x == x && element.y == y) {
                return false;
            }
        }
        return true;
    }
}
