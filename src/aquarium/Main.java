package aquarium;

import aquarium.gui.Aquarium;
import aquarium.gui.AquariumWindow;


@SuppressWarnings("unused")
public class Main  {
	static Launcher launcher;
	
	public static void main(String[] args){
		
		/*Aquarium aquarium = new Aquarium(argsParser(args));
		AquariumWindow animation = new AquariumWindow(aquarium);
		animation.displayOnscreen();*/
		
		
		launcher = new Launcher();
	}
	
	private static boolean[] argsParser(String[] args) {
		boolean[] argi = new boolean[2];
		for(String str : args) {
			if(str.equals("-client"))
				argi[0] = true;
			else if(str.equals("-server"))
				argi[1] = true;
		}
		return argi;
	}
}
