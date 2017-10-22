package tanks.DataBase.Battle;

import java.util.Random;
import java.util.Vector;
import tanks.Battle;
import tanks.BattleProcessor;
import tanks.DataBase.Battle.Bonus;
import tanks.DataBase.Battle.BonusPoint;
import tanks.DataBase.Battle.BonusPoints;
import tanks.DataBase.Battle.BonusPointsLoader;
import tanks.utils.Vector3;
import tanks.utils.Vector3I;

public class BonusDB {

   public static BonusPoints[] bp;
   private static Random r = new Random();


   public static void dispose() {
      bp = null;
   }

   public static void init() {
      bp = new BonusPoints[25];
      BonusPointsLoader.loadPoints("ServerData/maps/Mishin870.xml", "Mishin870");
      bp[0] = BonusPointsLoader.output;
      BonusPointsLoader.loadPoints("ServerData/maps/silence.xml", "silence");
      bp[1] = BonusPointsLoader.output;
      BonusPointsLoader.loadPoints("ServerData/maps/pp.xml", "pp");
      bp[2] = BonusPointsLoader.output;
      BonusPointsLoader.loadPoints("ServerData/maps/Kibersport.xml", "Kibersport");
      bp[3] = BonusPointsLoader.output;
      BonusPointsLoader.loadPoints("ServerData/maps/Konteynery.xml", "Konteynery");
      bp[4] = BonusPointsLoader.output;
      BonusPointsLoader.loadPoints("ServerData/maps/Ovrag.xml", "Ovrag");
      bp[5] = BonusPointsLoader.output;
      BonusPointsLoader.loadPoints("ServerData/maps/p9.xml", "p9");
      bp[6] = BonusPointsLoader.output;
      BonusPointsLoader.loadPoints("ServerData/maps/stadium.xml", "stadium");
      bp[7] = BonusPointsLoader.output;
      BonusPointsLoader.loadPoints("ServerData/maps/pesok.xml", "pesok");
      bp[8] = BonusPointsLoader.output;
      BonusPointsLoader.loadPoints("ServerData/maps/crazy.xml", "crazy");
      bp[9] = BonusPointsLoader.output;
      BonusPointsLoader.loadPoints("ServerData/maps/crazys.xml", "crazys");
      bp[10] = BonusPointsLoader.output;
      BonusPointsLoader.loadPoints("ServerData/maps/plato.xml", "plato");
      bp[11] = BonusPointsLoader.output;
      BonusPointsLoader.loadPoints("ServerData/maps/Ostrov.xml", "Ostrov");
      bp[12] = BonusPointsLoader.output;
      BonusPointsLoader.loadPoints("ServerData/maps/test.xml", "test");
      bp[13] = BonusPointsLoader.output;
      BonusPointsLoader.loadPoints("ServerData/maps/Kingdom.xml", "Kingdom");
      bp[14] = BonusPointsLoader.output;
      BonusPointsLoader.loadPoints("ServerData/maps/Lab.xml", "Lab");
      bp[15] = BonusPointsLoader.output;
      BonusPointsLoader.loadPoints("ServerData/maps/Pesheri.xml", "Pesheri");
      bp[16] = BonusPointsLoader.output;
      BonusPointsLoader.loadPoints("ServerData/maps/Ruins.xml", "Ruins");
      bp[17] = BonusPointsLoader.output;
      BonusPointsLoader.loadPoints("ServerData/maps/arena2.xml", "arena2");
      bp[18] = BonusPointsLoader.output;
      BonusPointsLoader.loadPoints("ServerData/maps/iran.xml", "iran");
      bp[19] = BonusPointsLoader.output;
      BonusPointsLoader.loadPoints("ServerData/maps/Persia.xml", "Persia");
      bp[20] = BonusPointsLoader.output;
      BonusPointsLoader.loadPoints("ServerData/maps/fort.xml", "fort");
      bp[21] = BonusPointsLoader.output;
      BonusPointsLoader.loadPoints("ServerData/maps/Vokzal.xml", "Vokzal");
      bp[22] = BonusPointsLoader.output;
      BonusPointsLoader.loadPoints("ServerData/maps/Trassa.xml", "Trassa");
      bp[23] = BonusPointsLoader.output;
      BonusPointsLoader.loadPoints("ServerData/maps/Unknown.xml", "Unknown");
      bp[24] = BonusPointsLoader.output;
   }

   private static void sendBattle(Battle var0, int var1, Vector3I var2) {
      Bonus var3 = new Bonus((byte)var1);
      var0.addBonusArr(var3);

      for(int var4 = 0; var4 < var0.pnum; ++var4) {
         BattleProcessor var5 = var0.processors[var4];
         var5.send("b;" + var3.id + ";" + var1 + ";" + var2.x + ";" + var2.y + ";" + var2.z + ";");
      }

   }

   private static BonusPoint getBonusPoint(BonusPoints var0, boolean var1, boolean var2, boolean var3, boolean var4, boolean var5, boolean var6) {
      Vector var7 = new Vector();

      int var8;
      for(var8 = 0; var8 < var0.points.length; ++var8) {
         BonusPoint var9 = var0.points[var8];
         if(var1 && var9.med || var2 && var9.armr || var3 && var9.damage || var4 && var9.nitro || var5 && var9.crystall || var6 && var9.gold) {
            var7.addElement(var0.points[var8]);
         }
      }

      if(var7.size() > 0) {
         var8 = r.nextInt(var7.size());
         return (BonusPoint)var7.elementAt(var8);
      } else {
         return null;
      }
   }

   private static BonusPoints getBonusPoints(String var0) {
      for(int var1 = 0; var1 < bp.length; ++var1) {
         if(("data/maps/" + bp[var1].mapXml + ".xml").equals(var0)) {
            return bp[var1];
         }
      }

      return null;
   }

   private static Vector3I getRandomPos(Vector3 var0, Vector3 var1) {
      Vector3I var5 = var0.getVector3I();
      Vector3I var6 = var1.getVector3I();
      int var2 = r.nextInt(var6.x - var5.x) + var5.x;
      int var3 = r.nextInt(var6.y - var5.y) + var5.y;
      int var4 = r.nextInt(var6.z - var5.z) + var5.z;
      return new Vector3I(var2, var3, var4);
   }

   public static void spawnDrop(Battle var0) {
      BonusPoints var1 = getBonusPoints(var0.mapXml);
      if(var1 != null) {
         BonusPoint var2 = getBonusPoint(var1, true, true, true, true, false, false);
         if(var2 != null) {
            Vector3I var3 = getRandomPos(var2.posmin, var2.posmax);
            int var4 = 0;
            if(var2.med) {
               ++var4;
            }

            if(var2.armr) {
               ++var4;
            }

            if(var2.damage) {
               ++var4;
            }

            if(var2.nitro) {
               ++var4;
            }

            if(var4 == 1) {
               if(var2.med) {
                  sendBattle(var0, 0, var3);
               }

               if(var2.armr) {
                  sendBattle(var0, 1, var3);
               }

               if(var2.damage) {
                  sendBattle(var0, 2, var3);
               }

               if(var2.nitro) {
                  sendBattle(var0, 3, var3);
               }

            } else {
               int var5;
               if(var4 == 4) {
                  var5 = r.nextInt(4);
                  sendBattle(var0, var5, var3);
               } else {
                  var5 = r.nextInt(var4);
                  int var6 = 0;

                  for(int var7 = 0; var7 < 4; ++var7) {
                     if(checkDropType(var2, var7)) {
                        if(var6 == var5) {
                           sendBattle(var0, var7, var3);
                           return;
                        }

                        ++var6;
                     }
                  }

               }
            }
         }
      }
   }

   private static boolean checkDropType(BonusPoint var0, int var1) {
      return var1 == 0?var0.med:(var1 == 1?var0.armr:(var1 == 2?var0.damage:(var1 == 3?var0.nitro:false)));
   }

   public static void spawnCrystall(Battle var0) {
      BonusPoints var1 = getBonusPoints(var0.mapXml);
      if(var1 != null) {
         BonusPoint var2 = getBonusPoint(var1, false, false, false, false, true, false);
         if(var2 != null) {
            Vector3I var3 = getRandomPos(var2.posmin, var2.posmax);
            sendBattle(var0, 4, var3);
         }
      }
   }

   public static void spawnGold(Battle var0) {
      BonusPoints var1 = getBonusPoints(var0.mapXml);
      if(var1 != null) {
         BonusPoint var2 = getBonusPoint(var1, false, false, false, false, false, true);
         if(var2 != null) {
            Vector3I var3 = getRandomPos(var2.posmin, var2.posmax);
            sendBattle(var0, 5, var3);
         }
      }
   }

}
