package com.lms;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;

public class UserSettings {
   private static final String SETTINGS_FILE = "user_settings.properties";
   private Properties props = new Properties();

   public void load() {
      File var1 = new File("user_settings.properties");
      if (var1.exists()) {
         try {
            FileInputStream var2 = new FileInputStream(var1);

            try {
               this.props.load(var2);
            } catch (Throwable var6) {
               try {
                  var2.close();
               } catch (Throwable var5) {
                  var6.addSuppressed(var5);
               }

               throw var6;
            }

            var2.close();
         } catch (Exception var7) {
            var7.printStackTrace();
         }
      }

   }

   public void save() {
      try {
         FileOutputStream var1 = new FileOutputStream("user_settings.properties");

         try {
            this.props.store(var1, "User Settings");
         } catch (Throwable var5) {
            try {
               var1.close();
            } catch (Throwable var4) {
               var5.addSuppressed(var4);
            }

            throw var5;
         }

         var1.close();
      } catch (Exception var6) {
         var6.printStackTrace();
      }

   }

   public void set(String var1, String var2) {
      this.props.setProperty(var1, var2);
   }

   public String get(String var1) {
      return this.props.getProperty(var1);
   }
}
