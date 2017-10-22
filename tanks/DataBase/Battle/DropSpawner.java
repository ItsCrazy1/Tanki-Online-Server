package tanks.DataBase.Battle;

import tanks.Battle;
import tanks.DataBase.Battle.BonusDB;

public class DropSpawner implements Runnable {

   private static int TIME_SEND_DROP = 20;
   private Battle b;
   public boolean started = true;


   public DropSpawner(Battle var1) {
      this.b = var1;
   }

   public void run() {
      for(; this.started; BonusDB.spawnDrop(this.b)) {
         try {
            Thread.sleep((long)(TIME_SEND_DROP * 1000));
         } catch (Exception var2) {
            ;
         }
      }

   }

}
