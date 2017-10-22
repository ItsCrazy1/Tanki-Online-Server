package tanks.DataBase.Colormap;

import tanks.DataBase.Colormap.Colormap;
import tanks.DataBase.Colormap.ResistInfo;

public class ColormapDB {

   public static Colormap[] colormaps;


   public static void dispose() {
      colormaps = null;
   }

   public static void init() {
      colormaps = new Colormap[15];
      colormaps[0] = new Colormap(1, 0, 0, new ResistInfo(75, 75, 75, 75));
      colormaps[1] = new Colormap(1, 0, 0, new ResistInfo(25, 25, 25, 25));
      colormaps[2] = new Colormap(0, 0, 0);
      colormaps[3] = new Colormap(0, 0, 0);
      colormaps[4] = new Colormap(0, 5, 1);
      colormaps[5] = new Colormap(0, 5, 1);
      colormaps[6] = new Colormap(0, 10, 2);
      colormaps[7] = new Colormap(0, 10, 2);
      colormaps[8] = new Colormap(0, 100, 3);
      colormaps[9] = new Colormap(0, 50, 3);
      colormaps[10] = new Colormap(0, 50, 3);
      colormaps[11] = new Colormap(0, 100, 3);
      colormaps[12] = new Colormap(0, 100, 3, new ResistInfo(50, 0, 0, 0));
      colormaps[13] = new Colormap(0, 50, 3, new ResistInfo(0, 50, 0, 0));
      colormaps[14] = new Colormap(0, 200, 4, new ResistInfo(0, 0, 50, 0));
   }

   public static int getSpecialColormapNum() {
      return 2;
   }
}
