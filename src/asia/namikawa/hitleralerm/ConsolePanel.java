package asia.namikawa.hitleralerm;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TimePicker;

public class ConsolePanel extends HitlerActivity implements TimePicker.OnTimeChangedListener, CheckBox.OnCheckedChangeListener {
  private MenuItem item1, item2;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    Log.i("ConsolePanel", "onCreate()");
    super.onCreate(savedInstanceState);
    requestWindowFeature(Window.FEATURE_NO_TITLE);

    initData();
    loadData();
    Log.i("ConsolePanel", "current: " + hour + ":" + minute);
    
    setContentView(R.layout.main);
  }

  @Override
  public void onContentChanged() {
    Log.i("ConsolePanel", "onContentChanged()");
    super.onContentChanged();
    

    TimePicker picker = (TimePicker) findViewById(R.id.pick_timer);
    picker.setCurrentHour(hour);
    picker.setCurrentMinute(minute);
    picker.setOnTimeChangedListener(this);

    CheckBox sun, mon, tue, wed, thu, fri, sat;
    sun = (CheckBox) findViewById(R.id.sunday);
    sun.setChecked(weekdays.get(R.id.sunday));
    sun.setOnCheckedChangeListener(this);
    mon = (CheckBox) findViewById(R.id.monday);
    mon.setChecked(weekdays.get(R.id.monday));
    mon.setOnCheckedChangeListener(this);
    tue = (CheckBox) findViewById(R.id.tuesday);
    tue.setChecked(weekdays.get(R.id.tuesday));
    tue.setOnCheckedChangeListener(this);
    wed = (CheckBox) findViewById(R.id.wednesday);
    wed.setChecked(weekdays.get(R.id.wednesday));
    wed.setOnCheckedChangeListener(this);
    thu = (CheckBox) findViewById(R.id.thursday);
    thu.setChecked(weekdays.get(R.id.thursday));
    thu.setOnCheckedChangeListener(this);
    fri = (CheckBox) findViewById(R.id.friday);
    fri.setChecked(weekdays.get(R.id.friday));
    fri.setOnCheckedChangeListener(this);
    sat = (CheckBox) findViewById(R.id.saturday);
    sat.setChecked(weekdays.get(R.id.saturday));
    sat.setOnCheckedChangeListener(this);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    Log.i("ConsolePanel", "onCreateOptionsMenu()");
    super.onCreateOptionsMenu(menu);

    item1 = menu.add(R.string.play);
    item1.setIcon(R.drawable.play);
    item2 = menu.add(R.string.about);
    item2.setIcon(R.drawable.info);

    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    Log.i("ConsolePanel", "onOptionsItemSelected()");
    if (item.equals(item1)) {
      Intent intent = new Intent(this, RingingPanel.class);
      startActivity(intent);
      return true;
    }
    if (item.equals(item2)) {
      Uri uri = Uri.parse("http://namikawa.asia/");
      Intent intent = new Intent(Intent.ACTION_VIEW, uri);
      startActivity(intent);
      return true;
    }
    return false;
  }

  @Override
  public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
    Log.i("ConsolePanel", "onTimeChanged()");
    hour = hourOfDay;
    this.minute = minute;

    refreshAlarm();
    saveData();
  }

  @Override
  public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
    Log.i("ConsolePanel", "onCheckedChanged()");
    int[] ids = { R.id.sunday, R.id.monday, R.id.tuesday, R.id.wednesday, R.id.thursday, R.id.friday, R.id.saturday };
    for (int id : ids) {
      if (id == buttonView.getId()) {
        weekdays.put(id, isChecked);
      }
    }

    refreshAlarm();
    saveData();
  }
}