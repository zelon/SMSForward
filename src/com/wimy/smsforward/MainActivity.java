package com.wimy.smsforward;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.widget.EditText;

public class MainActivity extends Activity {

    private static String preference_string = "default_setting";
    private static String key_to = "key_to";
    private static String key_keyword = "key_keyword";
	private static String forward_prefix = "[auto]";

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        restoreSetting();
    }

	@Override
	protected void onPause() {
		saveSetting();
		super.onPause();
	}

	private static SharedPreferences getSetting(Context context) {
		return context.getSharedPreferences(preference_string, Context.MODE_PRIVATE);
	}

	public static void onReceivedSMS(Context context, String message) {
		if (message == null) {
			return;
		}
		if (message.startsWith(forward_prefix)) {
			return;
		}
		String to = getSetting(context).getString(key_to, null);
		if (to == null) {
			return;
		}
		
		if (isMessageForForwarding(context, message)) {
			String to_be_sent_message = forward_prefix.concat(message);
			SmsManager.getDefault().sendTextMessage(to, null, to_be_sent_message, null, null);
		}
	}
	
	public static boolean isMessageForForwarding(Context context, String message) {
		SharedPreferences setting = getSetting(context);
		
		String raw_keywords = setting.getString(key_keyword, null);
		if (raw_keywords == null) {
			return false;
		}
		
		String[] keywords = raw_keywords.split(",");
		if (keywords == null || keywords.length <= 0) {
			return false;
		}
		for (String keyword : keywords) {
			if (message.contains(keyword)) {
				return true;
			}
		}
		return false;
	}

	private void saveSetting() {
		final SharedPreferences preference = getSetting(this);
		SharedPreferences.Editor editor = preference.edit();
		editor.putString(key_to, getTextString(R.id.editText_to));
		editor.putString(key_keyword, getTextString(R.id.editText_keywords));
		editor.commit();
	}
	
	private void restoreSetting() {
		final SharedPreferences preference = getSetting(this);
		
		String to = preference.getString(key_to, null);
		if (to != null) {
			setText(R.id.editText_to, to);
		}
		String keyword = preference.getString(key_keyword,  null);
		if (keyword != null) {
			setText(R.id.editText_keywords, keyword);
		}
	}

	private void setText(int id, String text) {
		final EditText edit_text = (EditText)findViewById(id);
		assert(edit_text != null);
		edit_text.setText(text);
	}
	
	private String getTextString(int id) {
		final EditText edit_text = (EditText)findViewById(id);
		assert(edit_text != null);
		return edit_text.getText().toString();
	}
}
