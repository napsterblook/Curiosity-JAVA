package engine.entities;

import org.lwjgl.glfw.GLFW;

import engine.Main;
import engine.inputs.Input;
import engine.maths.vector.Vector3f;

public class Camera {
	private Vector3f position, rotation;
	private float moveSpeed, sprintSpeed, crouchSpeed, sensitivity;
	private float oldMouseX = 0f, oldMouseY = 0f, newMouseX, newMouseY;
	
	public Camera(Vector3f position, float moveSpeed, float sprintSpeed, float crouchSpeed, float sensitivity) {
		this.moveSpeed = moveSpeed;
		this.sprintSpeed = sprintSpeed;
		this.crouchSpeed = crouchSpeed;
		this.position = position;
		this.sensitivity = sensitivity;
		rotation = new Vector3f();
	}
	
	public Camera() {};
	
	public void move() {
		
		newMouseX = (float) Input.getMouseX();
		newMouseY = (float) Input.getMouseY();
		
		float x = (float) Math.sin(Math.toRadians(-rotation.y)) * moveSpeed;
		float z = (float) Math.cos(Math.toRadians(-rotation.y)) * moveSpeed;
		
		if (Input.isKeyDown(GLFW.GLFW_KEY_A)) position = Vector3f.add(position, new Vector3f(-z, 0f, x));
		if (Input.isKeyDown(GLFW.GLFW_KEY_D)) position = Vector3f.add(position, new Vector3f(z, 0f, -x));
		if (Input.isKeyDown(GLFW.GLFW_KEY_W)) position = Vector3f.add(position, new Vector3f(-x, 0f, -z));
		if (Input.isKeyDown(GLFW.GLFW_KEY_S)) position = Vector3f.add(position, new Vector3f(x, 0f, z));
		if (Input.isKeyDown(GLFW.GLFW_KEY_SPACE)) position.y += moveSpeed;
		if (Input.isKeyDown(GLFW.GLFW_KEY_LEFT_CONTROL)) position.y -= moveSpeed;
		if (Input.isKeyDown(GLFW.GLFW_KEY_LEFT_SHIFT)) moveSpeed = sprintSpeed;
		if (Input.isKeyUp(GLFW.GLFW_KEY_LEFT_SHIFT) && Input.isKeyUp(GLFW.GLFW_KEY_V))
			moveSpeed = Main.MOVE_SPEED;
		if (Input.isKeyDown(GLFW.GLFW_KEY_V)) moveSpeed = crouchSpeed;
		
		float dx = (float) (newMouseX - oldMouseX);
		float dy = (float) (newMouseY - oldMouseY);
		
		rotation = Vector3f.add(rotation, new Vector3f(dy * sensitivity, dx * sensitivity, 0f));
		
		oldMouseX = newMouseX;
		oldMouseY = newMouseY;
	}

	public Vector3f getPosition() {
		return position;
	}
	
	public Vector3f getRotation() {
		return rotation;
	}
}
