package net.elzorro99.totemfactions.managers;

import net.elzorro99.totemfactions.Main;
import net.elzorro99.totemfactions.utils.scoreboards.v1_10_R1;
import net.elzorro99.totemfactions.utils.scoreboards.v1_11_R1;
import net.elzorro99.totemfactions.utils.scoreboards.v1_12_R1;
import net.elzorro99.totemfactions.utils.scoreboards.v1_13_R2;
import net.elzorro99.totemfactions.utils.scoreboards.v1_14_R1;
import net.elzorro99.totemfactions.utils.scoreboards.v1_15_R1;
import net.elzorro99.totemfactions.utils.scoreboards.v1_8_R3;
import net.elzorro99.totemfactions.utils.scoreboards.v1_9_R2;

public class MScoreboards {

	private Main main = Main.getInstance();
	
	public void initScoreboards() {
		if(main.getVersionServer().equals("v1_8_R3")) {
			main.scoreboardManager =  new v1_8_R3();
		} else if(main.getVersionServer().equals("v1_9_R2")) {
			main.scoreboardManager =  new v1_9_R2();
		} else if(main.getVersionServer().equals("v1_10_R1")) {
			main.scoreboardManager =  new v1_10_R1();
		} else if(main.getVersionServer().equals("v1_11_R1")) {
			main.scoreboardManager =  new v1_11_R1();
		} else if(main.getVersionServer().equals("v1_12_R1")) {
			main.scoreboardManager =  new v1_12_R1();
		} else if(main.getVersionServer().equals("v1_13_R2")) {
			main.scoreboardManager =  new v1_13_R2();
		} else if(main.getVersionServer().equals("v1_14_R1")) {
			main.scoreboardManager =  new v1_14_R1();
		} else if(main.getVersionServer().equals("v1_15_R1")) {
			main.scoreboardManager =  new v1_15_R1();
		}
	}
	
}
