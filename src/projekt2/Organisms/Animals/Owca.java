package projekt2.Organisms.Animals;

import projekt2.Organisms.Organism;
import projekt2.Organisms.Plants.Guarana;
import projekt2.World;

import java.awt.*;

public class Owca extends Animal{
    private static final int strength = 4;
    private static final int initiative = 4;
    private static final TypOrganizmu typOrganizmu = TypOrganizmu.OWCA;
    private static final Color color = new Color(173, 173, 173);

    public Owca(Point position, World world, int id, int age){
        super(id,age,strength,initiative,color,world,position,typOrganizmu);
    }
    public Owca(Point position, World world){
        super(Organism.AUTOID,0,strength,initiative,color,world,position,typOrganizmu);
    }
    @Override
    public Organism getChild(Point pos) {
        return new Owca(pos,getWorld());
    }
}
