package ruben.garcalia.guessthenumber.realgame;

import ruben.garcalia.guessthenumber.realgame.drawutils.DrawUtilsRuben;
import ruben.garcalia.guessthenumber.realgame.drawutils.ColorRuben;
import ruben.garcalia.guessthenumber.realgame.handler.CharacterRuben;
import ruben.garcalia.guessthenumber.realgame.handler.ObjectRuben;
import ruben.garcalia.guessthenumber.realgame.handler.SceneRuben;

import java.util.List;
import java.util.ArrayList;

import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;

import java.nio.*;
import java.time.Duration;
import java.time.Instant;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

public class GameRuben {

    // The window handle
    private long window;

    // Esto lo mismo necesita una vuelta
    public List<ObjectRuben> objs = new ArrayList<>();
    public CharacterRuben character = new CharacterRuben("Main Character", "soldier.png", 0f, 0f, 1f, 1f);;
    final public SceneRuben[] scenes = {};
    
    private int shaderProgram;

    public void run() {
        init();
        loop();

        // Free the window callbacks and destroy the window
        glfwFreeCallbacks(window);
        glfwDestroyWindow(window);

        // Terminate GLFW and free the error callback
        glfwTerminate();
        try {
            glfwSetErrorCallback(null).free();
        } catch (Error e) {
        }
    }

    private void init() {
        // Setup an error callback. The default implementation
        // will print the error message in System.err.
        GLFWErrorCallback.createPrint(System.err).set();

        // Initialize GLFW. Most GLFW functions will not work before doing this.
        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }

        // Configure GLFW
        glfwDefaultWindowHints(); // optional, the current window hints are already the default
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden after creation
        glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE);

        // Create the window
        window = glfwCreateWindow(600, 600, ":D", NULL, NULL);
        if (window == NULL) {
            throw new RuntimeException("Failed to create the GLFW window");
        }

        // Setup a key callback. It will be called every time a key is pressed, repeated or released.
        glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
            if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE) {
                glfwSetWindowShouldClose(window, true);
            }
            character.getInputs(window, key, scancode, action, mods);
        });

        

        // Get the thread stack and push a new frame
        try (MemoryStack stack = stackPush()) {
            IntBuffer pWidth = stack.mallocInt(1); // int*
            IntBuffer pHeight = stack.mallocInt(1); // int*

            // Get the window size passed to glfwCreateWindow
            glfwGetWindowSize(window, pWidth, pHeight);

            // Get the resolution of the primary monitor
            GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

            // Center the window
            //glfwSetWindowPos(
            //	window,
            //	(vidmode.width() - pWidth.get(0)) / 2,
            //	(vidmode.height() - pHeight.get(0)) / 2
            //);
        } // the stack frame is popped automatically

        // Make the OpenGL context current
        glfwMakeContextCurrent(window);
        // Enable v-sync
        glfwSwapInterval(1);

//                glEnable(GL_TEXTURE);
        // Make the window visible
        glfwShowWindow(window);
    }

    private void loop() {
        // This line is critical for LWJGL's interoperation with GLFW's
        // OpenGL context, or any context that is managed externally.
        // LWJGL detects the context that is current in the current thread,
        // creates the GLCapabilities instance and makes the OpenGL
        // bindings available for use.
        GL.createCapabilities();
        
        System.out.println("OpenGL Version: " + glGetString(GL_VERSION));
        
        // Inicializar shaders
        String vertexShaderSource = "#version 330 core\n"
                + "layout(location = 0) in vec3 aPos;\n"
                + "layout(location = 1) in vec2 aTexCoord;\n"
                + "out vec2 TexCoord;\n"
                + "void main() {\n"
                + "    gl_Position = vec4(aPos, 1.0);\n"
                + "    TexCoord = aTexCoord;\n"
                + "}\n";

        String fragmentShaderSource = "#version 330 core\n"
                + "out vec4 FragColor;\n"
                + "in vec2 TexCoord;\n"
                + "uniform sampler2D texture1;\n"
                + "void main() {\n"
                + "    FragColor = texture(texture1, TexCoord);\n"
                + "}\n";

        int vertexShader = glCreateShader(GL_VERTEX_SHADER);
        glShaderSource(vertexShader, vertexShaderSource);
        glCompileShader(vertexShader);

        int fragmentShader = glCreateShader(GL_FRAGMENT_SHADER);
        glShaderSource(fragmentShader, fragmentShaderSource);
        glCompileShader(fragmentShader);

        shaderProgram = glCreateProgram();
        glAttachShader(shaderProgram, vertexShader);
        glAttachShader(shaderProgram, fragmentShader);
        glLinkProgram(shaderProgram);

        glDeleteShader(vertexShader);
        glDeleteShader(fragmentShader);

        // Set the clear color
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        
        character.createVertex();
        character.loadTexture();
        
        double deltaTime = 0;
        double beginTime = System.nanoTime();
        
        // Run the rendering loop until the user has attempted to close
        // the window or has pressed the ESCAPE key.
        while (!glfwWindowShouldClose(window)) {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer
            
            glUseProgram(shaderProgram);

            character.update(deltaTime);

            glfwSwapBuffers(window); // swap the color buffers

            // Poll for window events. The key callback above will only be
            // invoked during this call.
            glfwPollEvents();
            
            deltaTime = (System.nanoTime() - beginTime) / 1e9;
            beginTime = System.nanoTime();
//            if (deltaTime != 0)
//                System.out.println(1 / deltaTime);
        }
    }

    public static void main(String[] args) {
        new GameRuben().run();
    }

}
