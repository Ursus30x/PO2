package projekt2.Organisms.Plants;

import projekt2.Logger;
import projekt2.Organisms.Organism;
import projekt2.World;

import java.awt.*;

public class WilczeJagody extends Plant{
    private static final int strength = 99;
    private static final TypOrganizmu typOrganizmu = TypOrganizmu.WILCZEJAGODY;
    private static final Color color = new Color(19, 52, 246, 255);

    public WilczeJagody(Point position, World world, int id, int age){
        super(id,age,strength,color,world,position,typOrganizmu);
    }
    public WilczeJagody(Point position, World world){
        super(Organism.AUTOID,0,strength,color,world,position,typOrganizmu);
    }
    @Override
    public void collision(Organism other) {
        String thisOrganizm = this.getTypOrganizmu().toString() + this.getId();
        String otherOrganizm = other.getTypOrganizmu().toString() + other.getId();

        Logger.addLog(thisOrganizm + " zatrul " + otherOrganizm);
        getWorld().killOrganism(this);
        getWorld().killOrganism(other);
    }
    @Override
    public Organism getChild(Point pos) {
        return new WilczeJagody(pos,getWorld());
    }
}
