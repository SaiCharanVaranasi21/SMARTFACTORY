public class VertexCoverApprox {
    private VertexCoverApprox() {}

    // Greedy 2-approximation: repeatedly select an uncovered edge and add both endpoints.
    public static boolean[] approximateVertexCover(int vertices, int[][] edges) {
        boolean[] chosen = new boolean[vertices];
        boolean[] covered = new boolean[edges.length];
        for (int i = 0; i < edges.length; i++) {
            if (!covered[i]) {
                int u = edges[i][0];
                int v = edges[i][1];
                chosen[u] = true;
                chosen[v] = true;
                for (int j = 0; j < edges.length; j++) {
                    if (edges[j][0] == u || edges[j][1] == u ||
                        edges[j][0] == v || edges[j][1] == v) covered[j] = true;
                }
            }
        }
        return chosen;
    }

    public static void demo() {
        // Vertices are jobs; an edge means two jobs conflict over a shared resource.
        String[] jobs = {"Packaging", "Assembly-A", "Assembly-B", "QualityCheck", "Dispatch"};
        int[][] edges = {{0,1}, {0,2}, {1,3}, {2,3}, {3,4}};
        boolean[] cover = approximateVertexCover(jobs.length, edges);
        int count = 0;
        System.out.println("\nCONFLICTING JOBS ANALYSIS");
        System.out.println("Jobs selected for rescheduling/resource review:");
        for (int i = 0; i < cover.length; i++) {
            if (cover[i]) {
                System.out.println("  - " + jobs[i]);
                count++;
            }
        }
        boolean allCovered = true;
        for (int i = 0; i < edges.length; i++) {
            if (!cover[edges[i][0]] && !cover[edges[i][1]]) allCovered = false;
        }
        System.out.println("Selected jobs: " + count);
        System.out.println("Conflict check: " + (allCovered ? "YES" : "NO"));
    }
}
