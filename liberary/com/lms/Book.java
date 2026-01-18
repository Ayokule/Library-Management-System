package com.lms;

import java.util.Objects;

public class Book {
   private String isbn;
   private String title;
   private String author;
   private String uniqueId;
   private boolean isLoaned;

   public Book(String var1, String var2, String var3) {
      this.uniqueId = var1 != null ? var1 : "";
      this.title = var2 != null ? var2 : "";
      this.author = var3 != null ? var3 : "";
      this.isbn = "";
      this.isLoaned = false;
   }

   public Book(String var1, String var2, String var3, String var4, boolean var5) {
      this.isbn = var1;
      this.title = var2;
      this.author = var3;
      this.uniqueId = var4;
      this.isLoaned = var5;
   }

   public String getUniqueId() {
      return this.uniqueId;
   }

   public String getIsbn() {
      return this.isbn;
   }

   public String getTitle() {
      return this.title;
   }

   public String getAuthor() {
      return this.author;
   }

   public boolean isLoaned() {
      return this.isLoaned;
   }

   public void setUniqueId(String var1) {
      this.uniqueId = var1;
   }

   public void setTitle(String var1) {
      this.title = var1;
   }

   public void setAuthor(String var1) {
      this.author = var1;
   }

   public void setLoaned(boolean var1) {
      this.isLoaned = var1;
   }

   public String getBookId() {
      return this.uniqueId;
   }

   public void setBookId(String var1) {
      this.uniqueId = var1;
   }

   public String getBookIdOrIsbn() {
      return this.isbn != null && !this.isbn.isEmpty() ? this.isbn : this.uniqueId;
   }

   public void setIsbn(String var1) {
      this.isbn = var1;
   }

   public String toCSV() {
      String var10000 = this.isbn != null ? this.isbn : "";
      return var10000 + "," + (this.title != null ? this.title : "") + "," + (this.author != null ? this.author : "") + "," + (this.uniqueId != null ? this.uniqueId : "") + "," + (this.isLoaned ? "Yes" : "No");
   }

   public static Book fromCSV(String var0) {
      try {
         String[] var1 = var0.split(",", -1);
         String var2;
         String var3;
         String var4;
         if (var1.length == 4) {
            var2 = var1[0].isEmpty() ? "" : var1[0];
            var3 = var1[1].isEmpty() ? "Unknown Title" : var1[1];
            var4 = var1[2].isEmpty() ? "Unknown Author" : var1[2];
            boolean var8 = var1[3].equalsIgnoreCase("Yes");
            if (var2.isEmpty()) {
               System.err.println("Warning: Book with empty ISBN loaded. Setting default ID.");
               var2 = "UNKNOWN-" + System.nanoTime();
            }

            return new Book(var2, var3, var4, (String)null, var8);
         } else if (var1.length == 5) {
            var2 = var1[0].isEmpty() ? "" : var1[0];
            var3 = var1[1].isEmpty() ? "Unknown Title" : var1[1];
            var4 = var1[2].isEmpty() ? "Unknown Author" : var1[2];
            String var5 = var1[3].isEmpty() ? "" : var1[3];
            boolean var6 = var1[4].equalsIgnoreCase("Yes");
            if (var2.isEmpty() && var5.isEmpty()) {
               System.err.println("Warning: Book with no ISBN or ID loaded. Setting default ID.");
               var2 = "UNKNOWN-" + System.nanoTime();
            }

            return new Book(var2, var3, var4, var5, var6);
         } else {
            System.err.println("Invalid CSV format for book: " + var0 + " (expected 4 or 5 fields, got " + var1.length + ")");
            return null;
         }
      } catch (Exception var7) {
         System.err.println("Error parsing book from CSV: " + var0 + " - " + var7.getMessage());
         return null;
      }
   }

   public String toString() {
      return "Book{uniqueId='" + this.uniqueId + "', title='" + this.title + "', author='" + this.author + "', isbn='" + this.isbn + "', isLoaned=" + this.isLoaned + "}";
   }

   public boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else if (var1 != null && this.getClass() == var1.getClass()) {
         Book var2 = (Book)var1;
         return Objects.equals(this.uniqueId, var2.uniqueId);
      } else {
         return false;
      }
   }

   public int hashCode() {
      return Objects.hash(new Object[]{this.uniqueId});
   }
}
