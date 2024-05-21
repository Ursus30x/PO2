package projekt2.Organisms.Plants;

import projekt2.Organisms.Organism;
import projekt2.World;

import java.awt.*;

public class Mlecz extends Plant{
    private static final int strength = 0;
    private static final TypOrganizmu typOrganizmu = TypOrganizmu.MLECZ;
    private static final Color color = new Color(205, 253, 84);

    public Mlecz(Point position, World world, int id, int age){
        super(id,age,strength,color,world,position,typOrganizmu);
    }
    public Mlecz(Point position, World world){
        super(Organism.AUTOID,0,strength,color,world,position,typOrganizmu);
    }

    @Override
    public void action() {
        for(int i = 0;i<3;i++){
            super.action();
        }
    }


    public Organism getChild(Point pos) {
        return new Mlecz(pos,getWorld());
    }
}
