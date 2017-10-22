package tanks.utils;

import tanks.utils.Vector3I;

public class Vector3 {

   public float x;
   public float y;
   public float z;


   public Vector3(float var1, float var2, float var3) {
      this.x = var1;
      this.y = var2;
      this.z = var3;
   }

   public void trace() {
      System.out.println("[Vector3 " + this.x + ", " + this.y + ", " + this.z + "]");
   }

   public String getStr(String var1) {
      return this.x + var1 + this.y + var1 + this.z;
   }

   public Vector3() {
      this.x = 0.0F;
      this.y = 0.0F;
      this.z = 0.0F;
   }

   public Vector3 scale(float var1) {
      this.x *= var1;
      this.y *= var1;
      this.z *= var1;
      return this;
   }

   public Vector3 invert() {
      this.x = -this.x;
      this.y = -this.y;
      this.z = -this.z;
      return this;
   }

   public Vector3 add(Vector3 var1) {
      this.x += var1.x;
      this.y += var1.y;
      this.z += var1.z;
      return this;
   }

   public Vector3I getVector3I() {
      return new Vector3I((int)this.x, (int)this.y, (int)this.z);
   }
}
