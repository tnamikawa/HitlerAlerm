package asia.namikawa.hitleralerm;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class RingingPanel extends HitlerActivity implements View.OnClickListener {
  
  public void onCreate(Bundle icicle) {
    super.onCreate(icicle);
    setContentView(R.layout.ringing);
    
    ImageButton btnStop = (ImageButton)findViewById(R.id.btn_stop);
    btnStop.setOnClickListener(this);
    
    initData();
    loadData();
    refreshAlarm();
    
    AlarmPlayer.start(this);
  }

  @Override
  public void onClick(View v) {
    if (R.id.btn_stop == v.getId()) {
      AlarmPlayer.stop();
    }
    
    setContentView(R.layout.stopped);
  }

  @Override
  protected void onPause() {
    super.onPause();
    if (AlarmPlayer.isJustStarted()) return;
    AlarmPlayer.stop();
  }
}
