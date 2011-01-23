package asia.namikawa.hitleralerm;

import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class ConsolePanel extends Activity implements AdapterView.OnItemSelectedListener {
  private final static String[] ITEMS = { "OFF", "06:00", "06:15", "06:30", "06:45", "07:00", "07:15", "07:30" };
  
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    requestWindowFeature(Window.FEATURE_NO_TITLE);
    setContentView(R.layout.main);
    
    Spinner spin1 = (Spinner)findViewById(R.id.spn_timer);
    spin1.setOnItemSelectedListener(this);
    ArrayAdapter<String> aa1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, ITEMS);
    aa1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    spin1.setAdapter(aa1);
    spin1.setSelection(getItemPosition());
  }
  
  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    super.onCreateOptionsMenu(menu);
    
    MenuItem item = menu.add("PLAY");
    item.setIcon(R.drawable.play);
    
    return true;
  }
  
  @Override
  public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
    String item = ITEMS[position];
    
    // 設定に保存
    SharedPreferences pref = getSharedPreferences("alerm", MODE_PRIVATE);
    SharedPreferences.Editor editor = pref.edit();
    editor.putString("wakeAt", item);
    editor.commit();
    
    AlarmManager man = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
    PendingIntent it = getalarmIntent();
    
    if ("OFF".equals(item)) {
      man.cancel(it);
      return;
    }
    
    Date wakeAt = getNextWakeAt(item);
    long daily = 1000 * 60 * 60 * 24;
    
    man.setRepeating(AlarmManager.RTC_WAKEUP, wakeAt.getTime(), daily, it);
  }

  @Override
  public void onNothingSelected(AdapterView<?> parent) {
  }
  
  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    Intent intent = new Intent(this, RingingPanel.class);
    startActivity(intent);
    return true;
  }
  
  private PendingIntent getalarmIntent() {
    Context context = getApplicationContext();
    Intent intent = new Intent(context, RingingPanel.class);
    return PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
  }
  
  private Date getNextWakeAt(String item) {
    String[] pair = item.split(":");
    Date now = new Date();
    int hour = Integer.parseInt(pair[0]);
    int minute = Integer.parseInt(pair[1]);
    
    Calendar cal = Calendar.getInstance();
    cal.setTimeInMillis(new Date().getTime());
    if (hour < now.getHours() || (hour == now.getHours() && minute <= now.getMinutes())) {
      cal.add(Calendar.DATE, 1);
    }
    cal.set(Calendar.HOUR_OF_DAY, hour);
    cal.set(Calendar.MINUTE, minute);
    cal.set(Calendar.SECOND, 0);
    
    cal.add(Calendar.MINUTE, 1);
    
    return new Date(cal.getTimeInMillis());
  }
  
  private int getItemPosition() {
    SharedPreferences pref = getSharedPreferences("alerm", MODE_PRIVATE);
    String savedItem = pref.getString("wakeAt", "OFF");
    Log.i("savedItem", savedItem);
    for (int i = 0; i < ITEMS.length; i++) {
      Log.i("ITEMS[" + i + "]", ITEMS[i]);
      if (ITEMS[i].equals(savedItem)) {
        Log.i("ITEMS[" + i + "]", "MATCH");
        return i;
      }
    }
    return 0;
  }
}