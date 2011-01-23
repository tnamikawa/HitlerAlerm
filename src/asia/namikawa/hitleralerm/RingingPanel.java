package asia.namikawa.hitleralerm;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class RingingPanel extends Activity implements View.OnClickListener {
  
  public void onCreate(Bundle icicle) {
    super.onCreate(icicle);
    setContentView(R.layout.ringing);
    
    ImageButton btnStop = (ImageButton)findViewById(R.id.btn_stop);
    btnStop.setOnClickListener(this);
    
    AlarmPlayer.start(this);
  }

  @Override
  public void onClick(View v) {
    if (R.id.btn_stop == v.getId()) {
      AlarmPlayer.stop();
    }
    
    setContentView(R.layout.stopped);
  }
}
