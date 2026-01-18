package com.lms;

import java.time.LocalDate;

public class Loan {
   private String id;
   private String bookIsbn;
   private String memberId;
   private LocalDate loanDate;
   private LocalDate returnDate;

   public Loan(String var1, String var2, String var3, LocalDate var4, LocalDate var5) {
      this.id = var1;
      this.bookIsbn = var2;
      this.memberId = var3;
      this.loanDate = var4;
      this.returnDate = var5;
   }

   public String getId() {
      return this.id;
   }

   public String getBookIsbn() {
      return this.bookIsbn;
   }

   public String getMemberId() {
      return this.memberId;
   }

   public LocalDate getLoanDate() {
      return this.loanDate;
   }

   public LocalDate getReturnDate() {
      return this.returnDate;
   }

   public void setReturnDate(LocalDate var1) {
      this.returnDate = var1;
   }

   public String toCSV() {
      String var10000 = this.id;
      return var10000 + "," + this.bookIsbn + "," + this.memberId + "," + this.loanDate + "," + (this.returnDate == null ? "" : this.returnDate.toString());
   }

   public static Loan fromCSV(String var0) {
      try {
         String[] var1 = var0.split(",", -1);
         if (var1.length != 5) {
            System.err.println("Invalid CSV format - expected 5 parts, got " + var1.length);
            return null;
         } else {
            for(int var2 = 0; var2 < var1.length; ++var2) {
               var1[var2] = var1[var2].trim();
            }

            if (var1[0].isEmpty()) {
               System.err.println("Invalid loan: Empty ID");
               return null;
            } else if (var1[1].isEmpty()) {
               System.err.println("Invalid loan: Empty ISBN");
               return null;
            } else if (var1[2].isEmpty()) {
               System.err.println("Invalid loan: Empty Member ID");
               return null;
            } else {
               LocalDate var5 = LocalDate.parse(var1[3]);
               LocalDate var3 = var1[4].isEmpty() ? null : LocalDate.parse(var1[4]);
               if (var5.isAfter(LocalDate.now())) {
                  System.err.println("Warning: Loan " + var1[0] + " has future date: " + var5 + ". Adjusting to today's date.");
                  var5 = LocalDate.now();
               }

               if (var3 != null && var3.isBefore(var5)) {
                  System.err.println("Warning: Loan " + var1[0] + " has return date before loan date. Clearing return date.");
                  var3 = null;
               }

               return new Loan(var1[0], var1[1], var1[2], var5, var3);
            }
         }
      } catch (Exception var4) {
         System.err.println("Error parsing loan from CSV: " + var0 + " - " + var4.getMessage());
         var4.printStackTrace();
         return null;
      }
   }
}
