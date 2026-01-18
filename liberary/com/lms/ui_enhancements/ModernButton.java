package com.lms.ui_enhancements;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JButton;

public class ModernButton extends JButton {
   private Color normalColor = new Color(41, 128, 185);
   private Color hoverColor = new Color(52, 152, 219);
   private Color pressedColor = new Color(31, 97, 141);

   public ModernButton(String var1) {
      super(var1);
      this.setupButton();
   }

   public ModernButton(String var1, Color var2) {
      super(var1);
      this.normalColor = var2;
      this.hoverColor = var2.brighter();
      this.pressedColor = var2.darker();
      this.setupButton();
   }

   private void setupButton() {
      this.setFocusPainted(false);
      this.setBorderPainted(false);
      this.setContentAreaFilled(false);
      this.setOpaque(true);
      this.setForeground(Color.WHITE);
      this.setFont(new Font("Segoe UI", 1, 12));
      this.setBackground(this.normalColor);
      this.addMouseListener(new MouseAdapter() {
         public void mouseEntered(MouseEvent var1) {
            ModernButton.this.setBackground(ModernButton.this.hoverColor);
            ModernButton.this.setCursor(Cursor.getPredefinedCursor(12));
         }

         public void mouseExited(MouseEvent var1) {
            ModernButton.this.setBackground(ModernButton.this.normalColor);
         }

         public void mousePressed(MouseEvent var1) {
            ModernButton.this.setBackground(ModernButton.this.pressedColor);
         }

         public void mouseReleased(MouseEvent var1) {
            ModernButton.this.setBackground(ModernButton.this.hoverColor);
         }
      });
   }

   protected void paintComponent(Graphics var1) {
      Graphics2D var2 = (Graphics2D)var1.create();
      var2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
      var2.setColor(this.getBackground());
      var2.fillRoundRect(0, 0, this.getWidth(), this.getHeight(), 10, 10);
      super.paintComponent(var2);
      var2.dispose();
   }

   protected void paintBorder(Graphics var1) {
   }
}
