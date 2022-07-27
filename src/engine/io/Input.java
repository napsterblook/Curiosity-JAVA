package engine.io;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;

public class Input {
	private static boolean[] keys = new boolean[GLFW.GLFW_KEY_LAST];
	private static boolean[] buttons = new boolean[GLFW.GLFW_MOUSE_BUTTON_LAST];
	private static double cursorPosX, cursorPosY;
	
	private GLFWKeyCallback keyboardCallback;
	private GLFWCursorPosCallback cursorPosCallback;
	private GLFWMouseButtonCallback mouseButtonCallback;
	
	public Input () {
		keyboardCallback = new GLFWKeyCallback() {
			@Override
			public void invoke(long window, int key, int scancode, int action, int mods) {
				keys[key] = (action != GLFW.GLFW_RELEASE);
			}
		};
		
		cursorPosCallback = new GLFWCursorPosCallback() {
			@Override
			public void invoke(long window, double xpos, double ypos) {
				cursorPosX = xpos;
				cursorPosY = ypos;
			}
		};
		
		mouseButtonCallback = new GLFWMouseButtonCallback() {
			@Override
			public void invoke(long window, int button, int action, int mods) {
				buttons[button] = (action != GLFW.GLFW_RELEASE);
			}
		};
	}
	
	public static boolean isKeyDown(int key) {
		return keys[key];
	}
	
	public static boolean isButtonDown(int button) {
		return buttons[button];
	}
	
	public void destroy() {
		keyboardCallback.free();
		cursorPosCallback.free();
		mouseButtonCallback.free();
	}

	public double getCursorPosX() {
		return cursorPosX;
	}

	public double getCursorPosY() {
		return cursorPosY;
	}

	public GLFWKeyCallback getKeyboardCallback() {
		return keyboardCallback;
	}

	public GLFWCursorPosCallback getCursorPosCallback() {
		return cursorPosCallback;
	}

	public GLFWMouseButtonCallback getMouseButtonCallback() {
		return mouseButtonCallback;
	}
}
