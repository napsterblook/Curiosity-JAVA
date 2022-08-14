package engine.utils;

import engine.maths.Matrix4f;
import engine.shaders.ShaderProgram;

public class ShaderLoader extends ShaderProgram {
	
	private int location_transformationMatrix;
	private int location_viewMatrix;

	public ShaderLoader(String vertexShader, String fragmentShader) {
		super(vertexShader, fragmentShader);
	}

	@Override
	protected void bindAttribs() {
		super.bindAttrib(0, "position");
		super.bindAttrib(1, "textureCoords");
	}

	@Override
	protected void getUniformLocations() {
		location_transformationMatrix = super.getUniformLocation("transformationMatrix");
		location_viewMatrix = super.getUniformLocation("viewMatrix");
	}
	
	public void loadTransformationMatrix(Matrix4f matrix) {
		super.loadMatrix(location_transformationMatrix, matrix);
	}
	
	public void loadViewMatrix(Matrix4f view) {
		super.loadMatrix(location_viewMatrix, view);
	}
}
