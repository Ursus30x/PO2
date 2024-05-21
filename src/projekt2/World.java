package projekt2;

import projekt2.GUI.WorldGUI;
import projekt2.Organisms.Animals.*;
import projekt2.Organisms.Organism;
import projekt2.Organisms.Plants.*;

import javax.swing.text.Position;
import java.awt.*;
import java.io.*;
import java.util.*;

public class World {
    public enum WorldType{
        NULL,
        SQUARE,
        HEX;

        public static WorldType toType(String type){
            if (Objects.equals(type, "HEX"))return HEX;
            else if(Objects.equals(type, "SQUARE"))return SQUARE;
            else return NULL;
        }
    }

    private final String worldName;
    private final WorldType worldType;
    private final int  sizeX;
    private final int sizeY;
    private int turn;
    private int idCounter;
    private ArrayList<Organism> organisms;
    private Czlowiek czlowiek;

    public World(String worldName,int sizeX, int sizeY, WorldGUI worldGUI,WorldType worldType){
        this.worldType = worldType;
        this.worldName = worldName;
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.turn = 0;
        this.idCounter = 0;
        organisms = new ArrayList<>();
        czlowiek = null;

    }
    public World(File save, WorldGUI worldGUI) throws FileNotFoundException {
        Scanner scanner = new Scanner(save);

        String line = scanner.nextLine();
        String[] properties = line.split(" ");

        this.worldType = WorldType.toType(properties[0]);
        this.worldName = properties[1];
        this.sizeX = Integer.parseInt(properties[2]);
        this.sizeY = Integer.parseInt(properties[3]);
        this.turn = Integer.parseInt(properties[4]);
        this.idCounter = Integer.parseInt(properties[5]);
        organisms = new ArrayList<>();

        while(scanner.hasNextLine()){
            line = scanner.nextLine();
            properties = line.split(" ");
            String orgType = properties[0];
            int id = Integer.parseInt(properties[1]);
            int age = Integer.parseInt(properties[2]);
            int strength = Integer.parseInt(properties[3]);
            int x = Integer.parseInt(properties[4]);
            int y = Integer.parseInt(properties[5]);
            int prevX = Integer.parseInt(properties[6]);
            int prevY = Integer.parseInt(properties[7]);

            Organism o = null;
            if(Objects.equals(orgType, Organism.TypOrganizmu.CZLOWIEK.toString())){
                o = new Czlowiek(new Point(x,y),this,id,age);
                czlowiek = (Czlowiek) o;
            }
            else if(Objects.equals(orgType, Organism.TypOrganizmu.WILK.toString())){
                o = new Wilk(new Point(x,y),this,id,age);
            }
            else if(Objects.equals(orgType, Organism.TypOrganizmu.OWCA.toString())){
                o = new Owca(new Point(x,y),this,id,age);
            }
            else if(Objects.equals(orgType, Organism.TypOrganizmu.LIS.toString())){
                o = new Lis(new Point(x,y),this,id,age);
            }
            else if(Objects.equals(orgType, Organism.TypOrganizmu.ZOLW.toString())){
                o = new Zlow(new Point(x,y),this,id,age);
            }
            else if(Objects.equals(orgType, Organism.TypOrganizmu.ANTYLOPA.toString())){
                o = new Antylopa(new Point(x,y),this,id,age);
            }
            else if(Objects.equals(orgType, Organism.TypOrganizmu.TRAWA.toString())){
                o = new Trawa(new Point(x,y),this,id,age);
            }
            else if(Objects.equals(orgType, Organism.TypOrganizmu.MLECZ.toString())){
                o = new Mlecz(new Point(x,y),this,id,age);
            }
            else if(Objects.equals(orgType, Organism.TypOrganizmu.GUARANA.toString())){
                o = new Guarana(new Point(x,y),this,id,age);
            }
            else if(Objects.equals(orgType, Organism.TypOrganizmu.WILCZEJAGODY.toString())){
                o = new WilczeJagody(new Point(x,y),this,id,age);
            }
            else if(Objects.equals(orgType, Organism.TypOrganizmu.BARSZCZSOSNOWSKIEGO.toString())){
                o = new BarszczSosnowskiego(new Point(x,y),this,id,age);
            }
            assert o != null;

            o.setPreviousPosition(new Point(prevX,prevY));
            o.setStrength(strength);
            this.addOrganism(o);
        }
        scanner.close();
    }

    public void saveWorld() throws IOException {
        try{
            FileWriter saveWriter = new FileWriter(worldName + ".sav");
            saveWriter.write(worldType.toString() + " " + worldName + " "
                    + sizeX + " " + sizeY + " " + turn + " " + idCounter + "\n");
            for(Organism o : organisms){
                String orgLine = o.getTypOrganizmu().toString() + " " + o.getId() + " " + o.getAge() + " " + o.getStrength() + " " + o.getPosition().x
                        + " " + o.getPosition().y + " " + o.getPreviousPosition().x + " " + o.getPreviousPosition().y + "\n";
                saveWriter.append(orgLine);
            }
            saveWriter.close();
        }
        catch (IOException e){
            e.printStackTrace();
        }

    }

    public void generateTestWorld(){
        Random rand = new Random();
        czlowiek = new Czlowiek(new Point(5,5),this);
        organisms.add(czlowiek);
//        organisms.add(new Owca(new Point(0,0),this));
//        organisms.add(new Owca(new Point(1,1),this));
    }

    public void addOrganism(Organism o){
        organisms.add(o);
    }
    public Organism findCollision(Point pos){
        ArrayList<Organism> organisms = this.getOrganisms();
        for (int i = 0;i<this.getOrganisms().size();i++) {
            if (this.getOrganisms().get(i).getPosition().equals(pos)) {
                return this.getOrganisms().get(i);
            }
        }
        return null;
    }

    private void SortOrganisms(){
        Collections.sort(organisms, new Comparator<Organism>() {
            @Override
            public int compare(Organism o1, Organism o2) {
                if(o1.getInitiative() == o2.getInitiative()) return Integer.compare(o1.getAge(), o2.getAge());
                else return Integer.compare(o2.getInitiative(), o1.getInitiative());
            }
        });
    }
    public void killOrganism(Organism killed){
        if(killed == null)return;
        if(killed == czlowiek)czlowiek = null;

        organisms.remove(killed);
    }
    private void increaseAgeForOrganisms(){
        for (Organism o1 : organisms) {
            int age = o1.getAge();
            o1.setAge(age+1);
        }
    }
    public void simulateTurn(){
        Logger.clearLog();
        SortOrganisms();

        increaseAgeForOrganisms();

        int dynamicSize = organisms.size();

        for (int i = 0;i < dynamicSize;i++) {
            Organism o1 = organisms.get(i);
            Organism o2 = null;
            o1.action();
            o2 = o1.findCollision();
            if (o2 != null) o2.collision(o1);
            dynamicSize = organisms.size();
        }
        turn++;
    }
    public int getSizeX(){
        return sizeX;
    }
    public int getSizeY(){
        return sizeY;
    }
    public int getTurn(){return turn;}
    public Czlowiek getCzlowiek(){
        return czlowiek;
    }
    public void setCzlowiek(Czlowiek czlowiek){this.czlowiek = czlowiek;}
    public int getIdCounter(){
        return idCounter;
    }
    public WorldType getWorldType(){
        return worldType;
    }
    public String getWorldName(){return worldName;}
    public void setIdCounter(int idCounter){
        this.idCounter = idCounter;
    }
    public ArrayList<Organism> getOrganisms(){
        return organisms;
    }
}
