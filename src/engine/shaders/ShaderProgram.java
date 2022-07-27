package engine.shaders;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.lwjgl.opengl.GL20;

public class ShaderProgram {
	private final int programID;
	private int vertexShaderID;
	private int fragmentShaderID;
	
	public Logger logger = Logger.getLogger(ShaderProgram.class.getName());
	
	public ShaderProgram() {
		programID = GL20.glCreateProgram();
		if (programID == 0) {
			System.err.println(String.format("[ERROR]: Could not create shader program: %s", 
					GL20.glGetProgramInfoLog(programID)));
			System.exit(-1);
		}
	}
	
	public void createVertexShader(String shaderCode) {
		vertexShaderID = createShader(shaderCode, GL20.GL_VERTEX_SHADER);
	}
	
	public void createFragmentShader(String shaderCode) {
		vertexShaderID = createShader(shaderCode, GL20.GL_FRAGMENT_SHADER);
	}
	
	protected int createShader(String shaderCode, int shaderType) {
		int shaderID = GL20.glCreateShader(shaderType);
		if (shaderID == 0) {
			System.err.println(String.format("[ERROR]: Could not create shader: %s",
					GL20.glGetShaderInfoLog(shaderID)));
			System.exit(-1);
		}
		
		GL20.glShaderSource(shaderID, shaderCode);
		GL20.glCompileShader(shaderID);
		
		if (GL20.glGetShaderi(shaderID, GL20.GL_COMPILE_STATUS) == 0) {
			System.err.println(String.format("[ERROR]: Failed to compile GLSL: %s",
					GL20.glGetShaderInfoLog(shaderID, 1024)));
			System.exit(-1);
		}
		
		GL20.glAttachShader(programID, shaderID);
		return shaderID;
	}
	
	public void linkShader() {
		GL20.glLinkProgram(programID);
		if (GL20.glGetProgrami(programID, GL20.GL_LINK_STATUS) == 0) {
			System.err.println(String.format("[ERROR]: Failed to link shader: %s",
					GL20.glGetProgramInfoLog(programID, 1024)));
			System.exit(-1);
		}
		
		if (vertexShaderID != 0) {
			GL20.glDetachShader(programID, vertexShaderID);
		}
		
		if (fragmentShaderID != 0) {
			GL20.glDetachShader(programID, fragmentShaderID);
		}
		
		GL20.glValidateProgram(programID);
		if (GL20.glGetProgrami(programID, GL20.GL_VALIDATE_STATUS) == 0) {
			logger.setLevel(Level.WARNING);
			logger.warning(String.format(": Low-level error found in shader: %s",
					GL20.glGetProgramInfoLog(programID, 1024)));
		}
	}
	
	public void bindShader() { GL20.glUseProgram(programID); }
	
	public void unbindShader() { GL20.glUseProgram(0); }
	
	public void destroy() {
		unbindShader();
		if (programID != 0) GL20.glDeleteProgram(programID);
	}
}
