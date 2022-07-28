package engine;

import org.lwjgl.opengl.Display;

import engine.renderer.Window;

public class Main {
	public static void main(String[] args) {
		Window.create();
		Window.setBackground(1.0f, 0.0f, 0.0f);
		
		while (!Display.isCloseRequested()) {
			Window.update();
		}
		
		Window.destroy();
	}
}
