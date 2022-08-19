package engine.utils;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import engine.models.BaseModel;
import engine.renderer.Texture;

public class ModelLoader {
	
	private List<Integer> vaos = new ArrayList<Integer>();
	private List<Integer> vbos = new ArrayList<Integer>();
	private List<Integer> textures = new ArrayList<Integer>();
	
	public BaseModel appendVAO(float[] positions, float[] textureCoords, float[] normals,
			int[] indices) {
		int vao = createVAO();
		bindIndicesBuffer(indices);
		appendList(0, 3, positions);
		appendList(1, 2, textureCoords);
		appendList(2, 3, normals);
		unbind();
		return new BaseModel(vao, indices.length);
	}
	
	public int loadPNG(String file) {
		int texture = Texture.loadTexture(String.format("%s.png", file));
		textures.add(texture);
		return texture;
	}
	
	public void destroy() {
		for(int vao: vaos)
			GL30.glDeleteVertexArrays(vao);
		for (int vbo: vbos)
			GL15.glDeleteBuffers(vbo);
		for (int texture: textures)
			GL11.glDeleteTextures(texture);
	}
	
	private int createVAO() {
		int vao = GL30.glGenVertexArrays();
		vaos.add(vao);
		GL30.glBindVertexArray(vao);
		return vao;
	}
	
	private void appendList(int attribute, int coordSize, float[] data) {
		int vbo = GL15.glGenBuffers();
		vbos.add(vbo);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
		FloatBuffer buffer = appendFloatBuffer(data);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
		GL20.glVertexAttribPointer(attribute, coordSize, GL11.GL_FLOAT, false, 0, 0);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
	}
	
	private void unbind() {
		GL30.glBindVertexArray(0);
	}
	
	private void bindIndicesBuffer(int[] indices) {
		int vbo = GL15.glGenBuffers();
		vbos.add(vbo);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vbo);
		IntBuffer buffer = appendIntBuffer(indices);
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
	}
	
	private IntBuffer appendIntBuffer(int[] data) {
		IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
		buffer.put(data).flip();
		return buffer;
	}
	
	private FloatBuffer appendFloatBuffer(float[] data) {
		FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
		buffer.put(data).flip();
		return buffer;
	}
}
