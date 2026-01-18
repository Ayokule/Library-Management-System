package com.lms;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.function.Function;

public class CsvUtils {
   public static void writeToCSV(String var0, List<?> var1) {
      try {
         File var2 = new File(var0);
         if (var2.exists()) {
            File var3 = new File(var0 + ".backup");
            if (var3.exists()) {
               var3.delete();
            }

            Files.copy(var2.toPath(), var3.toPath(), StandardCopyOption.REPLACE_EXISTING);
         }

         PrintWriter var11 = new PrintWriter(new FileWriter(var0));

         try {
            Iterator var4 = var1.iterator();

            while(var4.hasNext()) {
               Object var5 = var4.next();
               var11.println(var5.getClass().getMethod("toCSV").invoke(var5));
            }

            var11.flush();
         } finally {
            var11.close();
         }
      } catch (Exception var10) {
         System.err.println("Error writing to CSV: " + var0);
         var10.printStackTrace();
      }

   }

   public static <T> List<T> readFromCSV(String var0, Function<String, T> var1) {
      ArrayList var2 = new ArrayList();
      File var3 = new File(var0);
      if (!var3.exists()) {
         try {
            var3.createNewFile();
         } catch (IOException var9) {
            System.err.println("Warning: Could not create CSV file: " + var0);
         }

         return var2;
      } else {
         try {
            Scanner var4 = new Scanner(var3);

            try {
               int var5 = 0;
               int var6 = 0;

               while(var4.hasNextLine()) {
                  ++var5;
                  String var7 = var4.nextLine().trim();
                  if (!var7.isEmpty()) {
                     try {
                        Object var8 = var1.apply(var7);
                        if (var8 != null) {
                           var2.add(var8);
                           ++var6;
                        } else {
                           System.err.println("Skipped invalid record at line " + var5 + ": " + var7);
                        }
                     } catch (Exception var11) {
                        System.err.println("Error parsing line " + var5 + ": " + var7);
                        System.err.println("  Reason: " + var11.getMessage());
                     }
                  }
               }
            } catch (Throwable var12) {
               try {
                  var4.close();
               } catch (Throwable var10) {
                  var12.addSuppressed(var10);
               }

               throw var12;
            }

            var4.close();
         } catch (Exception var13) {
            System.err.println("Error reading from CSV: " + var0);
            var13.printStackTrace();
         }

         return var2;
      }
   }
}
