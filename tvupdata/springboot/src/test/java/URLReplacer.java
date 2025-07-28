import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class URLReplacer {

    public static void main(String[] args) {
        String ipFilePath = "D:\\temp\\TVBox\\code\\tvupdata\\springboot\\txt\\1.txt";
        String urlFilePath = "D:\\temp\\TVBox\\code\\tvupdata\\springboot\\txt\\2.txt";
        String outputFilePath = "D:\\temp\\TVBox\\code\\tvupdata\\springboot\\txt\\3.txt";
        String inputFilePath = "D:\\temp\\TVBox\\code\\tvupdata\\springboot\\txt\\4.txt";
        String groupedOutputFilePath = "D:\\temp\\TVBox\\code\\tvupdata\\springboot\\txt\\5.txt";
        String finalOutputFilePath = "D:\\temp\\TVBox\\code\\tvupdata\\springboot\\txt\\6.txt";

        try {
            List<String> ipList = readLinesFromFile(ipFilePath);
            List<String> urlList = readUrlLinesFromFile(urlFilePath);

            if (ipList.isEmpty() || urlList.isEmpty()) {
                System.out.println("IP list or URL list is empty.");
                return;
            }

//            List<String> newUrlList = new ArrayList<>();
//            for (String ip : ipList) {
//                for (String url : urlList) {
//                    String newUrl = replaceIpInUrl(url, ip);
//                    newUrlList.add(newUrl);
//                }
//            }
//
//            // Write the new URLs to 3.txt
//            writeLinesToFile(outputFilePath, newUrlList);
//            System.out.println("New URLs have been written to " + outputFilePath);

            // Read the new URLs from 4.txt and group them
            List<String> urlsFromInputFile = readUrlLinesFromFile(inputFilePath);
            Map<String, List<String>> groupedUrls = groupUrlsByIdentifier(urlsFromInputFile);

            // Write the grouped URLs to 5.txt
            writeGroupedUrlsToFile(groupedOutputFilePath, groupedUrls);
            System.out.println("Grouped URLs have been written to " + groupedOutputFilePath);

            // Create a map of identifiers and prefixes
            Map<String, String> identifierPrefixMap = createIdentifierPrefixMap(urlList);

            // Modify grouped URLs by adding prefix
            Map<String, List<String>> modifiedGroupedUrls = modifyGroupedUrlsWithPrefix(groupedUrls, identifierPrefixMap);

            // Write the modified grouped URLs to 6.txt
            writeModifiedGroupedUrlsToFile(finalOutputFilePath, modifiedGroupedUrls);
            System.out.println("Modified Grouped URLs have been written to " + finalOutputFilePath);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static List<String> readLinesFromFile(String filePath) throws IOException {
        List<String> lines = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        String line;
        while ((line = reader.readLine()) != null) {
            lines.add(line.trim());
        }
        reader.close();
        return lines;
    }

    private static void writeLinesToFile(String filePath, List<String> lines) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));
        for (String line : lines) {
            writer.write(line);
            writer.newLine();
        }
        writer.close();
    }

    private static String replaceIpInUrl(String url, String ip) {
        // Assuming the IP is at the beginning of the URL and followed by a colon and
        // port number or slash
        int endOfProtocolIndex = url.indexOf("//") + 2;
        int startOfPathIndex = url.indexOf("/", endOfProtocolIndex);
        if (startOfPathIndex == -1) {
            startOfPathIndex = url.length();
        }
        String protocolAndHost = url.substring(0, endOfProtocolIndex);
        String path = url.substring(startOfPathIndex);
        return protocolAndHost + ip + path;
    }

    private static Map<String, List<String>> groupUrlsByIdentifier(List<String> urls) {
        Map<String, List<String>> groupedUrls = new HashMap<>();

        for (String url : urls) {
            String identifier = getIdentifierFromUrl(url);
            groupedUrls.putIfAbsent(identifier, new ArrayList<>());

            List<String> group = groupedUrls.get(identifier);
            if (group.size() < 10) {
                group.add(url);
            }
        }

        return groupedUrls;
    }

    private static String getIdentifierFromUrl(String url) {
        // Extract the part after the domain but before the file name
        int startOfFileIndex = url.lastIndexOf("/") - 10;
        String pathPart = url.substring(startOfFileIndex);
        return pathPart.split("/")[0];
    }

    private static void writeGroupedUrlsToFile(String filePath, Map<String, List<String>> groupedUrls) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));
        for (Map.Entry<String, List<String>> entry : groupedUrls.entrySet()) {
            writer.write("Group Identifier: " + entry.getKey());
            writer.newLine();
            for (String url : entry.getValue()) {
                writer.write("  " + url);
                writer.newLine();
            }
            writer.newLine(); // Add a blank line between groups
        }
        writer.close();
    }

    private static List<String> readUrlLinesFromFile(String filePath) throws IOException {
        List<String> lines = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        String line;
        while ((line = reader.readLine()) != null) {
            line = line.trim();
            if (line.indexOf("http") > -1)
                lines.add(line);
        }
        reader.close();
        return lines;
    }

    private static Map<String, String> createIdentifierPrefixMap(List<String> urlList) {
        Map<String, String> identifierPrefixMap = new HashMap<>();
        for (String url : urlList) {
            String identifier = getIdentifierFromUrl(url);
            String prefix = url.substring(0, url.indexOf(","));
            identifierPrefixMap.put(identifier, prefix);
        }
        return identifierPrefixMap;
    }

    private static Map<String, List<String>> modifyGroupedUrlsWithPrefix(Map<String, List<String>> groupedUrls, Map<String, String> identifierPrefixMap) {
        Map<String, List<String>> modifiedGroupedUrls = new HashMap<>();
        for (Map.Entry<String, List<String>> entry : groupedUrls.entrySet()) {
            String identifier = entry.getKey();
            List<String> urls = entry.getValue();
            if (identifierPrefixMap.containsKey(identifier)) {
                String prefix = identifierPrefixMap.get(identifier);
                List<String> modifiedUrls = new ArrayList<>();
                for (String url : urls) {
                    modifiedUrls.add(prefix + "," + url);
                }
                modifiedGroupedUrls.put(identifier, modifiedUrls);
            } else {
                modifiedGroupedUrls.put(identifier, urls);
            }
        }
        return modifiedGroupedUrls;
    }

    private static void writeModifiedGroupedUrlsToFile(String filePath, Map<String, List<String>> modifiedGroupedUrls) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));
        for (Map.Entry<String, List<String>> entry : modifiedGroupedUrls.entrySet()) {
            writer.newLine();
            for (String url : entry.getValue()) {
                writer.write(url);
                writer.newLine();
            }
            writer.newLine(); // Add a blank line between groups
        }
        writer.close();
    }
}
