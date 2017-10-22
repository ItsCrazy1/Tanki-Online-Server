package tanks;


public class RankUtils {

   private static int[] rankScores = new int[]{0, 100, 500, 1500, 3700, 7100, 12300, 20000, 29000, '\ua028', '\udea8', 76000, 98000, 125000, 156000, 192000, 233000, 280000, 332000, 390000, 455000, 527000, 606000, 692000, 787000, 889000, 1000000};


   public static byte getRankNum(int var0) {
      byte var1 = 0;

      for(byte var2 = 0; var2 < rankScores.length; ++var2) {
         if(var0 >= rankScores[var2]) {
            var1 = var2;
         }
      }

      return var1;
   }

}
