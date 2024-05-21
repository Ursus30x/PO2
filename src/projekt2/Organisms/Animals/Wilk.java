package projekt2.Organisms.Animals;

import projekt2.Organisms.Organism;
import projekt2.Organisms.Plants.Guarana;
import projekt2.World;

import java.awt.*;

public class Wilk extends Animal{
    private static final int strength = 9;
    private static final int initiative = 5;
    private static final TypOrganizmu typOrganizmu = TypOrganizmu.WILK;

    private static final Color color = new Color(72, 72, 72);

    public Wilk(Point position, World world, int id, int age){
        super(id,age,strength,initiative,color,world,position,typOrganizmu);
    }
    public Wilk(Point position, World world){
        super(Organism.AUTOID,0,strength,initiative,color,world,position,typOrganizmu);
    }
    @Override
    public Organism getChild(Point pos) {
        return new Wilk(pos,getWorld());
    }
}
