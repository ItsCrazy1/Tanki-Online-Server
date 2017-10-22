package tanks.DataBase.Colormap;

import tanks.DataBase.Colormap.ResistInfo;

public class Colormap {

   public int price;
   public byte rank;
   public byte type;
   public ResistInfo res;


   public Colormap(int var1, int var2, int var3) {
      this.type = (byte)var1;
      this.price = var2;
      this.rank = (byte)var3;
      this.res = new ResistInfo(0, 0, 0, 0);
   }

   public Colormap(int var1, int var2, int var3, ResistInfo var4) {
      this.type = (byte)var1;
      this.price = var2;
      this.rank = (byte)var3;
      this.res = var4;
   }
}
