package com.lms.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JWindow;
import javax.swing.SwingUtilities;

public class SplashScreen extends JWindow {
   private JProgressBar progressBar;
   private long duration;

   public SplashScreen() {
      this(5000L);
   }

   public SplashScreen(long var1) {
      this.duration = var1;
      JPanel var3 = new JPanel(new BorderLayout(10, 10));
      var3.setBackground(new Color(240, 245, 250));
      var3.setBorder(BorderFactory.createLineBorder(new Color(0, 102, 204), 2));
      ImageIcon var4 = null;

      JLabel var6;
      try {
         var4 = new ImageIcon("image/images.png");
         Image var5 = var4.getImage().getScaledInstance(120, 120, 4);
         var6 = new JLabel(new ImageIcon(var5));
         var6.setHorizontalAlignment(0);
         var3.add(var6, "North");
      } catch (Exception var11) {
      }

      JLabel var12 = new JLabel("LIBRARY MANAGEMENT SYSTEM", 0);
      var12.setFont(new Font("Arial", 1, 22));
      var12.setForeground(new Color(0, 102, 204));
      var6 = new JLabel("Version 1.0.0", 0);
      var6.setFont(new Font("Arial", 0, 12));
      var6.setForeground(Color.GRAY);
      JLabel var7 = new JLabel("Developed by: Alonge Sodiq Ayomide", 0);
      var7.setFont(new Font("Arial", 0, 11));
      var7.setForeground(new Color(100, 100, 100));
      JPanel var8 = new JPanel(new GridLayout(3, 1, 5, 5));
      var8.setOpaque(false);
      var8.add(var12);
      var8.add(var6);
      var8.add(var7);
      var3.add(var8, "Center");
      JPanel var9 = new JPanel(new BorderLayout(5, 5));
      var9.setOpaque(false);
      JLabel var10 = new JLabel("Loading...", 0);
      var10.setFont(new Font("Arial", 0, 11));
      var10.setForeground(new Color(0, 102, 204));
      this.progressBar = new JProgressBar(0, 100);
      this.progressBar.setStringPainted(true);
      this.progressBar.setForeground(new Color(0, 102, 204));
      this.progressBar.setBackground(new Color(220, 230, 240));
      var9.add(var10, "North");
      var9.add(this.progressBar, "South");
      var3.add(var9, "South");
      this.setContentPane(var3);
      this.setSize(500, 350);
      this.setLocationRelativeTo((Component)null);
   }

   public void showSplash(Runnable var1) {
      this.setVisible(true);
      (new Thread(() -> {
         this.animateProgress(this.duration);
         SwingUtilities.invokeLater(() -> {
            this.setVisible(false);
            this.dispose();
            if (var1 != null) {
               var1.run();
            }

         });
      })).start();
   }

   private void animateProgress(long var1) {
      long var3 = System.currentTimeMillis();
      byte var5 = 100;
      long var10000 = var1 / (long)var5;

      for(int var8 = 0; var8 <= 100; ++var8) {
         final int progress = var8;
         SwingUtilities.invokeLater(() -> {
            this.progressBar.setValue(progress);
         });

         try {
            long var10 = System.currentTimeMillis() - var3;
            long var12 = (long)var8 * var1 / 100L;
            long var14 = Math.max(1L, var12 - var10);
            Thread.sleep(var14);
         } catch (InterruptedException var16) {
         }
      }

   }
}
