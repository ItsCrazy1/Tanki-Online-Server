package tanks;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Vector;
import tanks.BanList;
import tanks.Battle;
import tanks.Battles;
import tanks.Chat;
import tanks.Main;
import tanks.SocketProcessor;
import tanks.DataBase.User;
import tanks.DataBase.Users;
import tanks.DataBase.Battle.BonusDB;
import tanks.DataBase.Colormap.Colormap;
import tanks.DataBase.Colormap.ColormapDB;
import tanks.DataBase.Hull.HullDB;
import tanks.DataBase.Hull.HullM;
import tanks.DataBase.Numeric.Numeric;
import tanks.DataBase.Numeric.NumericDB;
import tanks.DataBase.Turret.TurretDB;
import tanks.DataBase.Turret.TurretM;

public class Lobby extends SocketProcessor implements Runnable {

   public User u;


   public Lobby(User var1, Socket var2, InputStream var3, OutputStream var4) {
      super(var2, var3, var4);
      this.u = var1;
   }

   private int getHullNum() {
      return this.u.garage.hulls[this.u.garage.currHull] == -1?0:this.u.garage.currHull * 4 + this.u.garage.hulls[this.u.garage.currHull];
   }

   private int getTurretNum() {
      return this.u.garage.turrets[this.u.garage.currTurret] == -1?0:this.u.garage.currTurret * 4 + this.u.garage.turrets[this.u.garage.currTurret];
   }

   private int getColormapNum() {
      return this.u.garage.colormaps[this.u.garage.currColormap] == -1?0:this.u.garage.currColormap;
   }

   private byte getB(String var1) {
      return (byte)Integer.parseInt(var1);
   }

   private int getI(String var1) {
      return Integer.parseInt(var1);
   }

   public void run() {
      this.send("lobby;" + this.u.login + ";" + this.u.garage.crystalls + ";" + this.u.garage.score + ";" + this.getHullNum() + ";" + this.getTurretNum() + ";" + this.getColormapNum() + ";");
      Chat.sendMsgs(this);
      Battles.sendBattles(this);
      Battles.sendPlayers(this);
      System.out.println("User enter, starting lobby...");

      while(this.state == STATE_NORM) {
         try {
            Thread.sleep(100L);
         } catch (Exception var9) {
            ;
         }

         String var1 = this.readInput();
         if(var1 != null && var1 != "") {
            String[] var2 = var1.split(";");
            if(var2[0].equals("chat")) {
               if(var2[1].substring(0, 1).equals("/")) {
                  var2[1] = var2[1].substring(1);
                  String[] var3 = var2[1].split(" ");
                  int var4;
                  if(this.u.type == 0 || this.u.type == 1) {
                     if(var3[0].equals("addscore")) {
                        try {
                           var4 = this.getI(var3[1]);
                           this.u.garage.score += var4;
                           this.refreshUser();
                           this.sendGarage();
                        } catch (Exception var8) {
                           ;
                        }
                        continue;
                     }

                     if(var3[0].equals("addcry")) {
                        try {
                           var4 = this.getI(var3[1]);
                           this.u.garage.crystalls += var4;
                           this.refreshUser();
                        } catch (Exception var7) {
                           ;
                        }
                        continue;
                     }
                  }

                  if(this.u.type == 0) {
                     if(var3[0].equals("clear")) {
                        try {
                           if(var3.length <= 1) {
                              Chat.clear();
                           } else {
                              Chat.clearu(var3[1]);
                           }
                        } catch (Exception var10) {
                           continue;
                        }
                     }

                     if(var3[0].equals("system") && var3.length > 1) {
                        Chat.addMessage(Main.systemUser, var2[1].substring(7));
                     }

                     Battle var5;
                     if(var3[0].equals("crys")) {
                        var4 = this.getI(var3[1]);
                        var5 = Battles.getById(var4);
                        if(var5 == null) {
                           this.send("cm;system;0;Р‘РёС‚РІР° РЅРµ РЅР°Р№РґРµРЅР°!;");
                           continue;
                        }

                        BonusDB.spawnCrystall(var5);
                        this.send("cm;system;0;РљСЂРёСЃС‚Р°Р»Р» СЃР±СЂРѕС€РµРЅ!;");
                     }

                     if(var3[0].equals("gold")) {
                        var4 = this.getI(var3[1]);
                        var5 = Battles.getById(var4);
                        if(var5 == null) {
                           this.send("cm;system;0;Р‘РёС‚РІР° РЅРµ РЅР°Р№РґРµРЅР°!;");
                           continue;
                        }

                        BonusDB.spawnGold(var5);
                        this.send("cm;system;0;Р“РѕР»Рґ СЃР±СЂРѕС€РµРЅ!;");
                     }

                     User var11;
                     if(var3[0].equals("ban") && var3.length > 2) {
                        var11 = Users.getUser(var3[1]);
                        if(var11 == null) {
                           continue;
                        }

                        BanList.addUser(var11);
                        Chat.addMessage(Main.systemUser, "РўР°РЅРєРёСЃС‚ " + var11.login + " Р»РёС€РµРЅ РїСЂР°РІР° РІС‹С…РѕРґР° РІ СЌС„РёСЂ. РџСЂРёС‡РёРЅР°: " + var3[2]);
                     }

                     if(var3[0].equals("unban") && var3.length > 1) {
                        var11 = Users.getUser(var3[1]);
                        if(var11 == null) {
                           continue;
                        }

                        BanList.removeUser(var11);
                        Chat.addMessage(Main.systemUser, "РўР°РЅРєРёСЃС‚Сѓ " + var11.login + " СЂР°Р·СЂРµС€РµРЅ РІС‹С…РѕРґ РІ СЌС„РёСЂ");
                     }
                  }
               } else if(BanList.isBanned(this.u)) {
                  this.send("cm;system;0;Р’С‹ РѕС‚РєР»СЋС‡РµРЅС‹ РѕС‚ С‡Р°С‚Р°;");
               } else {
                  Chat.addMessage(this, var2[1]);
               }
            }

            if(var2[0].equals("enb")) {
               if(Battles.enter(this, this.getI(var2[1]), this.getB(var2[2]))) {
                  this.state = STATE_ERROR;
               } else {
                  try {
                     this.s.close();
                  } catch (Exception var6) {
                     ;
                  }
               }
            }

            if(var2[0].equals("getp")) {
               Battles.sendBattlePlayers(this, this.getI(var2[1]));
            }

            if(var2[0].equals("cb")) {
               Battles.addBattle(this.getB(var2[1]), this.getB(var2[2]), this.getI(var2[3]), var2[4], var2[5], var2[6]);
            }

            if(var2[0].equals("setg")) {
               this.setTank(var2[1], this.getB(var2[2]));
            }

            if(var2[0].equals("buyg")) {
               if(var2[1].equals("n")) {
                  this.buyGarage(var2[1], this.getB(var2[2]), this.getI(var2[3]));
               } else {
                  this.buyGarage(var2[1], this.getB(var2[2]), 0);
               }
            }

            if(var2[0].equals("initg")) {
               this.sendGarage();
            }

            if(var2[0].equals("gi")) {
               this.sendItem(var2[1], var2[2]);
            }
         }
      }

      if(this.state == STATE_ERROR) {
         Main.removeLobby(this);
      }

   }

   private void setTank(String var1, byte var2) {
      if(var1.equals("h") && this.u.garage.hulls[var2] != -1) {
         this.u.garage.currHull = var2;
      }

      if(var1.equals("t") && this.u.garage.turrets[var2] != -1) {
         this.u.garage.currTurret = var2;
      }

      if(var1.equals("c") && this.u.garage.colormaps[var2] != 0) {
         this.u.garage.currColormap = var2;
      }

      this.send("sett;" + this.u.login + ";" + this.u.garage.crystalls + ";" + this.getHullNum() + ";" + this.getTurretNum() + ";" + this.getColormapNum());
   }

   private void buyGarage(String var1, byte var2, int var3) {
      byte var4 = this.u.getRank();
      if(var1.equals("h")) {
         if(this.u.garage.hulls[var2] == 3) {
            return;
         }

         HullM var5 = HullDB.hulls[var2].hm[this.u.garage.hulls[var2] + 1];
         if(this.u.garage.crystalls >= var5.price && var4 >= var5.rank) {
            this.u.garage.crystalls -= var5.price;
            ++this.u.garage.hulls[var2];
         }
      }

      if(var1.equals("t")) {
         if(this.u.garage.turrets[var2] == 3) {
            return;
         }

         TurretM var7 = TurretDB.turrets[var2].tm[this.u.garage.turrets[var2] + 1];
         if(this.u.garage.crystalls >= var7.price && var4 >= var7.rank) {
            this.u.garage.crystalls -= var7.price;
            ++this.u.garage.turrets[var2];
         }
      }

      if(var1.equals("c")) {
         if(this.u.garage.colormaps[var2] == 1) {
            return;
         }

         Colormap var8 = ColormapDB.colormaps[var2];
         if(this.u.garage.crystalls >= var8.price && var4 >= var8.rank) {
            this.u.garage.crystalls -= var8.price;
            this.u.garage.colormaps[var2] = 1;
         }
      }

      if(var1.equals("n")) {
         Numeric var9 = NumericDB.numerics[var2];
         int var6 = var9.price * var3;
         if(this.u.garage.crystalls >= var6 && var4 >= var9.rank) {
            this.u.garage.crystalls -= var6;
            this.u.garage.numerics[var2] += var3;
         }
      }

      this.sendGarage();
      this.send("sett;" + this.u.login + ";" + this.u.garage.crystalls + ";" + this.getHullNum() + ";" + this.getTurretNum() + ";" + this.getColormapNum());
   }

   private void sendItem(String var1, String var2) {
      int var3 = Integer.parseInt(var1);
      byte var4 = this.u.getRank();
      String var5;
      if(var2.equals("hull")) {
         if(this.u.garage.hulls[var3] == -1) {
            if(this.u.garage.crystalls >= HullDB.hulls[var3].hm[0].price && var4 >= HullDB.hulls[var3].hm[0].rank) {
               this.send("gi;h;" + var3 + ";0;" + HullDB.hulls[var3].hm[0].price);
               return;
            }

            if(this.u.garage.crystalls < HullDB.hulls[var3].hm[0].price && var4 >= HullDB.hulls[var3].hm[0].rank) {
               this.send("gi;h;" + var3 + ";1;" + HullDB.hulls[var3].hm[0].price);
               return;
            }

            if(var4 < HullDB.hulls[var3].hm[0].rank && this.u.garage.crystalls >= HullDB.hulls[var3].hm[0].price) {
               this.send("gi;h;" + var3 + ";2;" + HullDB.hulls[var3].hm[0].price + ";" + HullDB.hulls[var3].hm[0].rank);
               return;
            }

            if(this.u.garage.crystalls < HullDB.hulls[var3].hm[0].price && var4 < HullDB.hulls[var3].hm[0].rank) {
               this.send("gi;h;" + var3 + ";3;" + HullDB.hulls[var3].hm[0].price + ";" + HullDB.hulls[var3].hm[0].rank);
               return;
            }
         } else {
            var5 = this.u.garage.currHull == var3?"1":"0";
            if(this.u.garage.hulls[var3] == 3) {
               this.send("gi;h;" + var3 + ";8;" + var5 + ";");
               return;
            }

            if(this.u.garage.crystalls >= HullDB.hulls[var3].hm[this.u.garage.hulls[var3] + 1].price && var4 >= HullDB.hulls[var3].hm[this.u.garage.hulls[var3] + 1].rank) {
               this.send("gi;h;" + var3 + ";4;" + HullDB.hulls[var3].hm[this.u.garage.hulls[var3] + 1].price + ";" + var5 + ";");
               return;
            }

            if(this.u.garage.crystalls < HullDB.hulls[var3].hm[this.u.garage.hulls[var3] + 1].price && var4 >= HullDB.hulls[var3].hm[this.u.garage.hulls[var3] + 1].rank) {
               this.send("gi;h;" + var3 + ";5;" + HullDB.hulls[var3].hm[this.u.garage.hulls[var3] + 1].price + ";" + var5 + ";");
               return;
            }

            if(var4 < HullDB.hulls[var3].hm[this.u.garage.hulls[var3] + 1].rank && this.u.garage.crystalls >= HullDB.hulls[var3].hm[this.u.garage.hulls[var3] + 1].price) {
               this.send("gi;h;" + var3 + ";6;" + HullDB.hulls[var3].hm[this.u.garage.hulls[var3] + 1].price + ";" + HullDB.hulls[var3].hm[this.u.garage.hulls[var3] + 1].rank + ";" + var5 + ";");
               return;
            }

            if(this.u.garage.crystalls < HullDB.hulls[var3].hm[this.u.garage.hulls[var3] + 1].price && var4 < HullDB.hulls[var3].hm[this.u.garage.hulls[var3] + 1].rank) {
               this.send("gi;h;" + var3 + ";7;" + HullDB.hulls[var3].hm[this.u.garage.hulls[var3] + 1].price + ";" + HullDB.hulls[var3].hm[this.u.garage.hulls[var3] + 1].rank + ";" + var5 + ";");
               return;
            }
         }
      }

      if(var2.equals("turret")) {
         if(this.u.garage.turrets[var3] == -1) {
            if(this.u.garage.crystalls >= TurretDB.turrets[var3].tm[0].price && var4 >= TurretDB.turrets[var3].tm[0].rank) {
               this.send("gi;t;" + var3 + ";0;" + TurretDB.turrets[var3].tm[0].price);
               return;
            }

            if(this.u.garage.crystalls < TurretDB.turrets[var3].tm[0].price && var4 >= TurretDB.turrets[var3].tm[0].rank) {
               this.send("gi;t;" + var3 + ";1;" + TurretDB.turrets[var3].tm[0].price);
               return;
            }

            if(var4 < TurretDB.turrets[var3].tm[0].rank && this.u.garage.crystalls >= TurretDB.turrets[var3].tm[0].price) {
               this.send("gi;t;" + var3 + ";2;" + TurretDB.turrets[var3].tm[0].price + ";" + TurretDB.turrets[var3].tm[0].rank);
               return;
            }

            if(this.u.garage.crystalls < TurretDB.turrets[var3].tm[0].price && var4 < TurretDB.turrets[var3].tm[0].rank) {
               this.send("gi;t;" + var3 + ";3;" + TurretDB.turrets[var3].tm[0].price + ";" + TurretDB.turrets[var3].tm[0].rank);
               return;
            }
         } else {
            var5 = this.u.garage.currTurret == var3?"1":"0";
            if(this.u.garage.turrets[var3] == 3) {
               this.send("gi;t;" + var3 + ";8;" + var5 + ";");
               return;
            }

            if(this.u.garage.crystalls >= TurretDB.turrets[var3].tm[this.u.garage.turrets[var3] + 1].price && var4 >= TurretDB.turrets[var3].tm[this.u.garage.turrets[var3] + 1].rank) {
               this.send("gi;t;" + var3 + ";4;" + TurretDB.turrets[var3].tm[this.u.garage.turrets[var3] + 1].price + ";" + var5 + ";");
               return;
            }

            if(this.u.garage.crystalls < TurretDB.turrets[var3].tm[this.u.garage.turrets[var3] + 1].price && var4 >= TurretDB.turrets[var3].tm[this.u.garage.turrets[var3] + 1].rank) {
               this.send("gi;t;" + var3 + ";5;" + TurretDB.turrets[var3].tm[this.u.garage.turrets[var3] + 1].price + ";" + var5 + ";");
               return;
            }

            if(var4 < TurretDB.turrets[var3].tm[this.u.garage.turrets[var3] + 1].rank && this.u.garage.crystalls >= TurretDB.turrets[var3].tm[this.u.garage.turrets[var3] + 1].price) {
               this.send("gi;t;" + var3 + ";6;" + TurretDB.turrets[var3].tm[this.u.garage.turrets[var3] + 1].price + ";" + TurretDB.turrets[var3].tm[this.u.garage.turrets[var3] + 1].rank + ";" + var5 + ";");
               return;
            }

            if(this.u.garage.crystalls < TurretDB.turrets[var3].tm[this.u.garage.turrets[var3] + 1].price && var4 < TurretDB.turrets[var3].tm[this.u.garage.turrets[var3] + 1].rank) {
               this.send("gi;t;" + var3 + ";7;" + TurretDB.turrets[var3].tm[this.u.garage.turrets[var3] + 1].price + ";" + TurretDB.turrets[var3].tm[this.u.garage.turrets[var3] + 1].rank + ";" + var5 + ";");
               return;
            }
         }
      }

      if(var2.equals("colormap")) {
         if(this.u.garage.colormaps[var3] == 0) {
            if(this.u.garage.crystalls >= ColormapDB.colormaps[var3].price && var4 >= ColormapDB.colormaps[var3].rank) {
               this.send("gi;c;" + var3 + ";0;" + ColormapDB.colormaps[var3].price);
               return;
            }

            if(this.u.garage.crystalls < ColormapDB.colormaps[var3].price && var4 >= ColormapDB.colormaps[var3].rank) {
               this.send("gi;c;" + var3 + ";1;" + ColormapDB.colormaps[var3].price);
               return;
            }

            if(var4 < ColormapDB.colormaps[var3].rank && this.u.garage.crystalls >= ColormapDB.colormaps[var3].price) {
               this.send("gi;c;" + var3 + ";2;" + ColormapDB.colormaps[var3].price + ";" + ColormapDB.colormaps[var3].rank);
               return;
            }

            if(this.u.garage.crystalls < ColormapDB.colormaps[var3].price && var4 < ColormapDB.colormaps[var3].rank) {
               this.send("gi;c;" + var3 + ";3;" + ColormapDB.colormaps[var3].price + ";" + ColormapDB.colormaps[var3].rank);
               return;
            }
         } else {
            this.send("gi;c;" + var3 + ";8;" + (this.u.garage.currColormap == var3?"1":"0"));
         }
      }

      if(var2.equals("numeric")) {
         if(var4 >= NumericDB.numerics[var3].rank && this.u.garage.crystalls >= NumericDB.numerics[var3].price) {
            this.send("gi;n;" + var3 + ";0;" + NumericDB.numerics[var3].price);
            return;
         }

         if(var4 >= NumericDB.numerics[var3].rank && this.u.garage.crystalls < NumericDB.numerics[var3].price) {
            this.send("gi;n;" + var3 + ";1;" + NumericDB.numerics[var3].price);
            return;
         }

         if(var4 < NumericDB.numerics[var3].rank && this.u.garage.crystalls >= NumericDB.numerics[var3].price) {
            this.send("gi;n;" + var3 + ";2;" + NumericDB.numerics[var3].price + ";" + NumericDB.numerics[var3].rank);
            return;
         }

         if(var4 < NumericDB.numerics[var3].rank && this.u.garage.crystalls < NumericDB.numerics[var3].price) {
            this.send("gi;n;" + var3 + ";3;" + NumericDB.numerics[var3].price + ";" + NumericDB.numerics[var3].rank);
            return;
         }
      }

   }

   private void sendGarage() {
      byte var1 = this.u.getRank();
      Vector var2 = new Vector();
      Vector var3 = new Vector();
      Vector var4 = new Vector();
      Vector var5 = new Vector();

      int var6;
      byte var7;
      for(var6 = 0; var6 < this.u.garage.numerics.length; ++var6) {
         var7 = NumericDB.numerics[var6].rank;
         var2.addElement(this.u.garage.numerics[var6] + ";" + (var1 >= var7?"1":"0"));
      }

      for(var6 = 0; var6 < this.u.garage.turrets.length; ++var6) {
         var7 = TurretDB.turrets[var6].tm[this.u.garage.turrets[var6] == -1?0:this.u.garage.turrets[var6]].rank;
         var3.addElement((this.u.garage.turrets[var6] >= 0?"1":"0") + ";" + this.u.garage.turrets[var6] + ";" + (var1 >= var7?"1":"0") + ";" + this.u.garage.turrets[var6] + ";" + (this.u.garage.currTurret == var6?"1":"0"));
      }

      for(var6 = 0; var6 < this.u.garage.hulls.length; ++var6) {
         var7 = HullDB.hulls[var6].hm[this.u.garage.hulls[var6] == -1?0:this.u.garage.hulls[var6]].rank;
         var4.addElement((this.u.garage.hulls[var6] >= 0?"1":"0") + ";" + this.u.garage.hulls[var6] + ";" + (var1 >= var7?"1":"0") + ";" + this.u.garage.hulls[var6] + ";" + (this.u.garage.currHull == var6?"1":"0"));
      }

      for(var6 = 0; var6 < this.u.garage.colormaps.length; ++var6) {
         var7 = ColormapDB.colormaps[var6].rank;
         if(ColormapDB.colormaps[var6].type == 1 && this.u.garage.colormaps[var6] == 0) {
            var5.addElement("-1;" + String.valueOf(var6));
         } else {
            var5.addElement((this.u.garage.colormaps[var6] == 1?"1":"0") + ";" + var6 + ";" + (var1 >= var7?"1":"0") + ";" + (this.u.garage.currColormap == var6?"1":"0"));
         }
      }

      String var9 = "gar;" + this.u.garage.numerics.length + ";" + this.u.garage.turrets.length + ";" + this.u.garage.hulls.length + ";" + var5.size() + ";";

      int var8;
      for(var8 = 0; var8 < var2.size(); ++var8) {
         var9 = var9 + (String)var2.elementAt(var8) + ";";
      }

      for(var8 = 0; var8 < var3.size(); ++var8) {
         var9 = var9 + (String)var3.elementAt(var8) + ";";
      }

      for(var8 = 0; var8 < var4.size(); ++var8) {
         var9 = var9 + (String)var4.elementAt(var8) + ";";
      }

      for(var8 = 0; var8 < var5.size(); ++var8) {
         var9 = var9 + (String)var5.elementAt(var8) + ";";
      }

      this.send(var9);
   }

   private void refreshTank() {
      this.send("st;" + this.getHullNum() + ";" + this.getTurretNum() + ";" + this.getColormapNum());
   }

   private void refreshUser() {
      this.send("refr;" + this.u.garage.score + ";" + this.u.garage.crystalls);
   }
}
