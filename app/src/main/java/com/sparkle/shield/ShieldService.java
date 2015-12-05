package com.sparkle.shield;

import android.accessibilityservice.AccessibilityService;
import android.annotation.TargetApi;
import android.os.Build;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

public class ShieldService extends AccessibilityService {

    private static final String TAG = ShieldService.class.getSimpleName();
    // private String[] PACKAGES = {"com.tencent.mm"};

    @Override
    protected void onServiceConnected() {
        Log.i(TAG, "accessibility enabled!");

        /*
        AccessibilityServiceInfo asi = new AccessibilityServiceInfo();
        asi.packageNames = PACKAGES;
        asi.eventTypes = AccessibilityEvent.TYPES_ALL_MASK;
        asi.feedbackType = AccessibilityServiceInfo.FEEDBACK_SPOKEN;
        asi.notificationTimeout = 100;
        setServiceInfo(asi);
        */
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        int eventType = event.getEventType();
        Log.i(TAG, eventType + ":" + event.toString());
        for (CharSequence cs : event.getText()) {
            if (cs != null) {
                Log.i(TAG, "event text: " + cs.toString());
            }
        }
        Log.i(TAG, "event class: " + event.getClassName());

        AccessibilityNodeInfo nodeInfo = event.getSource();
        if (nodeInfo != null) {
            Log.i(TAG, nodeInfo.toString());
        }

        AccessibilityNodeInfo rootNode = getRootInActiveWindow();
        traverseNode(rootNode, 0);
    }

    @Override
    public void onInterrupt() {
        Log.i(TAG, "onInterrupt");
    }

    private void traverseNode(AccessibilityNodeInfo node, int layer) {
        showNodeInfo(node, layer);
        if (node != null) {
            for (int i = 0; i < node.getChildCount(); i++) {
                traverseNode(node.getChild(i), layer + 1);
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void showNodeInfo(AccessibilityNodeInfo node, int layer) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < layer; i++) {
            sb.append("  ");
        }

        if (node == null) {
            Log.i(TAG, sb + "node is null");
        } else {
            Log.i(TAG, sb + "childWidget: " + node.getClassName());
            Log.i(TAG, sb + "showDialog: " + node.canOpenPopup());
            Log.i(TAG, sb + "text: " + node.getText());
            Log.i(TAG, sb + "windowID: " + node.getWindowId());
        }
    }

}
