package tanks;

import java.util.Vector;
import tanks.Battle;
import tanks.Lobby;
import tanks.Main;
import tanks.DataBase.User;

public class Battles {

   public static Vector bts = new Vector();


   public static void addBattle(byte var0, byte var1, int var2, String var3, String var4, String var5) {
      Battle var6 = new Battle(var0, var1, var2, var3, var4, var5);
      sendToAll(var6);
      bts.addElement(var6);
   }

   public static void dispose() {
      for(int var0 = 0; var0 < bts.size(); ++var0) {
         Battle var1 = (Battle)bts.elementAt(var0);
         var1.dispose();
      }

      bts = new Vector();
   }

   public static Battle getById(int var0) {
      for(int var1 = 0; var1 < bts.size(); ++var1) {
         Battle var2 = (Battle)bts.elementAt(var1);
         if(var2.bid == var0) {
            return var2;
         }
      }

      return null;
   }

   public static void check(User var0) {
      for(int var1 = 0; var1 < bts.size(); ++var1) {
         Battle var2 = (Battle)bts.elementAt(var1);
         if(var2.check(var0)) {
            return;
         }
      }

   }

   public static void sendBattlePlayers(Lobby var0, int var1) {
      for(int var2 = 0; var2 < bts.size(); ++var2) {
         Battle var3 = (Battle)bts.elementAt(var2);
         if(var3.bid == var1) {
            var3.sendBattlePlayers(var0);
            return;
         }
      }

   }

   public static boolean enter(Lobby var0, int var1, byte var2) {
      for(int var3 = 0; var3 < bts.size(); ++var3) {
         Battle var4 = (Battle)bts.elementAt(var3);
         if(var4.bid == var1) {
            return var4.addPlayer(var0, var2);
         }
      }

      return false;
   }

   private static void sendToAll(Battle var0) {
      for(int var1 = 0; var1 < Main.lobbys.size(); ++var1) {
         Lobby var2 = (Lobby)Main.lobbys.elementAt(var1);
         var0.sendTo(var2);
      }

   }

   public static void sendBattles(Lobby var0) {
      for(int var1 = 0; var1 < bts.size(); ++var1) {
         Battle var2 = (Battle)bts.elementAt(var1);
         var2.sendTo(var0);
      }

   }

   public static void sendPlayers(Lobby var0) {
      for(int var1 = 0; var1 < bts.size(); ++var1) {
         Battle var2 = (Battle)bts.elementAt(var1);
         var2.sendPlayers(var0);
      }

   }

}
