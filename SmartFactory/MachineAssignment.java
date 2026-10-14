import java.util.Arrays;

public class MachineAssignment {

    private MachineAssignment() {
    }

    public static class AssignmentResult {
        private final int minimumCost;
        private final int[] machineForTask;

        public AssignmentResult(int minimumCost, int[] machineForTask) {
            this.minimumCost = minimumCost;
            this.machineForTask = machineForTask;
        }

        public int getMinimumCost() {
            return minimumCost;
        }

        // Returns a copy so callers cannot change the stored result.
        public int[] getMachineForTask() {
            return Arrays.copyOf(machineForTask, machineForTask.length);
        }
    }

    // Bitmask DP: each task is assigned to one unique machine.
    // machineForTask[task] stores the zero-based machine selected for that task.
    public static AssignmentResult solve(int[][] cost) {
        validateCostMatrix(cost);

        int n = cost.length;
        int states = 1 << n;
        int infinity = Integer.MAX_VALUE / 4;
        int[] dp = new int[states];
        int[] previousMask = new int[states];
        int[] chosenMachine = new int[states];

        Arrays.fill(dp, infinity);
        Arrays.fill(previousMask, -1);
        Arrays.fill(chosenMachine, -1);
        dp[0] = 0;

        for (int mask = 0; mask < states; mask++) {
            if (dp[mask] == infinity) {
                continue;
            }

            int task = countBits(mask);
            if (task >= n) {
                continue;
            }

            for (int machine = 0; machine < n; machine++) {
                if ((mask & (1 << machine)) == 0) {
                    int nextMask = mask | (1 << machine);
                    int newCost = dp[mask] + cost[task][machine];

                    if (newCost < dp[nextMask]) {
                        dp[nextMask] = newCost;
                        previousMask[nextMask] = mask;
                        chosenMachine[nextMask] = machine;
                    }
                }
            }
        }

        int[] machineForTask = new int[n];
        int mask = states - 1;
        for (int task = n - 1; task >= 0; task--) {
            machineForTask[task] = chosenMachine[mask];
            mask = previousMask[mask];
        }

        return new AssignmentResult(dp[states - 1], machineForTask);
    }

    // Kept for compatibility with any code using the earlier method.
    public static int minimumCost(int[][] cost) {
        return solve(cost).getMinimumCost();
    }

    private static void validateCostMatrix(int[][] cost) {
        if (cost == null || cost.length == 0 || cost.length > 15) {
            throw new IllegalArgumentException("Use between 1 and 15 tasks/machines.");
        }

        int n = cost.length;
        for (int row = 0; row < n; row++) {
            if (cost[row] == null || cost[row].length != n) {
                throw new IllegalArgumentException("The cost matrix must be square.");
            }
            for (int value : cost[row]) {
                if (value < 0 || value > 1000000) {
                    throw new IllegalArgumentException(
                        "Costs must be between 0 and 1000000."
                    );
                }
            }
        }
    }

    private static int countBits(int value) {
        int count = 0;
        while (value != 0) {
            value &= value - 1;
            count++;
        }
        return count;
    }
}
