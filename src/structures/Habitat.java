package structures;

import entities.Animal;
import world.Coord;

import java.util.ArrayList;

public class Habitat {
    private HabitatType type;
    private Coord position;

    /**
     * ArrayList de quem mora no habitat
     */
    private ArrayList<Animal> members;

    public Habitat(HabitatType type, Coord position, Animal owner) {
        this.type = type;
        this.position = position;
        this.members = new ArrayList<>();
    }

    public HabitatType getType() {
        return type;
    }

    public Coord getPosition() {
        return position;
    }

    public ArrayList<Animal> getMembers() {
        return members;
    }

    public boolean addMember(Animal animal) {
        if(members.contains(animal)) {
            return false;
        }
        members.add(animal);
        return true;
    }

    public boolean removeMember(Animal a) {
        if(members.contains(a)) {
            members.remove(a);
            return true;
        } else
            return false;
    }

    public void setType(HabitatType type) {
        this.type = type;
    }
}
