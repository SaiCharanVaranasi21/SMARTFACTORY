public class FactoryData {

    private static Machine[] machines = {
        new Machine("MCH-101", "Assembly-A", "PRD-A102", "Running", "Overheating",
            "Motor overheating detected. Cooling inspection required."),
        new Machine("MCH-102", "Assembly-B", "PRD-B205", "Maintenance", "Bearing Failure",
            "Abnormal vibration detected. Bearing replacement required."),
        new Machine("MCH-103", "Packaging-A", "PRD-C310", "Running", "Sensor Fault",
            "Temperature sensor fault detected during production."),
        new Machine("MCH-104", "Assembly-A", "PRD-A450", "Running", "Motor Overheating",
            "Motor overheating after continuous production. Cooling check required."),
        new Machine("MCH-105", "Packaging-B", "INV-X550", "Stopped", "Low Material",
            "Raw material level is low. Inventory refill required.")
    };

    public static Machine[] getMachines() {
        return machines;
    }

    public static boolean addMachine(String id, String productionLine, String productionCode,
                                     String status, String fault, String report) {
        if (!validRequiredFields(id, productionLine, productionCode, status)) {
            return false;
        }

        for (int i = 0; i < machines.length; i++) {
            if (machines[i].getId().equalsIgnoreCase(id)) {
                return false;
            }
        }

        Machine newMachine = new Machine(id, productionLine, productionCode, status, fault, report);
        Machine[] updated = new Machine[machines.length + 1];

        for (int i = 0; i < machines.length; i++) {
            updated[i] = machines[i];
        }
        updated[machines.length] = newMachine;
        machines = updated;
        return true;
    }

    public static Machine findMachine(String id) {
        for (int i = 0; i < machines.length; i++) {
            if (machines[i].getId().equalsIgnoreCase(id)) {
                return machines[i];
            }
        }
        return null;
    }

    public static boolean editMachine(String id, String productionLine, String productionCode,
                                      String status, String fault, String report) {
        if (!validRequiredFields(id, productionLine, productionCode, status)) {
            return false;
        }

        Machine machine = findMachine(id);
        if (machine == null) {
            return false;
        }

        machine.setProductionLine(productionLine);
        machine.setProductionCode(productionCode);
        machine.setStatus(status);
        machine.setFault(fault);
        machine.setReport(report);
        return true;
    }

    public static boolean deleteMachine(String id) {
        int index = -1;

        for (int i = 0; i < machines.length; i++) {
            if (machines[i].getId().equalsIgnoreCase(id)) {
                index = i;
                break;
            }
        }

        if (index == -1) {
            return false;
        }

        Machine[] updated = new Machine[machines.length - 1];
        int j = 0;

        for (int i = 0; i < machines.length; i++) {
            if (i != index) {
                updated[j++] = machines[i];
            }
        }

        machines = updated;
        return true;
    }

    public static void showAllMachines() {
        System.out.println("\nMACHINE DASHBOARD");
        System.out.println("Total Machines: " + machines.length);

        for (Machine machine : machines) {
            printMachine(machine);
        }
    }

    public static void filterByStatus(String status) {
        int count = 0;
        System.out.println("\nMACHINES: " + status.toUpperCase());

        for (Machine machine : machines) {
            if (machine.getStatus().equalsIgnoreCase(status)) {
                printShortMachine(machine);
                count++;
            }
        }

        System.out.println("Matching machines: " + count);
    }

    public static void showStatistics() {
        int running = 0;
        int maintenance = 0;
        int stopped = 0;
        int other = 0;

        for (Machine machine : machines) {
            String status = machine.getStatus();
            if (status.equalsIgnoreCase("Running")) {
                running++;
            } else if (status.equalsIgnoreCase("Maintenance")) {
                maintenance++;
            } else if (status.equalsIgnoreCase("Stopped")) {
                stopped++;
            } else {
                other++;
            }
        }

        System.out.println("\nMACHINE STATISTICS");
        System.out.println("Total Machines : " + machines.length);
        System.out.println("Running        : " + running);
        System.out.println("Maintenance    : " + maintenance);
        System.out.println("Stopped        : " + stopped);
        System.out.println("Other Status   : " + other);
    }

    public static void showProductionSummary() {
        System.out.println("\nPRODUCTION LINE SUMMARY");

        String[] lines = new String[machines.length];
        int[] counts = new int[machines.length];
        int unique = 0;

        for (int i = 0; i < machines.length; i++) {
            String line = machines[i].getProductionLine();
            int found = -1;

            for (int j = 0; j < unique; j++) {
                if (lines[j].equalsIgnoreCase(line)) {
                    found = j;
                    break;
                }
            }

            if (found == -1) {
                lines[unique] = line;
                counts[unique] = 1;
                unique++;
            } else {
                counts[found]++;
            }
        }

        for (int i = 0; i < unique; i++) {
            System.out.println(lines[i] + " : " + counts[i] + " machine(s)");
        }
    }

    public static void sortMachines(int option) {
        // Bubble sort is used here to keep the implementation simple and DSA-focused.
        for (int i = 0; i < machines.length - 1; i++) {
            for (int j = 0; j < machines.length - i - 1; j++) {
                String a;
                String b;

                if (option == 1) {
                    a = machines[j].getId();
                    b = machines[j + 1].getId();
                } else if (option == 2) {
                    a = machines[j].getStatus();
                    b = machines[j + 1].getStatus();
                } else {
                    a = machines[j].getProductionLine();
                    b = machines[j + 1].getProductionLine();
                }

                if (a.compareToIgnoreCase(b) > 0) {
                    Machine temp = machines[j];
                    machines[j] = machines[j + 1];
                    machines[j + 1] = temp;
                }
            }
        }
    }

    public static void showMaintenancePriority() {
        System.out.println("\nMAINTENANCE PRIORITY");

        int priorityCount = 0;

        // Maintenance and stopped machines are treated as high priority.
        // Faults containing overheating/failure/sensor are also highlighted.
        for (Machine machine : machines) {
            String status = machine.getStatus().toLowerCase();
            String fault = machine.getFault().toLowerCase();

            boolean high = status.equals("maintenance") || status.equals("stopped")
                || fault.indexOf("failure") >= 0
                || fault.indexOf("overheating") >= 0
                || fault.indexOf("sensor") >= 0;

            if (high) {
                System.out.println("Priority: HIGH | " + machine.getId()
                    + " | Status: " + machine.getStatus()
                    + " | Fault: " + machine.getFault());
                priorityCount++;
            }
        }

        if (priorityCount == 0) {
            System.out.println("No high-priority maintenance machines found.");
        } else {
            System.out.println("High-priority machines: " + priorityCount);
        }
    }

    private static boolean validRequiredFields(String id, String line, String code, String status) {
        return id != null && id.trim().length() > 0
            && line != null && line.trim().length() > 0
            && code != null && code.trim().length() > 0
            && status != null && status.trim().length() > 0;
    }

    public static void printMachine(Machine machine) {
        System.out.println("Machine ID      : " + machine.getId());
        System.out.println("Production Line : " + machine.getProductionLine());
        System.out.println("Production Code : " + machine.getProductionCode());
        System.out.println("Status          : " + machine.getStatus());
        System.out.println("Fault           : " + machine.getFault());
        System.out.println("Report          : " + machine.getReport());
    }

    private static void printShortMachine(Machine machine) {
        System.out.println(machine.getId() + " | "
            + machine.getProductionLine() + " | "
            + machine.getProductionCode() + " | "
            + machine.getStatus() + " | "
            + machine.getFault());
    }
}
