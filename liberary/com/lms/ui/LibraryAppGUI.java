package com.lms.ui;

import com.lms.Book;
import com.lms.LibraryManager;
import com.lms.Loan;
import com.lms.Member;
import com.lms.ui_enhancements.DashboardPanel;
import com.lms.ui.GlobalSearchDialog; // Import the new GlobalSearchDialog
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.PrintWriter;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.Stack;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class LibraryAppGUI extends JFrame {
   private Stack<List<Book>> undoStack = new Stack();
   private Stack<List<Book>> redoStack = new Stack();
   private LibraryManager manager = new LibraryManager();
   private JTable booksTable;
   private DefaultTableModel booksTableModel;
   private JTable membersTable;
   private DefaultTableModel membersTableModel;
   private JTable loansTable;
   private DefaultTableModel loansTableModel;
   private DashboardPanel dashboardPanel;
   private JTabbedPane tabbedPane;

   public static void main(String[] args) {
      SwingUtilities.invokeLater(() -> {
         SplashScreen splash = new SplashScreen(4000L);
         splash.showSplash(() -> {
            LibraryAppGUI app = new LibraryAppGUI();
            app.setVisible(true);
         });
      });
   }

   public LibraryAppGUI() {
      try {
         this.manager.loadAll();
      } catch (Exception var18) {
         System.err.println("Error loading data on startup: " + var18.getMessage());
         JOptionPane.showMessageDialog(this, "Warning: Could not load existing data.\nThe application will start with an empty library.\n\nError: " + var18.getMessage(), "Data Loading Warning", 2);
      }

      this.setTitle("Library Management System");
      this.setDefaultCloseOperation(3);
      this.setSize(800, 600);
      this.setLocationRelativeTo((Component)null);

      try {
         ImageIcon var1 = new ImageIcon("image/images.png");
         Image var2 = var1.getImage();
         Image var3 = var2.getScaledInstance(128, 128, 4);
         this.setIconImage(var3);
      } catch (Exception var17) {
         System.err.println("App icon not found: " + var17.getMessage());
      }

      JMenuBar var19 = new JMenuBar();
      JMenu var20 = new JMenu("File");
      JMenuItem var21 = new JMenuItem("New (Ctrl+N)");
      JMenuItem var4 = new JMenuItem("Save (Ctrl+S)");
      JMenuItem var5 = new JMenuItem("Search (Ctrl+F)");
      JMenuItem var6 = new JMenuItem("Export Books to CSV");
      JMenuItem var7 = new JMenuItem("Import Books from CSV");
      JMenuItem var8 = new JMenuItem("Print Book Catalog");
      JMenuItem var9 = new JMenuItem("Print Loan History");
      var20.add(var21);
      var20.add(var4);
      var20.add(var5);
      var20.addSeparator();
      var20.add(var6);
      var20.add(var7);
      var20.addSeparator();
      var20.add(var8);
      var20.add(var9);
      var6.addActionListener((var1x) -> {
         this.exportBooksToCSV();
      });
      var7.addActionListener((var1x) -> {
         this.importBooksFromCSV();
      });
      var8.addActionListener((var1x) -> {
         this.printBookCatalog();
      });
      var9.addActionListener((var1x) -> {
         this.printLoanHistory();
      });
      var19.add(var20);
      JMenu var10 = new JMenu("Edit");
      JMenuItem var11 = new JMenuItem("Undo (Ctrl+Z)");
      JMenuItem var12 = new JMenuItem("Redo (Ctrl+Y)");
      var10.add(var11);
      var10.add(var12);
      var19.add(var10);
      JMenu var13 = new JMenu("Help");
      JMenuItem var14 = new JMenuItem("About");
      var13.add(var14);
      var19.add(var13);
      var19.setBackground(new Color(41, 128, 185));
      var19.setForeground(Color.WHITE);
      var19.setOpaque(true);

      for(int var15 = 0; var15 < var19.getMenuCount(); ++var15) {
         JMenu var16 = var19.getMenu(var15);
         var16.setForeground(Color.WHITE);
      }

      this.setJMenuBar(var19);
      var4.setAccelerator(KeyStroke.getKeyStroke("control S"));
      var21.setAccelerator(KeyStroke.getKeyStroke("control N"));
      var5.setAccelerator(KeyStroke.getKeyStroke("control F"));
      var11.setAccelerator(KeyStroke.getKeyStroke("control Z"));
      var12.setAccelerator(KeyStroke.getKeyStroke("control Y"));
      var4.addActionListener((var1x) -> {
         this.manager.saveAll();
      });
      var21.addActionListener((var1x) -> {
         this.showNewDialog();
      });
      var5.addActionListener((var1x) -> {
         this.showSearchDialog();
      });
      var11.addActionListener((var1x) -> {
         this.undoBooks();
      });
      var12.addActionListener((var1x) -> {
         this.redoBooks();
      });
      var14.addActionListener((var1x) -> {
         this.showAboutDialog();
      });
      JRootPane var22 = this.getRootPane();
      var22.getInputMap(2).put(KeyStroke.getKeyStroke("control S"), "save");
      var22.getActionMap().put("save", new AbstractAction() {
         public void actionPerformed(ActionEvent var1) {
            LibraryAppGUI.this.manager.saveAll();
         }
      });
      var22.getInputMap(2).put(KeyStroke.getKeyStroke("control N"), "new");
      var22.getActionMap().put("new", new AbstractAction() {
         public void actionPerformed(ActionEvent var1) {
            LibraryAppGUI.this.showNewDialog();
         }
      });
      var22.getInputMap(2).put(KeyStroke.getKeyStroke("control F"), "search");
      var22.getActionMap().put("search", new AbstractAction() {
         public void actionPerformed(ActionEvent var1) {
            LibraryAppGUI.this.showSearchDialog();
         }
      });
      var22.getInputMap(2).put(KeyStroke.getKeyStroke("control Z"), "undo");
      var22.getActionMap().put("undo", new AbstractAction() {
         public void actionPerformed(ActionEvent var1) {
            LibraryAppGUI.this.undoBooks();
         }
      });
      var22.getInputMap(2).put(KeyStroke.getKeyStroke("control Y"), "redo");
      var22.getActionMap().put("redo", new AbstractAction() {
         public void actionPerformed(ActionEvent var1) {
            LibraryAppGUI.this.redoBooks();
         }
      });
      this.tabbedPane = new JTabbedPane();
      this.dashboardPanel = new DashboardPanel(this.manager);
      this.dashboardPanel.setDashboardListener(new DashboardPanel.DashboardListener() {
         public void onBooksClicked() {
            LibraryAppGUI.this.tabbedPane.setSelectedIndex(1);
         }

         public void onMembersClicked() {
            LibraryAppGUI.this.tabbedPane.setSelectedIndex(2);
         }

         public void onLoansClicked() {
            LibraryAppGUI.this.tabbedPane.setSelectedIndex(3);
         }
      });
      this.tabbedPane.addTab("Dashboard", this.dashboardPanel);
      JPanel var23 = this.createBooksPanel();
      this.tabbedPane.addTab("Books", var23);
      this.tabbedPane.addTab("Members", this.createMembersPanel());
      this.tabbedPane.addTab("Loans", this.createLoansPanel());
      this.add(this.tabbedPane);
      this.booksTable.getInputMap(0).put(KeyStroke.getKeyStroke("DELETE"), "deleteBookRow");
      this.booksTable.getActionMap().put("deleteBookRow", new AbstractAction() {
         public void actionPerformed(ActionEvent var1) {
            LibraryAppGUI.this.deleteBook();
         }
      });
      this.membersTable.getInputMap(0).put(KeyStroke.getKeyStroke("DELETE"), "deleteMemberRow");
      this.membersTable.getActionMap().put("deleteMemberRow", new AbstractAction() {
         public void actionPerformed(ActionEvent var1) {
            LibraryAppGUI.this.deleteMember();
         }
      });
      this.loansTable.getInputMap(0).put(KeyStroke.getKeyStroke("DELETE"), "deleteLoanRow");
      this.loansTable.getActionMap().put("deleteLoanRow", new AbstractAction() {
         public void actionPerformed(ActionEvent var1) {
            LibraryAppGUI.this.deleteLoan();
         }
      });
   }

   private JPanel createBooksPanel() {
      JPanel var1 = new JPanel(new BorderLayout());
      var1.setBackground(new Color(245, 245, 245));
      JPanel var2 = new JPanel();
      var2.setBackground(new Color(41, 128, 185));
      var2.setPreferredSize(new Dimension(0, 60));
      JLabel var3 = new JLabel("Books Management");
      var3.setFont(new Font("Segoe UI", 1, 22));
      var3.setForeground(Color.WHITE);
      var2.add(var3);
      var1.add(var2, "North");
      String[] var4 = new String[]{"Book Unique ID", "Title", "Author", "Book ID/ISBN"};
      this.booksTableModel = new DefaultTableModel(var4, 0);
      this.booksTable = new JTable(this.booksTableModel);
      this.booksTable.setFont(new Font("Segoe UI", 0, 12));
      this.booksTable.setRowHeight(28);
      this.booksTable.setGridColor(new Color(200, 200, 200));
      this.booksTable.setShowGrid(true);
      JTableHeader var5 = this.booksTable.getTableHeader();
      var5.setBackground(new Color(52, 152, 219));
      var5.setForeground(Color.WHITE);
      var5.setFont(new Font("Segoe UI", 1, 13));
      var5.setPreferredSize(new Dimension(0, 35));
      this.booksTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
         public Component getTableCellRendererComponent(JTable var1, Object var2, boolean var3, boolean var4, int var5, int var6) {
            Component var7 = super.getTableCellRendererComponent(var1, var2, var3, var4, var5, var6);
            if (var3) {
               var7.setBackground(new Color(41, 128, 185));
               var7.setForeground(Color.WHITE);
            } else if (var5 % 2 == 0) {
               var7.setBackground(new Color(255, 255, 255));
               var7.setForeground(Color.BLACK);
            } else {
               var7.setBackground(new Color(240, 248, 255));
               var7.setForeground(Color.BLACK);
            }

            return var7;
         }
      });
      this.refreshBooksTable();
      JScrollPane var6 = new JScrollPane(this.booksTable);
      var6.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
      var1.add(var6, "Center");
      JPanel var7 = new JPanel();
      var7.setBackground(new Color(245, 245, 245));
      var7.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));
      var7.setLayout(new FlowLayout(1, 15, 0));
      JButton var8 = this.createStyledButton("+ Add Book", new Color(39, 174, 96), new Color(46, 204, 113));
      JButton var9 = this.createStyledButton("Edit Book", new Color(41, 128, 185), new Color(52, 152, 219));
      JButton var10 = this.createStyledButton("Delete Book", new Color(192, 57, 43), new Color(231, 76, 60));
      var7.add(var8);
      var7.add(var9);
      var7.add(var10);
      var1.add(var7, "South");
      var8.addActionListener((var1x) -> {
         this.addBookDialog();
      });
      var9.addActionListener((var1x) -> {
         this.editBookDialog();
      });
      var10.addActionListener((var1x) -> {
         this.deleteBook();
      });
      return var1;
   }

   private JButton createStyledButton(String var1, final Color var2, final Color var3) {
      final JButton var4 = new JButton(var1);
      var4.setFont(new Font("Segoe UI", 1, 12));
      var4.setForeground(Color.WHITE);
      var4.setBackground(var2);
      var4.setOpaque(true);
      var4.setBorderPainted(false);
      var4.setFocusPainted(false);
      var4.setPreferredSize(new Dimension(140, 40));
      var4.setCursor(new Cursor(12));
      var4.addMouseListener(new MouseAdapter() {
         public void mouseEntered(MouseEvent var1) {
            var4.setBackground(var3);
         }

         public void mouseExited(MouseEvent var1) {
            var4.setBackground(var2);
         }
      });
      return var4;
   }

   private void refreshBooksTable() {
      this.booksTableModel.setRowCount(0);
      List var1 = this.manager.getBooks();
      Iterator var2 = var1.iterator();

      while(var2.hasNext()) {
         Book var3 = (Book)var2.next();
         this.booksTableModel.addRow(new Object[]{var3.getUniqueId(), var3.getTitle(), var3.getAuthor(), var3.getIsbn()});
      }

   }

   private LocalDate parseDateFlexible(String var1) throws Exception {
      var1 = var1.trim();

      try {
         return LocalDate.parse(var1);
      } catch (Exception var11) {
         String[] var2 = var1.split("[/\\-\\s.]+");
         if (var2.length != 3) {
            throw new Exception("Date must have 3 parts separated by /, -, or space (e.g., 02-3-2025 or 2025-01-15)");
         } else {
            try {
               int var3 = Integer.parseInt(var2[0]);
               int var4 = Integer.parseInt(var2[1]);
               int var5 = Integer.parseInt(var2[2]);
               boolean var6 = true;
               boolean var7 = true;
               boolean var8 = true;
               int var12;
               int var13;
               int var14;
               if (var5 > 31 && var5 >= 1000) {
                  var14 = var5;
                  if (var3 > 12) {
                     var12 = var3;
                     var13 = var4;
                  } else if (var4 > 12) {
                     var13 = var3;
                     var12 = var4;
                  } else {
                     var12 = var3;
                     var13 = var4;
                  }
               } else if (var3 > 31 && var3 >= 1000) {
                  var14 = var3;
                  var13 = var4;
                  var12 = var5;
               } else {
                  if (var3 > 12) {
                     var12 = var3;
                     var13 = var4;
                  } else if (var4 > 12) {
                     var13 = var3;
                     var12 = var4;
                  } else {
                     var12 = var3;
                     var13 = var4;
                  }

                  if (var5 < 100) {
                     if (var5 < 30) {
                        var14 = 2000 + var5;
                     } else {
                        var14 = 1900 + var5;
                     }
                  } else {
                     var14 = var5;
                  }
               }

               if (var13 >= 1 && var13 <= 12) {
                  if (var12 >= 1 && var12 <= 31) {
                     if (var14 >= 1900 && var14 <= 2100) {
                        return LocalDate.of(var14, var13, var12);
                     } else {
                        throw new Exception("Invalid year: " + var14 + " (must be 1900-2100)");
                     }
                  } else {
                     throw new Exception("Invalid day: " + var12 + " (must be 1-31)");
                  }
               } else {
                  throw new Exception("Invalid month: " + var13 + " (must be 1-12)");
               }
            } catch (NumberFormatException var9) {
               throw new Exception("Date parts must be numbers. Received: " + String.join(", ", var2));
            } catch (DateTimeException var10) {
               throw new Exception("Invalid date: " + var10.getMessage());
            }
         }
      }
   }

   private String getNextBookUniqueId() {
      int var1 = this.manager.getBooks().size() + 1;
      String var2 = String.format("BK%04d", var1);
      return var2;
   }

   private void addRealTimeValidation(final JTextField var1, final String var2, final JLabel var3) {
      var1.getDocument().addDocumentListener(new DocumentListener() {
         public void insertUpdate(DocumentEvent var1x) {
            this.validateField();
         }

         public void removeUpdate(DocumentEvent var1x) {
            this.validateField();
         }

         public void changedUpdate(DocumentEvent var1x) {
            this.validateField();
         }

         private void validateField() {
            String var1x = var1.getText().trim();
            String var2x = "";
            String var3x = var2;
            byte var4 = -1;
            switch(var3x.hashCode()) {
            case -1406328437:
               if (var3x.equals("author")) {
                  var4 = 1;
               }
               break;
            case 3241718:
               if (var3x.equals("isbn")) {
                  var4 = 2;
               }
               break;
            case 110371416:
               if (var3x.equals("title")) {
                  var4 = 0;
               }
            }

            switch(var4) {
            case 0:
               if (var1x.isEmpty()) {
                  var2x = "Title is required";
               }
               break;
            case 1:
               if (var1x.isEmpty()) {
                  var2x = "Author is required";
               } else if (!var1x.matches("[A-Za-z\\s.,]+")) {
                  var2x = "Use letters and spaces only";
               }
               break;
            case 2:
               if (!var1x.isEmpty()) {
                  String var5 = var1x.replaceAll("[-\\s]", "");
                  if (!var5.matches("\\d+")) {
                     var2x = "ISBN must contain only numbers and dashes";
                  } else if (var5.length() != 10 && var5.length() != 13) {
                     var2x = "ISBN must be 10 or 13 digits";
                  }
               }
            }

            var3.setText(var2x);
            var3.setForeground(Color.RED);
         }
      });
   }

   private void addBookDialog() {
      String var1 = this.getNextBookUniqueId();
      JTextField var2 = new JTextField();
      JTextField var3 = new JTextField();
      final JTextField var4 = new JTextField();
      var4.getDocument().addDocumentListener(new DocumentListener() {
         private void formatIsbn() {
            String var1 = var4.getText().replaceAll("[^\\dXx]", "");
            String var2 = var1;
            if (var1.length() == 13) {
               var2 = String.format("%s-%s-%s-%s-%s", var1.substring(0, 3), var1.substring(3, 4), var1.substring(4, 6), var1.substring(6, 12), var1.substring(12));
            } else if (var1.length() == 10) {
               var2 = String.format("%s-%s-%s-%s", var1.substring(0, 1), var1.substring(1, 4), var1.substring(4, 9), var1.substring(9));
            }

            if (!var4.getText().equals(var2)) {
               var4.setText(var2);
            }

         }

         public void insertUpdate(DocumentEvent var1) {
            this.formatIsbn();
         }

         public void removeUpdate(DocumentEvent var1) {
            this.formatIsbn();
         }

         public void changedUpdate(DocumentEvent var1) {
            this.formatIsbn();
         }
      });
      JLabel var5 = new JLabel(var1);
      JLabel var6 = new JLabel();
      var6.setForeground(Color.RED);
      JPanel var7 = new JPanel(new GridLayout(0, 1));
      var7.add(new JLabel("Book Unique ID (auto):"));
      var7.add(var5);
      var7.add(new JLabel("Title:*"));
      var7.add(var2);
      var7.add(new JLabel("Author:*"));
      var7.add(var3);
      var7.add(new JLabel("ISBN (optional, with or without dashes):"));
      var7.add(var4);
      var7.add(var6);
      var7.add(new JLabel("* Required fields"));
      var2.setToolTipText("Enter book title (required)");
      var3.setToolTipText("Enter author name (required, letters only)");
      var4.setToolTipText("Enter 10 or 13 digit ISBN (optional, dashes allowed)");
      this.addRealTimeValidation(var2, "title", var6);
      this.addRealTimeValidation(var3, "author", var6);
      this.addRealTimeValidation(var4, "isbn", var6);

      while(true) {
         int var8 = JOptionPane.showConfirmDialog(this, var7, "Add Book", 2);
         String var9 = var2.getText().trim();
         String var10 = var3.getText().trim();
         String var11 = var4.getText().trim();
         String var12 = var11.replaceAll("[^\\dXx]", "");
         if (var12.length() == 13) {
            var11 = String.format("%s-%s-%s-%s-%s", var12.substring(0, 3), var12.substring(3, 4), var12.substring(4, 6), var12.substring(6, 12), var12.substring(12));
         } else if (var12.length() == 10) {
            var11 = String.format("%s-%s-%s-%s", var12.substring(0, 1), var12.substring(1, 4), var12.substring(4, 9), var12.substring(9));
         }

         if (var8 != 0) {
            break;
         }

         if (!var9.isEmpty() && !var10.isEmpty()) {
            String var13 = var11.replaceAll("[-\\s]", "");
            if (!var11.isEmpty()) {
               if (!var13.matches("\\d+")) {
                  var6.setText("ISBN must contain only numbers and dashes.");
                  continue;
               }

               if (var13.length() != 10 && var13.length() != 13) {
                  var6.setText("ISBN must be 10 or 13 digits (excluding dashes).");
                  continue;
               }
            }

            boolean var14 = false;
            if (!var11.isEmpty()) {
               Iterator var15 = this.manager.getBooks().iterator();

               while(var15.hasNext()) {
                  Book var16 = (Book)var15.next();
                  if (var16.getIsbn() != null && var16.getIsbn().equals(var11)) {
                     var14 = true;
                     break;
                  }
               }
            }

            if (!var14) {
               this.pushBooksUndo();
               Book var17 = new Book(var1, var9, var10);
               var17.setIsbn(var11);
               this.manager.addBook(var17);
               this.manager.saveAll();
               this.manager.loadAll();
               this.refreshBooksTable();
               this.dashboardPanel.refresh();
               String var18 = "Book added successfully!\nUnique ID: " + var1 + "\nTitle: " + var9 + "\nAuthor: " + var10;
               if (!var11.isEmpty()) {
                  var18 = var18 + "\nISBN: " + var11;
               }

               JOptionPane.showMessageDialog(this, var18, "Success", 1);
               break;
            }

            var6.setText("Duplicate ISBN! A book with this ISBN already exists.");
         } else {
            var6.setText("Title and Author are required.");
         }
      }

   }

   private void editBookDialog() {
      int var1 = this.booksTable.getSelectedRow();
      if (var1 == -1) {
         JOptionPane.showMessageDialog(this, "Select a book to edit.");
      } else {
         String var2 = this.booksTableModel.getValueAt(var1, 0).toString();
         String var3 = this.booksTableModel.getValueAt(var1, 1).toString();
         String var4 = this.booksTableModel.getValueAt(var1, 2).toString();
         String var5 = this.booksTableModel.getValueAt(var1, 3).toString();
         JLabel var6 = new JLabel(var2);
         JTextField var7 = new JTextField(var3);
         JTextField var8 = new JTextField(var4);
         final JTextField var9 = new JTextField(var5);
         var9.getDocument().addDocumentListener(new DocumentListener() {
            private void formatIsbn() {
               String var1 = var9.getText().replaceAll("[^\\dXx]", "");
               String var2 = var1;
               if (var1.length() == 13) {
                  var2 = String.format("%s-%s-%s-%s-%s", var1.substring(0, 3), var1.substring(3, 4), var1.substring(4, 6), var1.substring(6, 12), var1.substring(12));
               } else if (var1.length() == 10) {
                  var2 = String.format("%s-%s-%s-%s", var1.substring(0, 1), var1.substring(1, 4), var1.substring(4, 9), var1.substring(9));
               }

               if (!var9.getText().equals(var2)) {
                  var9.setText(var2);
               }

            }

            public void insertUpdate(DocumentEvent var1) {
               this.formatIsbn();
            }

            public void removeUpdate(DocumentEvent var1) {
               this.formatIsbn();
            }

            public void changedUpdate(DocumentEvent var1) {
               this.formatIsbn();
            }
         });
         JLabel var10 = new JLabel();
         var10.setForeground(Color.RED);
         JPanel var11 = new JPanel(new GridLayout(0, 1));
         var11.add(new JLabel("Book Unique ID (not editable):"));
         var11.add(var6);
         var11.add(new JLabel("Title:*"));
         var11.add(var7);
         var11.add(new JLabel("Author:*"));
         var11.add(var8);
         var11.add(new JLabel("ISBN (optional, with or without dashes):"));
         var11.add(var9);
         var11.add(var10);
         var11.add(new JLabel("* Required fields"));
         int var12 = JOptionPane.showConfirmDialog(this, var11, "Edit Book", 2);
         if (var12 == 0) {
            String var13 = var7.getText().trim();
            String var14 = var8.getText().trim();
            String var15 = var9.getText().trim();
            String var16 = var15.replaceAll("[^\\dXx]", "");
            if (var16.length() == 13) {
               var15 = String.format("%s-%s-%s-%s-%s", var16.substring(0, 3), var16.substring(3, 4), var16.substring(4, 6), var16.substring(6, 12), var16.substring(12));
            } else if (var16.length() == 10) {
               var15 = String.format("%s-%s-%s-%s", var16.substring(0, 1), var16.substring(1, 4), var16.substring(4, 9), var16.substring(9));
            }

            if (var13.isEmpty() || var14.isEmpty()) {
               JOptionPane.showMessageDialog(this, "Title and Author are required.");
               return;
            }

            if (!var15.isEmpty()) {
               String var17 = var15.replaceAll("[-\\s]", "");
               if (!var17.matches("\\d+")) {
                  JOptionPane.showMessageDialog(this, "ISBN must contain only numbers and dashes.", "Invalid ISBN", 0);
                  return;
               }

               if (var17.length() != 10 && var17.length() != 13) {
                  JOptionPane.showMessageDialog(this, "ISBN must be 10 or 13 digits (excluding dashes).", "Invalid ISBN", 0);
                  return;
               }
            }

            boolean var21 = false;
            if (!var15.isEmpty()) {
               Iterator var18 = this.manager.getBooks().iterator();

               while(var18.hasNext()) {
                  Book var19 = (Book)var18.next();
                  if (var19.getIsbn() != null && var19.getIsbn().equals(var15) && !var19.getUniqueId().equals(var2)) {
                     var21 = true;
                     break;
                  }
               }
            }

            if (var21) {
               JOptionPane.showMessageDialog(this, "Duplicate ISBN! Another book with this ISBN already exists.", "Duplicate ISBN", 0);
               return;
            }

            Book var22 = null;
            Iterator var23 = this.manager.getBooks().iterator();

            while(var23.hasNext()) {
               Book var20 = (Book)var23.next();
               if (var20.getUniqueId().equals(var2)) {
                  var22 = var20;
                  break;
               }
            }

            if (var22 != null) {
               this.pushBooksUndo();
               var22.setTitle(var13);
               var22.setAuthor(var14);
               var22.setIsbn(var15);
               this.manager.saveAll();
               this.manager.loadAll();
               this.refreshBooksTable();
               this.dashboardPanel.refresh();
               String var24 = "Book updated successfully!\nUnique ID: " + var2 + "\nTitle: " + var13 + "\nAuthor: " + var14;
               if (!var15.isEmpty()) {
                  var24 = var24 + "\nISBN: " + var15;
               }

               JOptionPane.showMessageDialog(this, var24, "Success", 1);
            } else {
               JOptionPane.showMessageDialog(this, "Book not found for editing.");
            }
         }

      }
   }

   private void deleteBook() {
      int var1 = this.booksTable.getSelectedRow();
      if (var1 == -1) {
         JOptionPane.showMessageDialog(this, "Select a book to delete.");
      } else {
         String var2 = this.booksTableModel.getValueAt(var1, 0).toString();
         Book var3 = null;
         Iterator var4 = this.manager.getBooks().iterator();

         while(var4.hasNext()) {
            Book var5 = (Book)var4.next();
            if (var5.getUniqueId().equals(var2)) {
               var3 = var5;
               break;
            }
         }

         if (var3 == null) {
            JOptionPane.showMessageDialog(this, "Book not found for deletion.", "Error", 0);
         } else {
            boolean var7 = false;
            Iterator var8 = this.manager.getLoans().iterator();

            while(var8.hasNext()) {
               Loan var6 = (Loan)var8.next();
               if (var6.getBookIsbn() != null && var6.getBookIsbn().equals(var3.getIsbn())) {
                  var7 = true;
                  break;
               }
            }

            if (var7) {
               JOptionPane.showMessageDialog(this, "Cannot delete: This book is referenced in a loan.", "Delete Blocked", 2);
            } else {
               int var9 = JOptionPane.showConfirmDialog(this, "Delete book '" + var2 + "'?", "Confirm", 0);
               if (var9 == 0) {
                  this.pushBooksUndo();
                  this.manager.getBooks().remove(var3);
                  this.manager.saveAll();
                  this.manager.loadAll();
                  this.refreshBooksTable();
                  this.dashboardPanel.refresh();
                  JOptionPane.showMessageDialog(this, "Book deleted successfully!", "Success", 1);
               }

            }
         }
      }
   }

   private void showNewDialog() {
      String[] var1 = new String[]{"Book", "Member", "Loan"};
      int var2 = JOptionPane.showOptionDialog(this, "What would you like to add?", "New...", -1, 3, (Icon)null, var1, var1[0]);
      if (var2 == 0) {
         this.addBookDialog();
      } else if (var2 == 1) {
         this.addMemberDialog();
      } else if (var2 == 2) {
         this.addLoanDialog();
      }

   }

   private void exportBooksToCSV() {
      JFileChooser var1 = new JFileChooser();
      var1.setDialogTitle("Export Books to CSV");
      var1.setSelectedFile(new File("library_books_export.csv"));
      int var2 = var1.showSaveDialog(this);
      if (var2 == 0) {
         File var3 = var1.getSelectedFile();

         try {
            PrintWriter var4 = new PrintWriter(var3);

            try {
               var4.println("UniqueID,Title,Author,ISBN,IsLoaned");
               Iterator var5 = this.manager.getBooks().iterator();

               while(true) {
                  if (!var5.hasNext()) {
                     JOptionPane.showMessageDialog(this, "Books exported successfully!\n" + var3.getAbsolutePath(), "Export Complete", 1);
                     break;
                  }

                  Book var6 = (Book)var5.next();
                  var4.println(var6.toCSV());
               }
            } catch (Throwable var8) {
               try {
                  var4.close();
               } catch (Throwable var7) {
                  var8.addSuppressed(var7);
               }

               throw var8;
            }

            var4.close();
         } catch (Exception var9) {
            JOptionPane.showMessageDialog(this, "Error exporting books: " + var9.getMessage(), "Export Error", 0);
         }
      }

   }

   private void importBooksFromCSV() {
      JFileChooser var1 = new JFileChooser();
      var1.setDialogTitle("Import Books from CSV");
      int var2 = var1.showOpenDialog(this);
      if (var2 == 0) {
         File var3 = var1.getSelectedFile();
         int var4 = 0;
         int var5 = 0;

         try {
            Scanner var6 = new Scanner(var3);

            try {
               while(true) {
                  while(var6.hasNextLine()) {
                     String var7 = var6.nextLine();
                     Book var8 = Book.fromCSV(var7);
                     if (var8 != null && var8.getIsbn() != null && !var8.getIsbn().isEmpty()) {
                        if (!this.manager.getBooks().stream().anyMatch((var1x) -> {
                           return var1x.getIsbn().equals(var8.getIsbn());
                        })) {
                           this.manager.addBook(var8);
                           ++var4;
                        } else {
                           ++var5;
                        }
                     } else {
                        ++var5;
                     }
                  }

                  this.refreshBooksTable();
                  JOptionPane.showMessageDialog(this, "Books imported: " + var4 + "\nSkipped (duplicates/invalid): " + var5, "Import Complete", 1);
                  break;
               }
            } catch (Throwable var10) {
               try {
                  var6.close();
               } catch (Throwable var9) {
                  var10.addSuppressed(var9);
               }

               throw var10;
            }

            var6.close();
         } catch (Exception var11) {
            JOptionPane.showMessageDialog(this, "Error importing books: " + var11.getMessage(), "Import Error", 0);
         }
      }

   }

   private void printBookCatalog() {
      StringBuilder report = new StringBuilder();
      report.append("===============================================================================\n");
      report.append(String.format("%-79s\n", "                          LIBRARY BOOK CATALOG"));
      report.append(String.format("%-79s\n", "                          Generated: " + LocalDate.now()));
      report.append("===============================================================================\n\n");

      // Column headers and separators
      String headerFormat = "%-6s %-35s %-20s %-15s %-9s\n";
      report.append(String.format(headerFormat, "ID", "TITLE", "AUTHOR", "ISBN", "STATUS"));
      report.append(String.format(headerFormat,
              "-----",
              "-----------------------------------",
              "-------------------",
              "-------------",
              "---------"
      ));

      List<Book> allBooks = this.manager.getBooks();
      int availableBooks = 0;
      int loanedBooks = 0;

      for (Book book : allBooks) {
          String id = book.getUniqueId();
          String title = book.getTitle();
          String author = book.getAuthor();
          String isbn = book.getIsbn() != null ? book.getIsbn() : "";
          String status = book.isLoaned() ? "On Loan" : "Available";

          if (book.isLoaned()) {
              loanedBooks++;
          } else {
              availableBooks++;
          }

          // Truncate if necessary
          if (title.length() > 35) {
              title = title.substring(0, 32) + "...";
          }
          if (author.length() > 20) {
              author = author.substring(0, 17) + "...";
          }
          if (isbn.length() > 15) {
              isbn = isbn.substring(0, 12) + "...";
          }

          report.append(String.format(headerFormat, id, title, author, isbn, status));
      }

      report.append("\n===============================================================================\n");
      report.append(String.format("Total Books: %d      Available: %d      On Loan: %d\n", allBooks.size(), availableBooks, loanedBooks));
      report.append("===============================================================================\n");

      this.displayReport("Book Catalog Report", report.toString());
   }

   private void printLoanHistory() {
      StringBuilder var1 = new StringBuilder();
      var1.append("LIBRARY LOAN HISTORY REPORT\n");
      var1.append("Generated on: ").append(LocalDate.now()).append("\n");
      var1.append("=").append("=".repeat(50)).append("\n\n");
      List var2 = this.manager.getLoans();
      var1.append("Total Loans: ").append(var2.size()).append("\n\n");
      Iterator var3 = var2.iterator();

      while(var3.hasNext()) {
         Loan var4 = (Loan)var3.next();
         var1.append(String.format("Loan ID: %s\n", var4.getId()));
         var1.append(String.format("Book ISBN: %s\n", var4.getBookIsbn()));
         var1.append(String.format("Member ID: %s\n", var4.getMemberId()));
         var1.append(String.format("Loan Date: %s\n", var4.getLoanDate()));
         var1.append(String.format("Return Date: %s\n", var4.getReturnDate() == null ? "Not Returned" : var4.getReturnDate().toString()));
         var1.append("-".repeat(40)).append("\n");
      }

      this.displayReport("Loan History Report", var1.toString());
   }

   private void displayReport(String var1, String var2) {
      JTextArea var3 = new JTextArea(var2);
      var3.setEditable(false);
      var3.setFont(new Font("Monospaced", 0, 12));
      JScrollPane var4 = new JScrollPane(var3);
      var4.setPreferredSize(new Dimension(600, 400));
      JOptionPane.showMessageDialog(this, var4, var1, 1);
   }

   private void showSearchDialog() {
        GlobalSearchDialog searchDialog = new GlobalSearchDialog(this, this.manager);
        searchDialog.setVisible(true);
   }

   private void showAboutDialog() {
      JPanel var1 = new JPanel(new BorderLayout());
      var1.setBackground(new Color(41, 128, 185));
      JPanel var2 = new JPanel(new FlowLayout(1));
      var2.setBackground(new Color(41, 128, 185));
      var2.setPreferredSize(new Dimension(0, 70));
      JLabel var3 = new JLabel("Library Management System");
      var3.setFont(new Font("Segoe UI", 1, 24));
      var3.setForeground(Color.WHITE);
      var2.add(var3);
      var1.add(var2, "North");
      JPanel var4 = new JPanel();
      var4.setBackground(new Color(41, 128, 185));
      var4.setLayout(new BoxLayout(var4, 1));
      var4.setBorder(BorderFactory.createEmptyBorder(15, 25, 15, 25));
      JLabel var5 = new JLabel("Version: 1.0.0");
      var5.setFont(new Font("Segoe UI", 1, 14));
      var5.setForeground(Color.WHITE);
      var4.add(var5);
      var4.add(Box.createVerticalStrut(10));
      JLabel var6 = new JLabel("Developer: Alonge Sodiq");
      var6.setFont(new Font("Segoe UI", 0, 12));
      var6.setForeground(new Color(230, 240, 250));
      var4.add(var6);
      var4.add(Box.createVerticalStrut(5));
      JLabel var7 = new JLabel("Technical Support: ");
      var7.setFont(new Font("Segoe UI", 0, 12));
      var7.setForeground(new Color(230, 240, 250));
      var4.add(var7);

      JLabel githubLink = new JLabel("<html><a href=''>GitHub Profile</a></html>");
      githubLink.setCursor(new Cursor(Cursor.HAND_CURSOR));
      githubLink.addMouseListener(new MouseAdapter() {
          @Override
          public void mouseClicked(MouseEvent e) {
              try {
               Desktop.getDesktop().browse(new URI("https://github.com/Ayokule/Library-Management-System"));
              } catch (IOException | URISyntaxException ex) {
                  ex.printStackTrace();
              }
          }
      });
      var4.add(githubLink);

      var4.add(Box.createVerticalStrut(15));
      JLabel var8 = new JLabel("Key Features:");
      var8.setFont(new Font("Segoe UI", 1, 13));
      var8.setForeground(Color.WHITE);
      var4.add(var8);
      var4.add(Box.createVerticalStrut(5));
      String[] var9 = new String[]{"  Manage Books, Members, and Loans", "  Interactive Dashboard with Real-time Statistics", "  Quick Navigation between Sections", "  Export/Import Books to CSV", "  Print Reports (Catalog & Loan History)", "  Search Functionality", "  Undo/Redo Support for Books"};
      String[] var10 = var9;
      int var11 = var9.length;

      for(int var12 = 0; var12 < var11; ++var12) {
         String var13 = var10[var12];
         JLabel var14 = new JLabel(var13);
         var14.setFont(new Font("Segoe UI", 0, 11));
         var14.setForeground(new Color(200, 220, 240));
         var14.setAlignmentX(0.0F);
         var4.add(var14);
      }

      var4.add(Box.createVerticalStrut(15));
      JLabel var17 = new JLabel("Keyboard Shortcuts:");
      var17.setFont(new Font("Segoe UI", 1, 13));
      var17.setForeground(Color.WHITE);
      var4.add(var17);
      var4.add(Box.createVerticalStrut(5));
      String[] var18 = new String[]{"  Ctrl+N: New (Book/Member/Loan)", "  Ctrl+S: Save All Data", "  Ctrl+F: Search", "  Ctrl+Z: Undo", "  Ctrl+Y: Redo", "  Delete: Delete Selected Item"};
      String[] var19 = var18;
      int var21 = var18.length;

      for(int var23 = 0; var23 < var21; ++var23) {
         String var15 = var19[var23];
         JLabel var16 = new JLabel(var15);
         var16.setFont(new Font("Segoe UI", 0, 11));
         var16.setForeground(new Color(200, 220, 240));
         var16.setAlignmentX(0.0F);
         var4.add(var16);
      }

      var4.add(Box.createVerticalStrut(15));
      JLabel var20 = new JLabel("A modern, user-friendly library management solution");
      var20.setFont(new Font("Segoe UI", 2, 11));
      var20.setForeground(new Color(180, 200, 220));
      var20.setAlignmentX(0.0F);
      var4.add(var20);
      var4.add(Box.createVerticalGlue());
      JScrollPane var22 = new JScrollPane(var4);
      var22.setBackground(new Color(41, 128, 185));
      var22.getViewport().setBackground(new Color(41, 128, 185));
      var22.setBorder(BorderFactory.createEmptyBorder());
      var1.add(var22, "Center");
      JPanel var24 = new JPanel(new FlowLayout(1));
      var24.setBackground(new Color(30, 100, 160));
      var24.setPreferredSize(new Dimension(0, 50));
      JLabel var25 = new JLabel("© 2026 Library Management System. All rights reserved.");
      var25.setFont(new Font("Segoe UI", 0, 10));
      var25.setForeground(new Color(200, 220, 240));
      var24.add(var25);
      var1.add(var24, "South");
      JDialog var26 = new JDialog(this, "About Library Management System", true);
      var26.setDefaultCloseOperation(2);
      var26.setSize(700, 600);
      var26.setLocationRelativeTo(this);
      var26.setContentPane(var1);
      var26.setVisible(true);
   }

   private void showNotImplemented(String var1) {
      JOptionPane.showMessageDialog(this, var1 + " is not implemented yet.", "Not Implemented", 1);
   }

   private void pushBooksUndo() {
      ArrayList var1 = new ArrayList();
      Iterator var2 = this.manager.getBooks().iterator();

      while(var2.hasNext()) {
         Book var3 = (Book)var2.next();
         var1.add(Book.fromCSV(var3.toCSV()));
      }

      this.undoStack.push(var1);
      this.redoStack.clear();
   }

   private void undoBooks() {
      if (!this.undoStack.isEmpty()) {
         this.redoStack.push(new ArrayList(this.manager.getBooks()));
         List var1 = (List)this.undoStack.pop();
         this.manager.getBooks().clear();
         this.manager.getBooks().addAll(var1);
         this.manager.saveAll();
         this.refreshBooksTable();
      } else {
         JOptionPane.showMessageDialog(this, "Nothing to undo.", "Undo", 1);
      }

   }

   private void redoBooks() {
      if (!this.redoStack.isEmpty()) {
         this.undoStack.push(new ArrayList(this.manager.getBooks()));
         List var1 = (List)this.redoStack.pop();
         this.manager.getBooks().clear();
         this.manager.getBooks().addAll(var1);
         this.manager.saveAll();
         this.refreshBooksTable();
      } else {
         JOptionPane.showMessageDialog(this, "Nothing to redo.", "Redo", 1);
      }

   }

   private JPanel createMembersPanel() {
      JPanel var1 = new JPanel(new BorderLayout());
      var1.setBackground(new Color(245, 245, 245));
      JPanel var2 = new JPanel();
      var2.setBackground(new Color(155, 89, 182));
      var2.setPreferredSize(new Dimension(0, 60));
      JLabel var3 = new JLabel("Members Management");
      var3.setFont(new Font("Segoe UI", 1, 22));
      var3.setForeground(Color.WHITE);
      var2.add(var3);
      var1.add(var2, "North");
      String[] var4 = new String[]{"ID", "Name", "Email"};
      this.membersTableModel = new DefaultTableModel(var4, 0);
      this.membersTable = new JTable(this.membersTableModel);
      this.membersTable.setFont(new Font("Segoe UI", 0, 12));
      this.membersTable.setRowHeight(28);
      this.membersTable.setGridColor(new Color(200, 200, 200));
      JTableHeader var5 = this.membersTable.getTableHeader();
      var5.setBackground(new Color(188, 110, 211));
      var5.setForeground(Color.WHITE);
      var5.setFont(new Font("Segoe UI", 1, 13));
      var5.setPreferredSize(new Dimension(0, 35));
      this.membersTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
         public Component getTableCellRendererComponent(JTable var1, Object var2, boolean var3, boolean var4, int var5, int var6) {
            Component var7 = super.getTableCellRendererComponent(var1, var2, var3, var4, var5, var6);
            if (var3) {
               var7.setBackground(new Color(155, 89, 182));
               var7.setForeground(Color.WHITE);
            } else if (var5 % 2 == 0) {
               var7.setBackground(new Color(255, 255, 255));
               var7.setForeground(Color.BLACK);
            } else {
               var7.setBackground(new Color(245, 235, 250));
               var7.setForeground(Color.BLACK);
            }

            return var7;
         }
      });
      this.refreshMembersTable();
      JScrollPane var6 = new JScrollPane(this.membersTable);
      var6.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
      var1.add(var6, "Center");
      JPanel var7 = new JPanel();
      var7.setBackground(new Color(245, 245, 245));
      var7.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));
      var7.setLayout(new FlowLayout(1, 15, 0));
      JButton var8 = this.createStyledButton("+ Add Member", new Color(39, 174, 96), new Color(46, 204, 113));
      JButton var9 = this.createStyledButton("Edit Member", new Color(155, 89, 182), new Color(188, 110, 211));
      JButton var10 = this.createStyledButton("Delete Member", new Color(192, 57, 43), new Color(231, 76, 60));
      var7.add(var8);
      var7.add(var9);
      var7.add(var10);
      var1.add(var7, "South");
      var8.addActionListener((var1x) -> {
         this.addMemberDialog();
      });
      var9.addActionListener((var1x) -> {
         this.editMemberDialog();
      });
      var10.addActionListener((var1x) -> {
         this.deleteMember();
      });
      return var1;
   }

   private void addMemberDialog() {
      JTextField var1 = new JTextField();
      JTextField var2 = new JTextField();
      JTextField var3 = new JTextField();
      JLabel var4 = new JLabel();
      var4.setForeground(Color.RED);
      JPanel var5 = new JPanel(new GridLayout(0, 1));
      var5.add(new JLabel("Member ID:"));
      var5.add(var1);
      var5.add(new JLabel("Name:"));
      var5.add(var2);
      var5.add(new JLabel("Email:"));
      var5.add(var3);
      var5.add(var4);

      while(true) {
         int var6 = JOptionPane.showConfirmDialog(this, var5, "Add Member", 2);
         if (var6 != 0) {
            break;
         }

         String var7 = var1.getText().trim();
         String var8 = var2.getText().trim();
         String var9 = var3.getText().trim();
         StringBuilder var10 = new StringBuilder();
         if (var7.isEmpty() || var8.isEmpty()) {
            var10.append("ID and Name are required.\n");
         }

         if (!var7.matches("\\d+")) {
            var10.append("ID must be numeric.\n");
         }

         if (!var8.matches("[a-zA-Z ]+")) {
            var10.append("Name must contain only letters and spaces.\n");
         }

         if (!var9.isEmpty() && !var9.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            var10.append("Invalid email format.\n");
         }

         Iterator var11 = this.manager.getMembers().iterator();

         while(var11.hasNext()) {
            Member var12 = (Member)var11.next();
            if (var12.getId().equals(var7)) {
               var10.append("Duplicate Member ID!\n");
               break;
            }
         }

         if (var10.length() <= 0) {
            Member var13 = new Member(var7, var8, var9);
            this.manager.addMember(var13);
            this.manager.saveAll();
            this.refreshMembersTable();
            this.dashboardPanel.refresh();
            JOptionPane.showMessageDialog(this, "Member added successfully!", "Success", 1);
            break;
         }

         String var10001 = var10.toString();
         var4.setText("<html>" + var10001.replace("\n", "<br>") + "</html>");
      }

   }

   private void editMemberDialog() {
      int var1 = this.membersTable.getSelectedRow();
      if (var1 == -1) {
         JOptionPane.showMessageDialog(this, "Select a member to edit.");
      } else {
         String var2 = this.membersTableModel.getValueAt(var1, 0).toString();
         String var3 = this.membersTableModel.getValueAt(var1, 1).toString();
         String var4 = this.membersTableModel.getValueAt(var1, 2).toString();
         JTextField var5 = new JTextField(var3);
         JTextField var6 = new JTextField(var4);
         JLabel var7 = new JLabel();
         var7.setForeground(Color.RED);
         JPanel var8 = new JPanel(new GridLayout(0, 1));
         var8.add(new JLabel("Member ID (not editable):"));
         var8.add(new JLabel(var2));
         var8.add(new JLabel("Name:"));
         var8.add(var5);
         var8.add(new JLabel("Email:"));
         var8.add(var6);
         var8.add(var7);

         while(true) {
            int var9 = JOptionPane.showConfirmDialog(this, var8, "Edit Member", 2);
            if (var9 != 0) {
               break;
            }

            String var10 = var5.getText().trim();
            String var11 = var6.getText().trim();
            StringBuilder var12 = new StringBuilder();
            if (var10.isEmpty()) {
               var12.append("Name is required.\n");
            }

            if (!var10.matches("[a-zA-Z ]+")) {
               var12.append("Name must contain only letters and spaces.\n");
            }

            if (!var11.isEmpty() && !var11.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
               var12.append("Invalid email format.\n");
            }

            if (var12.length() <= 0) {
               Iterator var13 = this.manager.getMembers().iterator();

               while(var13.hasNext()) {
                  Member var14 = (Member)var13.next();
                  if (var14.getId().equals(var2)) {
                     var14.setName(var10);
                     var14.setEmail(var11);
                     break;
                  }
               }

               this.manager.saveAll();
               this.refreshMembersTable();
               JOptionPane.showMessageDialog(this, "Member updated successfully!", "Success", 1);
               break;
            }

            String var10001 = var12.toString();
            var7.setText("<html>" + var10001.replace("\n", "<br>") + "</html>");
         }

      }
   }

   private void deleteMember() {
      int var1 = this.membersTable.getSelectedRow();
      if (var1 == -1) {
         JOptionPane.showMessageDialog(this, "Select a member to delete.");
      } else {
         String var2 = this.membersTableModel.getValueAt(var1, 0).toString();
         Member var3 = null;
         Iterator var4 = this.manager.getMembers().iterator();

         while(var4.hasNext()) {
            Member var5 = (Member)var4.next();
            if (var5.getId().equals(var2)) {
               var3 = var5;
               break;
            }
         }

         if (var3 == null) {
            JOptionPane.showMessageDialog(this, "Member not found.", "Error", 0);
         } else {
            boolean var7 = false;
            Iterator var8 = this.manager.getLoans().iterator();

            while(var8.hasNext()) {
               Loan var6 = (Loan)var8.next();
               if (var6.getMemberId() != null && var6.getMemberId().equals(var2)) {
                  var7 = true;
                  break;
               }
            }

            if (var7) {
               JOptionPane.showMessageDialog(this, "Cannot delete: This member is referenced in a loan.", "Delete Blocked", 2);
            } else {
               int var9 = JOptionPane.showConfirmDialog(this, "Delete member '" + var2 + "'?", "Confirm", 0);
               if (var9 == 0) {
                  this.manager.getMembers().remove(var3);
                  this.manager.saveAll();
                  this.refreshMembersTable();
                  JOptionPane.showMessageDialog(this, "Member deleted successfully!", "Success", 1);
               }

            }
         }
      }
   }

   private void refreshMembersTable() {
      if (this.membersTableModel != null) {
         this.membersTableModel.setRowCount(0);
         List var1 = this.manager.getMembers();
         Iterator var2 = var1.iterator();

         while(var2.hasNext()) {
            Member var3 = (Member)var2.next();
            this.membersTableModel.addRow(new Object[]{var3.getId(), var3.getName(), var3.getEmail()});
         }
      }

   }

   private JPanel createLoansPanel() {
      JPanel var1 = new JPanel(new BorderLayout());
      var1.setBackground(new Color(245, 245, 245));
      JPanel var2 = new JPanel();
      var2.setBackground(new Color(230, 126, 34));
      var2.setPreferredSize(new Dimension(0, 60));
      JLabel var3 = new JLabel("Loans Management");
      var3.setFont(new Font("Segoe UI", 1, 22));
      var3.setForeground(Color.WHITE);
      var2.add(var3);
      var1.add(var2, "North");
      String[] var4 = new String[]{"ID", "Book ID", "Member ID", "Loan Date", "Return Date"};
      this.loansTableModel = new DefaultTableModel(var4, 0);
      this.loansTable = new JTable(this.loansTableModel);
      this.loansTable.setFont(new Font("Segoe UI", 0, 12));
      this.loansTable.setRowHeight(28);
      this.loansTable.setGridColor(new Color(200, 200, 200));
      JTableHeader var5 = this.loansTable.getTableHeader();
      var5.setBackground(new Color(241, 156, 57));
      var5.setForeground(Color.WHITE);
      var5.setFont(new Font("Segoe UI", 1, 13));
      var5.setPreferredSize(new Dimension(0, 35));
      this.loansTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
         public Component getTableCellRendererComponent(JTable var1, Object var2, boolean var3, boolean var4, int var5, int var6) {
            Component var7 = super.getTableCellRendererComponent(var1, var2, var3, var4, var5, var6);
            if (var3) {
               var7.setBackground(new Color(230, 126, 34));
               var7.setForeground(Color.WHITE);
            } else if (var5 % 2 == 0) {
               var7.setBackground(new Color(255, 255, 255));
               var7.setForeground(Color.BLACK);
            } else {
               var7.setBackground(new Color(255, 250, 240));
               var7.setForeground(Color.BLACK);
            }

            return var7;
         }
      });
      this.refreshLoansTable();
      JScrollPane var6 = new JScrollPane(this.loansTable);
      var6.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
      var1.add(var6, "Center");
      JPanel var7 = new JPanel();
      var7.setBackground(new Color(245, 245, 245));
      var7.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));
      var7.setLayout(new FlowLayout(1, 15, 0));
      JButton var8 = this.createStyledButton("+ Add Loan", new Color(39, 174, 96), new Color(46, 204, 113));
      JButton var9 = this.createStyledButton("Edit Loan", new Color(230, 126, 34), new Color(241, 156, 57));
      JButton var10 = this.createStyledButton("Delete Loan", new Color(192, 57, 43), new Color(231, 76, 60));
      var7.add(var8);
      var7.add(var9);
      var7.add(var10);
      var1.add(var7, "South");
      var8.addActionListener((var1x) -> {
         this.addLoanDialog();
      });
      var9.addActionListener((var1x) -> {
         this.editLoanDialog();
      });
      var10.addActionListener((var1x) -> {
         this.deleteLoan();
      });
      return var1;
   }

   private void refreshLoansTable() {
      if (this.loansTableModel != null) {
         this.loansTableModel.setRowCount(0);
         List var1 = this.manager.getLoans();
         Iterator var2 = var1.iterator();

         while(var2.hasNext()) {
            Loan var3 = (Loan)var2.next();
            this.loansTableModel.addRow(new Object[]{var3.getId(), var3.getBookIsbn(), var3.getMemberId(), var3.getLoanDate(), var3.getReturnDate()});
         }
      }

   }

   private void addLoanDialog() {
      JTextField var1 = new JTextField();
      List var2 = this.manager.getBooks();
      ArrayList var3 = new ArrayList();
      Iterator var4 = var2.iterator();

      while(var4.hasNext()) {
         Book var5 = (Book)var4.next();
         if (var5.getUniqueId() != null && !var5.getUniqueId().isEmpty() && var5.getIsbn() != null && !var5.getIsbn().isEmpty()) {
            var3.add(var5);
         }
      }

      if (var3.isEmpty()) {
         JOptionPane.showMessageDialog(this, "No books available for loan!\nBooks must have both Unique ID and ISBN to be loaned.", "No Available Books", 2);
      } else {
         String[] var25 = (String[])var3.stream().map((book) -> {
            return ((Book)book).getUniqueId();
         }).toArray((var0) -> {
            return new String[var0];
         });
         JComboBox var26 = new JComboBox(var25);
         var26.setEditable(false);
         JTextField var6 = new JTextField();
         JTextField var7 = new JTextField();
         var7.setToolTipText("Flexible format: 02-03-2025, 2-3-2025, 2025-01-15, 02032025, etc.");
         JLabel var8 = new JLabel();
         var8.setForeground(Color.RED);
         JLabel var9 = new JLabel("Hint: Accepts any date format - try: 02-03-2025, 3/2/2025, or 2025-02-03");
         var9.setForeground(Color.GRAY);
         JPanel var10 = new JPanel(new GridLayout(0, 1));
         var10.add(new JLabel("ID (numbers only, e.g. 001):"));
         var10.add(var1);
         var10.add(new JLabel("Book Unique ID (select):"));
         var10.add(var26);
         var10.add(new JLabel("Member ID (numbers only, e.g. 123):"));
         var10.add(var6);
         var10.add(new JLabel("Loan Date (flexible format):"));
         var10.add(var7);
         var10.add(var9);
         var10.add(var8);

         while(true) {
            int var11 = JOptionPane.showConfirmDialog(this, var10, "Add Loan", 2);
            if (var11 != 0) {
               break;
            }

            String var12 = var1.getText().trim();
            String var13 = (String)var26.getSelectedItem();
            String var14 = var6.getText().trim();
            String var15 = var7.getText().trim();
            if (var15.length() == 8) {
               String var10000 = var15.substring(0, 4);
               var15 = var10000 + "-" + var15.substring(4, 6) + "-" + var15.substring(6);
            }

            StringBuilder var16 = new StringBuilder();
            if (var12.isEmpty()) {
               var16.append("Loan ID is required.\n");
            }

            if (!var12.matches("\\d+")) {
               var16.append("Loan ID must be numeric.\n");
            }

            Book var17 = null;
            Iterator var18 = var2.iterator();

            while(var18.hasNext()) {
               Book var19 = (Book)var18.next();
               if (var19.getUniqueId() != null && var19.getUniqueId().equals(var13)) {
                  var17 = var19;
                  break;
               }
            }

            if (var17 == null) {
               var16.append("Book Unique ID not found in books table.\n");
            }

            boolean var27 = false;
            Iterator var28 = this.manager.getMembers().iterator();

            while(var28.hasNext()) {
               Member var20 = (Member)var28.next();
               if (var20.getId().equals(var14)) {
                  var27 = true;
                  break;
               }
            }

            if (var14.isEmpty()) {
               var16.append("Member ID is required.\n");
            }

            if (!var14.matches("\\d+")) {
               var16.append("Member ID must be numeric.\n");
            } else if (!var27) {
               var16.append("Member ID not found in members table.\n");
            }

            LocalDate var29 = null;
            if (var15.isEmpty()) {
               var16.append("Loan date is required.\n");
            } else {
               try {
                  var29 = this.parseDateFlexible(var15);
                  if (var29.isAfter(LocalDate.now())) {
                     var16.append("Loan date cannot be in the future. Today is " + LocalDate.now() + "\n");
                  }
               } catch (Exception var24) {
                  var16.append("Loan date format error: " + var24.getMessage() + "\nAccepts: YYYY-MM-DD, DDMMYYYY, DD/MM/YYYY, etc.\n");
               }
            }

            boolean var30 = this.manager.getLoans().stream().anyMatch((var1x) -> {
               return var1x.getId().equals(var12);
            });
            if (var30) {
               var16.append("Duplicate Loan ID!\n");
            }

            if (var16.length() > 0) {
               String var10001 = var16.toString();
               var8.setText("<html>" + var10001.replace("\n", "<br>") + "</html>");
            } else {
               String var21 = var17.getIsbn();
               String var22 = var17.getUniqueId();
               if (var21 != null && !var21.isEmpty()) {
                  if (var22 != null && !var22.isEmpty()) {
                     Loan var23 = new Loan(var12, var21, var14, var29, (LocalDate)null);
                     this.manager.addLoan(var23);
                     this.manager.saveAll();
                     this.loansTableModel.addRow(new Object[]{var23.getId(), var23.getBookIsbn(), var23.getMemberId(), var23.getLoanDate(), var23.getReturnDate()});
                     this.dashboardPanel.refresh();
                     JOptionPane.showMessageDialog(this, "Loan added successfully!", "Success", 1);
                     break;
                  }

                  var8.setText("<html><font color='red'><b>ERROR:</b> Selected book has no Unique ID!<br>Loans require books with valid ISBN and Unique ID.</font></html>");
               } else {
                  var8.setText("<html><font color='red'><b>ERROR:</b> Selected book has no ISBN!<br>Loans require books with valid ISBN and Unique ID.<br>Please select a different book or add ISBN to this book.</font></html>");
               }
            }
         }

      }
   }

   private void editLoanDialog() {
      int var1 = this.loansTable.getSelectedRow();
      if (var1 == -1) {
         JOptionPane.showMessageDialog(this, "Select a loan to edit.");
      } else {
         String var2 = this.loansTableModel.getValueAt(var1, 0).toString();
         String var3 = this.loansTableModel.getValueAt(var1, 1).toString();
         String var4 = this.loansTableModel.getValueAt(var1, 2).toString();
         String var5 = this.loansTableModel.getValueAt(var1, 3).toString();
         String var6 = this.loansTableModel.getValueAt(var1, 4) == null ? "" : this.loansTableModel.getValueAt(var1, 4).toString();
         String var7 = "ACTIVE";
         LocalDate var8 = LocalDate.parse(var5);
         if (!var6.isEmpty()) {
            var7 = "RETURNED";
         } else if (var8.plusDays(14L).isBefore(LocalDate.now())) {
            var7 = "OVERDUE";
         }

         JTextField var9 = new JTextField(var6);
         var9.setToolTipText("Flexible format: 02-03-2025, 2-3-2025, 2025-01-15, etc.");
         JLabel var10 = new JLabel("Status: " + var7);
         var10.setFont(new Font("Segoe UI", 1, 12));
         if (var7.equals("OVERDUE")) {
            var10.setForeground(new Color(192, 57, 43));
         } else if (var7.equals("RETURNED")) {
            var10.setForeground(new Color(39, 174, 96));
         } else {
            var10.setForeground(new Color(230, 126, 34));
         }

         JPanel var11 = new JPanel(new GridLayout(0, 1));
         var11.add(new JLabel("Loan ID: " + var2));
         var11.add(new JLabel("Book ISBN: " + var3));
         var11.add(new JLabel("Member ID: " + var4));
         var11.add(new JLabel("Loan Date: " + var5));
         var11.add(Box.createVerticalStrut(10));
         var11.add(var10);
         var11.add(Box.createVerticalStrut(10));
         var11.add(new JLabel("Edit Return Date (flexible format):"));
         var11.add(var9);
         JLabel var12 = new JLabel("Hint: Accepts formats like 02-03-2025, 2025-02-03");
         var12.setForeground(Color.GRAY);
         var11.add(var12);
         var11.add(Box.createVerticalStrut(5));
         var11.add(new JLabel("Leave empty to mark as not returned"));
         int var13 = JOptionPane.showConfirmDialog(this, var11, "Edit Loan - " + var7, 2);
         if (var13 == 0) {
            String var14 = var9.getText().trim();
            LocalDate var15 = null;
            if (!var14.isEmpty()) {
               try {
                  var15 = this.parseDateFlexible(var14);
                  if (var15.isAfter(LocalDate.now())) {
                     JOptionPane.showMessageDialog(this, "Error: Return date cannot be in the future.\nYou entered: " + var15 + "\nToday is: " + LocalDate.now(), "Invalid Date", 0);
                     return;
                  }
               } catch (Exception var17) {
                  JOptionPane.showMessageDialog(this, "Date format error: " + var17.getMessage() + "\nAccepts: YYYY-MM-DD, DDMMYYYY, DD/MM/YYYY, etc.", "Date Error", 0);
                  return;
               }
            }

            boolean var16 = this.manager.editLoanReturnDate(var2, var15);
            if (var16) {
               this.manager.saveAll();
               this.loansTableModel.setValueAt(var15 == null ? "" : var15.toString(), var1, 4);
               this.dashboardPanel.refresh();
               JOptionPane.showMessageDialog(this, "Loan updated successfully!", "Success", 1);
            } else {
               JOptionPane.showMessageDialog(this, "Edit failed.");
            }
         }

      }
   }

   private void deleteLoan() {
      int var1 = this.loansTable.getSelectedRow();
      if (var1 == -1) {
         JOptionPane.showMessageDialog(this, "Select a loan to delete.");
      } else {
         String var2 = this.loansTableModel.getValueAt(var1, 0).toString();
         int var3 = JOptionPane.showConfirmDialog(this, "Delete loan " + var2 + "?", "Confirm", 0);
         if (var3 == 0) {
            boolean var4 = this.manager.deleteLoan(var2);
            if (var4) {
               this.manager.saveAll();
               this.loansTableModel.removeRow(var1);
               this.dashboardPanel.refresh();
            } else {
               JOptionPane.showMessageDialog(this, "Delete failed.");
            }
         }

      }
   }
}
