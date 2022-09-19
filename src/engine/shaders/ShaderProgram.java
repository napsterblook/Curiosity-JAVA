package engine.shaders;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.FloatBuffer;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

import engine.Main;
import engine.graphics.Colour;
import engine.maths.vector.Matrix4f;

public abstract class ShaderProgram {
	private int program;
	private int vertexShader;
	private int fragmentShader;
	
	private static FloatBuffer matrixBuffer = BufferUtils.createFloatBuffer(16);
	
	public ShaderProgram(String vertexShader, String fragmentShader) {
		this.vertexShader = loadShader(vertexShader, GL20.GL_VERTEX_SHADER);
		this.fragmentShader = loadShader(fragmentShader, GL20.GL_FRAGMENT_SHADER);
		program = GL20.glCreateProgram();
		GL20.glAttachShader(program, this.getVertexShader());
		GL20.glAttachShader(program, this.getFragmentShader());
		bindAttribs();
		GL20.glLinkProgram(program);
		GL20.glValidateProgram(program);
		getUniformLocations();
	}
	
	public ShaderProgram() {}
	
	protected abstract void getUniformLocations();
	
	protected int getUniformLocation(String uniform) {
		return GL20.glGetUniformLocation(program, uniform);
	}
	
	public void start() {
		GL20.glUseProgram(program);
	}
	
	public void stop() {
		GL20.glUseProgram(0);
	}
	
	public void destroy() {
		stop();
		GL20.glDetachShader(program, getVertexShader());
		GL20.glDetachShader(program, getFragmentShader());
		GL20.glDeleteShader(getVertexShader());
		GL20.glDeleteShader(getFragmentShader());
		GL20.glDeleteProgram(program);
	}
	
	protected abstract void bindAttribs();
	
	protected void bindAttrib(int attrib, String variable) {
		GL20.glBindAttribLocation(program, attrib, variable);
	}
	
	protected void loadFloat(int location, float value) {
		GL20.glUniform1f(location, value);
	}
	
	protected void loadVector(int location, Vector3f vector) {
		GL20.glUniform3f(location, vector.x, vector.y, vector.z);
	}
	
	protected void loadColour(int location, Colour colour) {
		GL20.glUniform3f(location, colour.getR(), colour.getG(), colour.getB());
	}
	
	protected void load2DVector(int location, Vector2f vector) {
		GL20.glUniform2f(location, vector.x, vector.y);
	}
	
	protected void loadMatrix(int location, Matrix4f matrix) {
		matrix.store(matrixBuffer);
		matrixBuffer.flip();
		GL20.glUniformMatrix4fv(location, false, matrixBuffer);
	}
	
	protected void loadBoolean(int location, boolean value) {
		int integer = 0;
		if (value)
			integer = 1;
		GL20.glUniform1f(location, (float) integer);
	}

	private static int loadShader(String file, int type) {
		StringBuilder shaderSource = new StringBuilder();
		
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line;
			int ver = 460;
			
			if (!Main.OS.contains("Windows"))
				ver = 450;
			shaderSource.append(String.format("#version %s\n\n", ver));
			
			while ((line = reader.readLine()) != null)
				shaderSource.append(line).append("\n");
			reader.close();
		} catch (IOException e) {
			System.err.println("[ERROR]: Could not open shader file, printing stacktrace and exiting...");
			e.printStackTrace();
			System.exit(-1);
		}
		
		int shader = GL20.glCreateShader(type);
		GL20.glShaderSource(shader, shaderSource);
		GL20.glCompileShader(shader);
		
		if (GL20.glGetShaderi(shader, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
			System.err.println("[ERROR]: Shader program failed to compile, printing stacktrace and exiting...");
			System.err.println(GL20.glGetShaderInfoLog(shader, 2000));
			System.out.println();
		}
		
		return shader;
	}

	public int getVertexShader() {
		return vertexShader;
	}

	public int getFragmentShader() {
		return fragmentShader;
	}
}
