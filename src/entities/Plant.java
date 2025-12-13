package entities;

import structures.HabitatType;
import utils.Adjacent;
import utils.CellUtils;
import utils.Rand;
import world.*;
import java.util.ArrayList;
import java.util.Random;

public class Plant extends Entity implements Edible{
    private PlantType type;
    private int energy;
    private int reproductionCooldown;
    private int trueMaxAge;

    public Plant(World w, Coord coords, PlantType type){
        super(w, coords);
        this.type = type;
        energy = type.getMaxEnergy();
        reproductionCooldown = 0;
        trueMaxAge = Rand.getRandomNmb((int)(type.getMaxAge()*0.8), type.getMaxAge());
    }

    public void die(){
        super.setDied();
        System.out.println(type + " died at " + "[" + super.getCoords().getX() + "," + super.getCoords().getY() + "]");
    }

    public void updateAge(){
        if(super.getIsDead()){
            super.incrementDecompose();
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
        if(energy < type.getEnergyReproduction() && super.getAge() < type.getAgeReproduction() && reproductionCooldown > 0){
            reproductionCooldown--;
            return false;
        }

        //Adicionar uma mecánica de sorte na reprodução
        //Para evitar o crescimento exponencial de plantas
        int randomNumber = Rand.getRandomNmb(20);
        if(randomNumber != 1){
            return false;
        }

        Cell currentCell = CellUtils.findCell(super.getWorld(), super.getCoords());
        Cell plantSlot = Adjacent.getFirstAdjacent(super.getWorld(), currentCell, CellType.GRASS, LayerType.NONE, HabitatType.NONE);

        if(plantSlot == null){
            return false;
        }

        if(super.getWorld().bornEntity(plantSlot.getCoord(), this.type)){
            this.energy -= type.getEnergyReproduction();
            System.out.println("New " + this.type + " grew at [" + plantSlot.getCoord().getX() + "," + plantSlot.getCoord().getY() + "]");
            reproductionCooldown = 25;
            return true;
        }
        return false;
    }

    public PlantType getType(){
        return type;
    }

    @Override
    public int getCalories() {
        return type.getCalories();
    }
}
