package com.lms.ui;

import com.lms.Book;
import com.lms.LibraryManager;
import com.lms.Loan;
import com.lms.Member;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class GlobalSearchDialog extends JDialog {

    private LibraryManager manager;

    private JTextField searchField;
    private JCheckBox booksCheckBox;
    private JCheckBox membersCheckBox;
    private JCheckBox loansCheckBox;
    private JTextArea resultsArea;
    private JButton closeButton;
    private JButton exportButton;

    public GlobalSearchDialog(JFrame parent, LibraryManager manager) {
        super(parent, "GLOBAL SEARCH", true); // Modal dialog
        this.manager = manager;
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(700, 500);
        setLocationRelativeTo(parent);
        setResizable(false); // For now, keep it fixed as per ASCII art

        initComponents();
        layoutComponents();
        addListeners();
    }

    private void initComponents() {
        // Search field
        searchField = new JTextField(30);

        // Checkboxes
        booksCheckBox = new JCheckBox("Books");
        membersCheckBox = new JCheckBox("Members");
        loansCheckBox = new JCheckBox("Loans");

        // Set all checkboxes to be selected by default
        booksCheckBox.setSelected(true);
        membersCheckBox.setSelected(true);
        loansCheckBox.setSelected(true);


        // Results area
        resultsArea = new JTextArea();
        resultsArea.setEditable(false);
        resultsArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        resultsArea.setLineWrap(true);
        resultsArea.setWrapStyleWord(true);

        // Buttons
        closeButton = new JButton("Close");
        exportButton = new JButton("Export Results");
    }

    private void layoutComponents() {
        setLayout(new BorderLayout(10, 10)); // Padding between major sections
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(new EmptyBorder(10, 10, 10, 10)); // Padding around the content

        // Title Panel
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(new Color(41, 128, 185)); // Match existing UI color
        JLabel titleLabel = new JLabel("GLOBAL SEARCH");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titlePanel.add(titleLabel, BorderLayout.CENTER);
        titlePanel.setPreferredSize(new Dimension(0, 50)); // Fixed height for title bar
        add(titlePanel, BorderLayout.NORTH);

        // Search Input Panel
        JPanel searchInputPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        searchInputPanel.add(new JLabel("Search For:"));
        searchInputPanel.add(searchField);
        contentPanel.add(searchInputPanel);

        // Search In Checkboxes Panel
        JPanel searchInPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        searchInPanel.add(new JLabel("Search In:"));
        searchInPanel.add(booksCheckBox);
        searchInPanel.add(membersCheckBox);
        searchInPanel.add(loansCheckBox);
        contentPanel.add(searchInPanel);

        contentPanel.add(Box.createVerticalStrut(10)); // Spacer

        // Results Panel
        JPanel resultsPanel = new JPanel(new BorderLayout());
        resultsPanel.setBorder(BorderFactory.createTitledBorder("Results"));
        resultsPanel.add(new JScrollPane(resultsArea), BorderLayout.CENTER);
        resultsPanel.setPreferredSize(new Dimension(600, 250)); // Fixed size for results area
        contentPanel.add(resultsPanel);

        add(contentPanel, BorderLayout.CENTER);

        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        buttonPanel.add(closeButton);
        buttonPanel.add(exportButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void addListeners() {
        // Close button
        closeButton.addActionListener(e -> dispose());

        // Export button
        exportButton.addActionListener(e -> exportResults());

        // Trigger search on Enter in search field
        searchField.addActionListener(e -> performSearch());

        // Trigger search on checkbox change
        booksCheckBox.addActionListener(e -> performSearch());
        membersCheckBox.addActionListener(e -> performSearch());
        loansCheckBox.addActionListener(e -> performSearch());
    }

    private void performSearch() {
        String searchTerm = searchField.getText().trim();
        resultsArea.setText(""); // Clear previous results

        if (searchTerm.isEmpty()) {
            resultsArea.setText("Please enter a search term.");
            return;
        }

        StringBuilder results = new StringBuilder();

        // Search Books
        if (booksCheckBox.isSelected()) {
            List<Book> bookMatches = manager.searchBooks(searchTerm);
            if (!bookMatches.isEmpty()) {
                results.append("• Books (").append(bookMatches.size()).append(" matches)\n");
                bookMatches.forEach(book ->
                    results.append("    - \"").append(book.getTitle()).append("\" by ").append(book.getAuthor())
                           .append(" (ISBN: ").append(book.getIsbn() != null ? book.getIsbn() : "N/A").append(")\n")
                );
                results.append("\n");
            }
        }

        // Search Members
        if (membersCheckBox.isSelected()) {
            List<Member> memberMatches = manager.searchMembers(searchTerm);
            if (!memberMatches.isEmpty()) {
                results.append("• Members (").append(memberMatches.size()).append(" matches)\n");
                memberMatches.forEach(member ->
                    results.append("    - ").append(member.getName()).append(" (ID: ").append(member.getId()).append(")\n")
                );
                results.append("\n");
            }
        }

        // Search Loans
        if (loansCheckBox.isSelected()) {
            List<Loan> loanMatches = manager.searchLoans(searchTerm);
            if (!loanMatches.isEmpty()) {
                results.append("• Loans (").append(loanMatches.size()).append(" matches)\n");
                loanMatches.forEach(loan -> {
                    String bookTitle = manager.getBookByIsbn(loan.getBookIsbn()) != null ? manager.getBookByIsbn(loan.getBookIsbn()).getTitle() : "Unknown Book";
                    String memberName = manager.getMemberById(loan.getMemberId()) != null ? manager.getMemberById(loan.getMemberId()).getName() : "Unknown Member";
                    results.append("    - Loan ID: ").append(loan.getId()).append(", Book: ").append(bookTitle)
                           .append(", Member: ").append(memberName).append(", Loan Date: ").append(loan.getLoanDate())
                           .append(loan.getReturnDate() != null ? ", Returned: " + loan.getReturnDate() : "").append("\n");
                });
                results.append("\n");
            }
        }

        if (results.length() == 0) {
            results.append("No results found for '").append(searchTerm).append("'.");
        }
        resultsArea.setText(results.toString());
        resultsArea.setCaretPosition(0); // Scroll to top
    }

    private void exportResults() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Export Search Results");
        fileChooser.setSelectedFile(new File("search_results.txt"));
        int userSelection = fileChooser.showSaveDialog(this);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            try (FileWriter writer = new FileWriter(fileToSave)) {
                writer.write(resultsArea.getText());
                JOptionPane.showMessageDialog(this, "Results exported successfully to:\n" + fileToSave.getAbsolutePath(), "Export Complete", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error exporting results: " + ex.getMessage(), "Export Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
    }

    // Main method for testing (can be removed in final integration)
    public static void main(String[] args) {
        // Mock LibraryManager for testing
        LibraryManager mockManager = new LibraryManager() {
            // Override search methods for testing purposes
            @Override
            public List<Book> searchBooks(String term) {
                return List.of(
                    new Book("BK001", "Introduction to Java", "James Gosling"),
                    new Book("BK002", "Effective Java", "Joshua Bloch"),
                    new Book("BK003", "Java Concurrency in Practice", "Brian Goetz")
                ).stream().filter(b ->
                    b.getTitle().toLowerCase().contains(term.toLowerCase()) ||
                    b.getAuthor().toLowerCase().contains(term.toLowerCase()) ||
                    b.getIsbn().toLowerCase().contains(term.toLowerCase())
                ).collect(Collectors.toList());
            }

            @Override
            public List<Member> searchMembers(String term) {
                return List.of(
                    new Member("M001", "John Smith", "john.smith@example.com"),
                    new Member("M002", "Jane Doe", "jane.doe@example.com")
                ).stream().filter(m ->
                    m.getName().toLowerCase().contains(term.toLowerCase()) ||
                    m.getId().toLowerCase().contains(term.toLowerCase()) ||
                    m.getEmail().toLowerCase().contains(term.toLowerCase())
                ).collect(Collectors.toList());
            }

            @Override
            public List<Loan> searchLoans(String term) {
                return List.of(
                    new Loan("L001", "ISBN001", "M001", java.time.LocalDate.of(2023, 1, 10), null)
                ).stream().filter(l ->
                    l.getId().toLowerCase().contains(term.toLowerCase()) ||
                    l.getBookIsbn().toLowerCase().contains(term.toLowerCase()) ||
                    l.getMemberId().toLowerCase().contains(term.toLowerCase())
                ).collect(Collectors.toList());
            }

            @Override
            public Book getBookByIsbn(String isbn) {
                return new Book("BK001", "Introduction to Java", "James Gosling"); // Mock
            }

            @Override
            public Member getMemberById(String id) {
                return new Member("M001", "John Smith", "john.smith@example.com"); // Mock
            }
        };

        SwingUtilities.invokeLater(() -> {
            new GlobalSearchDialog(null, mockManager).setVisible(true);
        });
    }
}
