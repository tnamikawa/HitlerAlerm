package asia.namikawa.hitleralerm;

import java.util.Date;

import android.content.Context;
import android.media.MediaPlayer;

public class AlarmPlayer implements MediaPlayer.OnCompletionListener {
  private static MediaPlayer player = null;
  private static Date alarmStartedAt = null;

  @Override
  public void onCompletion(MediaPlayer mp) {
    AlarmPlayer.stop();
  }
  
  public static void start(Context context) {
    if (null == player) {
      player = MediaPlayer.create(context, R.raw.alarm);
    }
    player.start();
    alarmStartedAt = new Date();
  }
  
  synchronized public static void stop() {
    if (null == player){ return; }
    player.stop();
    player = null;
  }
  
  public static boolean isJustStarted() {
    Date now = new Date();
    return null != alarmStartedAt && now.getTime() < alarmStartedAt.getTime() + 2000;
  }
}
