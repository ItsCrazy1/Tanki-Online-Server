package tanks.DataBase.Hull;

import tanks.DataBase.Hull.Hull;
import tanks.DataBase.Hull.HullM;

public class HullDB {

   public static Hull[] hulls;


   public static void dispose() {
      hulls = null;
   }

   public static void init() {
      hulls = new Hull[4];
      hulls[0] = new Hull(new HullM(0, 0, 30), new HullM(50, 2, 35), new HullM(150, 4, 45), new HullM(500, 6, 60));
      hulls[1] = new Hull(new HullM(10, 2, 45), new HullM(70, 3, 53), new HullM(210, 5, 68), new HullM(700, 7, 90));
      hulls[2] = new Hull(new HullM(20, 3, 60), new HullM(100, 4, 70), new HullM(300, 6, 90), new HullM(900, 8, 120));
      hulls[3] = new Hull(new HullM(150, 7, 60), new HullM(500, 8, 70), new HullM(1500, 9, 90), new HullM(5000, 10, 120));
   }
}
