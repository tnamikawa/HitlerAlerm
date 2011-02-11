package asia.namikawa.hitleralerm;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

public class HitlerActivity extends Activity {
  protected int hour, minute;
  protected HashMap<Integer, Boolean> weekdays;
  
  protected void refreshAlarm() {
    AlarmManager man = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
    PendingIntent it = getalarmIntent();
    man.cancel(it);

    Date wakeAt = getNextWakeAt();
    if (null == wakeAt) {
      return;
    }
    man.set(AlarmManager.RTC_WAKEUP, wakeAt.getTime(), it);
  }

  protected PendingIntent getalarmIntent() {
    Context context = getApplicationContext();
    Intent intent = new Intent(context, RingingPanel.class);
    return PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
  }

  protected Date getNextWakeAt() {
    HashMap<Integer, Integer> wd2wd = new HashMap<Integer, Integer>();
    wd2wd.put(Calendar.SUNDAY, R.id.sunday);
    wd2wd.put(Calendar.MONDAY, R.id.monday);
    wd2wd.put(Calendar.TUESDAY, R.id.tuesday);
    wd2wd.put(Calendar.WEDNESDAY, R.id.wednesday);
    wd2wd.put(Calendar.THURSDAY, R.id.thursday);
    wd2wd.put(Calendar.FRIDAY, R.id.friday);
    wd2wd.put(Calendar.SATURDAY, R.id.saturday);
    
    Date now = new Date();

    Calendar cal = Calendar.getInstance();
    cal.setTimeInMillis(new Date().getTime());
    cal.set(Calendar.HOUR_OF_DAY, hour);
    cal.set(Calendar.MINUTE, minute);
    cal.set(Calendar.SECOND, 0);
    boolean isOn = false;
    for (int i = 0; i < 7; i++) {
      if (hour < now.getHours() || (hour == now.getHours() && minute <= now.getMinutes())) {
        cal.add(Calendar.DATE, 1);
      }
      Boolean isOk = weekdays.get(wd2wd.get(cal.get(Calendar.DAY_OF_WEEK)));
      if (isOk) {
        isOn = true;
        break;
      }
    }

    if (! isOn) {
      return null;
    }

    Log.i("HitlerActivity", "nextWakeAt: " + new Date(cal.getTimeInMillis()));
    return new Date(cal.getTimeInMillis());
  }

  
  protected void initData() {
    hour = 6;
    minute = 30;
    weekdays = new HashMap<Integer, Boolean>();
    weekdays.put(R.id.sunday, true);
    weekdays.put(R.id.monday, true);
    weekdays.put(R.id.tuesday, true);
    weekdays.put(R.id.wednesday, true);
    weekdays.put(R.id.thursday, true);
    weekdays.put(R.id.friday, true);
    weekdays.put(R.id.saturday, true);
  }

  protected void loadData() {
    int[] ids = {R.id.sunday, R.id.monday, R.id.tuesday, R.id.wednesday, R.id.thursday, R.id.friday, R.id.saturday};
    boolean isOff = false;
    
    SharedPreferences pref = getSharedPreferences("alerm", MODE_PRIVATE);
    String savedItem = pref.getString("wakeAt", "OFF");
    Log.i("savedItem", savedItem);

    if ("OFF".equals(savedItem)) {
      isOff = true;
      hour = 7;
      minute = 0;
      for (int id : ids) {
        if (isOff) {
          weekdays.put(id, false);
        }
      }
      return;
    }

    String[] pair = savedItem.split(":");
    hour = Integer.parseInt(pair[0]);
    minute = Integer.parseInt(pair[1]);
    isOff = false;

    for (int id : ids) {
      weekdays.put(id, pref.getBoolean(String.valueOf(id), true));
    }
  }
  
  protected void saveData() {
    int[] ids = {R.id.sunday, R.id.monday, R.id.tuesday, R.id.wednesday, R.id.thursday, R.id.friday, R.id.saturday};
    
    SharedPreferences pref = getSharedPreferences("alerm", MODE_PRIVATE);
    SharedPreferences.Editor editor = pref.edit();
    editor.putString("wakeAt", String.format("%02d:%02d", hour, minute));
    for (int id : ids) {
      editor.putBoolean(String.valueOf(id), weekdays.get(id));
    }
    editor.commit();
  }

}
