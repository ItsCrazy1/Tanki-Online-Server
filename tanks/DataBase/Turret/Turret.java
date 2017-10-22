package tanks.DataBase.Turret;

import tanks.DataBase.Turret.TurretM;

public class Turret {

   public TurretM[] tm = new TurretM[4];


   public Turret(TurretM var1, TurretM var2, TurretM var3, TurretM var4) {
      this.tm[0] = var1;
      this.tm[1] = var2;
      this.tm[2] = var3;
      this.tm[3] = var4;
   }
}
