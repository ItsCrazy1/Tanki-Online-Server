package tanks.DataBase.Battle;


public class Bonus {

   private static long currId = 0L;
   public long id;
   public byte type;


   public Bonus(byte var1) {
      this.id = (long)(currId++);
      this.type = var1;
   }

}
