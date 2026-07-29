package com.reinforcementcoach.app;

import android.Manifest;
import android.app.*;
import android.content.*;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.*;
import android.view.*;
import android.widget.*;
import java.util.*;

public class MainActivity extends Activity {
    static final String PREFS = "coach_prefs";
    static final int INK = Color.rgb(36,52,71), BG = Color.rgb(244,246,248), ACCENT = Color.rgb(106,140,163), GREEN = Color.rgb(47,111,85);
    TextView content, countText;
    String[] reinforcement = {
        "I noticed you worked on it today.",
        "I’m proud that you haven’t quit.",
        "I know this matters to you.",
        "I appreciate how much effort this takes."
    };
    String[] coaching = {
        "What is your goal?",
        "How do you think your current plan is working?",
        "What is telling you that?",
        "Would you like to hear an idea I have?",
        "Do you want encouragement, help thinking it through, or my opinion?",
        "What part feels hardest right now?"
    };
    int rIndex=0, cIndex=0;

    @Override public void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(buildUi());
        requestNotificationPermission();
    }

    View buildUi() {
        ScrollView scroll = new ScrollView(this); scroll.setBackgroundColor(BG);
        LinearLayout root = vertical(); root.setPadding(dp(18), dp(26), dp(18), dp(40)); scroll.addView(root);
        TextView title = text("Reinforcement Coach", 28, INK, true); root.addView(title);
        TextView sub = text("Support the person. Evaluate the strategy.", 16, Color.DKGRAY, false); sub.setPadding(0,0,0,dp(18)); root.addView(sub);

        LinearLayout mode = card();
        mode.addView(text("Choose what you need right now", 18, INK, true));
        Button encourage = button("ENCOURAGE", GREEN); encourage.setOnClickListener(v -> showReinforcement()); mode.addView(encourage);
        Button coach = button("COACH", ACCENT); coach.setOnClickListener(v -> showCoaching()); mode.addView(coach);
        Button frustrated = button("I’M GETTING FRUSTRATED", Color.rgb(158,80,66)); frustrated.setOnClickListener(v -> showFrustration()); mode.addView(frustrated);
        root.addView(mode);

        LinearLayout promptCard = card();
        promptCard.addView(text("Your next words",18,INK,true));
        content = text("Choose a mode above.",22,INK,true); content.setPadding(0,dp(14),0,dp(14)); promptCard.addView(content);
        Button another = button("SHOW ANOTHER", INK); another.setOnClickListener(v -> showNext()); promptCard.addView(another);
        root.addView(promptCard);

        LinearLayout truth = card(); truth.addView(text("Before you respond",18,INK,true));
        truth.addView(text("1. Am I evaluating the strategy rather than judging her?\n\n2. Have I acknowledged something true?\n\n3. Did she ask for advice, or did I ask permission?\n\n4. Am I helping her think—or trying to make her agree?",16,INK,false));
        Button done = button("I PAUSED AND RESPONDED CONSTRUCTIVELY", GREEN); done.setOnClickListener(v -> incrementCount()); truth.addView(done); root.addView(truth);

        LinearLayout replacement = card(); replacement.addView(text("Useful replacements",18,INK,true));
        replacement.addView(text("Instead of: “You’re doing it wrong.”\nSay: “I don’t think this approach is producing the result you want.”\n\nInstead of immediately advising:\nAsk: “Would you like encouragement, help thinking it through, or my opinion?”",16,INK,false)); root.addView(replacement);

        LinearLayout reminder = card(); reminder.addView(text("Daily reminder",18,INK,true));
        Button set = button("SET REMINDER TIME", ACCENT); set.setOnClickListener(v -> chooseReminderTime()); reminder.addView(set);
        countText = text(countLabel(),18,INK,true); countText.setPadding(0,dp(14),0,0); reminder.addView(countText); root.addView(reminder);
        return scroll;
    }

    String currentMode="none";
    void showReinforcement(){ currentMode="reinforce"; content.setText("“"+reinforcement[rIndex]+"”"); }
    void showCoaching(){ currentMode="coach"; content.setText("“"+coaching[cIndex]+"”"); }
    void showFrustration(){ currentMode="frustrated"; content.setText("Pause. Lower your voice. Say: “I want to be helpful, but I’m getting frustrated. Do you want support, problem-solving, or space right now?”"); }
    void showNext(){ if(currentMode.equals("reinforce")){rIndex=(rIndex+1)%reinforcement.length;showReinforcement();} else if(currentMode.equals("coach")){cIndex=(cIndex+1)%coaching.length;showCoaching();} else showFrustration(); }
    void incrementCount(){ SharedPreferences p=getSharedPreferences(PREFS,MODE_PRIVATE); int n=p.getInt("count",0)+1; p.edit().putInt("count",n).apply(); countText.setText(countLabel()); Toast.makeText(this,"Constructive pause recorded",Toast.LENGTH_SHORT).show(); }
    String countLabel(){ return "Constructive pauses completed: "+getSharedPreferences(PREFS,MODE_PRIVATE).getInt("count",0); }

    void chooseReminderTime(){
        SharedPreferences p=getSharedPreferences(PREFS,MODE_PRIVATE); int h=p.getInt("hour",18), m=p.getInt("minute",0);
        new TimePickerDialog(this,(v,hour,minute)->{
            p.edit().putInt("hour",hour).putInt("minute",minute).putBoolean("enabled",true).apply();
            scheduleNextReminder(this); Toast.makeText(this,"Daily reminder set",Toast.LENGTH_SHORT).show();
        },h,m,false).show();
    }

    static void scheduleNextReminder(Context context){
        SharedPreferences p=context.getSharedPreferences(PREFS,MODE_PRIVATE); if(!p.getBoolean("enabled",false)) return;
        int h=p.getInt("hour",18),m=p.getInt("minute",0); Calendar c=Calendar.getInstance(); c.set(Calendar.HOUR_OF_DAY,h);c.set(Calendar.MINUTE,m);c.set(Calendar.SECOND,0);c.set(Calendar.MILLISECOND,0); if(c.getTimeInMillis()<=System.currentTimeMillis())c.add(Calendar.DAY_OF_YEAR,1);
        AlarmManager am=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE); Intent i=new Intent(context,ReminderReceiver.class); PendingIntent pi=PendingIntent.getBroadcast(context,77,i,PendingIntent.FLAG_UPDATE_CURRENT|PendingIntent.FLAG_IMMUTABLE);
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M) am.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,c.getTimeInMillis(),pi); else am.set(AlarmManager.RTC_WAKEUP,c.getTimeInMillis(),pi);
    }

    void requestNotificationPermission(){ if(Build.VERSION.SDK_INT>=33 && checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS)!=PackageManager.PERMISSION_GRANTED) requestPermissions(new String[]{Manifest.permission.POST_NOTIFICATIONS},42); }
    LinearLayout vertical(){ LinearLayout l=new LinearLayout(this);l.setOrientation(LinearLayout.VERTICAL);return l; }
    LinearLayout card(){ LinearLayout l=vertical(); l.setPadding(dp(18),dp(18),dp(18),dp(18)); LinearLayout.LayoutParams p=new LinearLayout.LayoutParams(-1,-2);p.setMargins(0,0,0,dp(16));l.setLayoutParams(p); android.graphics.drawable.GradientDrawable g=new android.graphics.drawable.GradientDrawable();g.setColor(Color.WHITE);g.setCornerRadius(dp(18));g.setStroke(dp(1),Color.rgb(222,230,235));l.setBackground(g);return l; }
    TextView text(String s,int sp,int color,boolean bold){ TextView t=new TextView(this);t.setText(s);t.setTextSize(sp);t.setTextColor(color);if(bold)t.setTypeface(null,1);t.setLineSpacing(0,1.15f);return t; }
    Button button(String s,int color){ Button b=new Button(this);b.setText(s);b.setTextColor(Color.WHITE);b.setTextSize(15);b.setAllCaps(false);android.graphics.drawable.GradientDrawable g=new android.graphics.drawable.GradientDrawable();g.setColor(color);g.setCornerRadius(dp(14));b.setBackground(g);LinearLayout.LayoutParams p=new LinearLayout.LayoutParams(-1,dp(52));p.setMargins(0,dp(12),0,0);b.setLayoutParams(p);return b; }
    int dp(int n){return (int)(n*getResources().getDisplayMetrics().density+.5f);}    
}
