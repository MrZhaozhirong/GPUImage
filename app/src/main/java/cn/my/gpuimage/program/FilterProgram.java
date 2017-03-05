package cn.my.gpuimage.program;

import android.content.Context;
import android.opengl.GLES20;

import java.nio.FloatBuffer;
import java.util.LinkedList;

import cn.my.gpuimage.R;
import cn.my.gpuimage.common.OpenGlUtils;
import cn.my.gpuimage.common.TextResourceReader;

/**
 * Created by zzr on 2017/3/4.
 */

public class FilterProgram {

    protected int mGLProgramId;
    public int getProgram() { return mGLProgramId; }

    private final LinkedList<Runnable> mRunOnDraw;
    protected void runPendingOnDrawTasks() {
        while (!mRunOnDraw.isEmpty()) {
            mRunOnDraw.removeFirst().run();
        }
    }

    private final String mVertexShader;
    private final String mFragmentShader;

    public FilterProgram(Context context){
        this(context, R.raw.no_filter_vertex_shader, R.raw.no_filter_fragment_shader);
    }

    public FilterProgram(Context context, int vertexShaderResourceId, int fragmentShaderResourceId){
        mRunOnDraw = new LinkedList<Runnable>();
        mVertexShader = TextResourceReader.readTextFileFromResource(context,vertexShaderResourceId);
        mFragmentShader = TextResourceReader.readTextFileFromResource(context,fragmentShaderResourceId);
    }

    private boolean mIsInitialized;

    public final void init() {
        onInit();
        mIsInitialized = true;
        onInitialized();
    }

    public static final String POSITION= "position";
    public static final String INPUT_IMAGE_TEXTURE= "inputImageTexture";
    public static final String INPUT_TEXTURE_COORDINATE= "inputTextureCoordinate";

    protected int mGLAttribPosition;
    protected int mGLUniformImageTexture;
    protected int mGLAttribTextureCoordinate;

    public void onInit() {
        mGLProgramId = OpenGlUtils.loadProgram(mVertexShader, mFragmentShader);
        mGLAttribPosition = GLES20.glGetAttribLocation(mGLProgramId, POSITION);
        mGLUniformImageTexture = GLES20.glGetUniformLocation(mGLProgramId, INPUT_IMAGE_TEXTURE);
        mGLAttribTextureCoordinate = GLES20.glGetAttribLocation(mGLProgramId,
                INPUT_TEXTURE_COORDINATE);
        mIsInitialized = true;
    }

    public void onInitialized() {
        // subclass @Override
    }

    protected int mOutputWidth;
    protected int mOutputHeight;
    public void onOutputSizeChanged(int width, int height) {
        mOutputWidth = width;
        mOutputHeight = height;
    }


    public void onDraw(final int textureId, final FloatBuffer cubeBuffer,
                       final FloatBuffer textureBuffer) {
        GLES20.glUseProgram(mGLProgramId);
        runPendingOnDrawTasks();
        if (!mIsInitialized) {
            return;
        }
        //创建显示区域矩形缓冲区对象
        cubeBuffer.position(0);
        GLES20.glVertexAttribPointer(mGLAttribPosition, 2, GLES20.GL_FLOAT, false, 0, cubeBuffer);
        GLES20.glEnableVertexAttribArray(mGLAttribPosition);
        //创建纹理坐标缓冲区对象
        textureBuffer.position(0);
        GLES20.glVertexAttribPointer(mGLAttribTextureCoordinate, 2, GLES20.GL_FLOAT, false, 0,
                textureBuffer);
        GLES20.glEnableVertexAttribArray(mGLAttribTextureCoordinate);

        if (textureId != OpenGlUtils.NO_TEXTURE) {
            GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId);
            GLES20.glUniform1i(mGLUniformImageTexture, 0);
        }
        onDrawArraysPre();
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);
        GLES20.glDisableVertexAttribArray(mGLAttribPosition);
        GLES20.glDisableVertexAttribArray(mGLAttribTextureCoordinate);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
    }

    protected void onDrawArraysPre() {
        // subclass @Override
    }


    public final void destroy() {
        mIsInitialized = false;
        GLES20.glDeleteProgram(mGLProgramId);
        onDestroy();
    }
    public void onDestroy() {
        // subclass @Override
    }



}
