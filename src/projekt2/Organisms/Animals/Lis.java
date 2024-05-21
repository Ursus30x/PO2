package projekt2.Organisms.Animals;

import projekt2.Organisms.Organism;
import projekt2.Organisms.Plants.Guarana;
import projekt2.World;

import java.awt.*;

public class Lis extends Animal{
    private static final int strength = 3;
    private static final int initiative = 7;
    private static final TypOrganizmu typOrganizmu = TypOrganizmu.LIS;
    private static final Color color = new Color(255, 104, 37);

    public Lis(Point position, World world, int id, int age){
        super(id,age,strength,initiative,color,world,position,typOrganizmu);
    }
    public Lis(Point position, World world){
        super(Organism.AUTOID,0,strength,initiative,color,world,position,typOrganizmu);
    }
    @Override
    public void action(){
        setxMove(0);
        setyMove(0);
        super.action();

        Organism other = findCollision();
        if(other == null)return;

        while (other.getStrength() > getStrength()){
            setPosition(getPreviousPosition());
            super.action();
            other = findCollision();
            if(other == null)return;
        }

    }
    @Override
    public Organism getChild(Point pos) {
        return new Lis(pos,getWorld());
    }
}
