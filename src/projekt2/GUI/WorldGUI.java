package projekt2.GUI;

import projekt2.Logger;
import projekt2.Organisms.Animals.*;
import projekt2.Organisms.Organism;
import projekt2.Organisms.Plants.*;
import projekt2.World;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import static java.lang.Math.*;

public class WorldGUI implements ActionListener, KeyListener {

    private static final int MARGIN = 20;
    private boolean proccesignInput = false;
    private JFrame frame;
    private JMenuBar menuBar;
    private JMenu menu,newGameMenu;
    private JMenuItem newGameSquares,newGameHex,load,save,exit;
    private JPanel mainPanel;
    private BoardGraphics boardGraphics;
    private LogGraphics logGraphics;
    private PopupMenu popupMenu;
    private World world;



    private void createMenuBar(){
        menuBar = new JMenuBar();
        menu = new JMenu("Menu");

        newGameMenu = new JMenu("New game");
        newGameSquares = new JMenuItem("Square board");
        newGameHex = new JMenuItem("Hex board");

        load = new JMenuItem("Load");
        save = new JMenuItem("Save");
        exit = new JMenuItem("Exit");

        newGameSquares.addActionListener(this);
        newGameHex.addActionListener(this);
        load.addActionListener(this);
        save.addActionListener(this);
        exit.addActionListener(this);

        newGameMenu.add(newGameSquares);
        newGameMenu.add(newGameHex);
        menu.add(newGameMenu);
        menu.add(load);
        menu.add(save);
        menu.add(exit);
        menuBar.add(menu);


    }

    private void createPanel(){
        mainPanel = new JPanel();
        mainPanel.setBackground(Color.BLACK);
        mainPanel.setLayout(null);

    }
    private void createFrame(){
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension dimension = toolkit.getScreenSize();

        int initialSizeX = dimension.width / 2;
        int initialSizeY = dimension.height / 2;


        frame = new JFrame();
        frame.setBounds(100,100,initialSizeX,initialSizeY);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setJMenuBar(menuBar);
        frame.setLayout(new CardLayout());
        frame.addKeyListener(this);
        frame.add(mainPanel);
        frame.setVisible(true);

    }

    public WorldGUI(String title){
        createMenuBar();
        createPanel();
        createFrame();
    }

    private void startGame(){
        popupMenu = new PopupMenu();
        boardGraphics = new BoardGraphics();
        mainPanel.add(boardGraphics);

        logGraphics = new LogGraphics();
        mainPanel.add(logGraphics);

        refreshGUI();
    }

    private void newGameStart(World.WorldType worldType){
        String worldName = JOptionPane.showInputDialog(frame,"Podaj nazwe swiata","nowySwiat");
        int sizeX = Integer.parseInt(JOptionPane.showInputDialog(frame,
                "Podaj szerokosc swiata", "20"));
        int sizeY = Integer.parseInt(JOptionPane.showInputDialog(frame,
                "Podaj wysokosc swiata", "20"));

        world = new World(worldName,sizeX,sizeY,this,worldType);
        world.generateTestWorld();

        if(boardGraphics != null){
            mainPanel.remove(boardGraphics);
        }
        if(logGraphics != null){
            mainPanel.remove(logGraphics);
        }

        startGame();

    }
    private void loadGame() throws FileNotFoundException {
        String worldName = JOptionPane.showInputDialog(frame,"Podaj nazwe swiata","zapisSwiata");
        try{
            File save = new File(worldName + ".sav");
            world = new World(save,this);

            if(boardGraphics != null){
                mainPanel.remove(boardGraphics);
            }
            if(logGraphics != null){
                mainPanel.remove(logGraphics);
            }

            startGame();
        }
        catch(FileNotFoundException e){
            JOptionPane.showMessageDialog(frame, "Taki zapis nie isntieje!");
            return;
        }

    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == newGameSquares ) {
            newGameStart(World.WorldType.SQUARE);
        }
        else if (e.getSource() == newGameHex){
            newGameStart(World.WorldType.HEX);
        }
        else if(e.getSource() == save){
            try {
                world.saveWorld();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        } else if (e.getSource() == load) {
            try {
                loadGame();
            } catch (FileNotFoundException ex) {
                throw new RuntimeException(ex);
            }
        } else if(e.getSource() == exit){
            frame.dispose();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(world != null && !proccesignInput){
            proccesignInput = true;
            int keyCode = e.getKeyCode();
            if(keyCode == KeyEvent.VK_ENTER){
                world.simulateTurn();
            }
            else if(keyCode == KeyEvent.VK_P){
                world.getCzlowiek().activatePower();
            }
            else{
                world.getCzlowiek().updateMove(keyCode);
            }

            refreshGUI();
        }
        proccesignInput = false;
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    private class BoardGraphics extends JPanel implements MouseListener{
        private static final Color bgColor =  Color.WHITE;
        private int fieldSizeX;
        private int fieldSizeY;

        private int newOrgPosX;
        private int newOrgPosY;
        public BoardGraphics(){
            super();
            setBackground(Color.WHITE);
            setBounds(mainPanel.getX() + MARGIN,mainPanel.getY() + MARGIN,
                    mainPanel.getHeight() * 5 / 6 - MARGIN, mainPanel.getHeight() * 5 / 6 - MARGIN);
            addMouseListener(this);
        }

        private void squarePaint(Graphics g){
            int worldY = world.getSizeY();
            int worldX = world.getSizeX();

            fieldSizeX = this.getWidth() / worldX;
            fieldSizeY = this.getHeight() / worldY;

            g.setColor(bgColor);
            g.fillRect(0,0,worldX*fieldSizeX,worldY*fieldSizeY);
            for (Organism o : world.getOrganisms()) {
                //System.out.println(o.getPosition().x);
                g.setColor(o.getColor());
                g.fillRect(o.getPosition().x*fieldSizeX,o.getPosition().y*fieldSizeY,fieldSizeX,fieldSizeY);
            }
        }

        private void hexPaint(Graphics g){

            int worldY = world.getSizeY();
            int worldX = world.getSizeX();

            fieldSizeX = (int) (this.getWidth() / (worldX+(worldX)/2));
            fieldSizeY = this.getHeight() / worldY;

            Organism[][] organismsBoard = new Organism[worldX][worldY];
            for (Organism o : world.getOrganisms()) {
                organismsBoard[o.getPosition().x][o.getPosition().y] = o;
            }
            for(int y = 0;y<worldX;y++){
                for(int x = 0;x<worldY;x++){
                    Organism o = organismsBoard[x][y];
                    if(o != null){
                        g.setColor(o.getColor());
                    }
                    else{
                        g.setColor(bgColor);
                    }
                    g.fillRect((x*fieldSizeX) + (y*fieldSizeX/2), (y*fieldSizeY),fieldSizeX,fieldSizeY);
                }
            }
        }

        @Override
        public void paint(Graphics g){
            if(world.getWorldType() == World.WorldType.SQUARE){
                squarePaint(g);
            }
            else if(world.getWorldType() == World.WorldType.HEX){
                hexPaint(g);
            }


        }

        @Override
        public void mouseClicked(MouseEvent e) {
            int x = e.getX();
            int y = e.getY();
            System.out.println(x + " " + y + "\n");

            if(world.getWorldType() == World.WorldType.SQUARE){
                newOrgPosX = x/fieldSizeX;
                newOrgPosY = y/fieldSizeY;

                System.out.println(newOrgPosX + " " + newOrgPosY + "\n");
                if(world.findCollision(new Point(newOrgPosX,newOrgPosY)) == null )
                    popupMenu.show(boardGraphics,x,y);
            }
            else if(world.getWorldType() == World.WorldType.HEX){
                newOrgPosY = y/fieldSizeY;
                newOrgPosX = (2*x-newOrgPosY*fieldSizeX)/(2*fieldSizeX);
                System.out.println(newOrgPosX + " " + newOrgPosY + "\n");
                if(world.findCollision(new Point(newOrgPosX,newOrgPosY)) == null )
                    popupMenu.show(boardGraphics,x,y);
            }



        }

        @Override
        public void mousePressed(MouseEvent e) {

        }

        @Override
        public void mouseReleased(MouseEvent e) {

        }

        @Override
        public void mouseEntered(MouseEvent e) {

        }

        @Override
        public void mouseExited(MouseEvent e) {

        }
        public int getFieldSizeX(){
            return fieldSizeX;
        }
        public int getFieldSizeY(){
            return fieldSizeY;
        }
        public int getNewOrgPosX(){return newOrgPosX;}
        public int getNewOrgPosY(){return newOrgPosY;}
    }

    private class LogGraphics extends JPanel{
        private String logs;
        private final JTextArea textArea;
        public LogGraphics(){
            super();
            setBounds(boardGraphics.getX() + boardGraphics.getWidth() + MARGIN,
                    mainPanel.getY() + MARGIN,
                    mainPanel.getWidth() - boardGraphics.getWidth() - MARGIN * 3,
                    mainPanel.getHeight() * 5 / 6 - MARGIN);

            logs = Logger.getLog();

            textArea = new JTextArea(logs);
            textArea.setEditable(false);
            setLayout(new CardLayout());
            textArea.setLineWrap(true);
            textArea.setWrapStyleWord(true);
            textArea.setMargin(new Insets(5, 5, 5, 5));

            JScrollPane sp = new JScrollPane(textArea);
            add(sp);
        }

        public void refreshLogGraphics(){
            String header = "tu som logi\nTurn: " + world.getTurn() + "\n";
            logs = header + Logger.getLog();
            textArea.setText(logs);
        }
    }

    private void refreshGUI(){
        boardGraphics.paint(boardGraphics.getGraphics());
        logGraphics.refreshLogGraphics();
        SwingUtilities.updateComponentTreeUI(frame);
        frame.requestFocusInWindow();
    }

    private class PopupMenu extends JPopupMenu implements ActionListener{
        private JMenuItem[] menuItems;
        public PopupMenu(){
            menuItems = new JMenuItem[Organism.TypOrganizmu.values().length];
            int i = 0;
            for(Organism.TypOrganizmu type : Organism.TypOrganizmu.values()){
                menuItems[i] = new JMenuItem(type.toString());
                menuItems[i].addActionListener(this);
                this.add(menuItems[i]);
                i++;
            }
        }
        @Override
        public void actionPerformed(ActionEvent e) {
            int newOrgPosX = boardGraphics.newOrgPosX;
            int newOrgPosY = boardGraphics.newOrgPosY;

            Object src = e.getSource();
            if(src == menuItems[0]){//Czlowiek
                world.addOrganism(new Czlowiek(new Point(newOrgPosX,newOrgPosY),world));
            }
            else if(src == menuItems[1]){//Wilk
                world.addOrganism(new Wilk(new Point(newOrgPosX,newOrgPosY),world));
            }
            else if(src == menuItems[2]){//Owca
                world.addOrganism(new Owca(new Point(newOrgPosX,newOrgPosY),world));
            }
            else if(src == menuItems[3]){//Lis
                world.addOrganism(new Lis(new Point(newOrgPosX,newOrgPosY),world));
            }
            else if(src == menuItems[4]){//Zolw
                world.addOrganism(new Zlow(new Point(newOrgPosX,newOrgPosY),world));
            }
            else if(src == menuItems[5]){//Antylopa
                world.addOrganism(new Antylopa(new Point(newOrgPosX,newOrgPosY),world));
            }
            else if(src == menuItems[6]){//Trawa
                world.addOrganism(new Trawa(new Point(newOrgPosX,newOrgPosY),world));
            }
            else if(src == menuItems[7]){//Mlecz
                world.addOrganism(new Mlecz(new Point(newOrgPosX,newOrgPosY),world));
            }
            else if(src == menuItems[8]){//Guarana
                world.addOrganism(new Guarana(new Point(newOrgPosX,newOrgPosY),world));
            }
            else if(src == menuItems[9]){//Wilcze jagody
                world.addOrganism(new WilczeJagody(new Point(newOrgPosX,newOrgPosY),world));
            }
            else if(src == menuItems[10]){//Barszcz sosnowskiego
                world.addOrganism(new BarszczSosnowskiego(new Point(newOrgPosX,newOrgPosY),world));
            }
            refreshGUI();

        }
    }
}
