package com.lms;

public class Member {
   private String id;
   private String name;
   private String email;

   public void setName(String var1) {
      this.name = var1;
   }

   public void setEmail(String var1) {
      this.email = var1;
   }

   public Member(String var1, String var2, String var3) {
      this.id = var1;
      this.name = var2;
      this.email = var3;
   }

   public String getId() {
      return this.id;
   }

   public String getName() {
      return this.name;
   }

   public String getEmail() {
      return this.email;
   }

   public String toCSV() {
      return this.id + "," + this.name + "," + this.email;
   }

   public static Member fromCSV(String var0) {
      try {
         String[] var1 = var0.split(",", -1);
         if (var1.length != 3) {
            System.err.println("Invalid CSV format for member: " + var0 + " (expected 3 fields, got " + var1.length + ")");
            return null;
         } else {
            String var2 = var1[0].trim().isEmpty() ? null : var1[0].trim();
            String var3 = var1[1].trim().isEmpty() ? "Unknown Member" : var1[1].trim();
            String var4 = var1[2].trim().isEmpty() ? "no-email@library.local" : var1[2].trim();
            if (var2 != null && !var2.isEmpty()) {
               return new Member(var2, var3, var4);
            } else {
               System.err.println("Warning: Member with empty ID loaded. Skipping.");
               return null;
            }
         }
      } catch (Exception var5) {
         System.err.println("Error parsing member from CSV: " + var0 + " - " + var5.getMessage());
         return null;
      }
   }
}
