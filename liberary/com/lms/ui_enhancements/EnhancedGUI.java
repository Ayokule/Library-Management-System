package com.lms.ui_enhancements;

import com.lms.LibraryManager;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class EnhancedGUI extends JFrame {
   private LibraryManager manager = new LibraryManager();
   private JPanel contentArea;
   private CardLayout cardLayout;
   private DashboardPanel dashboardPanel;

   public EnhancedGUI() {
      this.manager.loadAll();
      this.initializeUI();
      this.setupComponents();
   }

   private void initializeUI() {
      this.setTitle("Library Management System - Enhanced UI");
      this.setDefaultCloseOperation(3);
      this.setSize(1000, 700);
      this.setLocationRelativeTo((Component)null);

      try {
         UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
      } catch (Exception var2) {
         var2.printStackTrace();
      }

   }

   private void setupComponents() {
      JPanel var1 = new JPanel(new BorderLayout());
      var1.setBackground(new Color(240, 240, 240));
      JPanel var2 = this.createNavigationPanel();
      this.cardLayout = new CardLayout();
      this.contentArea = new JPanel(this.cardLayout);
      this.contentArea.setBackground(new Color(240, 245, 250));
      this.dashboardPanel = new DashboardPanel(this.manager);
      this.dashboardPanel.setDashboardListener(new DashboardPanel.DashboardListener() {
         public void onBooksClicked() {
            EnhancedGUI.this.showBooksPanel();
         }

         public void onMembersClicked() {
            EnhancedGUI.this.showMembersPanel();
         }

         public void onLoansClicked() {
            EnhancedGUI.this.showLoansPanel();
         }
      });
      this.contentArea.add(this.dashboardPanel, "DASHBOARD");
      this.contentArea.add(this.createBooksPanel(), "BOOKS");
      this.contentArea.add(this.createMembersPanel(), "MEMBERS");
      this.contentArea.add(this.createLoansPanel(), "LOANS");
      var1.add(var2, "North");
      var1.add(this.contentArea, "Center");
      this.add(var1);
   }

   private JPanel createNavigationPanel() {
      JPanel var1 = new JPanel(new BorderLayout());
      var1.setBackground(new Color(41, 128, 185));
      var1.setPreferredSize(new Dimension(this.getWidth(), 60));
      var1.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
      JLabel var2 = new JLabel("ðŸ“š Library Management System");
      var2.setFont(new Font("Segoe UI", 1, 18));
      var2.setForeground(Color.WHITE);
      JPanel var3 = new JPanel(new FlowLayout(2, 10, 0));
      var3.setOpaque(false);
      JButton var4 = this.createNavButton("Dashboard");
      JButton var5 = this.createNavButton("Books");
      JButton var6 = this.createNavButton("Members");
      JButton var7 = this.createNavButton("Loans");
      var4.addActionListener((var1x) -> {
         this.cardLayout.show(this.contentArea, "DASHBOARD");
      });
      var5.addActionListener((var1x) -> {
         this.showBooksPanel();
      });
      var6.addActionListener((var1x) -> {
         this.showMembersPanel();
      });
      var7.addActionListener((var1x) -> {
         this.showLoansPanel();
      });
      var3.add(var4);
      var3.add(var5);
      var3.add(var6);
      var3.add(var7);
      var1.add(var2, "West");
      var1.add(var3, "East");
      return var1;
   }

   private JButton createNavButton(String var1) {
      final JButton var2 = new JButton(var1);
      var2.setFont(new Font("Segoe UI", 0, 12));
      var2.setForeground(Color.WHITE);
      var2.setBackground(new Color(52, 152, 219));
      var2.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
      var2.setFocusPainted(false);
      var2.setCursor(new Cursor(12));
      var2.addMouseListener(new MouseAdapter() {
         public void mouseEntered(MouseEvent var1) {
            var2.setBackground(new Color(41, 128, 185));
         }

         public void mouseExited(MouseEvent var1) {
            var2.setBackground(new Color(52, 152, 219));
         }
      });
      return var2;
   }

   private JPanel createBooksPanel() {
      JPanel var1 = new JPanel(new BorderLayout());
      var1.setBackground(new Color(240, 245, 250));
      var1.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
      JLabel var2 = new JLabel("BOOKS MANAGEMENT");
      var2.setFont(new Font("Arial", 1, 24));
      var2.setForeground(new Color(0, 102, 204));
      JPanel var3 = new JPanel();
      var3.setOpaque(false);
      var3.setLayout(new BoxLayout(var3, 1));
      int var4 = this.manager.getBooks().size();
      JLabel var5 = new JLabel("Total Books in System: " + var4);
      var5.setFont(new Font("Arial", 0, 14));
      var5.setForeground(new Color(60, 60, 60));
      JTextArea var6 = new JTextArea();
      var6.setEditable(false);
      var6.setFont(new Font("Monospaced", 0, 11));
      var6.setBackground(Color.WHITE);
      StringBuilder var7 = new StringBuilder();
      var7.append("BOOKS LIST\n");
      var7.append("=".repeat(70)).append("\n");
      var7.append(String.format("%-30s | %-20s | %s\n", "Title", "Author", "ISBN"));
      var7.append("=".repeat(70)).append("\n");
      this.manager.getBooks().forEach((var2x) -> {
         var7.append(String.format("%-30s | %-20s | %s\n", this.truncate(var2x.getTitle(), 28), this.truncate(var2x.getAuthor(), 18), var2x.getIsbn()));
      });
      var6.setText(var7.toString());
      JScrollPane var8 = new JScrollPane(var6);
      var1.add(var2, "North");
      var3.add(var5);
      var3.add(Box.createVerticalStrut(10));
      var3.add(var8);
      var1.add(var3, "Center");
      return var1;
   }

   private JPanel createMembersPanel() {
      JPanel var1 = new JPanel(new BorderLayout());
      var1.setBackground(new Color(240, 245, 250));
      var1.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
      JLabel var2 = new JLabel("MEMBERS MANAGEMENT");
      var2.setFont(new Font("Arial", 1, 24));
      var2.setForeground(new Color(0, 102, 204));
      JPanel var3 = new JPanel();
      var3.setOpaque(false);
      var3.setLayout(new BoxLayout(var3, 1));
      int var4 = this.manager.getMembers().size();
      JLabel var5 = new JLabel("Total Members in System: " + var4);
      var5.setFont(new Font("Arial", 0, 14));
      var5.setForeground(new Color(60, 60, 60));
      JTextArea var6 = new JTextArea();
      var6.setEditable(false);
      var6.setFont(new Font("Monospaced", 0, 11));
      var6.setBackground(Color.WHITE);
      StringBuilder var7 = new StringBuilder();
      var7.append("MEMBERS LIST\n");
      var7.append("=".repeat(70)).append("\n");
      var7.append(String.format("%-25s | %-20s | %s\n", "Member ID", "Name", "Email"));
      var7.append("=".repeat(70)).append("\n");
      this.manager.getMembers().forEach((var2x) -> {
         var7.append(String.format("%-25s | %-20s | %s\n", this.truncate(var2x.getId(), 23), this.truncate(var2x.getName(), 18), this.truncate(var2x.getEmail(), 20)));
      });
      var6.setText(var7.toString());
      JScrollPane var8 = new JScrollPane(var6);
      var1.add(var2, "North");
      var3.add(var5);
      var3.add(Box.createVerticalStrut(10));
      var3.add(var8);
      var1.add(var3, "Center");
      return var1;
   }

   private JPanel createLoansPanel() {
      JPanel var1 = new JPanel(new BorderLayout());
      var1.setBackground(new Color(240, 245, 250));
      var1.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
      JLabel var2 = new JLabel("LOANS TRACKING");
      var2.setFont(new Font("Arial", 1, 24));
      var2.setForeground(new Color(0, 102, 204));
      JPanel var3 = new JPanel();
      var3.setOpaque(false);
      var3.setLayout(new BoxLayout(var3, 1));
      long var4 = this.manager.getLoans().stream().filter((var0) -> {
         return var0.getReturnDate() == null;
      }).count();
      long var6 = this.manager.getLoans().stream().filter((var0) -> {
         return var0.getReturnDate() == null && var0.getLoanDate().plusDays(14L).isBefore(LocalDate.now());
      }).count();
      JLabel var8 = new JLabel(String.format("Active Loans: %d | Overdue: %d", var4, var6));
      var8.setFont(new Font("Arial", 0, 14));
      var8.setForeground(new Color(60, 60, 60));
      JTextArea var9 = new JTextArea();
      var9.setEditable(false);
      var9.setFont(new Font("Monospaced", 0, 10));
      var9.setBackground(Color.WHITE);
      StringBuilder var10 = new StringBuilder();
      var10.append("LOANS LIST\n");
      var10.append("=".repeat(90)).append("\n");
      var10.append(String.format("%-25s | %-25s | %-12s | %-12s | %s\n", "Member", "Book", "Loan Date", "Return Date", "Status"));
      var10.append("=".repeat(90)).append("\n");
      this.manager.getLoans().forEach((var2x) -> {
         String status = var2x.getReturnDate() == null ? (var2x.getLoanDate().plusDays(14L).isBefore(LocalDate.now()) ? "OVERDUE" : "ACTIVE") : "RETURNED";
         var10.append(String.format("%-25s | %-25s | %s | %s | %s\n", this.truncate(var2x.getMemberId(), 23), this.truncate(var2x.getBookIsbn(), 23), var2x.getLoanDate(), var2x.getReturnDate() != null ? var2x.getReturnDate().toString() : "N/A", status));
      });
      var9.setText(var10.toString());
      JScrollPane var11 = new JScrollPane(var9);
      var1.add(var2, "North");
      var3.add(var8);
      var3.add(Box.createVerticalStrut(10));
      var3.add(var11);
      var1.add(var3, "Center");
      return var1;
   }

   private void showBooksPanel() {
      this.cardLayout.show(this.contentArea, "BOOKS");
   }

   private void showMembersPanel() {
      this.cardLayout.show(this.contentArea, "MEMBERS");
   }

   private void showLoansPanel() {
      this.cardLayout.show(this.contentArea, "LOANS");
   }

   private String truncate(String var1, int var2) {
      if (var1 == null) {
         return "";
      } else {
         return var1.length() > var2 ? var1.substring(0, var2) : var1;
      }
   }

   public static void main(String[] args) {
      SwingUtilities.invokeLater(() -> {
         EnhancedGUI gui = new EnhancedGUI();
         gui.setVisible(true);
      });
   }
}
