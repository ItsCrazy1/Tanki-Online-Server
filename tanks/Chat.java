package tanks;

import java.util.Vector;
import tanks.ChatMsg;
import tanks.Lobby;
import tanks.Main;
import tanks.SocketProcessor;

public class Chat {

   public static int MAX_MSGS = 50;
   public static Vector msgs = new Vector();


   public static void addMessage(Lobby var0, String var1) {
      ChatMsg var2 = new ChatMsg(var0.u, var1);
      msgs.addElement(var2);
      sendToAll(var0, var2);

      while(msgs.size() > 50) {
         msgs.removeElementAt(0);
      }

   }

   public static void clear() {
      int var0 = 0;

      for(msgs = new Vector(); var0 < Main.lobbys.size(); ++var0) {
         Lobby var1 = (Lobby)Main.lobbys.elementAt(var0);
         if(var1.state != SocketProcessor.STATE_NORM) {
            Main.removeLobby(var0);
         }

         var1.send("clrch;");
      }

   }

   public static void clearu(String var0) {
      int var1 = 0;

      for(msgs = new Vector(); var1 < Main.lobbys.size(); ++var1) {
         Lobby var2 = (Lobby)Main.lobbys.elementAt(var1);
         if(var2.state != SocketProcessor.STATE_NORM) {
            Main.removeLobby(var1);
         }

         var2.send("clrchu;" + var0 + ";");
      }

   }

   private static void sendToAll(Lobby var0, ChatMsg var1) {
      for(int var2 = 0; var2 < Main.lobbys.size(); ++var2) {
         Lobby var3 = (Lobby)Main.lobbys.elementAt(var2);
         if(var3.state != SocketProcessor.STATE_NORM) {
            Main.removeLobby(var2);
         }

         var3.send("cm;" + var1.getName() + ";" + var1.getRank() + ";" + var1.text + ";");
      }

   }

   public static void sendMsgs(Lobby var0) {
      try {
         Thread.sleep(10L);
      } catch (Exception var5) {
         ;
      }

      for(int var1 = 0; var1 < msgs.size(); ++var1) {
         ChatMsg var2 = (ChatMsg)msgs.elementAt(var1);
         var0.send("cm;" + var2.getName() + ";" + var2.getRank() + ";" + var2.text + ";");

         try {
            Thread.sleep(10L);
         } catch (Exception var4) {
            ;
         }
      }

   }

}
