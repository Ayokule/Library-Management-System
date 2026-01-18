package com.lms;

import java.io.File;
import java.io.PrintStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class LibraryManager {
   private List<Book> books = new ArrayList();
   private List<Member> members = new ArrayList();
   private List<Loan> loans = new ArrayList();
   private static final String CSV_DIR = "Liberary_csv";
   private static final String BOOKS_FILE = "Liberary_csv/books.csv";
   private static final String MEMBERS_FILE = "Liberary_csv/members.csv";
   private static final String LOANS_FILE = "Liberary_csv/loans.csv";

   public boolean editBookLoanedStatus(String var1, boolean var2) {
      Iterator var3 = this.books.iterator();

      Book var4;
      do {
         if (!var3.hasNext()) {
            System.err.println("Error: ISBN not found for loaned status edit.");
            return false;
         }

         var4 = (Book)var3.next();
      } while(!var4.getIsbn().equals(var1));

      var4.setLoaned(var2);
      this.saveAll();
      return true;
   }

   public boolean deleteMember(String var1) {
      Iterator var2 = this.members.iterator();

      while(var2.hasNext()) {
         Member var3 = (Member)var2.next();
         if (var3.getId().equals(var1)) {
            Iterator var4 = this.loans.iterator();

            Loan var5;
            do {
               if (!var4.hasNext()) {
                  var2.remove();
                  this.saveAll();
                  return true;
               }

               var5 = (Loan)var4.next();
            } while(!var5.getMemberId().equals(var1));

            System.err.println("Error: Cannot delete member with active loans.");
            return false;
         }
      }

      return false;
   }

   public boolean editMember(String var1, String var2, String var3) {
      Iterator var4 = this.members.iterator();

      Member var5;
      do {
         if (!var4.hasNext()) {
            System.err.println("Error: Member ID not found.");
            return false;
         }

         var5 = (Member)var4.next();
      } while(!var5.getId().equals(var1));

      var5.setName(var2);
      var5.setEmail(var3);
      this.saveAll();
      return true;
   }

   public boolean deleteLoan(String var1) {
      Iterator var2 = this.loans.iterator();

      while(var2.hasNext()) {
         Loan var3 = (Loan)var2.next();
         if (var3.getId().equals(var1)) {
            if (!this.isbnExists(var3.getBookIsbn())) {
               System.err.println("Error: Cannot delete loan with invalid Book ISBN.");
               return false;
            }

            var2.remove();
            this.saveAll();
            return true;
         }
      }

      return false;
   }

   public boolean editLoanReturnDate(String var1, LocalDate var2) {
      Iterator var3 = this.loans.iterator();

      Loan var4;
      do {
         if (!var3.hasNext()) {
            return false;
         }

         var4 = (Loan)var3.next();
      } while(!var4.getId().equals(var1));

      if (!this.isbnExists(var4.getBookIsbn())) {
         System.err.println("Error: Cannot edit loan with invalid Book ISBN.");
         return false;
      } else {
         var4.setReturnDate(var2);
         this.saveAll();
         return true;
      }
   }

   public boolean deleteBook(String var1) {
      Iterator var2 = this.loans.iterator();

      Loan var3;
      do {
         if (!var2.hasNext()) {
            var2 = this.books.iterator();

            Book var4;
            do {
               if (!var2.hasNext()) {
                  return false;
               }

               var4 = (Book)var2.next();
            } while(!var4.getIsbn().equals(var1));

            var2.remove();
            this.saveAll();
            return true;
         }

         var3 = (Loan)var2.next();
      } while(!var3.getBookIsbn().equals(var1));

      System.err.println("Error: Cannot delete book. It is referenced in a loan.");
      return false;
   }

   public boolean editBook(String var1, String var2, String var3) {
      Iterator var4 = this.books.iterator();

      Book var5;
      do {
         if (!var4.hasNext()) {
            System.err.println("Error: ISBN not found.");
            return false;
         }

         var5 = (Book)var4.next();
      } while(!var5.getIsbn().equals(var1));

      var5.setTitle(var2);
      var5.setAuthor(var3);
      this.saveAll();
      return true;
   }

   public void addBook(Book var1) {
      if (this.isbnExists(var1.getIsbn())) {
         System.err.println("Error: Duplicate ISBN found. Cannot add duplicate.");
      } else {
         this.books.add(var1);
         this.saveAll();
      }

   }

   public void addMember(Member var1) {
      Iterator var2 = this.members.iterator();

      Member var3;
      do {
         if (!var2.hasNext()) {
            this.members.add(var1);
            this.saveAll();
            return;
         }

         var3 = (Member)var2.next();
      } while(!var3.getId().equals(var1.getId()));

      System.err.println("Error: Duplicate member ID found. Cannot add duplicate.");
   }

   public void addLoan(Loan var1) {
      if (!this.isbnExists(var1.getBookIsbn())) {
         System.err.println("Error: Book ISBN does not exist. Cannot add loan.");
      } else if (!this.memberExists(var1.getMemberId())) {
         System.err.println("Error: Member ID does not exist. Cannot add loan.");
      } else {
         this.loans.add(var1);
         this.saveAll();
      }

   }

   private boolean isbnExists(String var1) {
      Iterator var2 = this.books.iterator();

      Book var3;
      do {
         if (!var2.hasNext()) {
            return false;
         }

         var3 = (Book)var2.next();
      } while(!var3.getIsbn().equals(var1));

      return true;
   }

   private boolean memberExists(String var1) {
      Iterator var2 = this.members.iterator();

      Member var3;
      do {
         if (!var2.hasNext()) {
            return false;
         }

         var3 = (Member)var2.next();
      } while(!var3.getId().equals(var1));

      return true;
   }

   public void listBooks() {
      Iterator var1 = this.books.iterator();

      while(var1.hasNext()) {
         Book var2 = (Book)var1.next();
         System.out.println(var2.toCSV());
      }

   }

   public void listMembers() {
      Iterator var1 = this.members.iterator();

      while(var1.hasNext()) {
         Member var2 = (Member)var1.next();
         System.out.println(var2.toCSV());
      }

   }

   public void listLoans() {
      Iterator var1 = this.loans.iterator();

      while(var1.hasNext()) {
         Loan var2 = (Loan)var1.next();
         System.out.println(var2.toCSV());
      }

   }

   public void saveAll() {
      File var1 = new File("Liberary_csv");
      if (!var1.exists()) {
         var1.mkdirs();
      }

      CsvUtils.writeToCSV(BOOKS_FILE, this.books);
      CsvUtils.writeToCSV(MEMBERS_FILE, this.members);
      CsvUtils.writeToCSV(LOANS_FILE, this.loans);
   }

   public void loadAll() {
      File var1 = new File("Liberary_csv");
      if (!var1.exists()) {
         var1.mkdirs();
      }

      this.books = CsvUtils.readFromCSV(BOOKS_FILE, Book::fromCSV);
      this.members = CsvUtils.readFromCSV(MEMBERS_FILE, Member::fromCSV);
      this.loans = CsvUtils.readFromCSV(LOANS_FILE, Loan::fromCSV);
      this.validateDataIntegrity();
   }

   private void validateDataIntegrity() {
      int var1 = 0;
      Iterator var2 = this.loans.iterator();

      while(var2.hasNext()) {
         Loan var3 = (Loan)var2.next();
         boolean var4 = this.isbnExists(var3.getBookIsbn());
         boolean var5 = this.memberExists(var3.getMemberId());
         PrintStream var10000;
         String var10001;
         if (!var4) {
            var10000 = System.err;
            var10001 = var3.getId();
            var10000.println("WARNING: Orphaned loan " + var10001 + " references missing book ISBN: " + var3.getBookIsbn());
            ++var1;
         }

         if (!var5) {
            var10000 = System.err;
            var10001 = var3.getId();
            var10000.println("WARNING: Orphaned loan " + var10001 + " references missing member ID: " + var3.getMemberId());
            ++var1;
         }
      }

      if (var1 > 0) {
         System.err.println("Data integrity issues found: " + var1 + " problems detected");
         System.err.println("TIP: You may need to manually clean up loans.csv to remove orphaned records");
      }

   }

   public List<Book> getBooks() {
      return this.books;
   }

   public List<Member> getMembers() {
      return this.members;
   }

   public List<Loan> getLoans() {
      return this.loans;
   }

   // New search methods for GlobalSearchDialog
   public List<Book> searchBooks(String searchTerm) {
       String lowerCaseSearchTerm = searchTerm.toLowerCase();
       return this.books.stream()
               .filter(book -> (book.getUniqueId() != null && book.getUniqueId().toLowerCase().contains(lowerCaseSearchTerm)) ||
                                (book.getTitle() != null && book.getTitle().toLowerCase().contains(lowerCaseSearchTerm)) ||
                                (book.getAuthor() != null && book.getAuthor().toLowerCase().contains(lowerCaseSearchTerm)) ||
                                (book.getIsbn() != null && book.getIsbn().toLowerCase().contains(lowerCaseSearchTerm)))
               .collect(java.util.stream.Collectors.toList());
   }

   public List<Member> searchMembers(String searchTerm) {
       String lowerCaseSearchTerm = searchTerm.toLowerCase();
       return this.members.stream()
               .filter(member -> (member.getId() != null && member.getId().toLowerCase().contains(lowerCaseSearchTerm)) ||
                                 (member.getName() != null && member.getName().toLowerCase().contains(lowerCaseSearchTerm)) ||
                                 (member.getEmail() != null && member.getEmail().toLowerCase().contains(lowerCaseSearchTerm)))
               .collect(java.util.stream.Collectors.toList());
   }

   public List<Loan> searchLoans(String searchTerm) {
       String lowerCaseSearchTerm = searchTerm.toLowerCase();
       return this.loans.stream()
               .filter(loan -> (loan.getId() != null && loan.getId().toLowerCase().contains(lowerCaseSearchTerm)) ||
                                (loan.getBookIsbn() != null && loan.getBookIsbn().toLowerCase().contains(lowerCaseSearchTerm)) ||
                                (loan.getMemberId() != null && loan.getMemberId().toLowerCase().contains(lowerCaseSearchTerm)) ||
                                (loan.getLoanDate() != null && loan.getLoanDate().toString().toLowerCase().contains(lowerCaseSearchTerm)) ||
                                (loan.getReturnDate() != null && loan.getReturnDate().toString().toLowerCase().contains(lowerCaseSearchTerm)))
               .collect(java.util.stream.Collectors.toList());
   }

    // Helper methods for GlobalSearchDialog to retrieve full objects
    public Book getBookByIsbn(String isbn) {
        return this.books.stream()
                .filter(book -> book.getIsbn() != null && book.getIsbn().equals(isbn))
                .findFirst()
                .orElse(null);
    }

    public Member getMemberById(String id) {
        return this.members.stream()
                .filter(member -> member.getId() != null && member.getId().equals(id))
                .findFirst()
                .orElse(null);
    }
}
