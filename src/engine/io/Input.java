package engine.io;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import org.lwjgl.glfw.GLFWScrollCallback;

public class Input {
	private static boolean[] keys = new boolean[GLFW.GLFW_KEY_LAST],
			keysReleased = new boolean[GLFW.GLFW_KEY_LAST];
	
	private static boolean[] buttons = new boolean[GLFW.GLFW_MOUSE_BUTTON_LAST],
			buttonsReleased = new boolean[GLFW.GLFW_MOUSE_BUTTON_LAST];
	
	private static double mouseX, mouseY;
	private static double scrollX, scrollY;
	
	private GLFWKeyCallback keyboard, keyboardRelease;
	private GLFWCursorPosCallback mouseMove;
	private GLFWMouseButtonCallback mouseButtons, mouseButtonsRelease;
	private GLFWScrollCallback mouseScroll;
	
	public Input() {
		keyboard = new GLFWKeyCallback() {
			public void invoke(long window, int key, int scancode, int action, int mods) {
				keys[key] = (action != GLFW.GLFW_RELEASE);
			}
		};
		
		keyboardRelease = new GLFWKeyCallback() {
			public void invoke(long window, int key, int scancode, int action, int mods) {
				keysReleased[key] = (action == GLFW.GLFW_RELEASE);
			}
		};
		
		mouseMove = new GLFWCursorPosCallback() {
			public void invoke(long window, double xpos, double ypos) {
				mouseX = xpos;
				mouseY = ypos;
			}
		};
		
		mouseButtons = new GLFWMouseButtonCallback() {
			public void invoke(long window, int button, int action, int mods) {
				buttons[button] = (action != GLFW.GLFW_RELEASE);
			}
		};
		
		mouseButtonsRelease = new GLFWMouseButtonCallback() {
			public void invoke(long window, int button, int action, int mods) {
				buttonsReleased[button] = (action != GLFW.GLFW_RELEASE);
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
	
	public static boolean isKeyReleased(int key) {
		new Input();
		return !Input.isKeyDown(key);
	}
	
	public static boolean isButtonDown(int button) {
		return buttons[button];
	}
	
	public static boolean isButtonReleased(int button) {
		new Input();
		return !Input.isButtonDown(button);
	}
	
	public void destroy() {
		keyboard.free();
		keyboardRelease.free();
		
		mouseMove.free();
		mouseButtons.free();
		mouseButtonsRelease.free();
		
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
	
	public GLFWKeyCallback getKeyboardReleaseCallback() {
		return keyboardRelease;
	}

	public GLFWCursorPosCallback getMouseMoveCallback() {
		return mouseMove;
	}

	public GLFWMouseButtonCallback getMouseButtonsCallback() {
		return mouseButtons;
	}
	
	public GLFWMouseButtonCallback getMouseButtonsReleaseCallback() {
		return mouseButtonsRelease;
	}
	
	public GLFWScrollCallback getMouseScrollCallback() {
		return mouseScroll;
	}
}