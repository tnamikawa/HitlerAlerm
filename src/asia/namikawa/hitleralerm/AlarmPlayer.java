package asia.namikawa.hitleralerm;

import android.content.Context;
import android.media.MediaPlayer;

public class AlarmPlayer implements MediaPlayer.OnCompletionListener {
  private static MediaPlayer player = null;

  @Override
  public void onCompletion(MediaPlayer mp) {
    AlarmPlayer.stop();
  }
  
  public static void start(Context context) {
    if (null == player) {
      player = MediaPlayer.create(context, R.raw.alarm);
    }
    player.start();
  }
  
  public static void stop() {
    if (null == player){ return; }
    player.stop();
    player = null;
  }
}
