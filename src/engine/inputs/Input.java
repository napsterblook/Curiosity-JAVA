package engine.inputs;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import org.lwjgl.glfw.GLFWScrollCallback;

public class Input {
	
	private static boolean[] keys = new boolean[GLFW.GLFW_KEY_LAST];
	private static boolean[] buttons = new boolean[GLFW.GLFW_MOUSE_BUTTON_LAST];
	
	private static double mouseX, mouseY;
	private static double scrollX, scrollY;
	
	private GLFWKeyCallback keyboard;
	private GLFWMouseButtonCallback mouseButtons;
	
	private GLFWCursorPosCallback mouseMove;
	private GLFWScrollCallback mouseScroll;
	
	public Input() {
		
		keyboard = new GLFWKeyCallback() {
			public void invoke(long window, int key, int scancode, int action, int mods) {
				keys[key] = (action != GLFW.GLFW_RELEASE);
			}
		};
		
		mouseButtons = new GLFWMouseButtonCallback() {
			public void invoke(long window, int button, int action, int mods) {
				buttons[button] = (action != GLFW.GLFW_RELEASE);
			}
		};
		
		mouseMove = new GLFWCursorPosCallback() {
			public void invoke(long window, double xpos, double ypos) {
				mouseX = xpos;
				mouseY = ypos;
			}
		};
		
		mouseScroll = new GLFWScrollCallback() {
			public void invoke(long window, double offsetx, double offsety) {
				scrollX += offsetx;
				scrollY += offsety;
			}
		};
	}
	
	public static boolean isKeyDown(int key) {
		return keys[key];
	}
	
	public static boolean isButtonDown(int button) {
		return buttons[button];
	}
	
	public static boolean isKeyUp(int key) {
		return !Input.isKeyDown(key);
	}
	
	public static boolean isButtonUp(int button) {
		return !Input.isButtonDown(button);
	}
	
	public void destroy() {
		keyboard.free();
		mouseButtons.free();
		
		mouseMove.free();
		mouseScroll.free();
	}

	public static double getMouseX() {
		return mouseX;
	}

	public static double getMouseY() {
		return mouseY;
	}
	
	public static double getScrollX() {
		return scrollX;
	}

	public static double getScrollY() {
		return scrollY;
	}
	
	public GLFWKeyCallback getKeyboardCallback() {
		return keyboard;
	}

	public GLFWMouseButtonCallback getMouseButtonsCallback() {
		return mouseButtons;
	}

	public GLFWCursorPosCallback getMouseMoveCallback() {
		return mouseMove;
	}
	
	public GLFWScrollCallback getMouseScrollCallback() {
		return mouseScroll;
	}
}
