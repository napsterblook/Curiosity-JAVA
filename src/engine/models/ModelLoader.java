package engine.models;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

public class ModelLoader {
	
	private List<Integer> vaoList = new ArrayList<Integer>();
	private List<Integer> vboList = new ArrayList<Integer>();
	
	public BaseModel loadModel(float[] positions) {
		int vao = createVAO();
		storeData(0, positions);
		unbind();
		return new BaseModel(vao, positions.length / 3);
	}
	
	public void destroy() {
		for (int vao : vaoList) GL30.glDeleteVertexArrays(vao);
		for (int vbo : vboList) GL30.glDeleteVertexArrays(vbo);
	}
	
	private int createVAO() {
		int vao = GL30.glGenVertexArrays();
		GL30.glBindVertexArray(vao);
		return vao;
	}
	
	private void storeData(int attrib, float[] data) {
		int vbo = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
		FloatBuffer buffer = storeInFloatBuffer(data);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
		GL20.glVertexAttribPointer(attrib, 3, GL11.GL_FLOAT, false, 0, 0);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
	}
	
	private void unbind() { GL30.glBindVertexArray(0); }
	
	private FloatBuffer storeInFloatBuffer(float[] data) {
		FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
		buffer.put(data).flip();
		return buffer;
	}
}
