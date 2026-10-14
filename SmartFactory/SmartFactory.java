import java.io.BufferedReader;
import java.io.InputStreamReader;

public class SmartFactory {

    private static BufferedReader input =
        new BufferedReader(new InputStreamReader(System.in));

    private static String read(String message) throws Exception {
        System.out.print(message);
        return input.readLine();
    }

    private static int readInt(String message) throws Exception {
        while (true) {
            try {
                return Integer.parseInt(read(message));
            } catch (Exception e) {
                System.out.println("Enter a valid number.");
            }
        }
    }

    private static int readIntInRange(String message, int min, int max) throws Exception {
        while (true) {
            int value = readInt(message);
            if (value >= min && value <= max) return value;
            System.out.println("Enter a value from " + min + " to " + max + ".");
        }
    }

    private static String readRequired(String message) throws Exception {
        while (true) {
            String value = read(message).trim();
            if (!value.isEmpty()) return value;
            System.out.println("This value cannot be empty.");
        }
    }

    private static void addMachine() throws Exception {
        System.out.println("\nADD NEW MACHINE");

        String id = read("Machine ID: ").trim();
        String line = read("Production Line: ").trim();
        String code = read("Production Code: ").trim();
        String status = read("Status: ").trim();
        String fault = read("Fault: ").trim();
        String report = read("Maintenance Report: ").trim();

        if (id.length() == 0 || line.length() == 0 ||
            code.length() == 0 || status.length() == 0) {
            System.out.println(
                "Machine ID, production line, code and status are required."
            );
            return;
        }

        boolean added = FactoryData.addMachine(
            id, line, code, status, fault, report
        );

        if (added) {
            System.out.println("\nMachine added successfully.");
            System.out.println("Machine ID: " + id);
        } else {
            System.out.println(
                "\nMachine could not be added."
                + " A machine with this ID already exists."
            );
        }
    }

    private static void editMachine() throws Exception {
        System.out.println("\nEDIT MACHINE");

        String id = read("Enter Machine ID to edit: ").trim();
        Machine machine = FactoryData.findMachine(id);

        if (machine == null) {
            System.out.println("Machine not found.");
            return;
        }

        System.out.println("\nCurrent Details:");
        System.out.println("Production Line : " + machine.getProductionLine());
        System.out.println("Production Code : " + machine.getProductionCode());
        System.out.println("Status          : " + machine.getStatus());
        System.out.println("Fault           : " + machine.getFault());
        System.out.println("Report          : " + machine.getReport());

        String line = read("\nNew Production Line: ").trim();
        String code = read("New Production Code: ").trim();
        String status = read("New Status: ").trim();
        String fault = read("New Fault: ").trim();
        String report = read("New Maintenance Report: ").trim();

        if (line.length() == 0 || code.length() == 0 || status.length() == 0) {
            System.out.println("Production line, code and status are required.");
            return;
        }

        boolean updated = FactoryData.editMachine(
            id, line, code, status, fault, report
        );

        if (updated) {
            System.out.println("\nMachine details updated successfully.");
        } else {
            System.out.println("Machine could not be updated.");
        }
    }

    private static void searchMachines() throws Exception {
        System.out.println("\nMACHINE SEARCH");

        String query = readRequired(
            "Enter machine ID, production code, fault or report keyword: "
        ).toLowerCase();

        boolean found = false;
        int matchCount = 0;

        Machine[] machines = FactoryData.getMachines();

        for (int i = 0; i < machines.length; i++) {
            Machine machine = machines[i];

            String data =
                machine.getId().toLowerCase() + " " +
                machine.getProductionCode().toLowerCase() + " " +
                machine.getFault().toLowerCase() + " " +
                machine.getReport().toLowerCase();

            if (KMP.contains(data, query)) {
                System.out.println("\nMatching machine:");
                System.out.println("Machine ID      : " + machine.getId());
                System.out.println("Production Line : " + machine.getProductionLine());
                System.out.println("Production Code : " + machine.getProductionCode());
                System.out.println("Status          : " + machine.getStatus());
                System.out.println("Fault           : " + machine.getFault());
                found = true;
                matchCount++;
            }
        }

        if (!found) {
            System.out.println("No matching machine found.");
        } else {
            System.out.println("Total matches: " + matchCount);
        }
    }

    private static void fuzzyMachineSearch() throws Exception {
        System.out.println("\nFUZZY MACHINE SEARCH");

        String query = read("Enter machine ID: ");

        Machine[] machines = FactoryData.getMachines();

        int best = EditDistance.bestMachine(query, machines);
        Machine machine = machines[best];

        int distance =
            EditDistance.levenshtein(
                query.toLowerCase(),
                machine.getId().toLowerCase()
            );

        System.out.println("\nSuggested Machine: " + machine.getId());
        System.out.println("Edit Distance    : " + distance);
        System.out.println("Production Line  : " + machine.getProductionLine());
        System.out.println("Status           : " + machine.getStatus());
        System.out.println("Fault            : " + machine.getFault());
    }

    private static void faultAnalysis() throws Exception {
        System.out.println("\nFAULT KEYWORD ANALYSIS");
        int keywordCount = readIntInRange("How many keywords do you want to search? (1-20): ", 1, 20);
        AhoCorasick searcher = new AhoCorasick(5000);
        String[] keywords = new String[keywordCount];
        for (int i = 0; i < keywordCount; i++) {
            keywords[i] = readRequired("Keyword " + (i + 1) + ": ").toLowerCase();
            searcher.addPattern(keywords[i]);
        }
        searcher.build();

        Machine[] machines = FactoryData.getMachines();
        int matchedMachines = 0;
        for (int i = 0; i < machines.length; i++) {
            Machine machine = machines[i];
            int count = searcher.countMatches(
                (machine.getFault() + " " + machine.getReport()).toLowerCase()
            );
            if (count > 0) {
                System.out.println(machine.getId() + " | Matches: " + count);
                matchedMachines++;
            }
        }
        if (matchedMachines == 0) {
            System.out.println("No matching fault keywords found.");
        } else {
            System.out.println("Machines with matches: " + matchedMachines);
        }
    }

    private static void machineAssignment() throws Exception {
        System.out.println("\nPRODUCTION RESOURCE ASSIGNMENT");

        int n;
        while (true) {
            n = readInt("Enter number of tasks/machines (1-15): ");
            if (n >= 1 && n <= 15) {
                break;
            }
            System.out.println("Enter a value between 1 and 15.");
        }

        int[][] cost = new int[n][n];
        System.out.println("\nEnter assignment costs (0 to 1,000,000):");
        for (int task = 0; task < n; task++) {
            for (int machine = 0; machine < n; machine++) {
                while (true) {
                    int value = readInt("Task " + (task + 1)
                        + " -> Machine " + (machine + 1) + ": ");
                    if (value >= 0 && value <= 1000000) {
                        cost[task][machine] = value;
                        break;
                    }
                    System.out.println("Enter a cost between 0 and 1,000,000.");
                }
            }
        }

        try {
            MachineAssignment.AssignmentResult result = MachineAssignment.solve(cost);
            int[] assignment = result.getMachineForTask();

            System.out.println("\nOptimal assignment:");
            int total = 0;
            for (int task = 0; task < n; task++) {
                int machine = assignment[task];
                int assignedCost = cost[task][machine];
                total += assignedCost;
                System.out.println("Task " + (task + 1) + " -> Machine "
                    + (machine + 1) + " | Cost: " + assignedCost);
            }

            System.out.println("Minimum total cost: " + result.getMinimumCost());
        } catch (IllegalArgumentException e) {
            System.out.println("Could not calculate assignment: " + e.getMessage());
        }
    }


    private static void networkFlowMenu() throws Exception {
        System.out.println("\nPRODUCTION BOTTLENECK ANALYSIS");
        int n = readIntInRange("Number of network nodes (2-12): ", 2, 12);
        String[] names = new String[n];
        for (int i = 0; i < n; i++) names[i] = readRequired("Name of node " + (i + 1) + ": ");

        int source = readIntInRange("Source node number (1-" + n + "): ", 1, n) - 1;
        int sink = readIntInRange("Sink node number (1-" + n + "): ", 1, n) - 1;
        if (source == sink) {
            System.out.println("Source and sink must be different.");
            return;
        }

        int[][] capacity = new int[n][n];
        System.out.println("Enter directed edge capacities. Use 0 if no edge exists.");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i == j) {
                    capacity[i][j] = 0;
                } else {
                    capacity[i][j] = readIntInRange(
                        names[i] + " -> " + names[j] + " capacity: ", 0, 1000000
                    );
                }
            }
        }
        NetworkFlow.analyze(capacity, source, sink, names);
    }

    private static void vertexCoverMenu() throws Exception {
        System.out.println("\nCONFLICTING JOBS ANALYSIS");
        int vertices = readIntInRange("Number of jobs/resources (2-30): ", 2, 30);
        String[] names = new String[vertices];
        for (int i = 0; i < vertices; i++) names[i] = readRequired("Name of job " + (i + 1) + ": ");
        int edgeCount = readIntInRange("Number of conflict edges (0-100): ", 0, 100);
        int[][] edges = new int[edgeCount][2];
        for (int i = 0; i < edgeCount; i++) {
            System.out.println("Conflict edge " + (i + 1) + ":");
            int u = readIntInRange("  First job (1-" + vertices + "): ", 1, vertices) - 1;
            int v = readIntInRange("  Second job (1-" + vertices + "): ", 1, vertices) - 1;
            if (u == v) {
                System.out.println("A conflict must connect two different jobs. Please re-enter this edge.");
                i--;
                continue;
            }
            edges[i][0] = u;
            edges[i][1] = v;
        }

        boolean[] cover = VertexCoverApprox.approximateVertexCover(vertices, edges);
        int selected = 0;
        System.out.println("\nJobs selected for review:");
        for (int i = 0; i < cover.length; i++) {
            if (cover[i]) {
                System.out.println("  " + names[i]);
                selected++;
            }
        }
        if (selected == 0) System.out.println("  No jobs selected; no conflict edges were entered.");
        boolean allCovered = true;
        for (int[] edge : edges) {
            if (!cover[edge[0]] && !cover[edge[1]]) allCovered = false;
        }
        System.out.println("Selected jobs: " + selected);
        System.out.println("Conflict check: " + (allCovered ? "Passed" : "Failed"));
    }

    private static void deleteMachine() throws Exception {
        System.out.println("\nDELETE MACHINE");
        String id = read("Enter Machine ID to delete: ").trim();
        Machine machine = FactoryData.findMachine(id);

        if (machine == null) {
            System.out.println("Machine not found.");
            return;
        }

        System.out.println("Machine: " + machine.getId() + " | " + machine.getProductionLine());
        String confirm = read("Are you sure? (yes/no): ").trim();

        if (confirm.equalsIgnoreCase("yes")) {
            if (FactoryData.deleteMachine(id)) {
                System.out.println("Machine deleted successfully.");
            } else {
                System.out.println("Machine could not be deleted.");
            }
        } else {
            System.out.println("Deletion cancelled.");
        }
    }

    private static void filterMachines() throws Exception {
        System.out.println("\nFILTER MACHINES");
        System.out.println("1. Running");
        System.out.println("2. Maintenance");
        System.out.println("3. Stopped");
        System.out.println("4. Enter status manually");

        int choice = readInt("Enter choice: ");
        String status;

        switch (choice) {
            case 1:
                status = "Running";
                break;
            case 2:
                status = "Maintenance";
                break;
            case 3:
                status = "Stopped";
                break;
            case 4:
                status = read("Enter status: ").trim();
                break;
            default:
                System.out.println("Invalid choice.");
                return;
        }

        if (status.length() == 0) {
            System.out.println("Status cannot be empty.");
            return;
        }
        FactoryData.filterByStatus(status);
    }

    private static void sortMachines() throws Exception {
        System.out.println("\nSORT MACHINES");
        System.out.println("1. Machine ID");
        System.out.println("2. Status");
        System.out.println("3. Production Line");

        int choice = readInt("Sort by: ");
        if (choice < 1 || choice > 3) {
            System.out.println("Invalid choice.");
            return;
        }

        FactoryData.sortMachines(choice);
        System.out.println("\nSorted machine records:");
        FactoryData.showAllMachines();
    }

    private static void viewMachineDetails() throws Exception {
        System.out.println("\nMACHINE DETAILS");
        String id = read("Enter Machine ID: ").trim();
        Machine machine = FactoryData.findMachine(id);

        if (machine == null) {
            System.out.println("Machine not found.");
            return;
        }

        FactoryData.printMachine(machine);
    }

    private static void analyticsMenu() throws Exception {
        while (true) {
            System.out.println("\nMACHINE ANALYTICS");
            System.out.println("1. Filter by Status");
            System.out.println("2. Machine Statistics");
            System.out.println("3. Sort Machines");
            System.out.println("4. Maintenance Priority");
            System.out.println("5. Production Line Summary");
            System.out.println("6. View Machine Details");
            System.out.println("0. Back to Main Menu");

            int choice = readInt("Enter choice: ");
            switch (choice) {
                case 1:
                    filterMachines();
                    break;
                case 2:
                    FactoryData.showStatistics();
                    break;
                case 3:
                    sortMachines();
                    break;
                case 4:
                    FactoryData.showMaintenancePriority();
                    break;
                case 5:
                    FactoryData.showProductionSummary();
                    break;
                case 6:
                    viewMachineDetails();
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    private static void randomizedSortMenu() throws Exception {
        System.out.println("\nRANDOMIZED QUICKSORT");
        int n = readIntInRange("How many duration values? (1-1000): ", 1, 1000);
        int[] values = new int[n];
        for (int i = 0; i < n; i++) {
            values[i] = readIntInRange("Duration " + (i + 1) + ": ", 0, 1000000);
        }
        System.out.print("Before sorting: ");
        printValues(values);
        RandomizedAlgorithms.randomizedQuickSort(values);
        System.out.print("After sorting : ");
        printValues(values);
    }

    private static void printValues(int[] values) {
        for (int i = 0; i < values.length; i++) {
            if (i > 0) System.out.print(" ");
            System.out.print(values[i]);
        }
        System.out.println();
    }

    private static void parallelReportsMenu() throws Exception {
        System.out.println("\nPARALLEL FAULT-REPORT ANALYSIS");
        int reportCount = readIntInRange("Number of reports (1-100): ", 1, 100);
        String[] reports = new String[reportCount];
        for (int i = 0; i < reportCount; i++) {
            reports[i] = readRequired("Report " + (i + 1) + ": ");
        }
        int keywordCount = readIntInRange("Number of keywords (1-20): ", 1, 20);
        String[] keywords = new String[keywordCount];
        for (int i = 0; i < keywordCount; i++) {
            keywords[i] = readRequired("Keyword " + (i + 1) + ": ").toLowerCase();
        }
        try {
            ParallelAnalysis.analyze(reports, keywords);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Parallel analysis was interrupted.");
        }
    }

    private static void extraAlgorithmsMenu() throws Exception {
        while (true) {
            System.out.println("\nRANDOMIZED & PARALLEL ANALYSIS");
            System.out.println("1. Randomized QuickSort (enter duration values)");
            System.out.println("2. Parallel fault-report analysis (enter reports and keywords)");
            System.out.println("0. Back to Main Menu");

            int choice = readInt("Enter choice: ");
            switch (choice) {
                case 1:
                    randomizedSortMenu();
                    break;
                case 2:
                    parallelReportsMenu();
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    private static void showMenu() {
        System.out.println("\n================================================");
        System.out.println(" SMARTFACTORY");
        System.out.println(" Industrial Production Analytics Platform");
        System.out.println(" DSA-3 Final Review");
        System.out.println("================================================");
        System.out.println("1. View Machine Dashboard");
        System.out.println("2. Add New Machine");
        System.out.println("3. Edit Machine");
        System.out.println("4. Delete Machine");
        System.out.println("5. Search Machine / Code / Report");
        System.out.println("6. Fault Keyword Analysis");
        System.out.println("7. Fuzzy Machine Search");
        System.out.println("8. Machine Analytics");
        System.out.println("9. Optimize Machine Assignment");
        System.out.println("10. Production Bottleneck Analysis");
        System.out.println("11. Conflicting Jobs Analysis");
        System.out.println("12. Randomized & Parallel Analysis");
        System.out.println("0. Exit");
    }

    public static void main(String[] args) throws Exception {

        while (true) {
            showMenu();
            int choice = readInt("Enter choice: ");

            switch (choice) {
                case 1:
                    FactoryData.showAllMachines();
                    break;
                case 2:
                    addMachine();
                    break;
                case 3:
                    editMachine();
                    break;
                case 4:
                    deleteMachine();
                    break;
                case 5:
                    searchMachines();
                    break;
                case 6:
                    faultAnalysis();
                    break;
                case 7:
                    fuzzyMachineSearch();
                    break;
                case 8:
                    analyticsMenu();
                    break;
                case 9:
                    machineAssignment();
                    break;
                case 10:
                    networkFlowMenu();
                    break;
                case 11:
                    vertexCoverMenu();
                    break;
                case 12:
                    extraAlgorithmsMenu();
                    break;
                case 0:
                    System.out.println("\nSession ended.");
                    return;
                default:
                    System.out.println("Invalid choice. Select an option from the menu.");
            }
        }
    }
}
