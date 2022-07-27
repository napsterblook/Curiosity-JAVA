package engine.renderer;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;

import engine.io.Input;

public class Window {
	
	private long window;
	private int width, height;
	private String title;
	private float r, g, b;
	private Input input;

	public Window(int width, int height, String title) {
		this.width = width;
		this.height = height;
		this.title = title;
	}

	public void create() {
		windowHints();
		
		if (!GLFW.glfwInit()) {
			System.err.println("[ERROR]: Could not initialize GLFW");
			System.exit(-1);
		}
		
		input = new Input();
		window = GLFW.glfwCreateWindow(width, height, title, 0, 0);
		if (window == 0) {
			System.err.println("[ERROR]: Could not create window");
			System.exit(-1);
		}
		
		GLFWVidMode videoMode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());
		GLFW.glfwSetWindowPos(window, (videoMode.width() - width) / 2, (videoMode.height() - height) / 2);
		
		GLFW.glfwMakeContextCurrent(window);
		
		createCallbacks();
		
		GLFW.glfwShowWindow(window);
		GLFW.glfwSwapInterval(1);
		
		GL.createCapabilities();
	}
	
	private void createCallbacks() {
		GLFW.glfwSetKeyCallback(window, input.getKeyboardCallback());
		GLFW.glfwSetCursorPosCallback(window, input.getCursorPosCallback());
		GLFW.glfwSetMouseButtonCallback(window, input.getMouseButtonCallback());
	}
	
	private void windowHints() {
		GLFW.glfwDefaultWindowHints();
		GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, 4);
		GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, 6);
		GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_PROFILE, GLFW.GLFW_OPENGL_CORE_PROFILE);
	}
	
	public void setBackground(float r, float g, float b) {
		this.r = r;
		this.g = g;
		this.b = b;
	}
	
	public void update() {
		GL11.glViewport(0, 0, width, height);
		GL11.glClearColor(r, g, b, 1.0f);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		
		GLFW.glfwPollEvents();
		GLFW.glfwSwapBuffers(window); 
	}
	
	public void destroy() {
		input.destroy();
		GLFW.glfwTerminate();
		GLFW.glfwDestroyWindow(window);
	}
	
	public boolean shouldClose() {
		return GLFW.glfwWindowShouldClose(window);
	}
}
