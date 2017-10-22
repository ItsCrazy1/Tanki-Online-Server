package tanks;

import java.util.Vector;
import tanks.DataBase.User;

public class BanList {

   private static Vector banned = new Vector();


   public static void addUser(User var0) {
      if(!isBanned(var0)) {
         banned.addElement(var0);
      }

   }

   public static void removeUser(User var0) {
      if(isBanned(var0)) {
         banned.removeElement(var0);
      }

   }

   public static boolean isBanned(User var0) {
      return banned.contains(var0);
   }

}
