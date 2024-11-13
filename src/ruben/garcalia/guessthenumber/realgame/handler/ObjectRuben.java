/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ruben.garcalia.guessthenumber.realgame.handler;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

import ruben.garcalia.guessthenumber.realgame.drawutils.ColorRuben;

import org.lwjgl.BufferUtils;
import static org.lwjgl.opengl.ARBVertexArrayObject.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;

public class ObjectRuben {

  public float x;
  public float y;
  public float width;
  public float height;

  public ColorRuben color;
  public String name;
  public List<ByteBuffer> textureBuffer = new ArrayList<>();

  private int VAO, VBO, EBO;
  private List<Integer> textureIDs = new ArrayList<>();
  private int[] indexs;

  public int rows = 1;
  public int cols = 1;
  public int cellSizeX;
  public int cellSizeY;

  public boolean flipX = false;
  public boolean flipY = false;

  public float fps = 8;
  public float elapsedTime = 0;
  public int textureIndex = 0;

  String texturePath;

  public ObjectRuben(String name, String texturePath, float x, float y, float width, float height, ColorRuben color) {
    this.x = x;
    this.y = y;
    this.width = width;
    this.height = height;
    this.color = color;
    this.name = name;
    this.texturePath = texturePath;

    String executionPath = System.getProperty("user.dir");

    try {
      System.out.println(executionPath + "/" + this.texturePath);
      BufferedImage img = ImageIO.read(new File(executionPath + "/" + this.texturePath));
      int widthImage = img.getWidth();
      int heightImage = img.getHeight();

      int cellSizeX = widthImage / rows;
      int cellSizeY = heightImage / cols;

      for (int i = 0; i < cols * rows; i++) {
        ByteBuffer newBuffer = ByteBuffer.allocateDirect(cellSizeX * cellSizeY * 16);
        int[] pixelsTemp = new int[cellSizeX * cellSizeY * 4];
        img.getRGB(i / rows * cellSizeX, i / cols * cellSizeY, cellSizeX, cellSizeY, pixelsTemp, 0, cellSizeX);

        for (int imageIndex = 0; imageIndex < pixelsTemp.length; imageIndex++) {
          newBuffer.put((byte) ((pixelsTemp[imageIndex] >> 16) & 0xFF)); // R
          newBuffer.put((byte) ((pixelsTemp[imageIndex] >> 8) & 0xFF)); // G
          newBuffer.put((byte) ((pixelsTemp[imageIndex]) & 0xFF)); // B
          newBuffer.put((byte) ((pixelsTemp[imageIndex] >> 24) & 0xFF)); // A
        }
        newBuffer.flip();
        this.textureBuffer.add(newBuffer);
      }
    } catch (IOException ex) {
      Logger.getLogger(ObjectRuben.class.getName()).log(Level.SEVERE, null, ex);
    }

  }

  public ObjectRuben(String name, String texturePath, float x, float y, float width, float height, int cols, int rows) {
    this.x = x;
    this.y = y;
    this.width = width;
    this.height = height;
    this.color = new ColorRuben(1f, 1f, 1f, 1f);
    this.name = name;
    this.texturePath = texturePath;
    this.cols = cols;
    this.rows = rows; 

    String executionPath = System.getProperty("user.dir");

    try {
      System.out.println(executionPath + "/" + this.texturePath);
      BufferedImage img = ImageIO.read(new File(executionPath + "/" + this.texturePath));
      int widthImage = img.getWidth();
      int heightImage = img.getHeight();

      cellSizeX = widthImage / cols;
      cellSizeY = heightImage / rows;

      for (int i = 0; i < cols * rows; i++) {
        ByteBuffer newBuffer = ByteBuffer.allocateDirect(cellSizeX * cellSizeY * 16);
        int[] pixelsTemp = new int[cellSizeX * cellSizeY * 4];
        img.getRGB(i / rows * cellSizeX, i % cols * cellSizeY, cellSizeX, cellSizeY, pixelsTemp, 0, cellSizeX);

        for (int imageIndex = 0; imageIndex < pixelsTemp.length; imageIndex++) {
          newBuffer.put((byte) ((pixelsTemp[imageIndex] >> 16) & 0xFF)); // R
          newBuffer.put((byte) ((pixelsTemp[imageIndex] >> 8) & 0xFF)); // G
          newBuffer.put((byte) ((pixelsTemp[imageIndex]) & 0xFF)); // B
          newBuffer.put((byte) ((pixelsTemp[imageIndex] >> 24) & 0xFF)); // A
        }
        newBuffer.flip();
        this.textureBuffer.add(newBuffer);
      }
    } catch (IOException ex) {
      Logger.getLogger(ObjectRuben.class.getName()).log(Level.SEVERE, null, ex);
    }

  }

  public void loadTexture() {
    for (ByteBuffer texture : textureBuffer) {
      int textureID = glGenTextures();

      // Paso 2: Enlazar la textura
      glBindTexture(GL_TEXTURE_2D, textureID);

      // Paso 3: Configurar los parámetros de la textura
      glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
      glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
      glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
      glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);

      // Paso 4: Cargar la textura en OpenGL
      glTexImage2D(
          GL_TEXTURE_2D,
          0,
          GL_RGBA,
          cellSizeX, // Ancho de la textura
          cellSizeY, // Alto de la textura
          0,
          GL_RGBA,
          GL_UNSIGNED_BYTE, // GL_UNSIGNED_BYTE si cada canal ocupa 1 byte
          texture);

      textureIDs.add(textureID);
    }
  }

  public void createVertex() {

    // 1 0
    // 1 1 FlipX
    // 0 1
    // 0 0

    // 0 1
    // 0 0 FlipY
    // 1 0
    // 1 1

    // 0 0
    // 0 1 Normal
    // 1 1
    // 1 0

    // 1 1
    // 1 0 FlipX y FlipY
    // 0 0
    // 0 1

    float[] vertex = {
        this.x - (this.width / 2), this.y + (this.height / 2), 0.0f, 0.0f, 0.0f,
        this.x - (this.width / 2), this.y - (this.height / 2), 0.0f, 0.0f, 1.0f,
        this.x + (this.width / 2), this.y - (this.height / 2), 0.0f, 1.0f, 1.0f,
        this.x + (this.width / 2), this.y + (this.height / 2), 0.0f, 1.0f, 0.0f
    };

    if (this.flipX && this.flipY) {
      float[] FlipBoth = {
          this.x - (this.width / 2), this.y + (this.height / 2), 0.0f, 1.0f, 1.0f,
          this.x - (this.width / 2), this.y - (this.height / 2), 0.0f, 1.0f, 0.0f,
          this.x + (this.width / 2), this.y - (this.height / 2), 0.0f, 0.0f, 0.0f,
          this.x + (this.width / 2), this.y + (this.height / 2), 0.0f, 0.0f, 1.0f
      };
      vertex = FlipBoth;
    }

    if (this.flipX && !this.flipY) {
      float[] FlipBoth = {
          this.x - (this.width / 2), this.y + (this.height / 2), 0.0f, 1.0f, 0.0f,
          this.x - (this.width / 2), this.y - (this.height / 2), 0.0f, 1.0f, 1.0f,
          this.x + (this.width / 2), this.y - (this.height / 2), 0.0f, 0.0f, 1.0f,
          this.x + (this.width / 2), this.y + (this.height / 2), 0.0f, 0.0f, 0.0f
      };
      vertex = FlipBoth;
    }

    if (!this.flipX && this.flipY) {
      float[] FlipBoth = {
          this.x - (this.width / 2), this.y + (this.height / 2), 0.0f, 0.0f, 1.0f,
          this.x - (this.width / 2), this.y - (this.height / 2), 0.0f, 0.0f, 0.0f,
          this.x + (this.width / 2), this.y - (this.height / 2), 0.0f, 1.0f, 0.0f,
          this.x + (this.width / 2), this.y + (this.height / 2), 0.0f, 1.0f, 1.0f
      };
      vertex = FlipBoth;
    }

    // this.vertexs = vertex;
    int[] index = { 0, 1, 2, 0, 2, 3 };
    this.indexs = index;

    // Crear VAO, VBO y EBO
    VAO = glGenVertexArrays();
    VBO = glGenBuffers();
    EBO = glGenBuffers();

    FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(vertex.length);
    vertexBuffer.put(vertex).flip(); // Añadir los datos y preparar el buffer para lectura

    // Crear el buffer de índices
    IntBuffer indexBuffer = BufferUtils.createIntBuffer(index.length);
    indexBuffer.put(index).flip(); // Añadir los datos y preparar el buffer para lectura

    glBindVertexArray(VAO);

    // Configurar VBO
    glBindBuffer(GL_ARRAY_BUFFER, VBO);
    glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW); // Utiliza el FloatBuffer

    // Configurar EBO
    glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, EBO);
    glBufferData(GL_ELEMENT_ARRAY_BUFFER, indexBuffer, GL_STATIC_DRAW);

    // Definir atributos de posición (layout location = 0) y coordenadas de textura
    // (layout location = 1)
    glVertexAttribPointer(0, 3, GL_FLOAT, false, 5 * Float.BYTES, 0);
    glEnableVertexAttribArray(0);

    glVertexAttribPointer(1, 2, GL_FLOAT, false, 5 * Float.BYTES, 3 * Float.BYTES);
    glEnableVertexAttribArray(1);

    glBindBuffer(GL_ARRAY_BUFFER, 0);
    glBindVertexArray(0);
  }

  public void update(double deltaTime) {
    // createVertex();
    elapsedTime += deltaTime;
    if (elapsedTime >= 1 / fps) {
      textureIndex++;
      elapsedTime = 0;
    }

    if (textureIndex >= textureBuffer.size()) {
      textureIndex = 0;
    }
    draw();
  }

  public void draw() {
    glBindTexture(GL_TEXTURE_2D, textureIDs.get(textureIndex));

    // Enlazar el VAO
    glBindVertexArray(VAO);

    // Dibujar, pilla el vao vinculado y la textura
    glDrawElements(GL_TRIANGLES, this.indexs.length, GL_UNSIGNED_INT, 0);

    // Los desvincula para evitar modificaciones accidentales
    glBindVertexArray(0);
    glBindTexture(GL_TEXTURE_2D, 0);
  }
}
