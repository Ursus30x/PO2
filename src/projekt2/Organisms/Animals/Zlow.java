package projekt2.Organisms.Animals;

import projekt2.Logger;
import projekt2.Organisms.Organism;
import projekt2.Organisms.Plants.Guarana;
import projekt2.World;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class Zlow extends Animal{
    private static final int strength = 2;
    private static final int initiative = 1;
    private static final TypOrganizmu typOrganizmu = TypOrganizmu.ZOLW;
    private static final Color color = new Color(48, 103, 44);

    public Zlow(Point position, World world, int id, int age){
        super(id,age,strength,initiative,color,world,position,typOrganizmu);
    }
    public Zlow(Point position, World world){
        super(Organism.AUTOID,0,strength,initiative,color,world,position,typOrganizmu);
    }
    @Override
    public void action(){
        Random rand = new Random();
        if(rand.nextInt(4) == 0){
            super.action();
        }
    }
    @Override
    public void collision(Organism other){
        String thisOrganizm = this.getTypOrganizmu().toString() + this.getId();
        String otherOrganizm = other.getTypOrganizmu().toString() + other.getId();

        if(getTypOrganizmu() == other.getTypOrganizmu()){
            ArrayList<Point> freeFields = other.getFreeFields();
            if(this.getAge() < Organism.MATURE_AGE && other.getAge() < Organism.MATURE_AGE){
                Logger.addLog( thisOrganizm+ " i " + otherOrganizm + " sa zbyt mlode zeby sie rozmnozyc");
            }
            else if(freeFields.isEmpty()){
                Logger.addLog(thisOrganizm + " i " + otherOrganizm + " nie maja miejsca zeby sie rozmnozyc");
            }
            else{
                Random randIndex = new Random();
                int randField = randIndex.nextInt(freeFields.size());
                Logger.addLog(thisOrganizm + " rozmnozyl sie z " + otherOrganizm );
                createChild(freeFields.get(randField));
            }

            other.setPosition(other.getPreviousPosition());
        }
        else if(getStrength() > 5){
            Logger.addLog(otherOrganizm + " zabil " + thisOrganizm);
            getWorld().killOrganism(this);
        }
        else{
            Logger.addLog(otherOrganizm + " uciekl od " + this.getTypOrganizmu().toString());
            other.setPosition(getPreviousPosition());
        }
    }
    @Override
    public Organism getChild(Point pos) {
        return new Zlow(pos,getWorld());
    }
}
