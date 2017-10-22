package tanks.DataBase.Numeric;

import tanks.DataBase.Numeric.Numeric;

public class NumericDB {

   public static Numeric[] numerics;


   public static void dispose() {
      numerics = null;
   }

   public static void init() {
      numerics = new Numeric[5];
      numerics[0] = new Numeric(15, 2);
      numerics[1] = new Numeric(5, 3);
      numerics[2] = new Numeric(5, 3);
      numerics[3] = new Numeric(5, 3);
      numerics[4] = new Numeric(10, 9);
   }
}
