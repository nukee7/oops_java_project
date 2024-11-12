import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class FloydWarshall {

    static final int INF = 99999; 
    static int N;
    
    public static void main(String[] args) {
        String fileName = "data.csv";
        Map<String, Integer> cityIndex = new HashMap<>(); // Maps city names to indices
        int[][] dist;

        try {
            // First pass: count cities and assign each city an index
            int index = 0;
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            reader.readLine(); // Skip header line
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                String origin = parts[0].trim();
                String destination = parts[1].trim();
                if (!cityIndex.containsKey(origin)) {
                    cityIndex.put(origin, index++);
                }
                if (!cityIndex.containsKey(destination)) {
                    cityIndex.put(destination, index++);
                }
            }
            reader.close();

            N = cityIndex.size();
            dist = new int[N][N];

            // Initialize the distance matrix
            for (int[] row : dist) {
                Arrays.fill(row, INF);
            }
            for (int i = 0; i < N; i++) {
                dist[i][i] = 0;
            }

            // Second pass: populate the distance matrix with values from the CSV
            reader = new BufferedReader(new FileReader(fileName));
            reader.readLine(); // Skip header line again
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                String origin = parts[0].trim();
                String destination = parts[1].trim();
                int distance = Integer.parseInt(parts[2].trim());

                int originIndex = cityIndex.get(origin);
                int destIndex = cityIndex.get(destination);
                dist[originIndex][destIndex] = distance;
                dist[destIndex][originIndex] = distance;
            }
            reader.close();

            floydWarshall(dist);

            printSolution(dist, cityIndex);

            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter origin city: ");
            String originCity = scanner.nextLine().trim();
            System.out.print("Enter destination city: ");
            String destinationCity = scanner.nextLine().trim();
            scanner.close();

            getShortestDistance(originCity, destinationCity, cityIndex, dist);

        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
    }

    public static void floydWarshall(int[][] dist) {
        for (int k = 0; k < N; k++) {
            for (int i = 0; i < N; i++) {
                for (int j = 0; j < N; j++) {
                    if (dist[i][k] != INF && dist[k][j] != INF && dist[i][j] > dist[i][k] + dist[k][j]) {
                        dist[i][j] = dist[i][k] + dist[k][j];
                    }
                }
            }
        }
    }

    public static void printSolution(int[][] dist, Map<String, Integer> cityIndex) {
        String[] cities = new String[N];
        for (Map.Entry<String, Integer> entry : cityIndex.entrySet()) {
            cities[entry.getValue()] = entry.getKey();
        }

        System.out.println("Shortest distances between every pair of cities:");

        System.out.print("       ");
        for (int i = 0; i < N; i++) {
            System.out.printf("%10s", cities[i]);
        }
        System.out.println();

        for (int i = 0; i < N; i++) {
            System.out.printf("%-7s", cities[i]);
            for (int j = 0; j < N; j++) {
                if (dist[i][j] == INF) {
                    System.out.print("    INF   ");
                } else {
                    System.out.printf("%10d", dist[i][j]);
                }
            }
            System.out.println();
        }
    }

    // Method to get the shortest distance between two cities
    public static void getShortestDistance(String origin, String destination, Map<String, Integer> cityIndex, int[][] dist) {
        Integer originIndex = cityIndex.get(origin);
        Integer destinationIndex = cityIndex.get(destination);

        if (originIndex == null || destinationIndex == null) {
            System.out.println("One or both cities not found in the dataset.");
            return;
        }

        int distance = dist[originIndex][destinationIndex];
        if (distance == INF) {
            System.out.println("There is no path between " + origin + " and " + destination + ".");
        } else {
            System.out.println("The shortest distance between " + origin + " and " + destination + " is: " + distance);
        }
    }
}
