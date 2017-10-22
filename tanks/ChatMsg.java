package tanks;

import tanks.RankUtils;
import tanks.DataBase.User;

public class ChatMsg {

   public User u;
   public String text;


   public ChatMsg(User var1, String var2) {
      this.u = var1;
      this.text = var2;
   }

   public String getName() {
      return this.u.login;
   }

   public int getRank() {
      return RankUtils.getRankNum(this.u.garage.score);
   }
}
