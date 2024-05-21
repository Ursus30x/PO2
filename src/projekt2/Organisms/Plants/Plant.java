package projekt2.Organisms.Plants;

import projekt2.Logger;
import projekt2.Organisms.Organism;
import projekt2.World;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public abstract class Plant extends Organism {
    private static final int spreadingChance = 30;
    private static final int spreadingAge = 2;
    static private final int initiative = 0;
    protected Plant(int id, int age, int strength, Color color, World world, Point position, TypOrganizmu typOrganizmu){
        super(id,age,strength,initiative,color,world,position,typOrganizmu);
    }
    @Override
    public void action(){
        Random rand = new Random();
        String thisOrganizm = this.getTypOrganizmu().toString() + this.getId();

        if(getAge() < spreadingAge || rand.nextInt(spreadingChance) != 0)return;
        ArrayList<Point> freeFields = getFreeFields();

        if(freeFields.size() == 0){
            Logger.addLog(thisOrganizm + " nie mogl zasiac pola przez brak miejsca");
        }
        else{
            int randField = rand.nextInt(freeFields.size());
            Logger.addLog(thisOrganizm + " zasial pole obok");
            createChild(new Point(freeFields.get(randField).x,freeFields.get(randField).y));
        }
    }

    @Override
    public void collision(Organism other){
        String thisOrganizm = this.getTypOrganizmu().toString() + this.getId();
        String otherOrganizm = other.getTypOrganizmu().toString() + other.getId();
        if(getStrength() >= other.getStrength()){
            Logger.addLog(thisOrganizm + " zatrul " + otherOrganizm);
            getWorld().killOrganism(this);
            getWorld().killOrganism(other);
        }
        else{
            Logger.addLog( otherOrganizm + " zjadl " + thisOrganizm);
            getWorld().killOrganism(this);
        }
    }
}
