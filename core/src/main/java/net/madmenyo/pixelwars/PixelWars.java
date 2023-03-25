package net.madmenyo.pixelwars;

import com.badlogic.gdx.Game;
import com.kotcrab.vis.ui.VisUI;
import net.madmenyo.pixelwars.world.WorldScreen;


public class PixelWars extends Game {
	public Assets assets;

	@Override
	public void create() {
		VisUI.load("gui/uiskin.json");

		assets = new Assets();
		assets.load();


		setScreen(new WorldScreen(this));
	}
}