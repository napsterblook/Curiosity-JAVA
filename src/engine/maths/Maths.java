package engine.maths;

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
		
		Matrix4f.rotate((float) Math.toRadians(camera.getPitch()), new Vector3f(1.0f, 0.0f, 0.0f),
				viewMatrix, viewMatrix);
		
		Matrix4f.rotate((float) Math.toRadians(camera.getYaw()), new Vector3f(0.0f, 1.0f, 0.0f),
				viewMatrix, viewMatrix);
		
		Matrix4f.rotate((float) Math.toRadians(camera.getRoll()), new Vector3f(0.0f, 0.0f, 1.0f),
				viewMatrix, viewMatrix);
		
		Vector3f cameraPos = camera.getPosition();
		Vector3f negateCamera = new Vector3f(-cameraPos.x, -cameraPos.y, -cameraPos.z);
		Matrix4f.translate(negateCamera, viewMatrix, viewMatrix);
		
		return viewMatrix;
	}
}
