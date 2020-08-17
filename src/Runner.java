import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public class Runner {
    private static final String delimiter = ",";
    private static final String csvEndSuffix = ".csv";
    private static final String[] tables = new String[12];
    public static void main(String[] args) throws IOException{
        fillTable();
        HashMap<String, String> itemDisplayInfoMaterials = setupDisplayExtraItemsMap(tables[3] + "Sorted" + csvEndSuffix);
        HashMap<String, String> itemModifiedAppearance = setupItemModMap();
        HashMap<String, String> itemAppearance = setupItemAppMap();
        HashMap<String, String> itemModifiedAppearanceReversed = setupItemModReversedMap();
        HashMap<String, String> itemAppearanceReversed = setupItemAppReversedMap();
        HashMap<String, String> itemIcon = setupItemMap();
        HashMap<String, String> itemAppearanceIcon = setupItemAppIconMap();
        try (BufferedReader br = new BufferedReader(new FileReader(tables[2] + csvEndSuffix))) {
            String line;
            br.readLine();//skip header
            while ((line = br.readLine()) != null) {
                String[] displayRow = line.split(delimiter);







            }
        }

    }
    //table and path downloads for files
    private static void fillTable(){
        System.out.println("Filling table...");
        tables[0] = "item/item";
        tables[1] = "item/itemappearance";
        tables[2] = "item/itemdisplayinfo";
        tables[3] = "item/itemdisplayinfomaterialres";
        tables[4] = "item/itemmodifiedappearance";
        tables[5] = "item/itemsearchname";
        tables[6] = "creature/creaturedisplayinfo";
        tables[7] = "creature/creaturedisplayinfoextra";
        tables[8] = "creature/creaturemodeldata";
        tables[9] = "creature/npcmodelitemslotdisplayinfo";
        tables[10] = "listfile/modelfiledata";
        tables[11] = "listfile/texturefiledata";
    }
    private static HashMap<String, String> setupMap(String filename) throws IOException
    {
        HashMap<String, String> hm = new HashMap<>();
        BufferedReader br = new BufferedReader(new FileReader(filename));
        String line;
        br.readLine();//skip header
        while ((line = br.readLine()) != null) {
            String[] values = line.split(delimiter);
            hm.put(values[0], line);
        }
        br.close();
        return hm;
    }

    //FDID map creation
    private static HashMap<String, String> setupFDIDMap() throws IOException
    {
        String delim = ";";
        HashMap<String, String> hm = new HashMap<>();
        BufferedReader br = new BufferedReader(new FileReader("listfile/listfile.csv"));
        String line;
        while ( (line = br.readLine()) != null ) {
            String[] values = line.split(delim);
            hm.put(values[0],values[1]);
        }
        br.close();
        return hm;
    }

    //itemModifiedAppearance map creation
    private static HashMap<String, String> setupItemModMap() throws IOException
    {
        HashMap<String, String> hm = new HashMap<>();
        BufferedReader br = new BufferedReader(new FileReader(tables[4] + csvEndSuffix));
        String line;
        while ( (line = br.readLine()) != null ) {
            String[] values = line.split(delimiter);
            hm.put(values[1],values[3]);
        }
        br.close();
        return hm;
    }

    //itemAppearance map creation
    private static HashMap<String, String> setupItemAppMap() throws IOException
    {
        HashMap<String, String> hm = new HashMap<>();
        BufferedReader br = new BufferedReader(new FileReader(tables[1] + csvEndSuffix));
        String line;
        while ( (line = br.readLine()) != null ) {
            String[] values = line.split(delimiter);
            hm.put(values[0],values[2] + delimiter + values[3]);
        }
        br.close();
        return hm;
    }

    //setup itemmodifiedappearance map but reversed
    private static HashMap<String, String> setupItemModReversedMap() throws IOException
    {
        HashMap<String, String> hm = new HashMap<>();
        BufferedReader br = new BufferedReader(new FileReader(tables[4] + csvEndSuffix));
        String line;
        while ( (line = br.readLine()) != null ) {
            String[] values = line.split(delimiter);
            hm.put(values[3],values[1]);
        }
        br.close();
        return hm;
    }

    //setup item appearance map but reversed
    private static HashMap<String, String> setupItemAppReversedMap() throws IOException
    {
        HashMap<String, String> hm = new HashMap<>();
        BufferedReader br = new BufferedReader(new FileReader(tables[1] + csvEndSuffix));
        String line;
        while ( (line = br.readLine()) != null ) {
            String[] values = line.split(delimiter);
            hm.put(values[2],values[0]);
        }
        br.close();
        return hm;
    }

    //setup item map
    private static HashMap<String, String> setupItemMap() throws IOException
    {
        HashMap<String, String> hm = new HashMap<>();
        BufferedReader br = new BufferedReader(new FileReader(tables[0] + csvEndSuffix));
        String line;
        while ( (line = br.readLine()) != null ) {
            String[] values = line.split(delimiter);
            hm.put(values[0],values[7]);
        }
        br.close();
        return hm;
    }

    //setup item appearance map for icons
    private static HashMap<String, String> setupItemAppIconMap() throws IOException
    {
        HashMap<String, String> hm = new HashMap<>();
        BufferedReader br = new BufferedReader(new FileReader(tables[1] + csvEndSuffix));
        String line;
        while ( (line = br.readLine()) != null ) {
            String[] values = line.split(delimiter);
            hm.put(values[2],values[3]);
        }
        br.close();
        return hm;
    }

    //setup itemdisplayinfoextra map for items (tables npcmodelitemslotdisplayinfo and itemdisplayinfomaterialres)
    private static HashMap<String, String> setupDisplayExtraItemsMap(String filename) throws IOException
    {
        String splitter = ".";
        HashMap<String, String> hm = new HashMap<>();
        BufferedReader br = new BufferedReader(new FileReader(filename));
        StringBuilder perline = new StringBuilder();
        String last = "";
        String line;
        br.readLine();//skip header
        while ( (line = br.readLine()) != null ) {
            String[] values = line.split(delimiter);
            if (!last.equals(values[3])){
                hm.put(last, perline.toString());
                perline = new StringBuilder();
            }

            perline.append(values[1]).append(splitter).append(values[2]).append(delimiter);
            last = values[3];
        }
        br.close();
        return hm;
    }


}
