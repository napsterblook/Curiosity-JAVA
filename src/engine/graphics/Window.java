package engine.graphics;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWImage;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.glfw.GLFWWindowSizeCallback;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;

import engine.inputs.Input;
import engine.loaders.TextureLoader;

public class Window {
	private static int width, height, frames;
	private String title;
	private long window;
	private static long time;
	private Input input;
	private Colour background;
	private GLFWWindowSizeCallback sizeCallback;
	private boolean isResized, isFullscreen;
	private int[] windowPosX = new int[1], windowPosY = new int[1];
	
	public Window(Resolution res, Colour background, String title) {
		width = (int) res.getW();
		height = (int) res.getH();
		
		this.title = title;
		this.background = background;
	}
	
	//extra constructor just so that Renderer can access width and height methods
	public Window() { }
	
	public void create() {
		if (!GLFW.glfwInit()) {
			System.err.println("[ERROR]: GLFW could not be initialized, exiting...");
			System.exit(-1);
		}
		
		//this would be version 4.6 but latest version is not supported by most unix os
		GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, 4);
		GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, 5); 
		GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_PROFILE, GLFW.GLFW_OPENGL_CORE_PROFILE);
		GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_FORWARD_COMPAT, GL11.GL_TRUE);
		
		input = new Input();
		window = GLFW.glfwCreateWindow(width, height, title, isFullscreen ? GLFW.glfwGetPrimaryMonitor() : 0, 0);
		
		if (window == 0) {
			System.err.println("[ERROR]: GLFW could not create window, exiting...");
			System.exit(-1);
		}
		
		//centre window on screen
		GLFWVidMode videoMode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());
		windowPosX[0] = (videoMode.width() - width) / 2;
		windowPosY[0] = (videoMode.height() - height) / 2;
		GLFW.glfwSetWindowPos(window, windowPosX[0], windowPosY[0]);
		
		GLFW.glfwMakeContextCurrent(window);
		GL.createCapabilities();
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		
		createCallbacks();
		
		GLFW.glfwShowWindow(window);
		GLFW.glfwSwapInterval(1);
		time = System.currentTimeMillis();
	}
	
	private void createCallbacks() {
		sizeCallback = new GLFWWindowSizeCallback() {
			public void invoke(long window, int w, int h) {
				width = w;
				height = h;
				isResized = true;
			}
		};
		
		GLFW.glfwSetKeyCallback(window, input.getKeyboardCallback());
		GLFW.glfwSetCursorPosCallback(window, input.getMouseMoveCallback());
		GLFW.glfwSetMouseButtonCallback(window, input.getMouseButtonsCallback());
		GLFW.glfwSetScrollCallback(window, input.getMouseScrollCallback());
		GLFW.glfwSetWindowSizeCallback(window, sizeCallback);
	}
	
	public void update() {
		if (isResized) {
			GL11.glViewport(0, 0, width, height);
			isResized = false;
		}
		
		GL11.glClearColor(background.getR(), background.getG(), background.getB(), background.getA());
		//depth buffer bit is necessary to enable depth test so that vertices can render in front of each other
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		GLFW.glfwPollEvents();
		frames++;
		
		if (System.currentTimeMillis() > time + 1000) {
			GLFW.glfwSetWindowTitle(window, String.format("%1$s | FPS: %2$s", title, frames));
			time = System.currentTimeMillis();
			frames = 0;
		}
	}
	
	public void swapBuffers() {
		GLFW.glfwSwapBuffers(window);
	}
	
	public boolean shouldClose() {
		return GLFW.glfwWindowShouldClose(window);
	}
	
	public void destroy() {
		input.destroy();
		sizeCallback.free();
		GLFW.glfwWindowShouldClose(window);
		GLFW.glfwDestroyWindow(window);
		GLFW.glfwTerminate();
	}

	public boolean isFullscreen() { return isFullscreen; }

	public void setFullscreen(boolean isFullscreen) {
		this.isFullscreen = isFullscreen;
		isResized = true;
		
		if (isFullscreen) {
			GLFW.glfwGetWindowPos(window, windowPosX, windowPosY);
			GLFW.glfwSetWindowMonitor(window, GLFW.glfwGetPrimaryMonitor(), 0, 0, width, height, 0);
		} else
			GLFW.glfwSetWindowMonitor(window, 0, windowPosX[0], windowPosY[0], width, height, 0);
	}
	
	public void setLocked(boolean locked) {
		GLFW.glfwSetInputMode(window, GLFW.GLFW_CURSOR, locked? GLFW.GLFW_CURSOR_DISABLED: GLFW.GLFW_CURSOR_NORMAL);
	}
	
	public void setIcon(TextureLoader texture) {
		GLFWImage image = GLFWImage.malloc(); GLFWImage.Buffer buffer = GLFWImage.malloc(1);
		image.set(texture.getWidth(), texture.getHeight(), texture.getImage());
		buffer.put(0, image);
		GLFW.glfwSetWindowIcon(window, buffer);
	}
	
	public int getWidth() { return width; }

	public int getHeight() { return height; }

	public String getTitle() { return title; }

	public long getWindow() { return window; }
}