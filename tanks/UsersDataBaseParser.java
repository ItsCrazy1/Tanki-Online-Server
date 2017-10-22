package tanks;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import tanks.DataBase.Users;

public class UsersDataBaseParser {

   public static void load(String var0) {
      try {
         File var1 = new File(var0);
         if(!var1.exists()) {
            return;
         }

         Scanner var2 = new Scanner(var1);

         while(var2.hasNextLine()) {
            Users.addUser(var2.nextLine(), var2.nextLine(), Integer.parseInt(var2.nextLine()));
         }

         var2.close();
      } catch (IOException var3) {
         ;
      }

   }
}
