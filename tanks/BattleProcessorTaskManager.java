package tanks;

import java.util.Vector;
import tanks.BattleProcessor;
import tanks.BattleProcessorTask;

public class BattleProcessorTaskManager {

   public BattleProcessor bp;
   public Vector tasks = new Vector();
   public int tnum = 0;


   public BattleProcessorTaskManager(BattleProcessor var1) {
      this.bp = var1;
   }

   public void addTask(BattleProcessorTask var1) {
      this.tasks.addElement(var1);
      ++this.tnum;
   }

   public void removeTask(int var1) {
      this.tasks.removeElementAt(var1);
      --this.tnum;
   }

   public void update(long var1) {
      long var3 = var1 + (long)this.bp.ping;

      for(int var5 = 0; var5 < this.tnum; ++var5) {
         BattleProcessorTask var6 = (BattleProcessorTask)this.tasks.elementAt(var5);
         if(var3 >= var6.time) {
            this.bp.startTask(var6);
            this.removeTask(var5);
         }
      }

   }
}
