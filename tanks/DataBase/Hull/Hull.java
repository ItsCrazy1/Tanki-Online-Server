package tanks.DataBase.Hull;

import tanks.DataBase.Hull.HullM;

public class Hull {

   public HullM[] hm = new HullM[4];


   public Hull(HullM var1, HullM var2, HullM var3, HullM var4) {
      this.hm[0] = var1;
      this.hm[1] = var2;
      this.hm[2] = var3;
      this.hm[3] = var4;
   }
}
