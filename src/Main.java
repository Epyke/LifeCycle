import obstacles.Rock;
import world.World;

public static void main(){
  World world1 = new World(30);
  world1.worldGen();
  System.out.println(world1.worldView());
}
