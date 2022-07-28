package engine.renderer;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.PixelFormat;

import engine.utils.FileLoader;

public class Window {
	
	private static final int WIDTH = 1280, HEIGHT = 720;
	private static final int FPS_CAP = 120;
	private static float r = 0.0f, g = 0.0f, b = 0.0f;

	public static void create() {
		
		ContextAttribs attribs = new ContextAttribs(3,2)
			.withForwardCompatible(true)
			.withProfileCore(true);
		
		try {
			Display.setDisplayMode(new DisplayMode(WIDTH, HEIGHT));
			Display.create(new PixelFormat(), attribs);
			Display.setTitle(FileLoader.getTitle());
		} catch (LWJGLException e) {
			System.err.println(String.format("[ERROR]: Failed to create window: %s", e));
		}
		
		GL11.glViewport(0, 0, WIDTH, HEIGHT);
	}
	
	public static void setBackground(float r, float g, float b) {
		Window.r = r;
		Window.g = g;
		Window.b = b;
	}
	
	public static void update() {
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
		GL11.glClearColor(r, g, b, 1.0f);
		
		Display.sync(FPS_CAP);
		Display.update();
	}
	
	public static void destroy() {
		Display.destroy();
	}
} 
