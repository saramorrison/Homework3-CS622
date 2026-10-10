package homework3;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.HashMap;
import java.util.Map;

public class Main {

    public static void main(String[] args) {
        // get breed data from API
        String apiURL = "https://dogapi.dog/api/v2/breeds";

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiURL))
                .build();

        try {
            HttpResponse<String> response = client.send(
                    request,
                    HttpResponse.BodyHandlers.ofString()
            );

            String data = response.body();

            Files.writeString(Path.of("breeds.json"), data);
            System.out.println("Data saved to breeds.json");

            // words to search for
            String[] words = {"dog", "breed", "weight"};

            // Use Regex to count how many times each word appears
            Map<String, Integer> regexCounts = new HashMap<>();
            long regexStart = System.nanoTime();

            for (String word : words) {
                Pattern pattern = Pattern.compile(
                        "\\b" + Pattern.quote(word) + "\\b",
                        Pattern.CASE_INSENSITIVE
                );

                Matcher matcher = pattern.matcher(data);
                int count = 0;

                while (matcher.find()) {
                    count++;
                }

                regexCounts.put(word, count);
            }

            long regexEnd = System.nanoTime();

            System.out.println("Regex Results:");

            for (String word : words) {
                System.out.println(
                        word + ": " + regexCounts.get(word) + " times"
                );
            }

            System.out.println("Regex search time: "
                    + (regexEnd - regexStart) + " nanoseconds");

            long indexStart = System.nanoTime();

            Map<String, Integer> index = buildIndex(data);
            Map<String, Integer> indexCounts = new HashMap<>();

            for (String word : words) {
                int count = index.getOrDefault(word.toLowerCase(), 0);
                indexCounts.put(word, count);
            }

            long indexEnd = System.nanoTime();

            System.out.println("\nInverted Index Results:");

            for (String word : words) {
                System.out.println(
                        word + ": " + indexCounts.get(word) + " times"
                );
            }

            System.out.println("Inverted index search time: "
                    + (indexEnd - indexStart) + " nanoseconds");

        } catch (IOException e) {
            System.out.println("Error retrieving or saving data.");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Request was interrupted.");
        }
    }

    public static Map<String, Integer> buildIndex(String data) {
        Map<String, Integer> index = new HashMap<>();

        String[] tokens = data.toLowerCase().split("\\W+");

        for (String token : tokens) {
            if (!token.isEmpty()) {
                // Add one to the count each time a word appears
                index.merge(token, 1, Integer::sum);
            }
        }

        return index;
    }

}
