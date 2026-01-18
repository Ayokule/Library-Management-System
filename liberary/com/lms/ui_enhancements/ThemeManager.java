package com.lms.ui_enhancements;

import java.awt.Color;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;

public class ThemeManager {
   public static final Color PRIMARY_COLOR = new Color(41, 128, 185);
   public static final Color SECONDARY_COLOR = new Color(52, 152, 219);
   public static final Color SUCCESS_COLOR = new Color(46, 204, 113);
   public static final Color WARNING_COLOR = new Color(241, 196, 15);
   public static final Color DANGER_COLOR = new Color(231, 76, 60);
   public static final Color BACKGROUND_COLOR = new Color(236, 240, 241);
   public static final Color CARD_COLOR;
   public static final Color TEXT_PRIMARY;
   public static final Color TEXT_SECONDARY;

   public static void applyModernTheme() {
      try {
         UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
      } catch (Exception var1) {
         System.err.println("Failed to set system look and feel: " + var1.getMessage());
      }

      UIManager.put("Button.background", PRIMARY_COLOR);
      UIManager.put("Button.foreground", Color.WHITE);
      UIManager.put("Button.font", new Font("Segoe UI", 1, 12));
      UIManager.put("Button.border", BorderFactory.createEmptyBorder(10, 15, 10, 15));
      UIManager.put("Panel.background", BACKGROUND_COLOR);
      UIManager.put("TabbedPane.background", BACKGROUND_COLOR);
      UIManager.put("TabbedPane.foreground", TEXT_PRIMARY);
      UIManager.put("Table.background", CARD_COLOR);
      UIManager.put("Table.gridColor", new Color(220, 220, 220));
      UIManager.put("TableHeader.font", new Font("Segoe UI", 1, 12));
      UIManager.put("Label.font", new Font("Segoe UI", 0, 12));
      UIManager.put("Label.foreground", TEXT_PRIMARY);
      UIManager.put("TextField.background", CARD_COLOR);
      UIManager.put("TextField.border", BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)), BorderFactory.createEmptyBorder(5, 8, 5, 8)));
   }

   public static JPanel createModernPanel() {
      JPanel var0 = new JPanel();
      var0.setBackground(BACKGROUND_COLOR);
      var0.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
      return var0;
   }

   public static JPanel createCardPanel() {
      JPanel var0 = new JPanel();
      var0.setBackground(CARD_COLOR);
      var0.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(220, 220, 220), 1), BorderFactory.createEmptyBorder(15, 15, 15, 15)));
      return var0;
   }

   public static JLabel createTitleLabel(String var0) {
      JLabel var1 = new JLabel(var0);
      var1.setFont(new Font("Segoe UI", 1, 18));
      var1.setForeground(TEXT_PRIMARY);
      return var1;
   }

   public static JLabel createSubtitleLabel(String var0) {
      JLabel var1 = new JLabel(var0);
      var1.setFont(new Font("Segoe UI", 0, 14));
      var1.setForeground(TEXT_SECONDARY);
      return var1;
   }

   static {
      CARD_COLOR = Color.WHITE;
      TEXT_PRIMARY = new Color(44, 62, 80);
      TEXT_SECONDARY = new Color(127, 140, 141);
   }
}
