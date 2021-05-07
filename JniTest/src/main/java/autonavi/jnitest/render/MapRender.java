package autonavi.jnitest.render;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.opengl.GLES30;
import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;
import android.util.Log;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import autonavi.jnitest.R;

public class MapRender implements GLSurfaceView.Renderer {
    private static final String TAG = "TriangleSample";


    private static final String LINE_VERTEX_SHADER = "" +
            "#version 300 es                          \n" +
            "layout(location = 0) in vec4 vPosition;  \n" +
            "void main()                              \n" +
            "{                                        \n" +
            "   gl_Position = vPosition;              \n" +
            "   gl_PointSize = 20.0;              \n" +
            "}                                        \n";
    private static final String LINE_FRAGMENT_SHADER = "" +
            "#version 300 es                              \n" +
            "precision mediump float;                     \n" +
            "layout(location = 0) out vec4 fragColor;       \n" +
            "uniform vec4 inFragColor;                          \n" +
            "void main()                                  \n" +
            "{                                            \n" +
            "   fragColor = inFragColor;  \n" +
            "}                                            \n";

    private static final String VERTEX_SHADER = "" +
            "#version 300 es                            \n" +
            "layout(location = 0) in vec4 a_position;   \n" +
            "layout(location = 1) in vec2 a_texCoord;   \n" +
            "out vec2 v_texCoord;                       \n" +
            "void main()                                \n" +
            "{                                          \n" +
            "   gl_Position = a_position;               \n" +
            "   v_texCoord = a_texCoord;                \n" +
            "}                                          \n";
    /**
     * 片段着色器
     */
    private static final String FRAGMENT_SHADER = "" +
            "#version 300 es                                     \n" +
            "precision mediump float;                            \n" +
            "in vec2 v_texCoord;                                 \n" +
            "layout(location = 0) out vec4 outColor;             \n" +
            "uniform sampler2D s_TextureMap;                     \n" +
            "void main()                                         \n" +
            "{                                                   \n" +
            "  outColor = texture(s_TextureMap, v_texCoord);     \n" +
            "}                                                   \n";

    Bitmap bitmap;

    public MapRender(Context context) {
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.week1);
    }

    int lineProgram;
    int linePosition;
    int lineColor;

    int textureProgram;
    int texturePosition;
    int textureCoord;
    int texture;
    IntBuffer intBuffer;


    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        lineProgram = GLUtil.createProgram(LINE_VERTEX_SHADER, LINE_FRAGMENT_SHADER);
        linePosition = GLES30.glGetAttribLocation(lineProgram, "vPosition");
        lineColor = GLES30.glGetUniformLocation(lineProgram, "inFragColor");

        intBuffer = IntBuffer.allocate(1);
        GLES30.glGenTextures(1, intBuffer);
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, intBuffer.get(0));
        GLES30.glTexParameterf(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_WRAP_S, GLES30.GL_CLAMP_TO_EDGE);
        GLES30.glTexParameterf(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_WRAP_T, GLES30.GL_CLAMP_TO_EDGE);
        GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_MIN_FILTER, GLES30.GL_LINEAR);
        GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_MAG_FILTER, GLES30.GL_LINEAR);
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, GLES30.GL_NONE);

        textureProgram = GLUtil.createProgram(VERTEX_SHADER, FRAGMENT_SHADER);
        texturePosition = GLES30.glGetAttribLocation(textureProgram, "a_position");
        textureCoord = GLES30.glGetAttribLocation(textureProgram, "a_texCoord");
        texture = GLES30.glGetUniformLocation(textureProgram, "v_texCoord");

        int color = 0xE1E6EA;
        float r = Color.red(color) / 255.0f;
        float g = Color.green(color) / 255.0f;
        float b = Color.blue(color) / 255.0f;
        GLES30.glClearColor(r, g, b, 1);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES30.glViewport(0, 0, width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT); // clear color buffer
        int vertexCount = 3;
        // 1. 选择使用的程序
        GLES30.glUseProgram(lineProgram);

        float[] vertices = new float[]{
                -0.333333f, 1.0f, 0,
                -0.333333f, -1.0f, 0,
                0.333333f, 1.0f, 0,
                0.333333f, -1.0f, 0,
                -1.0f, 0.5f, 0,
                1.0f, 0.5f, 0,
                -1.0f, 0f, 0,
                1.0f, 0f, 0,
                -1.0f, -0.5f, 0,
                1.0f, -0.5f, 0,
        };

        FloatBuffer vertexBuffer = GLUtil.array2Buffer(vertices);

        // 2. 加载顶点数据
        GLES30.glVertexAttribPointer(linePosition, vertexCount, GLES30.GL_FLOAT, false, 3 * 4, vertexBuffer);
        GLES30.glEnableVertexAttribArray(linePosition);
        GLES30.glUniform4f(lineColor, 1.0f, 1.0f, 1.0f, 0);
        // 3. 绘制
        GLES30.glLineWidth(30.0f);
        GLES30.glDrawArrays(GLES30.GL_LINES, 0, 10);
        drawRect(0, 0);
        drawRect(0.66666667f, 0);
        drawRect(0.66666667f * 2, 0);
        drawRect(0, -0.5f);
        drawRect(0.66666667f, -0.5f);
        drawRect(0.66666667f * 2, -0.5f);
        drawRect(0, -0.5f * 2);
        drawRect(0.66666667f, -0.5f * 2);
        drawRect(0.66666667f * 2, -0.5f * 2);
        drawRect(0, -0.5f * 3);
        drawRect(0.66666667f, -0.5f * 3);
        drawRect(0.66666667f * 2, -0.5f * 3);

        GLES30.glLinkProgram(textureProgram);
        // Use the program object
        GLES30.glUseProgram(textureProgram);
        float verticesCoords[] = {
                -0.1f, 0.1f, 0.0f,  // Position 0
                -0.1f, -0.1f, 0.0f,  // Position 1
                0.1f, -0.1f, 0.0f,  // Position 2
                0.1f, 0.1f, 0.0f,  // Position 3
        };

        float textureCoords[] = {
                0.0f, 0.0f,        // TexCoord 0
                0.0f, 1.0f,        // TexCoord 1
                1.0f, 1.0f,        // TexCoord 2
                1.0f, 0.0f         // TexCoord 3
        };

        GLUtils.texImage2D(GLES30.GL_TEXTURE_2D, 0, bitmap, 0);
        vertexBuffer = GLUtil.array2Buffer(verticesCoords);
        GLES30.glVertexAttribPointer(texturePosition, 3, GLES30.GL_FLOAT,
                false, 3 * 4, vertexBuffer);

        FloatBuffer fragmentBuffer = GLUtil.array2Buffer(textureCoords);
        // Load the texture coordinate
        GLES30.glVertexAttribPointer(textureCoord, 2, GLES30.GL_FLOAT,
                false, 2 * 4, fragmentBuffer);

        GLES30.glEnableVertexAttribArray(texturePosition);
        GLES30.glEnableVertexAttribArray(textureCoord);

        // Bind the RGBA map
        GLES30.glActiveTexture(GLES30.GL_TEXTURE0);
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, intBuffer.get(0));
        GLES30.glUniform1i(texture, 0);


        short indices[] = {0, 1, 2, 0, 2, 3};
        ByteBuffer vbb = ByteBuffer.allocateDirect(indices.length * 2);
        vbb.order(ByteOrder.nativeOrder()); // 必须要是 native order
        ShortBuffer indicesBuffer = vbb.asShortBuffer();
        indicesBuffer.put(indices);
        indicesBuffer.position(0); // 这一行不要漏了

        GLES30.glDrawElements(GLES30.GL_TRIANGLES, 6, GLES30.GL_UNSIGNED_SHORT, indicesBuffer);
    }

    void drawRect(float xDiff, float yDiff) {
        int vertexCount = 3;
        float[] vertices = new float[]{
                -0.9f, 0.9f, 0,
                -0.45f, 0.9f, 0,
                -0.45f, 0.6f, 0,
                -0.9f, 0.6f, 0
        };
        for (int i = 0; i < vertices.length; i++) {
            if (i % 3 == 0) {
                vertices[i] += xDiff;
            } else if (i % 3 == 1) {
                vertices[i] += yDiff;
            }
        }
        FloatBuffer vertexBuffer = GLUtil.array2Buffer(vertices);
        // 2. 加载顶点数据
        GLES30.glVertexAttribPointer(linePosition, vertexCount, GLES30.GL_FLOAT, false, 3 * 4, vertexBuffer);
        GLES30.glEnableVertexAttribArray(linePosition);
        GLES30.glUniform4f(lineColor, 1.0f, 1.0f, 1.0f, 0);
        // 3. 绘制
        GLES30.glLineWidth(30.0f);
        GLES30.glDrawArrays(GLES30.GL_TRIANGLE_FAN, 0, 4);

//        float[] center = new float[3];
//        center[0] = (vertices[0] + vertices[3]) / 2;
//        center[1] = (vertices[1] + vertices[7]) / 2;
//        center[2] = 0;
//
//        vertexBuffer = GLUtil.array2Buffer(center);
//        // 2. 加载顶点数据
//        GLES30.glVertexAttribPointer(linePosition, vertexCount, GLES30.GL_FLOAT, false, 3 * 4, vertexBuffer);
//        GLES30.glEnableVertexAttribArray(linePosition);
//        GLES30.glUniform4f(lineColor, 0f, 0f, 1.0f, 0);
//        // 3. 绘制
//        GLES30.glDrawArrays(GLES30.GL_POINTS, 0, 1);
    }
}
