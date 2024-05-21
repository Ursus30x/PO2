package projekt2.Organisms;

import projekt2.Organisms.Plants.Trawa;
import projekt2.World;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public abstract class Organism {
    static public final int AUTOID = -1;
    public static final int MATURE_AGE = 2;

    public enum TypOrganizmu {
        CZLOWIEK,
        WILK,
        OWCA,
        LIS,
        ZOLW,
        ANTYLOPA,
        TRAWA,
        MLECZ,
        GUARANA,
        WILCZEJAGODY,
        BARSZCZSOSNOWSKIEGO;

        @Override
        public String toString() {
            if(this.equals(CZLOWIEK)) return "Czlowiek";
            else if (this.equals(WILK)) return "Wilk";
            else if (this.equals(OWCA)) return "Owca";
            else if (this.equals(LIS)) return "Lis";
            else if (this.equals(ZOLW)) return "Zolw";
            else if (this.equals(ANTYLOPA)) return "Antylopa";
            else if (this.equals(TRAWA)) return "Trawa";
            else if (this.equals(MLECZ)) return "Mlecz";
            else if (this.equals(GUARANA)) return "Guarana";
            else if (this.equals(WILCZEJAGODY)) return "Wilcze-Jagody";
            else if (this.equals(BARSZCZSOSNOWSKIEGO)) return "Barszcz-Sosnowskiego";
            else return "null";
        }
    }
    private TypOrganizmu typOrganizmu;
    private final int id;
    private int age;
    private int strength;
    private final int initiative;
    private final Color color;
    private World world;
    private Point position;
    private Point previousPosition;

    protected Organism(int id,int age,int strength,int initiative,Color color,World world,Point position,TypOrganizmu typOrganizmu){
        if(id == AUTOID){
            this.id = world.getIdCounter();
            world.setIdCounter(this.id+1);
        }
        else{
            this.id = id;
        }
        this.age = age;
        this.strength = strength;
        this.initiative = initiative;
        this.color = color;
        this.world = world;
        this.position = position;
        this.previousPosition = position;
        this.typOrganizmu = typOrganizmu;
    }
    protected int randMove(){
        Random rand = new Random();
        return rand.nextInt(3) - 1;
    }

    public abstract void action();
    public abstract void  collision(Organism other);
    public abstract Organism getChild(Point pos);
    public void createChild(Point pos){
        world.getOrganisms().add(getChild(pos));
    }
    public Organism findCollision(){
        ArrayList<Organism> organisms = world.getOrganisms();
        for (int i = 0;i < world.getOrganisms().size();i++) {
            if (world.getOrganisms().get(i).getPosition().equals(this.position) && world.getOrganisms().get(i).getId() != this.getId()) {
                return world.getOrganisms().get(i);
            }
        }
        return null;
    }


    public Organism findCollision(Point pos){
        ArrayList<Organism> organisms = world.getOrganisms();
        for (int i = 0;i<world.getOrganisms().size();i++) {
            if (world.getOrganisms().get(i).getPosition().equals(pos)) {
                return world.getOrganisms().get(i);
            }
        }
        return null;
    }
    private ArrayList<Point> getFreeFieldsSquare(){
        ArrayList<Point> freeFields = new ArrayList<>();
        for(int x = -1;x<=1;x++){
            for(int y = -1;y<=1;y++){
                Point pos = new Point(getPosition().x + x, getPosition().y + y);
                if(inWorldBounds(pos)
                        && findCollision(pos) == null
                        && !(getPreviousPosition().equals(pos))
                )
                    freeFields.add(pos);
            }
        }
        return freeFields;
    }
    private ArrayList<Point> getFreeFieldsHex(){
        ArrayList<Point> freeFields = new ArrayList<>();
        for(int x = -1;x<=1;x++){
            for(int y = -1;y<=1;y++){
                if(x != y || (x == 0 && y == 0)){
                    Point pos = new Point(getPosition().x + x, getPosition().y + y);
                    if(inWorldBounds(pos)
                            && findCollision(pos) == null
                            && !(getPreviousPosition().equals(pos))
                    )
                        freeFields.add(pos);
                }

            }
        }
        return freeFields;
    }
    public ArrayList<Point> getFreeFields(){
        if(getWorld().getWorldType().equals(World.WorldType.SQUARE)){
            return getFreeFieldsSquare();
        }
        else if(getWorld().getWorldType().equals(World.WorldType.HEX)){
            return getFreeFieldsHex();
        }
        return null;
    }
    private boolean inWorldBounds(Point pos){
        return 0 <= pos.x && pos.x < getWorld().getSizeX() && 0 <= pos.y && pos.y < getWorld().getSizeY();
    }
    public int getStrength(){
        return strength;
    }
    public void setStrength(int strength){
        this.strength = strength;
    }
    public int getInitiative() {
        return initiative;
    }
    public TypOrganizmu getTypOrganizmu(){
        return typOrganizmu;
    }
    public int getAge(){
        return age;
    }
    public void setAge(int age){
        this.age = age;
    }
    public Point getPosition(){
        return position;
    }
    public void setPosition(Point position){
        this.position = position;
    }
    public int getId(){
        return id;
    }
    public Point getPreviousPosition(){
        return previousPosition;
    }
    public Color getColor(){
        return color;
    }
    public void setPreviousPosition(Point position){
        this.previousPosition = position;
    }
    public World getWorld(){
        return world;
    }
}
