package projekt2.Organisms.Animals;

import projekt2.Logger;
import projekt2.Organisms.Organism;
import projekt2.World;

import javax.swing.text.Position;
import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public abstract class Animal extends Organism {
    private int xMove;
    private int yMove;
    protected Animal(int id, int age, int strength, int initiative, Color color, World world, Point position,TypOrganizmu typOrganizmu){
        super(id,age,strength,initiative,color,world,position,typOrganizmu);
    }

    private void actionSquare(){
        xMove = randMove();
        yMove = randMove();

        while(getPosition().x + xMove < 0
                || getPosition().x + xMove >= getWorld().getSizeX()){
            xMove = randMove();
        }

        while(getPosition().y + yMove < 0
                || getPosition().y + yMove >= getWorld().getSizeY()){
            yMove = randMove();
        }

        setPreviousPosition(getPosition());
        Point newPoint = new Point(getPosition().x + xMove,getPosition().y + yMove);

        setPosition(newPoint);
    }

    private void actionHex(){
        xMove = randMove();
        yMove = randMove();

        while(getPosition().x + xMove < 0
                || getPosition().x + xMove >= getWorld().getSizeX()){
            xMove = randMove();
        }

        while((getPosition().y + yMove < 0
                || getPosition().y + yMove >= getWorld().getSizeY())
                && (yMove == xMove || (xMove != 0 && yMove != 0))){
            yMove = randMove();
        }

        setPreviousPosition(getPosition());
        Point newPoint = new Point(getPosition().x + xMove,getPosition().y + yMove);

        setPosition(newPoint);
    }
    @Override
    public void action(){
        if(getWorld().getWorldType().equals(World.WorldType.SQUARE)){
            actionSquare();
        }
        else if(getWorld().getWorldType().equals(World.WorldType.HEX)){
            actionHex();
        }
    }

    public void collision(Organism other){
        String thisOrganizm = this.getTypOrganizmu().toString() + this.getId();
        String otherOrganizm = other.getTypOrganizmu().toString() + other.getId();
        if(this.getTypOrganizmu() == other.getTypOrganizmu()){
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
        else if(this.getStrength() >= other.getStrength()){
            Logger.addLog(thisOrganizm + " zabil " + otherOrganizm);
            getWorld().killOrganism(other);
        }
        else{
            Logger.addLog(otherOrganizm + " zabil " + this.getTypOrganizmu().toString());
            getWorld().killOrganism(this);
        }
    }

    protected int getxMove(){
        return xMove;
    }
    protected void setxMove(int move){
        xMove = move;
    }
    protected int getyMove(){
        return yMove;
    }
    protected void setyMove(int move){
        yMove = move;
    }
}
