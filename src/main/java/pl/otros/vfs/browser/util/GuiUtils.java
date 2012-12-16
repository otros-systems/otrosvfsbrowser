/*
 * Copyright (c) 2012. Krzysztof Otrebski
 * All right reserved
 */

package pl.otros.vfs.browser.util;

import org.pushingpixels.trident.Timeline;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class GuiUtils {
  public static Color getAverageColor(Color color1, Color color2) {
    return new Color(color1.getRed() / 2 + color2.getRed() / 2, color1.getGreen() / 2 + color2.getGreen() / 2, color1.getBlue() / 2 + color2.getBlue() / 2);
  }

  public static void addBlinkOnFocusGain(JComponent component){
    final Timeline timeline = new Timeline(component);
    timeline.addPropertyToInterpolate("background", component.getBackground(), GuiUtils.getAverageColor(component.getBackground(), component.getForeground()));
    timeline.setDuration(150);
    component.addFocusListener(new FocusListener() {
      @Override
      public void focusGained(FocusEvent e) {
        timeline.playLoop(2, Timeline.RepeatBehavior.REVERSE);
      }

      @Override
      public void focusLost(FocusEvent e) {
      }
    });

  }
}
