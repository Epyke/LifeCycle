package entities;

import world.Coord;
import world.World;

public class Plant extends Entity {
    private PlantType type;
    private int energy;

    public Plant(World w, Coord coords, PlantType type){
        super(w, coords);
        this.type = type;
        energy = type.getMaxEnergy();
    }

    public void die(){
        super.setDied();
        type.setSymb('X');
        System.out.println(type + " died at " + "[" + super.getCoords().getX() + "," + super.getCoords().getY() + "]");
    }

    public void updateAge(){
        if(super.getIsDead()){
            return;
        }
        super.incrementAge();
        if(super.getAge() > type.getMaxAge()){
            die();
        }
    }

    public void updateStats(){
        if(energy < type.getMaxEnergy()){
            if(energy > (type.getMaxEnergy() - type.getEnergyRate())){
                energy += type.getMaxEnergy() - energy;
            } else {
                energy += type.getEnergyRate();
            }
        }
    }

    public boolean reproduction(){
        if(energy >= type.getEnergyReproduction()){
            return true;
        }
        return false;
    }
}
