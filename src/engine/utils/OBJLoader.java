package engine.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import engine.maths.Vector2f;
import engine.maths.Vector3f;
import engine.models.BaseModel;

public class OBJLoader {
	
	private static FileReader fileReader;
	
	public static BaseModel loadObj(String fileName, ModelLoader loader) {
		try {
			fileReader = new FileReader(new File(String.format("res/models/%s.obj", fileName)));
		} catch (FileNotFoundException e) {
			System.err.println(String.format(
					"[ERROR]: Could not find model at %s, printing stacktrace and exiting...", fileName));
			e.printStackTrace();
			System.exit(-1);
		}
		
		BufferedReader bufferedReader = new BufferedReader(fileReader);
		String line = "";
		List<Vector3f> vertices = new ArrayList<Vector3f>(), normals = new ArrayList<Vector3f>();
		List<Vector2f> textures = new ArrayList<Vector2f>();
		List<Integer> indices = new ArrayList<Integer>();
		
		float[] verticesArray, normalsArray = null, textureArray = null;
		int[] indicesArray;
		boolean reading = true;
		
		try {
			while (reading) {
				line = bufferedReader.readLine();
				String[] currentLine = line.split(" ");
				switch(currentLine[0]) {
				case "v":
					Vector3f vertex = new Vector3f(Float.parseFloat(currentLine[1]),
							Float.parseFloat(currentLine[2]), Float.parseFloat(currentLine[3]));
					vertices.add(vertex);
					break;
				case "vt":
					Vector2f texture = new Vector2f(Float.parseFloat(currentLine[1]),
							Float.parseFloat(currentLine[2]));
					textures.add(texture);
					break;
				case "vn":
					Vector3f normal = new Vector3f(Float.parseFloat(currentLine[1]),
							Float.parseFloat(currentLine[2]), Float.parseFloat(currentLine[3]));
					normals.add(normal);
					break;
				case "f":
					textureArray = new float[vertices.size() * 2];
					normalsArray = new float[vertices.size() * 3];
					reading = false;
					break;
				}
			}
			
			while (line != null) {
				if (!line.startsWith("f ")) {
					line = bufferedReader.readLine();
					continue;
				}
				
				String[] currentLine = line.split(" ");
				List<String[]> vertexBundle = new ArrayList<String[]>();
				String[] vertex1 = currentLine[1].split("/"), vertex2 = currentLine[2].split("/"),
						 vertex3 = currentLine[3].split("/");
				vertexBundle.add(vertex1);
				vertexBundle.add(vertex2);
				vertexBundle.add(vertex3);
				
				for (int i = 0; i < vertexBundle.size(); i++) 
					processVertex(vertexBundle.get(i), indices, textures, normals, textureArray, normalsArray);
				
				line = bufferedReader.readLine();
			}
			bufferedReader.close();
			
		} catch (IOException e) {
			System.err.println("[ERROR]: Could not read file for some reason? Printing stacktrace and exiting...");
			e.printStackTrace();
			System.exit(-1);
		}
		
		verticesArray = new float[vertices.size() * 3];
		indicesArray = new int[indices.size()];
		
		int vertexPointer = 0;
		for (Vector3f vertex: vertices) {
			verticesArray[vertexPointer++] = vertex.x;
			verticesArray[vertexPointer++] = vertex.y;
			verticesArray[vertexPointer++] = vertex.z;
		}
		
		for (int i = 0; i < indices.size(); i++) {
			indicesArray[i] = indices.get(i);
		}
		return loader.appendVAO(verticesArray, textureArray, indicesArray);
	}
	
	private static void processVertex(String[] vertexData, List<Integer> indices,
			List<Vector2f> textures, List<Vector3f> normals, float[] textureArray, float[] normalsArray) {
		int currentVertexPointer = Integer.parseInt(vertexData[0]) - 1;
		indices.add(currentVertexPointer);
		Vector2f currentTex = textures.get(Integer.parseInt(vertexData[1]) - 1);
		textureArray[currentVertexPointer * 2] = currentTex.x;
		textureArray[currentVertexPointer * 2 + 1] = 1 - currentTex.y;
		
		Vector3f currentNorm = normals.get(Integer.parseInt(vertexData[2]) - 1);
		normalsArray[currentVertexPointer * 3] = currentNorm.x;
		normalsArray[currentVertexPointer * 3 + 1] = currentNorm.y;
		normalsArray[currentVertexPointer * 3 + 2] = currentNorm.z;
	}
}
