package tanks.utils;

import tanks.utils.Vector3;

public class Vector3I {

   public int x;
   public int y;
   public int z;


   public Vector3I(int var1, int var2, int var3) {
      this.x = var1;
      this.y = var2;
      this.z = var3;
   }

   public void trace() {
      System.out.println("[Vector3I " + this.x + ", " + this.y + ", " + this.z + "]");
   }

   public Vector3I() {
      this.x = 0;
      this.y = 0;
      this.z = 0;
   }

   public Vector3I scale(int var1) {
      this.x *= var1;
      this.y *= var1;
      this.z *= var1;
      return this;
   }

   public Vector3I invert() {
      this.x = -this.x;
      this.y = -this.y;
      this.z = -this.z;
      return this;
   }

   public Vector3I add(Vector3I var1) {
      this.x += var1.x;
      this.y += var1.y;
      this.z += var1.z;
      return this;
   }

   public Vector3 getVector3() {
      return new Vector3((float)this.x, (float)this.y, (float)this.z);
   }
}
