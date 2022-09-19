package engine.maths;

import engine.maths.vector.Matrix4f;
import engine.maths.vector.Vector3f;

import org.joml.Vector4f;

import engine.entities.Camera;

public class Maths {
	public static Matrix4f createTransformationMatrix(Vector3f translation, float rx, float ry,
			float rz, float scale) {
		Matrix4f matrix = new Matrix4f();
		matrix.setIdentity();
		Matrix4f.translate(translation, matrix, matrix);
		Matrix4f.rotate((float) Math.toRadians(rx), new Vector3f(1.0f, 0.0f, 0.0f), matrix, matrix);
		Matrix4f.rotate((float) Math.toRadians(ry), new Vector3f(0.0f, 1.0f, 0.0f), matrix, matrix);
		Matrix4f.rotate((float) Math.toRadians(rz), new Vector3f(0.0f, 0.0f, 1.0f), matrix, matrix);
		Matrix4f.scale(new Vector3f(scale, scale, scale), matrix, matrix);
		
		return matrix;
	}
	
	public static Matrix4f createViewMatrix(Camera camera) {
		Matrix4f viewMatrix = new Matrix4f();
		viewMatrix.setIdentity();
		
		Matrix4f.rotate((float) Math.toRadians(camera.getRotation().x), new Vector3f(1.0f, 0.0f, 0.0f),
				viewMatrix, viewMatrix);
		
		Matrix4f.rotate((float) Math.toRadians(camera.getRotation().y), new Vector3f(0.0f, 1.0f, 0.0f),
				viewMatrix, viewMatrix);
		
		Matrix4f.rotate((float) Math.toRadians(camera.getRotation().z), new Vector3f(0.0f, 0.0f, 1.0f),
				viewMatrix, viewMatrix);
		
		Vector3f cameraPos = new Vector3f(camera.getPosition().x, camera.getPosition().y, camera.getPosition().z);
		Vector3f negateCamera = new Vector3f(-cameraPos.x, -cameraPos.y, -cameraPos.z);
		Matrix4f.translate(negateCamera, viewMatrix, viewMatrix);
		
		return viewMatrix;
	}
	
	public static float Clamp(float value, float min, float max) {
		if (value < min) return min;
		if (value > max) return max;
		return value;
	}
	
	public static Vector4f Clamp(Vector4f value, float min, float max) {
		Vector4f result = value;
		if (value.x < min) result.x = min;
		if (value.x > max) result.x = max;
		
		if (value.y < min) result.y = min;
		if (value.y > max) result.y = max;
		
		if (value.z < min) result.z = min;
		if (value.z > max) result.z = max;
		
		if (value.w < min) result.w = min;
		if (value.w > max) result.w = max;
		
		return result;
	}
}
