package engine.models;

public class BaseModel {
	private int vao;
	private int vertices;
	
	public BaseModel(int vao, int vertices) {
		this.vao = vao;
		this.vertices = vertices;
	}
	
	public int getVAO() { return vao; }
	
	public int getVertices() { return vertices; }
}
