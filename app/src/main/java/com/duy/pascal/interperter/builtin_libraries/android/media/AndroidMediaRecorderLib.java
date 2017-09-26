/*
 *  Copyright (c) 2017 Tran Le Duy
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.duy.pascal.interperter.builtin_libraries.android.media;

import android.content.Context;
import android.content.Intent;
import android.media.MediaRecorder;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.WindowManager;

import com.duy.pascal.BasePascalApplication;
import com.duy.pascal.interperter.ast.expressioncontext.ExpressionContextMixin;
import com.duy.pascal.interperter.builtin_libraries.PascalLibrary;
import com.duy.pascal.interperter.builtin_libraries.android.AndroidLibraryManager;
import com.duy.pascal.interperter.builtin_libraries.android.activity.PascalActivityTask;
import com.duy.pascal.interperter.builtin_libraries.android.activity.PascalActivityTaskExecutor;
import com.duy.pascal.interperter.builtin_libraries.android.temp.AndroidUtilsLib;
import com.duy.pascal.interperter.builtin_libraries.annotations.PascalMethod;
import com.duy.pascal.interperter.builtin_libraries.annotations.PascalParameter;
import com.googlecode.sl4a.Log;
import com.googlecode.sl4a.rpc.RpcDefault;
import com.googlecode.sl4a.rpc.RpcOptional;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * A facade for recording media.
 * <p>
 * Guidance notes: Use e.g. '/sdcard/file.ext' for your media destination file. A file extension of
 * mpg will use the default settings for format and codec (often h263 which won't work with common
 * PC media players). A file extension of mp4 or 3gp will use the appropriate format with the (more
 * common) h264 codec. A video player such as QQPlayer (from the android market) plays both codecs
 * and uses the composition matrix (embedded in the video file) to correct for image rotation. Many
 * PC based media players ignore this matrix. Standard video sizes may be specified.
 *
 * @author Felix Arends (felix.arends@gmail.com)
 * @author Damon Kohler (damonkohler@gmail.com)
 * @author John Karwatzki (jokar49@gmail.com)
 */
public class AndroidMediaRecorderLib extends PascalLibrary {

    private final MediaRecorder mMediaRecorder = new MediaRecorder();
    private final Context mContext;
    private AndroidLibraryManager mManager;

    public AndroidMediaRecorderLib(AndroidLibraryManager manager) {
        mContext = manager.getContext();
        mManager = manager;
    }

    @SuppressWarnings("unused")
    @PascalMethod(description = "Records audio from the microphone and saves it to the given location.")
    public void recorderStartMicrophone(@PascalParameter(name = "targetPath") String targetPath)
            throws IOException {
        startAudioRecording(targetPath, MediaRecorder.AudioSource.MIC);
    }

    @SuppressWarnings("unused")
    @PascalMethod(description = "Records video from the camera and saves it to the given location. "
            + "\nDuration specifies the maximum duration of the recording session. "
            + "\nIf duration is 0 this method will return and the recording will only be STOPPED "
            + "\nwhen recorderStop is called or when a scripts exits. "
            + "\nOtherwise it will block for the time period equal to the duration argument."
            + "\nvideoSize: 0=160x120, 1=320x240, 2=352x288, 3=640x480, 4=800x480.")
    public void recorderStartVideo(@PascalParameter(name = "targetPath") String targetPath,
                                   @PascalParameter(name = "duration") @RpcDefault("0") Integer duration,
                                   @PascalParameter(name = "videoSize") @RpcDefault("1") Integer videoSize) throws Exception {
        int ms = convertSecondsToMilliseconds(duration);
        startVideoRecording(new File(targetPath), ms, videoSize);
    }

    private void startVideoRecording(File file, int milliseconds, int videoSize) throws Exception {
        mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);

        int audioSource = MediaRecorder.AudioSource.MIC;
        try {
            Field source = Class.forName("android.media.MediaRecorder$AudioSource").getField("CAMCORDER");
            source.getInt(null);
        } catch (Exception e) {
            Log.e(e);
        }
        int xSize;
        int ySize;
        switch (videoSize) {
            case 0:
                xSize = 160;
                ySize = 120;
                break;
            case 1:
                xSize = 320;
                ySize = 240;
                break;
            case 2:
                xSize = 352;
                ySize = 288;
                break;
            case 3:
                xSize = 640;
                ySize = 480;
                break;
            case 4:
                xSize = 800;
                ySize = 480;
                break;
            default:
                xSize = 320;
                ySize = 240;
                break;
        }

        mMediaRecorder.setAudioSource(audioSource);
        String extension = file.toString().split("\\.")[1];
        switch (extension) {
            case "mp4":
                mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
                mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
                mMediaRecorder.setVideoSize(xSize, ySize);
                mMediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
                break;
            case "3gp":
                mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
                mMediaRecorder.setVideoSize(xSize, ySize);
                mMediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
                break;
            default:

                mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
                mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
                mMediaRecorder.setVideoSize(xSize, ySize);
                mMediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.DEFAULT);
                break;
        }

        mMediaRecorder.setOutputFile(file.getAbsolutePath());
        if (milliseconds > 0) {
            mMediaRecorder.setMaxDuration(milliseconds);
        }
        PascalActivityTask<Exception> prepTask = prepare();
        mMediaRecorder.start();
        if (milliseconds > 0) {
            new CountDownLatch(1).await(milliseconds, TimeUnit.MILLISECONDS);
        }
        prepTask.finish();
    }

    @SuppressWarnings("unused")
    @PascalMethod(description = "Records video (and optionally audio) from the camera and saves it to the given location. "
            + "\nDuration specifies the maximum duration of the recording session. "
            + "\nIf duration is not provided this method will return immediately and the recording will only be STOPPED "
            + "\nwhen recorderStop is called or when a scripts exits. "
            + "\nOtherwise it will block for the time period equal to the duration argument.")
    public void recorderCaptureVideo(@PascalParameter(name = "targetPath") String targetPath,
                                     @PascalParameter(name = "duration") @RpcOptional Integer duration,
                                     @PascalParameter(name = "recordAudio") @RpcDefault("true") Boolean recordAudio) throws Exception {
        int ms = convertSecondsToMilliseconds(duration);
        startVideoRecording(new File(targetPath), ms, recordAudio);
    }

    private void startVideoRecording(File file, int milliseconds, boolean withAudio) throws Exception {
        mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
        if (withAudio) {
            int audioSource = MediaRecorder.AudioSource.MIC;
            try {
                Field source =
                        Class.forName("android.media.MediaRecorder$AudioSource").getField("CAMCORDER");
                audioSource = source.getInt(null);
            } catch (Exception e) {
                Log.e(e);
            }
            mMediaRecorder.setAudioSource(audioSource);
            mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
            mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
        } else {
            mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
        }
        mMediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.DEFAULT);
        mMediaRecorder.setOutputFile(file.getAbsolutePath());
        if (milliseconds > 0) {
            mMediaRecorder.setMaxDuration(milliseconds);
        }
        PascalActivityTask<Exception> prepTask = prepare();
        mMediaRecorder.start();
        if (milliseconds > 0) {
            new CountDownLatch(1).await(milliseconds, TimeUnit.MILLISECONDS);
        }
        prepTask.finish();
    }

    private void startAudioRecording(String targetPath, int source) throws IOException {
        mMediaRecorder.setAudioSource(source);
        mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
        mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
        mMediaRecorder.setOutputFile(targetPath);
        mMediaRecorder.prepare();
        mMediaRecorder.start();
    }

    @SuppressWarnings("unused")
    @PascalMethod(description = "Stops a previously started recording.")
    public void recorderStop() {
        mMediaRecorder.stop();
        mMediaRecorder.reset();
    }

    @SuppressWarnings("unused")
    @PascalMethod(description = "Starts the video capture application to record a video and saves it to the specified path.")
    public void startInteractiveVideoRecording(@PascalParameter(name = "path") final String path) {
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        File file = new File(path);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
        AndroidUtilsLib facade = mManager.getReceiver(AndroidUtilsLib.class);
        facade.startActivityForResult(intent);
    }

    @Override
    public boolean instantiate(Map<String, Object> pluginargs) {
        return false;
    }

    @Override
    public void shutdown() {
        mMediaRecorder.release();
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public void declareConstants(ExpressionContextMixin parentContext) {

    }

    @Override
    public void declareTypes(ExpressionContextMixin parentContext) {

    }

    @Override
    public void declareVariables(ExpressionContextMixin parentContext) {

    }

    @Override
    public void declareFunctions(ExpressionContextMixin parentContext) {

    }

    // TODO(damonkohler): This shares a lot of code with the CameraFacade. It's probably worth moving
    // it there.
    private PascalActivityTask<Exception> prepare() throws Exception {
        PascalActivityTask<Exception> task = new PascalActivityTask<Exception>() {
            @Override
            public void onCreate() {
                super.onCreate();
                final SurfaceView view = new SurfaceView(getActivity());
                getActivity().setContentView(view);
                getActivity().getWindow().setSoftInputMode(
                        WindowManager.LayoutParams.SOFT_INPUT_STATE_UNCHANGED);
                view.getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
                view.getHolder().addCallback(new Callback() {
                    @Override
                    public void surfaceDestroyed(SurfaceHolder holder) {
                    }

                    @Override
                    public void surfaceCreated(SurfaceHolder holder) {
                        try {
                            mMediaRecorder.setPreviewDisplay(view.getHolder().getSurface());
                            mMediaRecorder.prepare();
                            setResult(null);
                        } catch (IOException e) {
                            setResult(e);
                        }
                    }

                    @Override
                    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                    }
                });
            }
        };

        PascalActivityTaskExecutor taskExecutor =
                ((BasePascalApplication) mContext).getTaskExecutor();
        taskExecutor.execute(task);

        Exception e = task.getResult();
        if (e != null) {
            throw e;
        }
        return task;
    }

    private int convertSecondsToMilliseconds(Integer seconds) {
        if (seconds == null) {
            return 0;
        }
        return (int) (seconds * 1000L);
    }
}
