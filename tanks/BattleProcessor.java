package tanks;

import tanks.Battle;
import tanks.BattleProcessorTask;
import tanks.BattleProcessorTaskManager;
import tanks.Lobby;
import tanks.Main;
import tanks.SocketProcessor;
import tanks.DataBase.User;
import tanks.DataBase.Battle.BonusDB;
import tanks.DataBase.Battle.Mine;
import tanks.DataBase.Colormap.ColormapDB;
import tanks.DataBase.Colormap.ResistInfo;
import tanks.DataBase.Hull.HullDB;
import tanks.DataBase.Turret.TurretDB;
import tanks.utils.Vector3;

public class BattleProcessor extends SocketProcessor implements Runnable {

   public boolean loaded = false;
   public BattleProcessorTaskManager taskManager;
   private static int DROP_EFFECT_TIME = 30;
   private static int INTERPOLATE_TIME = 500;
   private static int MAX_RESPAWN_TIME = 5;
   private static int MAX_AFK_TIME = 300;
   private static int MAX_PING_TIME = 3;
   private float MAX_HEALTH = 30.0F;
   private float health = 30.0F;
   public User u;
   public Battle b;
   public byte command;
   private int afktime = 0;
   private int afksec = 0;
   private int respawntime = 0;
   private int respawnsec = 0;
   private int pingtime = 0;
   private int pingsec = 0;
   private boolean isResp = false;
   public int id;
   public ResistInfo res;
   public int turret;
   public int score = 0;
   public int destructs = 0;
   public int ping = 50;
   private long prevPingTime;
   private boolean pingSended = false;
   private int[] effectsTime;
   private boolean[] effectEnabled;


   public BattleProcessor(Lobby var1, Battle var2, byte var3, int var4) {
      super(var1.s, var1.is, var1.os);
      this.u = var1.u;
      this.taskManager = new BattleProcessorTaskManager(this);
      this.b = var2;
      this.command = var3;
      this.id = var4;
      this.res = ColormapDB.colormaps[this.u.garage.currColormap].res;
      this.turret = this.u.garage.currTurret;
      this.effectsTime = new int[3];
      this.effectsTime[0] = 0;
      this.effectsTime[1] = 0;
      this.effectsTime[2] = 0;
      this.effectEnabled = new boolean[3];
      this.effectEnabled[0] = false;
      this.effectEnabled[1] = false;
      this.effectEnabled[2] = false;
      this.MAX_HEALTH = (float)HullDB.hulls[this.u.garage.currHull].hm[this.u.garage.hulls[this.u.garage.currHull]].health;
      this.health = this.MAX_HEALTH;
   }

   public void startTask(BattleProcessorTask var1) {
      this.send(var1.task);
   }

   public void stopEffect(int var1) {
      this.effectsTime[var1] = 0;
      this.effectEnabled[var1] = false;
      if(var1 == 2) {
         for(int var2 = 0; var2 < this.b.pnum; ++var2) {
            BattleProcessor var3 = this.b.processors[var2];
            if(var3.id == this.id) {
               var3.send("esp;-1;" + var1 + ";");
            } else {
               var3.send("esp;" + this.id + ";" + var1 + ";");
            }
         }
      }

   }

   public void startEffect(int var1, int var2) {
      if(this.effectsTime[var1] <= var2 * 1000) {
         this.effectsTime[var1] = var2 * 1000;
         this.effectEnabled[var1] = true;

         for(int var3 = 0; var3 < this.b.pnum; ++var3) {
            BattleProcessor var4 = this.b.processors[var3];
            if(var4.id == this.id) {
               var4.send("est;-1;" + var1 + ";" + var2 + ";");
            } else {
               var4.send("est;" + this.id + ";" + var1 + ";" + var2 + ";");
            }
         }

      }
   }

   public void startEffect2(byte var1) {
      if(var1 == 1 && this.u.garage.numerics[0] > 0) {
         --this.u.garage.numerics[0];
         this.heal();

         for(int var2 = 0; var2 < this.b.pnum; ++var2) {
            if(this.b.processors[var2].id == var1) {
               this.b.processors[var2].send("est;-1;3;3;");
            } else {
               this.b.processors[var2].send("est;" + var1 + ";3;3;");
            }
         }
      }

      if(var1 > 1 && var1 < 5 && this.u.garage.numerics[var1 - 1] > 0) {
         --this.u.garage.numerics[var1 - 1];
         this.startEffect(var1 - 2, 45);
      }

   }

   private float fl(String var1) {
      return Float.parseFloat(var1);
   }

   private Vector3 readv3(String[] var1) {
      return new Vector3(this.fl(var1[0]), this.fl(var1[1]), this.fl(var1[2]));
   }

   public void run() {
      System.out.println("Client enter in battle...");
      this.send("battle;" + this.b.mapXml + ";" + this.u.garage.currTurret + ";" + this.u.garage.turrets[this.u.garage.currTurret] + ";" + this.u.garage.currHull + ";" + this.u.garage.hulls[this.u.garage.currHull] + ";" + this.u.garage.currColormap + ";" + TurretDB.turrets[this.u.garage.currTurret].tm[this.u.garage.turrets[this.u.garage.currTurret]].params + ";" + this.b.getTeam(this.command) + ";" + this.MAX_HEALTH + ";" + this.b.dest + ";" + this.u.getNumericItemsStr() + ";" + INTERPOLATE_TIME + ";");
      this.pingtime = 0;
      this.pingsec = 0;

      try {
         Thread.sleep(3000L);
      } catch (Exception var12) {
         ;
      }

      while(this.state == STATE_NORM) {
         if(this.loaded) {
            this.afksec += 100;
            if(this.afksec >= 1000) {
               this.afksec -= 1000;
               ++this.afktime;
            }

            if(this.afktime >= MAX_AFK_TIME) {
               this.state = STATE_ERROR;
            }
         }

         if(this.isResp) {
            this.respawnsec += 5;
            if(this.respawnsec >= 1000) {
               this.respawnsec -= 1000;
               ++this.respawntime;
            }

            if(this.respawntime >= MAX_RESPAWN_TIME) {
               this.respawn();
               this.isResp = false;
            }
         }

         if(this.loaded && !this.pingSended) {
            this.pingsec += 5;
            if(this.pingsec >= 1000) {
               this.pingsec -= 1000;
               ++this.pingtime;
            }

            if(this.pingtime >= MAX_PING_TIME) {
               this.pingsec = 0;
               this.pingtime = 0;
               this.prevPingTime = System.currentTimeMillis();
               this.pingSended = true;
               this.send("pi;");
            }
         }

         for(int var1 = 0; var1 < 3; ++var1) {
            if(this.effectEnabled[var1]) {
               this.effectsTime[var1] -= 5;
               if(this.effectsTime[var1] <= 0) {
                  this.stopEffect(var1);
               }
            }
         }

         try {
            Thread.sleep(5L);
         } catch (Exception var11) {
            ;
         }

         this.taskManager.update(System.currentTimeMillis());
         String var18 = this.readInput();
         if(var18 != null && var18 != "") {
            this.afktime = 0;
            this.afksec = 0;
            String[] var2 = var18.split(";");
            int var3 = 0;

            while(var3 < var2.length) {
               long var4;
               if(var2[var3].equals("pi")) {
                  var4 = System.currentTimeMillis();
                  long var33 = var4 - this.prevPingTime;
                  if(var33 > 1000L) {
                     var33 = 1000L;
                  }

                  this.ping = (int)(var33 / 2L);
                  this.pingSended = false;
                  ++var3;
               } else {
                  String[] var20;
                  String[] var22;
                  if(var2[var3].equals("minec")) {
                     var20 = var2[var3 + 1].split("@");
                     var22 = var2[var3 + 2].split("@");
                     Vector3 var32 = this.readv3(var20);
                     Vector3 var30 = this.readv3(var22);
                     Mine var29 = new Mine(this, var32, var30);
                     this.b.addMineArr(var29);
                     this.sendMineToAll(var29);
                     var3 += 3;
                  } else {
                     int var6;
                     if(var2[var3].equals("mined")) {
                        var4 = Long.parseLong(var2[var3 + 1]);

                        for(var6 = 0; var6 < this.b.mnum; ++var6) {
                           if(this.b.mines[var6].id == var4) {
                              Mine var7 = this.b.mines[var6];
                              this.health -= 250.0F;
                              if(this.health <= 0.0F) {
                                 ++this.b.fond;
                                 ++this.b.crysFond;
                                 if(this.b.crysFond >= 5) {
                                    this.b.crysFond -= 5;
                                    BonusDB.spawnCrystall(this.b);
                                 }

                                 ++this.b.goldFond;
                                 if(this.b.goldFond >= 20) {
                                    this.b.goldFond -= 20;
                                    BonusDB.spawnGold(this.b);
                                 }

                                 this.killBy(var7.owner);
                                 var7.owner.addScore();
                                 this.addDestructs();
                              }

                              this.b.removeMineArr(var6);
                              break;
                           }
                        }

                        for(var6 = 0; var6 < this.b.pnum; ++var6) {
                           this.b.processors[var6].send("mined;" + var2[var3 + 1] + ";");
                        }
                     }

                     int var21;
                     BattleProcessor var27;
                     if(var2[var3].equals("vis")) {
                        for(var21 = 0; var21 < this.b.pnum; ++var21) {
                           var27 = this.b.processors[var21];
                           if(var27.id != this.id) {
                              var27.send("vis;" + this.id + ";");
                           }
                        }

                        ++var3;
                     } else if(var2[var3].equals("fall")) {
                        if(!this.isResp) {
                           this.kill();
                        }

                        ++var3;
                     } else {
                        int var5;
                        String var23;
                        BattleProcessor var25;
                        if(var2[var3].equals("k")) {
                           var23 = "k;" + this.id + ";" + var2[var3 + 1] + ";" + var2[var3 + 2] + ";";

                           for(var5 = 0; var5 < this.b.pnum; ++var5) {
                              var25 = this.b.processors[var5];
                              if(var25.id != this.id) {
                                 var25.send(var23 + (this.ping + var25.ping) + ";");
                              }
                           }

                           var3 += 3;
                        } else if(var2[var3].equals("wst")) {
                           var23 = "wst;" + this.id + ";";

                           for(var5 = 0; var5 < this.b.pnum; ++var5) {
                              var25 = this.b.processors[var5];
                              if(var25.id != this.id) {
                                 var25.send(var23 + (this.ping + var25.ping) + ";");
                              }
                           }

                           ++var3;
                        } else if(var2[var3].equals("r")) {
                           var23 = "r;" + this.id + ";" + var2[var3 + 1] + ";";

                           for(var5 = 0; var5 < this.b.pnum; ++var5) {
                              var25 = this.b.processors[var5];
                              if(var25.id != this.id) {
                                 var25.send(var23 + (this.ping + var25.ping) + ";");
                              }
                           }

                           var3 += 2;
                        } else if(var2[var3].equals("battleok")) {
                           for(var21 = 0; var21 < this.b.pnum; ++var21) {
                              var27 = this.b.processors[var21];
                              if(var27.id != this.id) {
                                 this.sendTankAndEnter(var27);
                              }
                           }

                           this.loaded = true;
                           this.sendTanks();
                           this.prevPingTime = System.currentTimeMillis();
                           this.pingSended = true;
                           this.send("start;");
                           this.sendPlayers();
                           this.sendMines();
                           ++var3;
                        } else if(var2[var3].equals("d")) {
                           var21 = Integer.parseInt(var2[var3 + 1]);
                           int[] var24 = new int[var21];

                           for(var6 = 2; var6 < var21 + 2; ++var6) {
                              var24[var6 - 2] = Integer.parseInt(var2[var3 + var6]);
                           }

                           float var31 = Float.parseFloat(var2[var3 + var21 + 2]);
                           BattleProcessor var8;
                           int var28;
                           if(var21 == 1) {
                              for(var28 = 0; var28 < this.b.pnum; ++var28) {
                                 var8 = this.b.processors[var28];
                                 if(var8.id == var24[0]) {
                                    var8.addDamage(this, var31);
                                    break;
                                 }
                              }
                           } else {
                              for(var28 = 0; var28 < this.b.pnum; ++var28) {
                                 var8 = this.b.processors[var28];

                                 for(int var9 = 0; var9 < var21; ++var9) {
                                    if(var24[var9] == var8.id) {
                                       var8.addDamage(this, var31);
                                       break;
                                    }
                                 }
                              }
                           }

                           var3 += var21 + 5;
                        } else {
                           if(this.u.type == 0) {
                              if(var2[var3].equals("addcrys")) {
                                 BonusDB.spawnCrystall(this.b);
                                 ++var3;
                                 continue;
                              }

                              if(var2[var3].equals("addgold")) {
                                 BonusDB.spawnGold(this.b);
                                 ++var3;
                                 continue;
                              }
                           }

                           if(var2[var3].equals("chat")) {
                              if(var2.length - var3 >= 2) {
                                 var20 = var2[var3 + 1].split(" ");

                                 try {
                                    if(var20[0].substring(0, 1).equals("/")) {
                                       if(this.u.type == 0) {
                                          try {
                                             if(var20[0].substring(1, 7).equals("system")) {
                                                for(var5 = 0; var5 < this.b.pnum; ++var5) {
                                                   var25 = this.b.processors[var5];
                                                   var25.send("cm;1;" + var2[var3 + 1].substring(7) + ";");
                                                }
                                             }
                                          } catch (Exception var16) {
                                             ;
                                          }

                                          try {
                                             if(var20[0].substring(1, 4).equals("ctf")) {
                                                var22 = var2[var3 + 1].substring(4).split("%");

                                                for(var6 = 0; var6 < this.b.pnum; ++var6) {
                                                   BattleProcessor var26 = this.b.processors[var6];
                                                   var26.send("ctfmsg;" + var22[0] + ";" + var22[1] + ";");
                                                }
                                             }
                                          } catch (Exception var15) {
                                             ;
                                          }

                                          try {
                                             if(var20[0].substring(1, 6).equals("moder")) {
                                                for(var5 = 0; var5 < this.b.pnum; ++var5) {
                                                   var25 = this.b.processors[var5];
                                                   var25.send("cm;2;" + var2[var3 + 1].substring(6) + ";");
                                                }
                                             }
                                          } catch (Exception var14) {
                                             ;
                                          }
                                       }
                                    } else {
                                       try {
                                          for(var5 = 0; var5 < this.b.pnum; ++var5) {
                                             var25 = this.b.processors[var5];
                                             var25.send("cm;0;" + this.u.login + ";" + this.u.getRank() + ";" + var2[var3 + 1] + ";");
                                          }
                                       } catch (Exception var13) {
                                          ;
                                       }
                                    }
                                 } catch (Exception var17) {
                                    ;
                                 }

                                 var3 += 2;
                              }
                           } else if(var2[var3].equals("takeb")) {
                              var4 = Long.parseLong(var2[var3 + 1]);
                              this.b.takeBonus(this, var4);
                              var3 += 2;
                           } else if(var2[var3].equals("steff")) {
                              byte var19 = Byte.parseByte(var2[var3 + 1]);
                              this.startEffect2(var19);
                              var3 += 2;
                           } else if(var2[var3].equals("bstop")) {
                              this.state = STATE_STOP;
                              ++var3;
                           } else {
                              ++var3;
                           }
                        }
                     }
                  }
               }
            }
         }
      }

      if(this.state == STATE_ERROR) {
         try {
            this.is.close();
            this.os.close();
            this.s.close();
         } catch (Exception var10) {
            ;
         }

         System.out.println("User disconnect from battle...");
         this.b.removePlayer(this.u, this.command);
         this.b.removeProcessor(this);
      }

      if(this.state == STATE_STOP) {
         System.out.println("User close battle...");
         this.b.removePlayer(this.u, this.command);
         Main.startLobby(this, this.u);
         this.b.removeProcessor(this);
      }

   }

   private void sendMineToAll(Mine var1) {
      for(int var2 = 0; var2 < this.b.pnum; ++var2) {
         if(this.b.processors[var2].id == this.id) {
            this.b.processors[var2].send("minec;" + var1.id + ";-1;" + var1.pos.getStr("@") + ";" + var1.rot.getStr("@") + ";");
         } else {
            this.b.processors[var2].send("minec;" + var1.id + ";" + var1.owner.id + ";" + var1.pos.getStr("@") + ";" + var1.rot.getStr("@") + ";");
         }
      }

   }

   public void sendMines() {
      for(int var1 = 0; var1 < this.b.mnum; ++var1) {
         Mine var2 = this.b.mines[var1];
         this.send("minec;" + var2.id + ";" + (var2.owner.id == this.id?-1:var2.owner.id) + ";" + var2.pos.getStr("@") + ";" + var2.rot.getStr("@") + ";");
      }

   }

   public void sendTankAndEnter(BattleProcessor var1) {
      if(this.b.type == 0) {
         this.u.sendBattleInfo(var1, 2, "g", this.id);
         var1.send("log;" + this.u.getRank() + ";" + this.u.login + ";РІСЃС‚СѓРїРёР» РІ Р±РѕР№;");
      } else {
         this.u.sendBattleInfo(var1, var1.command == this.command?1:2, this.command == 0?"r":"b", this.id);
         var1.send("log;" + this.u.getRank() + ";" + this.u.login + ";РІСЃС‚СѓРїРёР» РІ Р±РѕР№;");
      }

   }

   private float getDamage(BattleProcessor var1, float var2) {
      float var3 = var2;
      if(var1.turret == 0) {
         var3 = var2 / 100.0F * (float)(100 - this.res.smoky);
      }

      if(var1.turret == 1) {
         var3 = var2 / 100.0F * (float)(100 - this.res.flamethrower);
      }

      if(var1.turret == 2) {
         var3 = var2 / 100.0F * (float)(100 - this.res.twins);
      }

      if(var1.turret == 3) {
         var3 = var2 / 100.0F * (float)(100 - this.res.railgun);
      }

      if(var1.effectEnabled[1]) {
         var3 = (float)((double)var3 * 1.3D);
      }

      if(this.effectEnabled[0]) {
         var3 = (float)((double)var3 / 1.3D);
      }

      return var3;
   }

   public void addDamage(BattleProcessor var1, float var2) {
      if(!this.isResp) {
         this.health -= this.getDamage(var1, var2);
         if(this.health <= 0.0F) {
            ++this.b.fond;
            ++this.b.crysFond;
            if(this.b.crysFond >= 5) {
               this.b.crysFond -= 5;
               BonusDB.spawnCrystall(this.b);
            }

            ++this.b.goldFond;
            if(this.b.goldFond >= 20) {
               this.b.goldFond -= 20;
               BonusDB.spawnGold(this.b);
            }

            this.killBy(var1);
            var1.addScore();
            this.addDestructs();
         } else {
            this.send("h;" + this.health + ";");
         }

      }
   }

   public void addScore() {
      ++this.score;
      this.u.garage.score += 10;

      for(int var1 = 0; var1 < this.b.pnum; ++var1) {
         if(this.b.processors[var1].id == this.id) {
            this.send("bsc;" + this.score + ";" + this.u.garage.score + ";");
         } else {
            this.b.processors[var1].send("tsc;" + this.id + ";" + this.score + ";");
         }
      }

      if(this.score >= this.b.dest) {
         this.b.end();
      }

   }

   public void addDestructs() {
      ++this.destructs;

      for(int var1 = 0; var1 < this.b.pnum; ++var1) {
         if(this.b.processors[var1].id == this.id) {
            this.send("tds;-1;" + this.destructs + ";");
         } else {
            this.b.processors[var1].send("tds;" + this.id + ";" + this.destructs + ";");
         }
      }

   }

   private void kill() {
      for(int var1 = 0; var1 < this.b.pnum; ++var1) {
         BattleProcessor var2 = this.b.processors[var1];
         if(var2.id != this.id) {
            var2.send("kt;" + this.id + ";");
         }

         var2.send("log;" + this.u.getRank() + ";" + this.u.login + ";СЃР°РјРѕСѓРЅРёС‡С‚РѕР¶РёР»СЃСЏ;");
      }

      this.send("ktt;");
      this.respawntime = 0;
      this.respawnsec = 0;
      this.isResp = true;
   }

   private void killBy(BattleProcessor var1) {
      for(int var2 = 0; var2 < this.b.pnum; ++var2) {
         BattleProcessor var3 = this.b.processors[var2];
         if(var3.id != this.id) {
            var3.send("kt;" + this.id + ";");
         }

         var3.send("f;" + this.b.fond + ";");
         var3.send("log2;" + var1.u.getRank() + ";" + var1.u.login + ";СѓРЅРёС‡С‚РѕР¶РёР»;" + this.u.getRank() + ";" + this.u.login + ";");
      }

      this.send("ktt;");
      this.respawntime = 0;
      this.respawnsec = 0;
      this.isResp = true;
   }

   private void respawn() {
      this.health = this.MAX_HEALTH;
      long var1 = System.currentTimeMillis();
      long var3 = var1 - (long)this.ping + (long)INTERPOLATE_TIME;
      String var5 = "res;" + this.id + ";";
      String var6 = "resp;h;" + this.health + ";";

      for(int var7 = 0; var7 < this.b.pnum; ++var7) {
         BattleProcessor var8 = this.b.processors[var7];
         if(var8.id == this.id) {
            var8.taskManager.addTask(new BattleProcessorTask(var3, var6));
         } else {
            var8.taskManager.addTask(new BattleProcessorTask(var3, var5));
         }
      }

      this.stopEffect(0);
      this.stopEffect(1);
      this.stopEffect(2);
   }

   public void heal() {
      this.health = this.MAX_HEALTH;
      this.send("h;" + this.health + ";");
   }

   public void sendTank(BattleProcessor var1) {
      if(this.b.type == 0) {
         this.u.sendBattleInfo(var1, 2, "g", this.id);
      } else {
         this.u.sendBattleInfo(var1, var1.command == this.command?1:2, this.command == 0?"r":"b", this.id);
      }

   }

   public void sendPlayers() {
      for(int var1 = 0; var1 < this.b.pnum; ++var1) {
         if(this.b.processors[var1].id != this.id) {
            this.send("tinf;" + this.b.processors[var1].id + ";" + this.b.processors[var1].score + ";" + this.b.processors[var1].destructs + ";");
         }
      }

   }

   private void sendTanks() {
      if(this.b.type == 0) {
         for(int var1 = 0; var1 < this.b.pnum; ++var1) {
            BattleProcessor var2 = this.b.processors[var1];
            if(var2.id != this.id && var2.loaded) {
               var2.u.sendBattleInfo(this, 2, "g", var2.id);
            }
         }
      } else {
         boolean var4 = this.command == 0;

         for(int var5 = 0; var5 < this.b.pnum; ++var5) {
            BattleProcessor var3 = this.b.processors[var5];
            if(var3.id != this.id && var3.loaded) {
               var3.u.sendBattleInfo(this, var3.command == this.command?1:2, var3.command == 0?"r":"b", var3.id);
            }
         }
      }

   }

}
