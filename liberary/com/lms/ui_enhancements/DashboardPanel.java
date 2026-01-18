package com.lms.ui_enhancements;

import com.lms.LibraryManager;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class DashboardPanel extends JPanel {
   private LibraryManager manager;
   private JLabel totalBooksValueLabel;
   private JLabel activeMembersValueLabel;
   private JLabel booksOnLoanValueLabel;
   private JLabel overdueBooksValueLabel;
   private JTextArea summaryArea;
   private DashboardPanel.DashboardListener dashboardListener;

   public DashboardPanel(LibraryManager var1) {
      this.manager = var1;
      this.setLayout(new BorderLayout(10, 10));
      this.setBackground(new Color(240, 245, 250));
      this.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
      this.add(this.createHeaderPanel(), "North");
      this.add(this.createMainPanel(), "Center");
      this.add(this.createFooterPanel(), "South");
      this.updateStats();
   }

   public void setDashboardListener(DashboardPanel.DashboardListener var1) {
      this.dashboardListener = var1;
   }

   private JPanel createHeaderPanel() {
      JPanel var1 = new JPanel(new BorderLayout());
      var1.setOpaque(false);
      JLabel var2 = new JLabel("DASHBOARD");
      var2.setFont(new Font("Arial", 1, 28));
      var2.setForeground(new Color(0, 102, 204));
      JPanel var3 = new JPanel(new FlowLayout(0, 0, 0));
      var3.setOpaque(false);
      var3.add(var2);
      LocalDate var10002 = LocalDate.now();
      JLabel var4 = new JLabel("Today: " + var10002.format(DateTimeFormatter.ofPattern("MMM dd, yyyy")));
      var4.setFont(new Font("Arial", 0, 12));
      var4.setForeground(Color.GRAY);
      JButton var5 = new JButton("Refresh");
      var5.setFont(new Font("Arial", 0, 11));
      var5.setFocusPainted(false);
      var5.setBackground(new Color(0, 102, 204));
      var5.setForeground(Color.WHITE);
      var5.setCursor(new Cursor(12));
      var5.addActionListener((var1x) -> {
         this.updateStats();
      });
      var1.add(var3, "West");
      JPanel var6 = new JPanel(new FlowLayout(2, 15, 0));
      var6.setOpaque(false);
      var6.add(var4);
      var6.add(var5);
      var1.add(var6, "East");
      return var1;
   }

   private JPanel createMainPanel() {
      JPanel var1 = new JPanel(new BorderLayout(10, 10));
      var1.setOpaque(false);
      var1.add(this.createQuickStatsPanel(), "North");
      var1.add(this.createSummaryPanel(), "Center");
      return var1;
   }

   private JPanel createQuickStatsPanel() {
      JPanel var1 = new JPanel(new GridLayout(1, 4, 15, 0));
      var1.setOpaque(false);
      this.totalBooksValueLabel = new JLabel("0");
      var1.add(this.createClickableStatCard("Total Books", this.totalBooksValueLabel, new Color(52, 152, 219), () -> {
         if (this.dashboardListener != null) {
            this.dashboardListener.onBooksClicked();
         }

      }));
      this.activeMembersValueLabel = new JLabel("0");
      var1.add(this.createClickableStatCard("Active Members", this.activeMembersValueLabel, new Color(46, 204, 113), () -> {
         if (this.dashboardListener != null) {
            this.dashboardListener.onMembersClicked();
         }

      }));
      this.booksOnLoanValueLabel = new JLabel("0");
      var1.add(this.createClickableStatCard("Books on Loan", this.booksOnLoanValueLabel, new Color(241, 196, 15), () -> {
         if (this.dashboardListener != null) {
            this.dashboardListener.onLoansClicked();
         }

      }));
      this.overdueBooksValueLabel = new JLabel("0");
      var1.add(this.createClickableStatCard("Overdue Books", this.overdueBooksValueLabel, new Color(231, 76, 60), () -> {
         if (this.dashboardListener != null) {
            this.dashboardListener.onLoansClicked();
         }

      }));
      return var1;
   }

   private JPanel createClickableStatCard(String var1, JLabel var2, final Color var3, final Runnable var4) {
      final JPanel var5 = new JPanel(new GridLayout(2, 1, 5, 5));
      var5.setBackground(Color.WHITE);
      var5.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(var3, 2), BorderFactory.createEmptyBorder(10, 10, 10, 10)));
      var5.setCursor(new Cursor(12));
      JLabel var6 = new JLabel(var1, 0);
      var6.setFont(new Font("Arial", 0, 11));
      var6.setForeground(Color.GRAY);
      var2.setFont(new Font("Arial", 1, 36));
      var2.setForeground(var3);
      var2.setHorizontalAlignment(0);
      var5.add(var6);
      var5.add(var2);
      var5.addMouseListener(new MouseAdapter() {
         public void mouseEntered(MouseEvent var1) {
            var5.setBackground(new Color(245, 245, 245));
            var5.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(var3, 3), BorderFactory.createEmptyBorder(10, 10, 10, 10)));
         }

         public void mouseExited(MouseEvent var1) {
            var5.setBackground(Color.WHITE);
            var5.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(var3, 2), BorderFactory.createEmptyBorder(10, 10, 10, 10)));
         }

         public void mouseClicked(MouseEvent var1) {
            var4.run();
         }
      });
      return var5;
   }

   private JPanel createSummaryPanel() {
      JPanel var1 = new JPanel(new BorderLayout());
      var1.setOpaque(false);
      var1.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(0, 102, 204), 1), "LIBRARY SUMMARY", 1, 2, new Font("Arial", 1, 12), new Color(0, 102, 204)));
      this.summaryArea = new JTextArea();
      this.summaryArea.setEditable(false);
      this.summaryArea.setFont(new Font("Monospaced", 0, 11));
      this.summaryArea.setBackground(Color.WHITE);
      this.summaryArea.setForeground(new Color(50, 50, 50));
      this.summaryArea.setMargin(new Insets(10, 10, 10, 10));
      this.summaryArea.setLineWrap(true);
      this.summaryArea.setWrapStyleWord(true);
      JScrollPane var2 = new JScrollPane(this.summaryArea);
      var2.setBorder(BorderFactory.createEmptyBorder());
      var1.add(var2, "Center");
      return var1;
   }

   private JPanel createFooterPanel() {
      JPanel var1 = new JPanel(new BorderLayout());
      var1.setOpaque(false);
      JLabel var2 = new JLabel("Status: System Ready | Click on any card to navigate");
      var2.setFont(new Font("Arial", 0, 10));
      var2.setForeground(new Color(100, 100, 100));
      var1.add(var2, "West");
      return var1;
   }

   public void refresh() {
      this.updateStats();
   }

   private void updateStats() {
      int var1 = this.manager.getBooks().size();
      int var2 = this.manager.getMembers().size();
      long var3 = this.manager.getLoans().stream().filter((var0) -> {
         return var0.getReturnDate() == null;
      }).count();
      long var5 = this.manager.getLoans().stream().filter((var0) -> {
         return var0.getReturnDate() == null && var0.getLoanDate().plusDays(14L).isBefore(LocalDate.now());
      }).count();
      this.totalBooksValueLabel.setText(String.valueOf(var1));
      this.activeMembersValueLabel.setText(String.valueOf(var2));
      this.booksOnLoanValueLabel.setText(String.valueOf(var3));
      this.overdueBooksValueLabel.setText(String.valueOf(var5));
      StringBuilder var7 = new StringBuilder();
      var7.append("LIBRARY OVERVIEW\n");
      var7.append("=".repeat(50)).append("\n\n");
      var7.append(String.format("BOOKS\n"));
      var7.append(String.format("  Total Books: %d\n", var1));
      var7.append(String.format("  Available: %d\n", var1 - (int)var3));
      var7.append(String.format("  On Loan: %d\n\n", (int)var3));
      var7.append(String.format("MEMBERS\n"));
      var7.append(String.format("  Active Members: %d\n\n", var2));
      var7.append(String.format("LOANS\n"));
      var7.append(String.format("  Active Loans: %d\n", this.manager.getLoans().stream().filter((var0) -> {
         return var0.getReturnDate() == null;
      }).count()));
      var7.append(String.format("  Overdue Books: %d\n", var5));
      var7.append(String.format("  Returned: %d\n", this.manager.getLoans().stream().filter((var0) -> {
         return var0.getReturnDate() != null;
      }).count()));
      var7.append("\n" + "=".repeat(50));
      LocalDate var10001 = LocalDate.now();
      var7.append("\nLast Updated: " + var10001.format(DateTimeFormatter.ofPattern("MMM dd, yyyy")));
      this.summaryArea.setText(var7.toString());
      this.revalidate();
      this.repaint();
   }

   public interface DashboardListener {
      void onBooksClicked();

      void onMembersClicked();

      void onLoansClicked();
   }
}
