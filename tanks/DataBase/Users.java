package tanks.DataBase;

import java.util.Vector;
import tanks.DataBase.User;
import tanks.DataBase.Colormap.ColormapDB;
import tanks.DataBase.Hull.HullDB;
import tanks.DataBase.Numeric.NumericDB;
import tanks.DataBase.Turret.TurretDB;

public class Users {

   private static Vector users = new Vector();


   public static void addUser(String var0, String var1, int var2) {
      User var3 = new User(var0, var1, (byte)var2);
      users.addElement(var3);
      var3.garage.initHulls(HullDB.hulls.length);
      var3.garage.setHull(0, 0);
      var3.garage.initTurrets(TurretDB.turrets.length);
      var3.garage.setTurret(0, 0);
      var3.garage.initColormaps(ColormapDB.colormaps.length);
      var3.garage.initNumerics(NumericDB.numerics.length);
      var3.garage.setColormap(ColormapDB.getSpecialColormapNum(), 1);
   }

   public static void dispose() {
      users = new Vector();
   }

   public static int getNumUsers() {
      return users.size();
   }

   public static User getUser(int var0) {
      return (User)users.elementAt(var0);
   }

   public static User getUser(String var0) {
      for(int var1 = 0; var1 < users.size(); ++var1) {
         User var2 = (User)users.elementAt(var1);
         if(var2.login.equals(var0)) {
            return var2;
         }
      }

      return null;
   }

   public static User checkEnter(String var0, String var1) {
      for(int var2 = 0; var2 < users.size(); ++var2) {
         User var3 = (User)users.elementAt(var2);
         if(var3.check(var0, var1)) {
            return var3;
         }
      }

      return null;
   }

}
