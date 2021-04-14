package com.qcmian.happycolor.utils;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.util.Log;

import com.qcmian.happycolor.MyApplication;
import com.qcmian.happycolor.R;
import com.qcmian.happycolor.mission.MissionColorFactory;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class TTSHelper {
    private static TextToSpeech tts;
    private static boolean init = false;

    private static File soundDir;

    private static SoundPool soundPool;
    static Map<String, Integer> map = new HashMap<>();

    public static void init(Context context) {
        soundDir = new File(context.getCacheDir(), "sound");
        if (!soundDir.exists()) {
            soundDir.mkdirs();
        }
        initTTS(context);

        SoundPool.Builder builder = new SoundPool.Builder();
        builder.setMaxStreams(2);
        soundPool = builder.build();
        File[] files = soundDir.listFiles();
        for (File f : files) {
            int id = soundPool.load(f.getAbsolutePath(), 1);
            map.put(f.getName(), id);
        }
        map.put("match1", soundPool.load(context, R.raw.match1, 1));
        map.put("match2", soundPool.load(context, R.raw.match2, 1));
        map.put("match3", soundPool.load(context, R.raw.match3, 1));
        map.put("match4", soundPool.load(context, R.raw.match4, 1));
        map.put("match5", soundPool.load(context, R.raw.match5, 1));
        map.put("match6", soundPool.load(context, R.raw.match6, 1));
        map.put("click", soundPool.load(context, R.raw.click, 1));
        map.put("bgm", soundPool.load(context, R.raw.bgm1, 2));
        map.put("appear", soundPool.load(context, R.raw.appear, 1));
        map.put("胜利", soundPool.load(context, R.raw.waou, 1));

    }

    static MissionColorFactory.MissionColor tempMissionColor = null;

    public static void loadAllSounds(MissionColorFactory.MissionColor missionColor) {

        if (!init) {
            tempMissionColor = missionColor;
            return;
        }
        tts.setOnUtteranceProgressListener(new UtteranceProgressListener() {

            @Override
            public void onStart(String utteranceId) {

            }

            @Override
            public void onDone(String utteranceId) {
                File f = new File(soundDir, utteranceId);
                int id = soundPool.load(f.getAbsolutePath(), 1);
                map.put(utteranceId, id);
            }

            @Override
            public void onError(String utteranceId) {

            }
        });

        for (MissionColorFactory.Circle circle : missionColor.circles) {
            if (!map.containsKey(circle.name)) {
                speak(circle.name);
            }
        }
        tempMissionColor = null;
    }

    private static void initTTS(Context context) {
        tts = new TextToSpeech(context, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    tts.setLanguage(Locale.SIMPLIFIED_CHINESE);
                    tts.setSpeechRate(0.8f);
                    tts.setPitch(5.0f);
                    init = true;
                    if (tempMissionColor != null) {
                        loadAllSounds(tempMissionColor);
                    }
                }
            }
        });
    }


    public static void speak(String text) {
        if (!init) {
            return;
        }

        if (map.containsKey(text)) {
            int id = map.get(text);
            soundPool.play(id, 1, 1, 1, 0, 1);
        } else {
            try {
                File f = new File(soundDir, text);
                if (f.exists()) {
                    f.delete();
                }
                f.createNewFile();
                tts.synthesizeToFile(text, null, f, text);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Log.i("LJZ", "tts play:" + text);

    }

    public static void speakWithLoop(String text) {
        if (!init) {
            return;
        }

        if (map.containsKey(text)) {
            int id = map.get(text);
            soundPool.play(id, 1, 1, 2, -1, 1);
        } else {
            try {
                File f = new File(soundDir, text);
                if (f.exists()) {
                    f.delete();
                }
                f.createNewFile();
                tts.synthesizeToFile(text, null, f, text);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Log.i("LJZ", "tts play:" + text);

    }

    public static void speakWithoutBuffer(String text) {
        if (!init) {
            return;
        }
        tts.speak(text, TextToSpeech.QUEUE_ADD, null, null);
    }

    static MediaPlayer mediaPlayer;

    public static void playBGM(int id) {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
        mediaPlayer = MediaPlayer.create(MyApplication.context, id);
        mediaPlayer.start();
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mediaPlayer.setLooping(true);
                mediaPlayer.start();
            }
        });

    }
}
