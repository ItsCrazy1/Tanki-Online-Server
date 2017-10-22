package tanks;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;
import tanks.DataBase.User;
import tanks.DataBase.Users;

public class GarageDataBaseParser {

   public static void load() {
      for(int var0 = 0; var0 < Users.getNumUsers(); ++var0) {
         User var1 = Users.getUser(var0);
         loadUserGarage(var1);
      }

   }

   public static void save() {
      for(int var0 = 0; var0 < Users.getNumUsers(); ++var0) {
         User var1 = Users.getUser(var0);
         saveUserGarage(var1);
      }

   }

   private static void loadUserGarage(User var0) {
      try {
         File var1 = new File("ServerDataBase/Garage/" + var0.login + ".txt");
         if(!var1.exists()) {
            return;
         }

         Scanner var2 = new Scanner(var1);
         var0.garage.crystalls = Integer.parseInt(var2.nextLine());
         var0.garage.score = Integer.parseInt(var2.nextLine());
         var0.garage.currHull = Integer.parseInt(var2.nextLine());
         var0.garage.currTurret = Integer.parseInt(var2.nextLine());
         var0.garage.currColormap = Integer.parseInt(var2.nextLine());

         int var3;
         for(var3 = 0; var3 < var0.garage.hulls.length; ++var3) {
            var0.garage.setHull(Integer.parseInt(var2.nextLine()), Integer.parseInt(var2.nextLine()));
         }

         for(var3 = 0; var3 < var0.garage.turrets.length; ++var3) {
            var0.garage.setTurret(Integer.parseInt(var2.nextLine()), Integer.parseInt(var2.nextLine()));
         }

         for(var3 = 0; var3 < var0.garage.colormaps.length; ++var3) {
            var0.garage.setColormap(Integer.parseInt(var2.nextLine()), Integer.parseInt(var2.nextLine()));
         }

         for(var3 = 0; var3 < 5; ++var3) {
            var0.garage.setNumeric(var3, Integer.parseInt(var2.nextLine()));
         }

         var2.close();
      } catch (IOException var4) {
         ;
      }

   }

   private static void saveUserGarage(User var0) {
      try {
         File var1 = new File("ServerDataBase/Garage/" + var0.login + ".txt");
         if(!var1.exists()) {
            return;
         }

         PrintWriter var2 = new PrintWriter(var1);
         var2.append(var0.garage.crystalls + "\n");
         var2.append(var0.garage.score + "\n");
         var2.append(var0.garage.currHull + "\n");
         var2.append(var0.garage.currTurret + "\n");
         var2.append(var0.garage.currColormap + "\n");

         int var3;
         for(var3 = 0; var3 < var0.garage.hulls.length; ++var3) {
            var2.append(var3 + "\n");
            var2.append(var0.garage.hulls[var3] + "\n");
         }

         for(var3 = 0; var3 < var0.garage.turrets.length; ++var3) {
            var2.append(var3 + "\n");
            var2.append(var0.garage.turrets[var3] + "\n");
         }

         for(var3 = 0; var3 < var0.garage.colormaps.length; ++var3) {
            var2.append(var3 + "\n");
            var2.append(var0.garage.colormaps[var3] + "\n");
         }

         for(var3 = 0; var3 < 5; ++var3) {
            var2.append(var0.garage.numerics[var3] + "\n");
         }

         var2.flush();
         var2.close();
      } catch (IOException var4) {
         ;
      }

   }
}
