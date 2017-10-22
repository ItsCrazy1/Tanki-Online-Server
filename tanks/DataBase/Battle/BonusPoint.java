package tanks.DataBase.Battle;

import tanks.utils.Vector3;

public class BonusPoint {

   public boolean med;
   public boolean armr;
   public boolean damage;
   public boolean nitro;
   public boolean crystall;
   public boolean gold;
   public Vector3 posmin;
   public Vector3 posmax;


   public BonusPoint(boolean var1, boolean var2, boolean var3, boolean var4, boolean var5, boolean var6, Vector3 var7, Vector3 var8) {
      this.med = var1;
      this.armr = var2;
      this.damage = var3;
      this.nitro = var4;
      this.crystall = var5;
      this.gold = var6;
      this.posmin = var7;
      this.posmax = var8;
   }

   public void trace() {
      System.out.println(this.med + ";" + this.armr + ";" + this.damage + ";" + this.nitro + ";" + this.crystall + ";" + this.gold);
      this.posmin.trace();
      this.posmax.trace();
   }
}
