package tanks.DataBase.Battle;

import tanks.BattleProcessor;
import tanks.utils.Vector3;

public class Mine {

   private static long currId = 0L;
   public BattleProcessor owner;
   public long id;
   public Vector3 pos;
   public Vector3 rot;


   public Mine(BattleProcessor var1, Vector3 var2, Vector3 var3) {
      this.id = (long)(currId++);
      this.owner = var1;
      this.pos = var2;
      this.rot = var3;
   }

}
