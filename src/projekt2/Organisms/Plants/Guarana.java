package projekt2.Organisms.Plants;

import projekt2.Logger;
import projekt2.Organisms.Organism;
import projekt2.World;

import java.awt.*;

public class Guarana extends Plant{
    private static final int strength = 0;
    private static final TypOrganizmu typOrganizmu = TypOrganizmu.GUARANA;
    private static final Color color = new Color(213, 0, 0);

    public Guarana(Point position, World world, int id, int age){
        super(id,age,strength,color,world,position,typOrganizmu);
    }
    public Guarana(Point position, World world){
        super(Organism.AUTOID,0,strength,color,world,position,typOrganizmu);
    }



    @Override
    public void collision(Organism other) {
        String thisOrganizm = this.getTypOrganizmu().toString() + this.getId();
        String otherOrganizm = other.getTypOrganizmu().toString() + other.getId();
        if(getStrength() >= other.getStrength()){
            Logger.addLog(thisOrganizm + " zatrul " + otherOrganizm);
            getWorld().killOrganism(this);
            getWorld().killOrganism(other);
        }
        else{
            other.setStrength(other.getStrength()+3);
            Logger.addLog( otherOrganizm + " zjadl " + thisOrganizm);
            getWorld().killOrganism(this);
        }
    }

    @Override
    public Organism getChild(Point pos) {
        return new Guarana(pos,getWorld());
    }
}
