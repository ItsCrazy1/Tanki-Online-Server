package tanks.DataBase;

import tanks.BattleProcessor;
import tanks.RankUtils;
import tanks.DataBase.Garage;
import tanks.DataBase.Colormap.ColormapDB;
import tanks.DataBase.Hull.HullDB;
import tanks.DataBase.Turret.TurretDB;

public class User {

   public String login;
   public String pass;
   public byte type;
   public Garage garage = new Garage();


   public User(String var1, String var2, byte var3) {
      this.login = var1;
      this.pass = var2;
      this.type = var3;
      this.garage.currColormap = ColormapDB.getSpecialColormapNum();
   }

   public boolean check(String var1, String var2) {
      return var1.equals(this.login) && var2.equals(this.pass) && this.type != -1;
   }

   public byte getRank() {
      return RankUtils.getRankNum(this.garage.score);
   }

   public String getNumericItemsStr() {
      return this.garage.getNumericItemsStr();
   }

   public void sendBattleInfo(BattleProcessor var1, int var2, String var3, int var4) {
      int var5 = HullDB.hulls[this.garage.currHull].hm[this.garage.hulls[this.garage.currHull]].health;
      var1.send("ct;" + this.garage.currTurret + ";" + this.garage.turrets[this.garage.currTurret] + ";" + this.garage.currHull + ";" + this.garage.hulls[this.garage.currHull] + ";" + this.garage.currColormap + ";" + TurretDB.turrets[this.garage.currTurret].tm[this.garage.turrets[this.garage.currTurret]].params + ";" + var2 + ";" + var3 + ";" + this.getRank() + ";" + this.login + ";" + var4 + ";" + var5 + ";");
   }
}
