package projekt2.Organisms.Animals;

import projekt2.Logger;
import projekt2.Organisms.Organism;
import projekt2.Organisms.Plants.Guarana;
import projekt2.World;

import javax.swing.text.Position;
import java.awt.*;
import java.awt.event.KeyEvent;

public class Czlowiek extends Animal{
    private static final int strength = 5;
    private static final int initiative = 4;
    private static final TypOrganizmu typOrganizmu = TypOrganizmu.CZLOWIEK;
    private static final Color color = new Color(248, 143, 143);
    private boolean usePower;
    public Czlowiek(Point position, World world, int id, int age){
        super(id,age,strength,initiative,color,world,position,typOrganizmu);
    }

    public Czlowiek(Point position, World world){
        super(Organism.AUTOID,0,strength,initiative,color,world,position,typOrganizmu);
    }
    private void updateMoveSquare(int keyCode){
        if(keyCode == KeyEvent.VK_UP){
            if(getyMove() > -1 && getPosition().y > 0 )setyMove(getyMove()-1);
        }
        else if(keyCode == KeyEvent.VK_DOWN) {
            if (getyMove() < 1 && getPosition().y + 1 < getWorld().getSizeY()) setyMove(getyMove() + 1);
        }
        else if(keyCode == KeyEvent.VK_RIGHT) {
            if (getxMove() < 1 && getPosition().x + 1 < getWorld().getSizeX()) setxMove(getxMove() + 1);
        }
        else if(keyCode == KeyEvent.VK_LEFT) {
            if (getxMove() > -1 && getPosition().x > 0) setxMove(getxMove() - 1);
        }
    }
    private void updateMoveHex(int keyCode){
        setyMove(0);
        setxMove(0);
        if(keyCode == KeyEvent.VK_W){
            if(getPosition().y > 0)setyMove(-1);
        }
        else if(keyCode == KeyEvent.VK_E){
            if(getPosition().y > 0 && getPosition().x + 1 < getWorld().getSizeX()){
                setyMove(-1);
                setxMove(1);
            }
        }
        else if(keyCode == KeyEvent.VK_A){
            if(getPosition().x > 0)setxMove(-1);
        }
        else if(keyCode == KeyEvent.VK_D){
            if(getPosition().x + 1< getWorld().getSizeX())setxMove(1);
        }
        else if(keyCode == KeyEvent.VK_S){
            setyMove(0);
            setxMove(0);
        }
        else if(keyCode == KeyEvent.VK_Z){
            if(getPosition().x > 0 && getPosition().y + 1< getWorld().getSizeY()){
                setxMove(-1);
                setyMove(1);
            }
        }
        else if(keyCode == KeyEvent.VK_X){
            if(getPosition().y + 1 < getWorld().getSizeY() ){
                setyMove(1);
            }
        }
    }
    public void updateMove(int keyCode){
        if(getWorld().getWorldType() == World.WorldType.SQUARE){
            updateMoveSquare(keyCode);
        }
        else if(getWorld().getWorldType() == World.WorldType.HEX){
            updateMoveHex(keyCode);
        }
    }
    private void specialPower(){
        for(int x = -1;x<=1;x++){
            for(int y = -1;y<=1;y++){
                Point pos = new Point(getPosition().x + x, getPosition().y + y);
                Organism other = findCollision(pos);
                if(other != null && !(x == 0 && y == 0)){
                    String otherOrganism = other.getTypOrganizmu().toString() + other.getId();
                    getWorld().killOrganism(other);
                    Logger.addLog(otherOrganism + " zostaje spalony calopaleniem");
                }
            }
        }
        usePower = false;
    }
    @Override
    public void action(){
        Point pos = new Point(getPosition().x+getxMove(),getPosition().y+getyMove());
        setPosition(pos);
        setxMove(0);
        setyMove(0);
        if(usePower)specialPower();
    }

    @Override
    public Organism getChild(Point pos) {
        return new Czlowiek(pos,getWorld());
    }
    public void activatePower(){
        usePower = true;
    }
}
