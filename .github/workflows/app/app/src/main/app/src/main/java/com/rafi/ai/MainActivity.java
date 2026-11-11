
package com.rafi.ai;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.View;
import android.widget.*;
import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends Activity {

    TextView status, response;
    EditText input;
    TextToSpeech tts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setStatusBarColor(Color.rgb(8, 10, 22));
        getWindow().setNavigationBarColor(Color.rgb(8, 10, 22));

        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(24, 35, 24, 20);
        root.setGravity(Gravity.CENTER_HORIZONTAL);
        root.setBackgroundColor(Color.rgb(8, 10, 22));

        TextView title = new TextView(this);
        title.setText("RAFI AI");
        title.setTextColor(Color.WHITE);
        title.setTextSize(32);
        title.setTypeface(null, Typeface.BOLD);
        title.setGravity(Gravity.CENTER);
        root.addView(title);

        TextView subtitle = new TextView(this);
        subtitle.setText("NEXT GENERATION ASSISTANT");
        subtitle.setTextColor(Color.rgb(0, 210, 255));
        subtitle.setTextSize(12);
        subtitle.setGravity(Gravity.CENTER);
        root.addView(subtitle);

        TextView orb = new TextView(this);
        orb.setText("◉");
        orb.setTextSize(100);
        orb.setTextColor(Color.rgb(0, 210, 255));
        orb.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams orbParams =
            new LinearLayout.LayoutParams(-1, 220);
        orbParams.setMargins(0, 25, 0, 15);
        root.addView(orb, orbParams);

        status = new TextView(this);
        status.setText("SYSTEM ONLINE");
        status.setTextColor(Color.GREEN);
        status.setTextSize(14);
        status.setGravity(Gravity.CENTER);
        root.addView(status);

        response = new TextView(this);
        response.setText("Hello! I am RAFI AI.\nHow can I help you?");
        response.setTextColor(Color.WHITE);
        response.setTextSize(17);
        response.setGravity(Gravity.CENTER);
        response.setPadding(12, 25, 12, 25);
        root.addView(response);

        input = new EditText(this);
        input.setHint("Type your message...");
        input.setTextColor(Color.WHITE);
        input.setHintTextColor(Color.GRAY);
        input.setSingleLine(true);
        root.addView(input, new LinearLayout.LayoutParams(-1, 60));

        Button send = new Button(this);
        send.setText("SEND MESSAGE");
        root.addView(send, new LinearLayout.LayoutParams(-1, 55));

        Button voice = new Button(this);
        voice.setText("🎙 SPEAK TO RAFI AI");
        root.addView(voice, new LinearLayout.LayoutParams(-1, 55));

        setContentView(root);

        tts = new TextToSpeech(this, result -> {
            if (result == TextToSpeech.SUCCESS) {
                tts.setLanguage(new Locale("bn", "BD"));
            }
        });

        send.setOnClickListener(v -> {
            String message = input.getText().toString().trim();
            if (!message.isEmpty()) {
                respond(message);
                input.setText("");
            }
        });

        voice.setOnClickListener(v -> startVoice());
    }

    private void startVoice() {
        try {
            Intent intent = new Intent(
                RecognizerIntent.ACTION_RECOGNIZE_SPEECH
            );
            intent.putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
            );
            intent.putExtra(
                RecognizerIntent.EXTRA_LANGUAGE,
                "bn-BD"
            );
            startActivityForResult(intent, 100);
        } catch (Exception e) {
            status.setText("VOICE INPUT UNAVAILABLE");
        }
    }

    @Override
    protected void onActivityResult(
        int requestCode, int resultCode, Intent data
    ) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {
            ArrayList<String> results =
                data.getStringArrayListExtra(
                    RecognizerIntent.EXTRA_RESULTS
                );

            if (results != null && !results.isEmpty()) {
                String message = results.get(0);
                input.setText(message);
                respond(message);
            }
        }
    }

    private void respond(String message) {
        String reply;

        String lower = message.toLowerCase();

        if (lower.contains("hello") || lower.contains("হ্যালো")) {
            reply = "হ্যালো! আমি RAFI AI। তোমাকে কীভাবে সাহায্য করতে পারি?";
        } else if (lower.contains("তোমার নাম")) {
            reply = "আমার নাম RAFI AI NEXT GEN।";
        } else if (lower.contains("কেমন আছ")) {
            reply = "আমি ভালো আছি। তুমি কেমন আছো?";
        } else {
            reply = "তোমার মেসেজ পেয়েছি। আমার AI Brain সংযুক্ত হলে আরও বুদ্ধিমান উত্তর দিতে পারব।";
        }

        response.setText("You: " + message + "\n\nRAFI AI: " + reply);
        status.setText("READY");
        tts.speak(reply, TextToSpeech.QUEUE_FLUSH, null, "rafi_reply");
    }

    @Override
    protected void onDestroy() {
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();
    }
                          }
                  
