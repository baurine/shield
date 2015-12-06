package com.sparkle.shield;

import android.accessibilityservice.AccessibilityService;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import java.util.List;

public class ShieldService extends AccessibilityService {

    private static final String TAG = ShieldService.class.getSimpleName();

    private static final String NAME_RELATIVE_LAYOUT = "android.widget.RelativeLayout";
    private static final String NAME_TEXT_VIEW = "android.widget.TextView";

    private static final String STR_DISCOVER_CN = "[发现";
    private static final String STR_ME_CN = "我";

    public static final String NAME = TAG;

    private static final String KEY_SHIELD_WECHAT_DISCOVER = "KEY_SHIELD_WECHAT_DISCOVER";
    private boolean shieldWechatDiscover = true;

    public static void start(Context from, boolean shieldWechatDiscover) {
        Intent intent = new Intent(from, ShieldService.class);
        intent.putExtra(KEY_SHIELD_WECHAT_DISCOVER, shieldWechatDiscover);
        from.startService(intent);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            shieldWechatDiscover = intent.getBooleanExtra(KEY_SHIELD_WECHAT_DISCOVER, true);
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    protected void onServiceConnected() {
        Log.i(TAG, "accessibility enabled!");
    }

    @Override
    public void onInterrupt() {
        Log.i(TAG, "onInterrupt");
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        // AccessibilityEvent.getText() 返回一个不会为 null 的数组
        // 所以这里不用判 null, 如果 getText() 为空, 则 content 为 "[]"
        String content = event.getText().toString();
        Log.i(TAG, "event content: " + content);

        // 目前只支持中文
        if (content.startsWith(STR_DISCOVER_CN)) {
            if (event.getClassName().equals(NAME_RELATIVE_LAYOUT)) {
                Log.i(TAG, "click discover");
                if (shieldWechatDiscover) {
                    jumpToAboutMe(event.getSource());
                }
            }
        } else {
            // TODO: 处理点击 "朋友圈" 事件
        }
    }

    private void jumpToAboutMe(AccessibilityNodeInfo source) {
        if (source != null) {

            List<AccessibilityNodeInfo> nodes = source.getParent()
                    .findAccessibilityNodeInfosByText(STR_ME_CN);

            for (AccessibilityNodeInfo node : nodes) {
                if (node.getText() != null &&
                        node.getText().toString().equals(STR_ME_CN) &&
                        node.getClassName().equals(NAME_TEXT_VIEW)) {
                    node.getParent().performAction(AccessibilityNodeInfo.ACTION_CLICK);
                }
                node.recycle();
            }

            source.recycle();
        }
    }

}
