import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public class YidongTv {

    public static void main(String[] args) {
        String ipFilePath = "D:\\temp\\TVBox\\code\\tvupdata\\springboot\\txt\\tv1.txt";
        String outputFilePath = "D:\\temp\\TVBox\\code\\tvupdata\\springboot\\txt\\tv2.txt";

        try {
            LinkedHashMap<String, LinkedHashMap<String, ArrayList<String>>> allLives = new LinkedHashMap<>();
            String ipList = readLinesFromFile(ipFilePath);
            TxtSubscribe.parse(allLives, ipList);

            for (Map.Entry<String, LinkedHashMap<String, ArrayList<String>>> entry : allLives.entrySet()) {
                LinkedHashMap<String, ArrayList<String>> lives = entry.getValue();
                ArrayList<String> rmLk = new ArrayList<>();
                for (Map.Entry<String, ArrayList<String>> entry2 : lives.entrySet()) {
                    String lk = entry2.getKey();
                    ArrayList<String> lv = entry2.getValue();
                    ArrayList<String> newLv = new ArrayList<>();
                    for (String string : lv) {
                        if (string.indexOf("ottrrs.hl.chinamobile.com") > -1) {
                            newLv.add(string.replace("88888888", "88888893") + "$hlj移动超清");
                            newLv.add(string.replace("88888888", "88888890") + "$hlj移动高清");
                            newLv.add(string + "$hlj移动");
                        }
                    }
                    if (newLv.size() > 0)
                        lives.put(lk, newLv);
                    else
                        rmLk.add(lk);
                }
                for (String lk : rmLk) {
                    lives.remove(lk);
                }
            }
            String txtLive = TxtSubscribe.live2Txt(allLives);
            writeLinesToFile(outputFilePath, txtLive);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String readLinesFromFile(String filePath) throws IOException {
        StringBuilder lines = new StringBuilder();
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        String line;
        while ((line = reader.readLine()) != null) {
            lines.append(line.trim());
            lines.append("\n");
        }
        reader.close();
        return lines.toString();
    }

    private static void writeLinesToFile(String filePath, String lines) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));
        writer.write(lines);
        writer.newLine();
        writer.close();
    }
}
