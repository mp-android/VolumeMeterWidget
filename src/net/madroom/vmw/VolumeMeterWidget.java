package net.madroom.vmw;

import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.IBinder;
import android.widget.RemoteViews;

public class VolumeMeterWidget extends AppWidgetProvider {

    private static final String ACTION_VOLUME_CHANGED = "android.media.VOLUME_CHANGED_ACTION";

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        context.startService(new Intent(context, MyService.class));
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
    }
    
    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
    }
    
    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals(ACTION_VOLUME_CHANGED)) {
            context.startService(new Intent(context, MyService.class));
        }
        super.onReceive(context, intent);
    }

    public static class MyService extends Service {
        Context mContext;
        ComponentName mComponentName;
        AppWidgetManager mManager;
        RemoteViews mRemoteViews;
        AudioManager mAudioManager;
        int mRiMax;
        int mNoMax;
        int mVoMax;
        int mMuMax;
        int mAlMax;
        int mSyMax;
//        int mDtMax;

        @Override
        public void onCreate() {
            /**
             * initialize.
             */
            mContext = this;
            mComponentName = new ComponentName(mContext, VolumeMeterWidget.class);
            mManager = AppWidgetManager.getInstance(mContext);
            mRemoteViews = new RemoteViews(mContext.getPackageName(), R.layout.volume_meter_widget_layout);
            mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

            /**
             * Max Volume
             */
            mRiMax = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_RING);
            mNoMax = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_NOTIFICATION);
            mVoMax = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_VOICE_CALL);
            mMuMax = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
            mAlMax = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_ALARM);
            mSyMax = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_SYSTEM);
//            mDtMax = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_DTMF);
        }

        @Override
        public void onStart(Intent intent, int startId) {

            final int widgetCount = AppWidgetManager.getInstance(mContext).getAppWidgetIds(new ComponentName(mContext, VolumeMeterWidget.class)).length;
            if(widgetCount==0) return;

            /**
             * Current Volume
             */
            final int riVol = mAudioManager.getStreamVolume(AudioManager.STREAM_RING);
            final int noVol = mAudioManager.getStreamVolume(AudioManager.STREAM_NOTIFICATION);
            final int voVol = mAudioManager.getStreamVolume(AudioManager.STREAM_VOICE_CALL);
            final int muVol = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
            final int alVol = mAudioManager.getStreamVolume(AudioManager.STREAM_ALARM);
            final int syVol = mAudioManager.getStreamVolume(AudioManager.STREAM_SYSTEM);
//            final int dtVol = mAudioManager.getStreamVolume(AudioManager.STREAM_DTMF);

            /**
             * Set
             */
            mRemoteViews.setInt(R.id.ringer_progress, "setMax", mRiMax);
            mRemoteViews.setInt(R.id.ringer_progress, "setProgress", riVol);
            mRemoteViews.setInt(R.id.notification_progress, "setMax", mNoMax);
            mRemoteViews.setInt(R.id.notification_progress, "setProgress", noVol);
            mRemoteViews.setInt(R.id.voice_progress, "setMax", mVoMax);
            mRemoteViews.setInt(R.id.voice_progress, "setProgress", voVol);
            mRemoteViews.setInt(R.id.music_progress, "setMax", mMuMax);
            mRemoteViews.setInt(R.id.music_progress, "setProgress", muVol);
            mRemoteViews.setInt(R.id.alarm_progress, "setMax", mAlMax);
            mRemoteViews.setInt(R.id.alarm_progress, "setProgress", alVol);
            mRemoteViews.setInt(R.id.system_progress, "setMax", mSyMax);
            mRemoteViews.setInt(R.id.system_progress, "setProgress", syVol);

            mManager.updateAppWidget(mComponentName, mRemoteViews);

            System.exit(0);

        }

        @Override
        public IBinder onBind(Intent intent) {
            return null;
        }
    }
}
