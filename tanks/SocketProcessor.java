package tanks;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

class SocketProcessor {

   public static byte STATE_NORM = 1;
   public static byte STATE_ERROR = 2;
   public static byte STATE_STOP = 3;
   public byte state;
   public Socket s;
   public InputStream is;
   public OutputStream os;


   public SocketProcessor(Socket var1, InputStream var2, OutputStream var3) {
      this.state = STATE_NORM;
      this.s = var1;
      this.is = var2;
      this.os = var3;
   }

   public SocketProcessor(Socket var1) {
      this.state = STATE_NORM;

      try {
         this.s = var1;
         this.is = var1.getInputStream();
         this.os = var1.getOutputStream();
      } catch (Throwable var3) {
         this.state = STATE_ERROR;
      }

   }

   public void trace(String var1) {
      System.err.println(var1);
   }

   public void send(String var1) {
      try {
         this.os.write(var1.getBytes());
         this.os.flush();
      } catch (Exception var3) {
         this.state = STATE_ERROR;
      }

   }

   public String readInput() {
      try {
         byte[] var1 = new byte[1024];
         if(this.is.available() > 0) {
            this.is.read(var1);
            return new String(var1);
         } else {
            return null;
         }
      } catch (Exception var3) {
         this.state = STATE_ERROR;
         return null;
      }
   }

}
