package tanks.DataBase.Turret;

import tanks.DataBase.Turret.Turret;
import tanks.DataBase.Turret.TurretM;

public class TurretDB {

   public static Turret[] turrets;


   public static void dispose() {
      turrets = null;
   }

   public static void init() {
      turrets = new Turret[4];
      turrets[0] = new Turret(new TurretM(0, 0, "0;m0"), new TurretM(30, 2, "0;m1"), new TurretM(90, 4, "0;m2"), new TurretM(300, 6, "0;m3"));
      turrets[1] = new Turret(new TurretM(5, 3, "1;m0"), new TurretM(40, 4, "1;m1"), new TurretM(120, 6, "1;m2"), new TurretM(400, 8, "1;m3"));
      turrets[2] = new Turret(new TurretM(10, 4, "2;m0"), new TurretM(50, 5, "2;m1"), new TurretM(150, 7, "2;m2"), new TurretM(500, 9, "2;m3"));
      turrets[3] = new Turret(new TurretM(100, 6, "3;m0"), new TurretM(350, 7, "3;m1"), new TurretM(1100, 8, "3;m2"), new TurretM(3700, 9, "3;m3"));
   }
}
