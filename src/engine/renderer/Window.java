package engine.renderer;

import org.lwjgl.glfw.GLFW;

public class Window {
	
	private long window;
	private int width, height;
	private String title;

	public Window(int width, int height, String title) {
		this.width = width;
		this.height = height;
		this.title = title;
	}

	public void create() {
		GLFW.glfwDefaultWindowHints();
		GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, 4);
		GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, 6);
		GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_PROFILE, GLFW.GLFW_OPENGL_CORE_PROFILE);
		
		window = GLFW.glfwCreateWindow(width, height, title, 0, 0);
		if (window == 0) {
			System.err.println("[ERROR]: Could not create window");
			System.exit(-1);
		}
		
		GLFW.glfwMakeContextCurrent(window);
		GLFW.glfwShowWindow(window);
		GLFW.glfwSwapInterval(1);
	}
	
	public void update() {
		GLFW.glfwSwapBuffers(window); 
	}
	
	public boolean shouldClose() {
		return GLFW.glfwWindowShouldClose(window);
	}
}
