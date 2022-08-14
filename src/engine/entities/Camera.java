package engine.entities;

import org.lwjgl.glfw.GLFW;

import engine.io.Input;
import engine.maths.Vector3f;

public class Camera {
	private Vector3f position = new Vector3f(0.0f, 0.0f, 0.0f);
	private float pitch, yaw, roll, moveSpeed;
	
	public Camera(float moveSpeed) {
		this.moveSpeed = moveSpeed;
	}
	
	public void move() {
		if (Input.isKeyDown(GLFW.GLFW_KEY_W))
			position.z -= moveSpeed;
		
		if (Input.isKeyDown(GLFW.GLFW_KEY_A))
			position.x -= moveSpeed;
		
		if (Input.isKeyDown(GLFW.GLFW_KEY_S))
			position.z += moveSpeed;
		
		if (Input.isKeyDown(GLFW.GLFW_KEY_D))
			position.x += moveSpeed;
		
		if (Input.isKeyDown(GLFW.GLFW_KEY_SPACE))
			position.y += moveSpeed;
		
		if (Input.isKeyDown(GLFW.GLFW_KEY_LEFT_SHIFT))
			position.y -= moveSpeed;
	}

	public Vector3f getPosition() {
		return position;
	}

	public float getPitch() {
		return pitch;
	}

	public float getYaw() {
		return yaw;
	}

	public float getRoll() {
		return roll;
	}
}
