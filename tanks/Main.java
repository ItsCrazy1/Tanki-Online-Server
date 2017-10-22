package tanks;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;
import tanks.BattleProcessor;
import tanks.Battles;
import tanks.ControlWindow;
import tanks.GarageDataBaseParser;
import tanks.Lobby;
import tanks.SocketProcessor;
import tanks.Starter;
import tanks.UsersDataBaseParser;
import tanks.DataBase.User;
import tanks.DataBase.Users;
import tanks.DataBase.Battle.BonusDB;
import tanks.DataBase.Colormap.ColormapDB;
import tanks.DataBase.Hull.HullDB;
import tanks.DataBase.Numeric.NumericDB;
import tanks.DataBase.Turret.TurretDB;

public class Main implements Runnable {

   private static Main instance;
   private ServerSocket ss;
   private static double version = 0.1D;
   public static Lobby systemUser;
   public static Vector lobbys = new Vector();
   private static boolean isDebug = false;
   private static ControlWindow cw;
   private static boolean started = false;


   public static void main(String[] var0) throws Exception {
      cw = new ControlWindow();
      cw.setVisible(true);
   }

   private static void disposeLobbys() {
      for(int var0 = 0; var0 < lobbys.size(); ++var0) {
         Lobby var1 = (Lobby)lobbys.elementAt(var0);
         var1.state = SocketProcessor.STATE_ERROR;

         try {
            var1.is.close();
            var1.os.close();
            var1.s.close();
         } catch (Exception var3) {
            ;
         }
      }

      lobbys = new Vector();
   }

   public static void stopServer() {
      if(started) {
         GarageDataBaseParser.save();
         started = false;
         NumericDB.dispose();
         Battles.dispose();
         disposeLobbys();
         HullDB.dispose();
         TurretDB.dispose();
         ColormapDB.dispose();
         disposeUsers();
         BonusDB.dispose();
         System.gc();
         cw.showMessage("Сервер остановлен!", "Готово");
      }
   }

   public static void startServer() {
      try {
         started = true;
         NumericDB.init();
         HullDB.init();
         TurretDB.init();
         ColormapDB.init();
         initUsers();
         BonusDB.init();
         ServerSocket var0;
         if(isDebug) {
            var0 = new ServerSocket(2845);
         } else {
            var0 = new ServerSocket(5482);
         }

         (new Thread(new Main(var0))).start();
         cw.showMessage("Сервер запущен!", "Готово");
      } catch (Exception var1) {
         cw.showMessage(var1.getMessage(), "Ошибка!");
      }

   }

   public Main(ServerSocket var1) {
      instance = this;
      this.ss = var1;
   }

   public void run() {
      while(true) {
         try {
            if(started) {
               Socket var1 = this.ss.accept();
               (new Thread(new Starter(var1))).start();
               continue;
            }
         } catch (Exception var3) {
            cw.showMessage(var3.getMessage(), "Ошибка!");
         }

         try {
            this.ss.close();
         } catch (Exception var2) {
            cw.showMessage(var2.getMessage(), "Ошибка!");
         }

         this.ss = null;
         return;
      }
   }

   private static void disposeUsers() {
      Users.dispose();
   }

   private static void initUsers() {
      UsersDataBaseParser.load("ServerDataBase/Users.txt");
      systemUser = new Lobby(Users.getUser(0), (Socket)null, (InputStream)null, (OutputStream)null);
      GarageDataBaseParser.load();
   }

   public static void removeLobby(int var0) {
      Lobby var1 = (Lobby)lobbys.elementAt(var0);
      lobbys.removeElementAt(var0);
      var1 = null;
   }

   public static void removeLobby(Lobby var0) {
      lobbys.removeElement(var0);
      var0 = null;
   }

   public static void startLobby(BattleProcessor var0, User var1) {
      Lobby var2 = new Lobby(var1, var0.s, var0.is, var0.os);
      lobbys.addElement(var2);
      (new Thread(var2)).start();
   }

   public static void startLobby(Starter var0, User var1) {
      cw.listModel.addElement(var1.login);
      Battles.check(var1);
      Lobby var2 = new Lobby(var1, var0.s, var0.is, var0.os);
      lobbys.addElement(var2);
      (new Thread(var2)).start();
      var0 = null;
   }

}
