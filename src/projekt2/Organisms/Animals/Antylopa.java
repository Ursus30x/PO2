package projekt2.Organisms.Animals;

import projekt2.Organisms.Organism;
import projekt2.Organisms.Plants.Guarana;
import projekt2.World;

import java.awt.*;
import java.util.Random;

public class Antylopa extends Animal{
    private static final int strength = 4;
    private static final int initiative = 4;
    private static final TypOrganizmu typOrganizmu = TypOrganizmu.ANTYLOPA;
    private static final Color color = new Color(93, 68, 68);

    public Antylopa(Point position, World world,int id, int age){
        super(id,age,strength,initiative,color,world,position,typOrganizmu);
    }
    public Antylopa(Point position, World world){
        super(Organism.AUTOID,0,strength,initiative,color,world,position,typOrganizmu);
    }
    @Override
    public int randMove() {
        Random rand = new Random();
        return rand.nextInt(5)-2;
    }
    @Override
    public void collision(Organism other) {
        Random rand = new Random();
        if(rand.nextBoolean()){
            setxMove(0);
            setyMove(0);
            while(findCollision() != null){
                setPosition(getPreviousPosition());
                action();
            }
        }
        else{
            super.collision(other);
        }
    }
    @Override
    public Organism getChild(Point pos) {
        return new Antylopa(pos,getWorld());
    }
}
