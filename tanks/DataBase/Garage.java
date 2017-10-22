package tanks.DataBase;


public class Garage {

   public int crystalls = 5;
   public int score = 0;
   public byte[] hulls;
   public byte[] turrets;
   public byte[] colormaps;
   public int[] numerics;
   public int currHull = 0;
   public int currTurret = 0;
   public int currColormap = 0;


   public void initHulls(int var1) {
      this.hulls = new byte[var1];

      for(int var2 = 0; var2 < var1; ++var2) {
         this.hulls[var2] = -1;
      }

   }

   public void initTurrets(int var1) {
      this.turrets = new byte[var1];

      for(int var2 = 0; var2 < var1; ++var2) {
         this.turrets[var2] = -1;
      }

   }

   public void initColormaps(int var1) {
      this.colormaps = new byte[var1];

      for(int var2 = 0; var2 < var1; ++var2) {
         this.colormaps[var2] = 0;
      }

   }

   public void initNumerics(int var1) {
      this.numerics = new int[var1];

      for(int var2 = 0; var2 < var1; ++var2) {
         this.numerics[var2] = 0;
      }

   }

   public String getNumericItemsStr() {
      return this.numerics[0] + ";" + this.numerics[1] + ";" + this.numerics[2] + ";" + this.numerics[3] + ";" + this.numerics[4];
   }

   public void setHull(int var1, int var2) {
      this.hulls[var1] = (byte)var2;
   }

   public void setTurret(int var1, int var2) {
      this.turrets[var1] = (byte)var2;
   }

   public void setColormap(int var1, int var2) {
      this.colormaps[var1] = (byte)var2;
   }

   public void setNumeric(int var1, int var2) {
      this.numerics[var1] = var2;
   }
}
