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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import org.lwjgl.BufferUtils;
import ruben.garcalia.guessthenumber.realgame.drawutils.ColorRuben;
import ruben.garcalia.guessthenumber.realgame.drawutils.DrawUtilsRuben;

import org.lwjgl.glfw.*;
import static org.lwjgl.opengl.ARBVertexArrayObject.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import org.lwjgl.openvr.Texture;
import org.lwjgl.opengl.GLCapabilities;

public class ObjectRuben {

    public float x;
    public float y;
    public float width;
    public float height;
    public ColorRuben color;
    public String name;
    public Texture texture;
    public ByteBuffer textureBuffer;

    private int VAO, VBO, EBO;
    private List<Integer> textureIDs = new ArrayList<>();
    private int textureIndex = 0;
    private int[] indexs;
    private float[] vertexs;

    private int imgWidth;
    private int imgHeight;
    
    public boolean flipX = false;
    public boolean flipY = false;

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
//            byte[] bytes = Files.readAllBytes(path);
//            this.textureBuffer = ByteBuffer.allocateDirect(bytes.length);
//            this.textureBuffer.put(bytes, 0, bytes.length);
            BufferedImage img = ImageIO.read(new File(executionPath + this.texturePath));
            this.textureBuffer = ByteBuffer.allocateDirect(img.getWidth() * img.getHeight() * 4);
            for (int j = 0; j < img.getHeight(); j++) {
                for (int i = 0; i < img.getWidth(); i++) {
                    int pixel = img.getRGB(i, j);
                    this.textureBuffer.put((byte) ((pixel >> 16) & 0xFF)); // R
                    this.textureBuffer.put((byte) ((pixel >> 8) & 0xFF));  // G
                    this.textureBuffer.put((byte) (pixel & 0xFF));         // B
                    this.textureBuffer.put((byte) ((pixel >> 24) & 0xFF)); // A
                }
            }
            this.textureBuffer.flip();

            this.imgHeight = img.getHeight();
            this.imgWidth = img.getWidth();
        } catch (IOException ex) {
            Logger.getLogger(ObjectRuben.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public ObjectRuben(String name, String texturePath, float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.color = new ColorRuben(1f, 1f, 1f, 1f);
        this.name = name;
        this.texturePath = texturePath;

        String executionPath = System.getProperty("user.dir");

        try {
//            byte[] bytes = Files.readAllBytes(path);
//            this.textureBuffer = ByteBuffer.allocateDirect(bytes.length);
//            this.textureBuffer.put(bytes, 0, bytes.length);
            System.out.println(executionPath + "/"+ this.texturePath);
            BufferedImage img = ImageIO.read(new File(executionPath + "/"+ this.texturePath));

            this.textureBuffer = ByteBuffer.allocateDirect(img.getWidth() * img.getHeight() * 4);
            for (int j = 0; j < img.getHeight(); j++) {
                for (int i = 0; i < img.getWidth(); i++) {
                    int pixel = img.getRGB(i, j);
                    this.textureBuffer.put((byte) ((pixel >> 16) & 0xFF)); // R
                    this.textureBuffer.put((byte) ((pixel >> 8) & 0xFF));  // G
                    this.textureBuffer.put((byte) (pixel & 0xFF));         // B
                    this.textureBuffer.put((byte) ((pixel >> 24) & 0xFF)); // A
                }
            }
            this.textureBuffer.flip();
        } catch (IOException ex) {
            Logger.getLogger(ObjectRuben.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void loadTexture() {
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
                32, // Ancho de la textura
                32, // Alto de la textura
                0,
                GL_RGBA,
                GL_UNSIGNED_BYTE, // GL_UNSIGNED_BYTE si cada canal ocupa 1 byte
                this.textureBuffer
        );

        textureIDs.add(textureID);
    }

    public void createVertex() {
        
        // 1 0
        // 1 1  FlipX
        // 0 1
        // 0 0
        
        // 0 1
        // 0 0  FlipY 
        // 1 0
        // 1 1
        
        // 0 0
        // 0 1  Normal
        // 1 1
        // 1 0
        
        // 1 1
        // 1 0  FlipX y FlipY
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

        this.vertexs = vertex;
        int[] index = {0, 1, 2, 0, 2, 3};
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

        // Definir atributos de posición (layout location = 0) y coordenadas de textura (layout location = 1)
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 5 * Float.BYTES, 0);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1, 2, GL_FLOAT, false, 5 * Float.BYTES, 3 * Float.BYTES);
        glEnableVertexAttribArray(1);

        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindVertexArray(0);
    }

    public void update() {
//        createVertex();
        draw();
    }

    public void draw() {
        glBindTexture(GL_TEXTURE_2D, textureIDs.get(textureIndex));

        // Enlazar el VAO y dibujar
        glBindVertexArray(VAO);
        glDrawElements(GL_TRIANGLES, this.indexs.length, GL_UNSIGNED_INT, 0);

        glBindVertexArray(0);
        glBindTexture(GL_TEXTURE_2D, 0);
    }
}
