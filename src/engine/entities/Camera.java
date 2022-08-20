package engine.entities;

import org.lwjgl.glfw.GLFW;

import engine.Main;
import engine.io.Input;
import engine.maths.Vector3f;

public class Camera {
	private Vector3f position = new Vector3f(0.0f, 0.0f, 0.0f);
	private float pitch, yaw, roll, moveSpeed, sprintSpeed, crouchSpeed;
	
	public Camera(float moveSpeed, float sprintSpeed, float crouchSpeed) {
		this.moveSpeed = moveSpeed;
		this.sprintSpeed = sprintSpeed;
		this.crouchSpeed = crouchSpeed;
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
		
		if (Input.isKeyDown(GLFW.GLFW_KEY_LEFT_CONTROL))
			position.y -= moveSpeed;
		
		if (Input.isKeyDown(GLFW.GLFW_KEY_LEFT_SHIFT))
			moveSpeed = sprintSpeed;
			
		if (Input.isKeyReleased(GLFW.GLFW_KEY_LEFT_SHIFT) && Input.isKeyReleased(GLFW.GLFW_KEY_V))
			moveSpeed = Main.MOVE_SPEED;
		
		if (Input.isKeyDown(GLFW.GLFW_KEY_V))
			moveSpeed = crouchSpeed;
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
