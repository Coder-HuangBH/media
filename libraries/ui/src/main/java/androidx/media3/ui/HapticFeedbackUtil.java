package androidx.media3.ui;

import android.content.Context;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.os.VibratorManager;

public class HapticFeedbackUtil {

  private static final long LONG_PRESS_DURATION_MS = 30;

  public static void performLongPressHaptic(Context context) {
    try {
      Vibrator vibrator = getVibrator(context);
      if (vibrator == null || !vibrator.hasVibrator()) {
        return;
      }
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        vibrator.vibrate(VibrationEffect.createOneShot(LONG_PRESS_DURATION_MS, VibrationEffect.DEFAULT_AMPLITUDE));
      } else {
        vibrator.vibrate(LONG_PRESS_DURATION_MS);
      }
    } catch (SecurityException ignored) {
    }
  }

  @SuppressWarnings("deprecation")
  private static Vibrator getVibrator(Context context) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
      VibratorManager vibratorManager =
          (VibratorManager) context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE);
      return vibratorManager != null ? vibratorManager.getDefaultVibrator() : null;
    } else {
      return (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
    }
  }
}
