package tanks;

import java.util.Vector;
import tanks.BattleProcessor;
import tanks.Lobby;
import tanks.Main;
import tanks.SocketProcessor;
import tanks.DataBase.User;
import tanks.DataBase.Battle.Bonus;
import tanks.DataBase.Battle.DropSpawner;
import tanks.DataBase.Battle.Mine;

public class Battle {

   public static int id = 0;
   public Vector pr = new Vector();
   public Vector pb = new Vector();
   public byte type;
   public byte max;
   public int dest;
   public String name;
   public String mapName;
   public String mapXml;
   public int bid;
   public volatile BattleProcessor[] processors;
   public int pnum = 0;
   private int currId = 0;
   public DropSpawner ds;
   public int fond = 0;
   public int crysFond = 0;
   public int goldFond = 0;
   public volatile Bonus[] bonuses;
   public int bnum = 0;
   public volatile Mine[] mines;
   public int mnum = 0;


   public Battle(byte var1, byte var2, int var3, String var4, String var5, String var6) {
      this.type = var1;
      this.max = var2;
      this.dest = var3;
      this.name = var4;
      this.mapName = var5;
      this.mapXml = var6;
      this.bid = id++;
      this.ds = new DropSpawner(this);
      (new Thread(this.ds)).start();
   }

   public void end() {
      int[] var1 = this.calcRewards();

      int var2;
      for(var2 = 0; var2 < this.pnum; ++var2) {
         for(int var3 = 0; var3 < this.pnum; ++var3) {
            if(var2 == var3) {
               this.processors[var2].send("rw;-1;" + var1[var2] + ";");
            } else {
               this.processors[var2].send("rw;" + this.processors[var3].id + ";" + var1[var3] + ";");
            }
         }
      }

      for(var2 = 0; var2 < this.pnum; ++var2) {
         this.processors[var2].send("bpause;");
      }

      this.fond = 0;
      this.crysFond = 0;
      this.goldFond = 0;

      for(var2 = 0; var2 < this.pnum; ++var2) {
         this.processors[var2].score = 0;
         this.processors[var2].destructs = 0;
      }

   }

   private int[] calcRewards() {
      int[] var1 = new int[this.pnum];
      int var2 = 0;

      for(int var3 = 0; var3 < this.pnum; ++var3) {
         var2 += this.processors[var3].score;
      }

      float var5 = (float)(this.fond / var2);

      for(int var4 = 0; var4 < this.pnum; ++var4) {
         var1[var4] = (int)Math.floor((double)(var5 * (float)this.processors[var4].score));
      }

      return var1;
   }

   public void dispose() {
      for(int var1 = 0; var1 < this.pnum; ++var1) {
         this.processors[var1].state = SocketProcessor.STATE_ERROR;

         try {
            this.processors[var1].is.close();
            this.processors[var1].os.close();
            this.processors[var1].s.close();
         } catch (Exception var3) {
            ;
         }
      }

      this.processors = null;
      this.bonuses = null;
   }

   public void removeBonusArr(int var1) {
      if(this.bnum > var1 && var1 >= 0) {
         Bonus[] var2 = new Bonus[this.bnum - 1];
         System.arraycopy(this.bonuses, 0, var2, 0, var1);
         System.arraycopy(this.bonuses, var1 + 1, var2, var1, this.bnum - var1 - 1);
         this.bonuses = var2;
         var2 = null;
         --this.bnum;
      }
   }

   public void removeMineArr(int var1) {
      if(this.mnum > var1 && var1 >= 0) {
         Mine[] var2 = new Mine[this.bnum - 1];
         System.arraycopy(this.mines, 0, var2, 0, var1);
         System.arraycopy(this.mines, var1 + 1, var2, var1, this.mnum - var1 - 1);
         this.mines = var2;
         var2 = null;
         --this.mnum;
      }
   }

   public void removeProcessorArr(int var1) {
      if(this.pnum > var1 && var1 >= 0) {
         BattleProcessor[] var2 = new BattleProcessor[this.pnum - 1];
         System.arraycopy(this.processors, 0, var2, 0, var1);
         System.arraycopy(this.processors, var1 + 1, var2, var1, this.pnum - var1 - 1);
         this.processors = var2;
         var2 = null;
         --this.pnum;
      }
   }

   public void addProcessorArr(BattleProcessor var1) {
      if(this.pnum == 0) {
         this.processors = new BattleProcessor[1];
         this.processors[0] = var1;
         this.pnum = 1;
      } else {
         BattleProcessor[] var2 = new BattleProcessor[this.pnum + 1];
         System.arraycopy(this.processors, 0, var2, 0, this.pnum);
         var2[this.pnum] = var1;
         this.processors = var2;
         var2 = null;
         ++this.pnum;
      }

   }

   public void addBonusArr(Bonus var1) {
      if(this.bnum == 0) {
         this.bonuses = new Bonus[1];
         this.bonuses[0] = var1;
         this.bnum = 1;
      } else {
         Bonus[] var2 = new Bonus[this.bnum + 1];
         System.arraycopy(this.bonuses, 0, var2, 0, this.bnum);
         var2[this.bnum] = var1;
         this.bonuses = var2;
         var2 = null;
         ++this.bnum;
      }

   }

   public void addMineArr(Mine var1) {
      if(this.mnum == 0) {
         this.mines = new Mine[1];
         this.mines[0] = var1;
         this.mnum = 1;
      } else {
         Mine[] var2 = new Mine[this.mnum + 1];
         System.arraycopy(this.mines, 0, var2, 0, this.mnum);
         var2[this.mnum] = var1;
         this.mines = var2;
         var2 = null;
         ++this.mnum;
      }

   }

   public void takeBonus(BattleProcessor var1, long var2) {
      for(int var4 = 0; var4 < this.bnum; ++var4) {
         Bonus var5 = this.bonuses[var4];
         if(var5.id == var2) {
            this.removeBonusArr(var4);
            this.tb(var1, var2);
            if(var5.type == 0) {
               var1.heal();

               for(int var6 = 0; var6 < this.pnum; ++var6) {
                  if(this.processors[var6].id == var1.id) {
                     this.processors[var6].send("est;-1;3;3;");
                  } else {
                     this.processors[var6].send("est;" + var1.id + ";3;3;");
                  }
               }
            }

            if(var5.type > 0 && var5.type < 4) {
               var1.startEffect(var5.type - 1, 30);
            }

            if(var5.type == 4) {
               ++var1.u.garage.crystalls;
               var1.send("cryst;" + var1.u.garage.crystalls + ";");
            }

            if(var5.type == 5) {
               var1.u.garage.crystalls += 100;
               var1.send("cryst;" + var1.u.garage.crystalls + ";");
            }

            var5 = null;
            return;
         }
      }

   }

   private void tb(BattleProcessor var1, long var2) {
      for(int var4 = 0; var4 < this.pnum; ++var4) {
         BattleProcessor var5 = this.processors[var4];
         if(var5.id != var1.id) {
            var5.send("takeb;" + var2 + ";");
         }
      }

   }

   public boolean check(User var1) {
      for(int var2 = 0; var2 < this.pnum; ++var2) {
         BattleProcessor var3 = this.processors[var2];
         if(var3.u.login.equals(var1.login)) {
            this.removeProcessorArr(var2);

            try {
               var3.is.close();
               var3.os.close();
               var3.s.close();
            } catch (Exception var5) {
               ;
            }

            var3.is = null;
            var3.os = null;
            var3.s = null;
            this.removePlayer(var3.u, var3.command);
            var3 = null;
            return true;
         }
      }

      return false;
   }

   public void sendCloseToAll(int var1, String var2) {
      for(int var3 = 0; var3 < this.pnum; ++var3) {
         BattleProcessor var4 = this.processors[var3];
         var4.send("log;" + var1 + ";" + var2 + ";РїРѕРєРёРЅСѓР» Р±РѕР№;");
      }

   }

   public void removeProcessor(BattleProcessor var1) {
      for(int var2 = 0; var2 < this.pnum; ++var2) {
         if(this.processors[var2] == var1) {
            this.removeProcessorMines(var1);
            this.removeProcessorArr(var2);
            var1 = null;
            return;
         }
      }

   }

   public void removeProcessorMines(BattleProcessor var1) {
      for(int var2 = 0; var2 < this.mnum; ++var2) {
         if(this.mines[var2].owner.id == var1.id) {
            for(int var3 = 0; var3 < this.pnum; ++var3) {
               if(this.processors[var3].id != var1.id) {
                  this.processors[var3].send("mined;" + this.mines[var2].id + ";");
               }
            }

            this.removeMineArr(var2);
         }
      }

   }

   public void sendBattlePlayers(Lobby var1) {
      int var2 = this.pr.size();
      int var3 = this.pb.size();
      String var4;
      int var5;
      User var6;
      if(this.type == 0) {
         var4 = "g;";

         for(var5 = 0; var5 < this.pr.size(); ++var5) {
            var6 = (User)this.pr.elementAt(var5);
            var4 = var4 + var6.getRank() + ";" + var6.login + ";";
         }

         var1.send("bp;" + var4 + "e;");
      } else {
         var4 = "r;";

         for(var5 = 0; var5 < this.pr.size(); ++var5) {
            var6 = (User)this.pr.elementAt(var5);
            var4 = var4 + var6.getRank() + ";" + var6.login + ";";
         }

         String var8 = "b;";

         for(int var9 = 0; var9 < this.pb.size(); ++var9) {
            User var7 = (User)this.pb.elementAt(var9);
            var8 = var8 + var7.getRank() + ";" + var7.login + ";";
         }

         var1.send("bp;" + var4 + var8 + "e;");
      }

   }

   public boolean addPlayer(Lobby var1, byte var2) {
      if(!this.addPlayerToList(var1.u, var2)) {
         return false;
      } else {
         BattleProcessor var3 = new BattleProcessor(var1, this, var2, this.currId);
         ++this.currId;
         (new Thread(var3)).start();
         this.addProcessorArr(var3);
         return true;
      }
   }

   private boolean addPlayerToList(User var1, byte var2) {
      if(this.type == 0) {
         if(this.pr.contains(var1)) {
            return false;
         }

         if(this.pr.size() >= this.max) {
            return false;
         }

         this.pr.addElement(var1);
      }

      if(this.type == 1) {
         if(var2 == 0) {
            if(this.pr.contains(var1)) {
               return false;
            }

            if(this.pr.size() >= this.max) {
               return false;
            }

            this.pr.addElement(var1);
         } else {
            if(this.pb.contains(var1)) {
               return false;
            }

            if(this.pb.size() >= this.max) {
               return false;
            }

            this.pb.addElement(var1);
         }
      }

      this.sendToAll();
      return true;
   }

   public String getTeam(byte var1) {
      return this.type == 0?"g":(var1 == 0?"r":"b");
   }

   public void removePlayer(User var1, byte var2) {
      for(int var3 = 0; var3 < this.pnum; ++var3) {
         BattleProcessor var4 = this.processors[var3];
         if(var4.u != var1) {
            var4.send("rt;" + var1.login + ";");
         }
      }

      if(this.type == 0) {
         this.pr.removeElement(var1);
      }

      if(this.type == 1) {
         if(var2 == 0) {
            this.pr.removeElement(var1);
         } else {
            this.pb.removeElement(var1);
         }
      }

      String var5 = var1.login;
      byte var6 = var1.getRank();
      this.sendCloseToAll(var6, var5);
      this.sendToAll();
   }

   private void sendToAll() {
      for(int var1 = 0; var1 < Main.lobbys.size(); ++var1) {
         Lobby var2 = (Lobby)Main.lobbys.elementAt(var1);
         var2.send("bpi;" + this.bid + ";" + this.pr.size() + ";" + this.pb.size() + ";");
      }

   }

   public void sendPlayers(Lobby var1) {
      var1.send("bpi;" + this.bid + ";" + this.pr.size() + ";" + this.pb.size() + ";");
   }

   public void sendTo(Lobby var1) {
      var1.send("ab;" + this.type + ";" + this.max + ";" + this.dest + ";" + this.name + ";" + this.mapName + ";" + this.mapXml + ";" + this.bid + ";");
   }

}
