package com.wimy.smsforward;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.provider.Telephony;
import android.telephony.SmsMessage;

public class SMSReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		SmsMessage[] sms_messages = Telephony.Sms.Intents.getMessagesFromIntent(intent);
		
		for ( SmsMessage message : sms_messages) {
			String message_body = message.getMessageBody();
			if (message_body != null) {
				MainActivity.onReceivedSMS(context, message_body);
			}
		}
	}
}
