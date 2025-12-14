import entities.AnimalType;
import obstacles.Rock;
import world.Time;
import world.World;

public static void main(){
  World world1 = new World(30);
  world1.worldGen();
  Time worldTime = new Time(world1);
  worldTime.specieExinction(AnimalType.SHEEP);
}
