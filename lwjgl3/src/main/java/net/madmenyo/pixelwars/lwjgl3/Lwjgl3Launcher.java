package net.madmenyo.pixelwars.lwjgl3;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.tools.texturepacker.TexturePacker;
import net.madmenyo.pixelwars.PixelWars;

/** Launches the desktop (LWJGL3) application. */
public class Lwjgl3Launcher {
	public static void main(String[] args) {
		PackSprites();
		PackSkin();
		createApplication();
	}

	private static void PackSkin() {
		TexturePacker.Settings settings = new TexturePacker.Settings();
		settings.fast = true;
		settings.combineSubdirectories = true;
		settings.flattenPaths = true;

		String input = "../raw/skin/images";
		String output = "gui/";
		String filename = "uiskin.atlas";

		TexturePacker.process(settings, input, output, filename);
	}

	private static void PackSprites() {
		TexturePacker.Settings settings = new TexturePacker.Settings();
		settings.fast = true;
		settings.combineSubdirectories = true;
		settings.flattenPaths = true;

		String input = "../raw/sprites";
		String output = "sprites/";
		String filename = "spritesheet.atlas";

		TexturePacker.process(settings, input, output, filename);

	}

	private static Lwjgl3Application createApplication() {
		return new Lwjgl3Application(new PixelWars(), getDefaultConfiguration());
	}

	private static Lwjgl3ApplicationConfiguration getDefaultConfiguration() {
		Lwjgl3ApplicationConfiguration configuration = new Lwjgl3ApplicationConfiguration();
		configuration.setTitle("PixelWars");
		configuration.useVsync(true);
		//// Limits FPS to the refresh rate of the currently active monitor.
		configuration.setForegroundFPS(Lwjgl3ApplicationConfiguration.getDisplayMode().refreshRate);
		//// If you remove the above line and set Vsync to false, you can get unlimited FPS, which can be
		//// useful for testing performance, but can also be very stressful to some hardware.
		//// You may also need to configure GPU drivers to fully disable Vsync; this can cause screen tearing.
		configuration.setWindowedMode(1280, 720);
		configuration.setWindowIcon("libgdx128.png", "libgdx64.png", "libgdx32.png", "libgdx16.png");
		return configuration;
	}
}