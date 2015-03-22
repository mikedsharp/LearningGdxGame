package com.programmerinprogress.learninggame.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.programmerinprogress.learninggame.LearningGdxGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.title = "Drop";
        config.width = 640;
        config.height = 480;
		new LwjglApplication(new LearningGdxGame(), config);
	}
}
