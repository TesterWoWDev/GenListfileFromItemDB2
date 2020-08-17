import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Runner {
    private static final String delimiter = ",";
    private static final String csvEndSuffix = ".csv";
    private static HashMap<String, String> fileIDs;
    private static HashMap<String, String> modelFDID;
    private static HashMap<String, String> textureFDID;
    private static final String[] tables = new String[12];
    public static void main(String[] args) throws IOException{
        fillTable();
        startupTables();
        sortInfoMatRes();
        HashMap<String, String> itemDisplayInfoMaterials = setupDisplayExtraItemsMap(tables[3] + "Sorted" + csvEndSuffix);
        FileWriter listfileWriter = new FileWriter("listfileNEW.csv");
        try (BufferedReader br = new BufferedReader(new FileReader(tables[2] + csvEndSuffix))) {
            String line;
            br.readLine();//skip header
            while ((line = br.readLine()) != null) {
                String[] displayRow = line.split(delimiter);
                if(itemDisplayInfoMaterials.get(displayRow[0]) != null){
                    String displayExtraItemsLine = itemDisplayInfoMaterials.get(displayRow[0]);
                    String[] itemSplit = displayExtraItemsLine.split(delimiter);
                    for (String s : itemSplit) {
                        String[] currItem = s.split("\\.");
                        String filename;
                        if(fileIDs.get(textureFDID.get(currItem[1])) == null){//materialID exists, but no path found
                            if(displayRow[10].equals("0") && !displayRow[11].equals("0") && fileIDs.get(modelFDID.get(displayRow[11])) != null){
                                filename = fileIDs.get(modelFDID.get(displayRow[11]));
                                filename = filename.substring(0,filename.length() - 3) + "_";
                            }else if(fileIDs.get(modelFDID.get(displayRow[10])) == null){
                                filename = "";
                            }else{
                                filename = fileIDs.get(modelFDID.get(displayRow[10]));
                                filename = filename.substring(0,filename.length() - 3) + "_";
                            }
                            if(textureFDID.get(currItem[1]) != null)
                        System.out.println(textureFDID.get(currItem[1]) + ";"+filename + textureFDID.get(currItem[1]) + ".blp");
                            listfileWriter.write(textureFDID.get(currItem[1]) + ";"+filename + textureFDID.get(currItem[1]) + ".blp\n");
                        }
                    }
                }
            }
        }
        listfileWriter.close();
    }
    //sorts itemdisplayinfomaterialres to put all displayIDs in groups for parsing later(cause blizzard cant keep their shit together)
    private static void sortInfoMatRes() throws IOException {
        System.out.println("Sorting itemdisplayinfomaterialres...");
        BufferedReader reader = new BufferedReader(new FileReader(tables[3] + csvEndSuffix));
        Map<String, List<String>> map = new TreeMap<>();
        String line;
        reader.readLine();//skip header
        while ((line = reader.readLine()) != null) {
            String key = getField(line);
            List<String> l = map.computeIfAbsent(key, k -> new LinkedList<>());
            l.add(line);
        }
        reader.close();
        FileWriter writer = new FileWriter(tables[3] + "Sorted" + csvEndSuffix);
        for (List<String> list : map.values()) {
            for (String val : list) {
                writer.write(val);
                writer.write("\n");
            }
        }
        writer.close();
    }

    // extract value you want to sort on(for sorting itemdisplayinfomaterialres)Used in sortInfoMatRes()
    private static String getField(String line) {
        return line.split(",")[3];
    }

    private static HashMap<String, String> setupModelMap() throws IOException
    {
        HashMap<String, String> hm = new HashMap<>();
        BufferedReader br = new BufferedReader(new FileReader(tables[10] + csvEndSuffix));
        String line;
        while ( (line = br.readLine()) != null ) {
            String[] values = line.split(delimiter);
            hm.put(values[3],values[0]);
        }
        br.close();
        return hm;
    }

    //setup textureID->FDID map
    private static HashMap<String, String> setupTextureMap() throws IOException
    {
        HashMap<String, String> hm = new HashMap<>();
        BufferedReader br = new BufferedReader(new FileReader(tables[11] + csvEndSuffix));
        String line;
        while ( (line = br.readLine()) != null ) {
            String[] values = line.split(delimiter);
            hm.put(values[2],values[0]);
        }
        br.close();
        return hm;
    }
    private static void startupTables() throws IOException {
        System.out.println("Starting Tables...");
        fileIDs = setupFDIDMap();
        modelFDID = setupModelMap();
        textureFDID = setupTextureMap();
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
