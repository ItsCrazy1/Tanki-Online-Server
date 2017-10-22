package tanks;

import java.net.Socket;
import tanks.Main;
import tanks.SocketProcessor;
import tanks.DataBase.User;
import tanks.DataBase.Users;

public class Starter extends SocketProcessor implements Runnable {

   private static String WRONG_LOGIN = "РќРµРІРµСЂРЅРѕРµ РёРјСЏ РїРѕР»СЊР·РѕРІР°С‚РµР»СЏ РёР»Рё РїР°СЂРѕР»СЊ";


   public Starter(Socket var1) {
      super(var1);
   }

   public void run() {
      this.send("login;");
      System.out.println("Client enter...");

      while(this.state == STATE_NORM) {
         try {
            Thread.sleep(100L);
         } catch (Exception var6) {
            ;
         }

         String var1 = this.readInput();
         if(var1 != null && var1 != "") {
            String[] var2 = var1.split(";");
            if(var2[0].equals("login")) {
               String var3 = var2[1];
               String var4 = var2[2];
               User var5 = Users.checkEnter(var3, var4);
               if(var5 == null) {
                  this.send("error;" + WRONG_LOGIN);
               } else {
                  this.state = STATE_STOP;
                  Main.startLobby(this, var5);
               }
            }
         }
      }

   }

}
