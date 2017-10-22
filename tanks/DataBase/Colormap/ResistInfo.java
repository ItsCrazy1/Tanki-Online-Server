package tanks.DataBase.Colormap;


public class ResistInfo {

   public byte smoky;
   public byte flamethrower;
   public byte twins;
   public byte railgun;


   public ResistInfo(int var1, int var2, int var3, int var4) {
      this.smoky = (byte)var1;
      this.flamethrower = (byte)var2;
      this.twins = (byte)var3;
      this.railgun = (byte)var4;
   }
}
