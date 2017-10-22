package tanks.DataBase.Battle;

import java.io.File;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import tanks.DataBase.Battle.BonusPoint;
import tanks.DataBase.Battle.BonusPoints;
import tanks.utils.Vector3;

public class BonusPointsLoader {

   public static BonusPoints output;


   public static void loadPoints(String var0, String var1) {
      try {
         File var2 = new File(var0);
         DocumentBuilderFactory var3 = DocumentBuilderFactory.newInstance();
         DocumentBuilder var4 = var3.newDocumentBuilder();
         Document var5 = var4.parse(var2);
         var5.getDocumentElement().normalize();
         NodeList var6 = var5.getElementsByTagName("bonus-regions");
         Node var7 = var6.item(0);
         Element var8 = (Element)var7;
         NodeList var9 = var8.getElementsByTagName("bonus-region");
         BonusPoint[] var10 = new BonusPoint[var9.getLength()];

         for(int var11 = 0; var11 < var10.length; ++var11) {
            boolean var12 = false;
            boolean var13 = false;
            boolean var14 = false;
            boolean var15 = false;
            boolean var16 = false;
            boolean var17 = false;
            Node var18 = var9.item(var11);
            Element var19 = (Element)var18;
            Vector3 var20 = readVector3(var19.getElementsByTagName("min").item(0));
            Vector3 var21 = readVector3(var19.getElementsByTagName("max").item(0));
            NodeList var22 = var19.getElementsByTagName("bonus-type");

            for(int var23 = 0; var23 < var22.getLength(); ++var23) {
               Node var24 = var22.item(var23);
               Element var25 = (Element)var24;
               String var26 = var25.getTextContent();
               if(var26.equals("medkit")) {
                  var12 = true;
               } else if(var26.equals("armorup")) {
                  var13 = true;
               } else if(var26.equals("damageup")) {
                  var14 = true;
               } else if(var26.equals("nitro")) {
                  var15 = true;
               } else if(var26.equals("crystal")) {
                  var16 = true;
               } else if(var26.equals("crystal_100")) {
                  var17 = true;
               }
            }

            var10[var11] = new BonusPoint(var12, var13, var14, var15, var16, var17, var20, var21);
         }

         output = new BonusPoints(var1, var10);
      } catch (Exception var27) {
         ;
      }

   }

   private static Vector3 readVector3(Node var0) {
      Element var1 = (Element)var0;
      float var2 = Float.parseFloat(var1.getElementsByTagName("x").item(0).getTextContent());
      float var3 = Float.parseFloat(var1.getElementsByTagName("y").item(0).getTextContent());
      float var4 = Float.parseFloat(var1.getElementsByTagName("z").item(0).getTextContent());
      return new Vector3(var2, var3, var4);
   }
}
