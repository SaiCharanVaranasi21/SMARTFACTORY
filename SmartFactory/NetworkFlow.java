public class NetworkFlow {
    private NetworkFlow() {}

    public static final class FlowResult {
        public final int maxFlow;
        public final boolean[] reachableFromSource;
        private FlowResult(int flow, boolean[] reachable) {
            this.maxFlow = flow;
            this.reachableFromSource = reachable;
        }
    }

    // Edmonds-Karp: maximum flow using BFS to find augmenting paths.
    public static int maxFlow(int[][] capacity, int source, int sink) {
        return calculate(capacity, source, sink).maxFlow;
    }

    public static void analyze(int[][] capacity, int source, int sink, String[] names) {
        if (capacity == null || capacity.length < 2 || names == null || names.length != capacity.length) {
            throw new IllegalArgumentException("Invalid network data.");
        }
        int n = capacity.length;
        if (source < 0 || source >= n || sink < 0 || sink >= n || source == sink) {
            throw new IllegalArgumentException("Invalid source or sink.");
        }
        for (int i = 0; i < n; i++) {
            if (capacity[i] == null || capacity[i].length != n) {
                throw new IllegalArgumentException("Capacity matrix must be square.");
            }
            for (int j = 0; j < n; j++) {
                if (capacity[i][j] < 0) throw new IllegalArgumentException("Capacities cannot be negative.");
            }
        }

        FlowResult result = calculate(capacity, source, sink);
        System.out.println("\nMaximum flow: " + result.maxFlow);
        System.out.println("Bottleneck edges:");
        int cutCapacity = 0;
        boolean printed = false;
        for (int u = 0; u < n; u++) {
            for (int v = 0; v < n; v++) {
                if (result.reachableFromSource[u] && !result.reachableFromSource[v] && capacity[u][v] > 0) {
                    System.out.println(names[u] + " -> " + names[v] + " | Capacity: " + capacity[u][v]);
                    cutCapacity += capacity[u][v];
                    printed = true;
                }
            }
        }
        if (!printed) System.out.println("No positive-capacity cut edges found.");
        System.out.println("Total bottleneck capacity: " + cutCapacity);
        
    }

    private static FlowResult calculate(int[][] capacity, int source, int sink) {
        int n = capacity.length;
        int[][] residual = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++) residual[i][j] = capacity[i][j];

        int totalFlow = 0;
        int[] parent = new int[n];
        while (bfs(residual, source, sink, parent)) {
            int pathFlow = Integer.MAX_VALUE;
            for (int v = sink; v != source; v = parent[v])
                pathFlow = Math.min(pathFlow, residual[parent[v]][v]);
            for (int v = sink; v != source; v = parent[v]) {
                int u = parent[v];
                residual[u][v] -= pathFlow;
                residual[v][u] += pathFlow;
            }
            totalFlow += pathFlow;
        }
        return new FlowResult(totalFlow, reachable(residual, source));
    }

    private static boolean bfs(int[][] residual, int source, int sink, int[] parent) {
        int n = residual.length;
        boolean[] visited = new boolean[n];
        int[] queue = new int[n];
        int front = 0, rear = 0;
        queue[rear++] = source;
        visited[source] = true;
        parent[source] = -1;
        while (front < rear) {
            int u = queue[front++];
            for (int v = 0; v < n; v++) {
                if (!visited[v] && residual[u][v] > 0) {
                    visited[v] = true;
                    parent[v] = u;
                    queue[rear++] = v;
                }
            }
        }
        return visited[sink];
    }

    private static boolean[] reachable(int[][] residual, int source) {
        int n = residual.length;
        boolean[] seen = new boolean[n];
        int[] queue = new int[n];
        int front = 0, rear = 0;
        queue[rear++] = source;
        seen[source] = true;
        while (front < rear) {
            int u = queue[front++];
            for (int v = 0; v < n; v++) {
                if (!seen[v] && residual[u][v] > 0) {
                    seen[v] = true;
                    queue[rear++] = v;
                }
            }
        }
        return seen;
    }

    public static void demo() {
        // Source -> production stages -> sink; capacities represent units/hour.
        int[][] capacity = {
            {0, 8, 5, 0, 0, 0},
            {0, 0, 0, 5, 3, 0},
            {0, 0, 0, 2, 4, 0},
            {0, 0, 0, 0, 0, 7},
            {0, 0, 0, 0, 0, 6},
            {0, 0, 0, 0, 0, 0}
        };
        String[] names = {"Source", "Line-A", "Line-B", "Cell-X", "Cell-Y", "Sink"};
        FlowResult result = calculate(capacity, 0, 5);
        System.out.println("\nPRODUCTION BOTTLENECK ANALYSIS");
        System.out.println("Maximum flow: " + result.maxFlow + " units/hour");
        System.out.println("Bottleneck edges:");
        int cutCapacity = 0;
        boolean printed = false;
        for (int u = 0; u < capacity.length; u++) {
            for (int v = 0; v < capacity.length; v++) {
                if (result.reachableFromSource[u] && !result.reachableFromSource[v] && capacity[u][v] > 0) {
                    System.out.println("  " + names[u] + " -> " + names[v] + " (" + capacity[u][v] + " units/hour)");
                    cutCapacity += capacity[u][v];
                    printed = true;
                }
            }
        }
        if (!printed) System.out.println("  No cut edge found in this network.");
        System.out.println("Total bottleneck capacity: " + cutCapacity + " units/hour");
    }
}
