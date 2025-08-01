import java.io.BufferedReader;
import java.io.StringReader;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TxtSubscribe {

    private static final Pattern NAME_PATTERN = Pattern.compile(".*,(.+?)$");
    private static final Pattern GROUP_PATTERN = Pattern.compile("group-title=\"(.*?)\"");

    public static void parse(LinkedHashMap<String, LinkedHashMap<String, ArrayList<String>>> linkedHashMap, String str) {
//        System.out.print("----------# parse.str:" + str);
        if (str.startsWith("#EXTM3U")) {
            parseM3u(linkedHashMap, str);
        } else {
            parseTxt(linkedHashMap, str);
        }
    }

    private static void parseM3u(LinkedHashMap<String, LinkedHashMap<String, ArrayList<String>>> linkedHashMap, String str) {
        ArrayList<String> urls;
        try {
            BufferedReader bufferedReader = new BufferedReader(new StringReader(str));
            LinkedHashMap<String, ArrayList<String>> channel = new LinkedHashMap<>();
            LinkedHashMap<String, ArrayList<String>> channelTemp = channel;
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                if (line.equals(""))
                    continue;
                if (line.startsWith("#EXTM3U"))
                    continue;
                if (line.startsWith("#EXTINF")) {
                    String name = getStrByRegex(NAME_PATTERN, line);
                    String group = getStrByRegex(GROUP_PATTERN, line);
                    // 此时再读取一行，就是对应的 url 链接了
                    String url = bufferedReader.readLine().trim();
                    if (linkedHashMap.containsKey(group)) {
                        channelTemp = linkedHashMap.get(group);
                    } else {
                        channelTemp = new LinkedHashMap<>();
                        linkedHashMap.put(group, channelTemp);
                    }
                    if (null != channelTemp && channelTemp.containsKey(name)) {
                        urls = channelTemp.get(name);
                    } else {
                        urls = new ArrayList<>();
                        channelTemp.put(name, urls);
                    }
                    if (null != urls && !urls.contains(url))
                        urls.add(url);
                }
            }
            bufferedReader.close();
            if (channel.isEmpty())
                return;
            linkedHashMap.put("未分组", channel);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String getStrByRegex(Pattern pattern, String line) {
        Matcher matcher = pattern.matcher(line);
        if (matcher.find())
            return matcher.group(1);
        return pattern.pattern().equals(GROUP_PATTERN.pattern()) ? "未分组" : "未命名";
    }

    private static void parseTxt(LinkedHashMap<String, LinkedHashMap<String, ArrayList<String>>> linkedHashMap, String str) {
        ArrayList<String> arrayList;
        try {
            BufferedReader bufferedReader = new BufferedReader(new StringReader(str));
            String readLine = bufferedReader.readLine();
            LinkedHashMap<String, ArrayList<String>> linkedHashMap2 = new LinkedHashMap<>();
            LinkedHashMap<String, ArrayList<String>> linkedHashMap3 = linkedHashMap2;
            while (readLine != null) {
                if (readLine.trim().isEmpty()) {
                    readLine = bufferedReader.readLine();
                } else {
                    String[] split = readLine.split(",", 2);
                    if (split.length < 2) {
                        readLine = bufferedReader.readLine();
                    } else {
                        if (readLine.contains("#genre#")) {
                            String trim = split[0].trim();
                            if (!linkedHashMap.containsKey(trim)) {
                                linkedHashMap3 = new LinkedHashMap<>();
                                linkedHashMap.put(trim, linkedHashMap3);
                            } else {
                                linkedHashMap3 = linkedHashMap.get(trim);
                            }
                        } else {
                            String trim2 = split[0].trim();
                            for (String str2 : split[1].trim().split("#")) {
                                String trim3 = str2.trim();
                                if (!trim3.isEmpty() && (trim3.startsWith("http") || trim3.startsWith("rtsp") || trim3.startsWith("rtmp"))) {
                                    if (!linkedHashMap3.containsKey(trim2)) {
                                        arrayList = new ArrayList<>();
                                        linkedHashMap3.put(trim2, arrayList);
                                    } else {
                                        arrayList = linkedHashMap3.get(trim2);
                                    }
                                    if (!arrayList.contains(trim3)) {
                                        arrayList.add(trim3);
                                    }
                                }
                            }
                        }
                        readLine = bufferedReader.readLine();
                    }
                }
            }
            bufferedReader.close();
            if (linkedHashMap2.isEmpty()) {
                return;
            }
            linkedHashMap.put("未分组", linkedHashMap2);
        } catch (Throwable unused) {
        }
    }

    public static String live2Txt(LinkedHashMap<String, LinkedHashMap<String, ArrayList<String>>> lives) {
        StringBuilder sb = new StringBuilder();
        Iterator<String> groupKeys = lives.keySet().iterator();
        while (groupKeys.hasNext()) {
            String group = groupKeys.next();
            sb.append(group + ",#genre#\n");
            LinkedHashMap<String, ArrayList<String>> allChannel = lives.get(group);
            if (allChannel.isEmpty())
                continue;
            Iterator<String> channelKeys = allChannel.keySet().iterator();
            while (channelKeys.hasNext()) {
                String channel = channelKeys.next();
                ArrayList<String> allUrls = allChannel.get(channel);
                if (allUrls.isEmpty())
                    continue;
                for (int i = 0; i < allUrls.size(); i++) {
                    sb.append(channel + "," + allUrls.get(i) + "\n");
                }
            }
        }
        return sb.toString();
    }
}
