package projekt2.Organisms.Plants;

import projekt2.Organisms.Organism;
import projekt2.World;

import java.awt.*;

public class Trawa extends Plant{
    private static final int strength = 0;
    private static final TypOrganizmu typOrganizmu = TypOrganizmu.TRAWA;
    private static final Color color = new Color(158, 255, 127);

    public Trawa(Point position, World world, int id, int age){
        super(id,age,strength,color,world,position,typOrganizmu);
    }
    public Trawa(Point position, World world){
        super(Organism.AUTOID,0,strength,color,world,position,typOrganizmu);
    }

    @Override
    public Organism getChild(Point pos) {
        return new Trawa(pos,getWorld());
    }
}
