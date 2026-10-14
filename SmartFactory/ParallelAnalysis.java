public class ParallelAnalysis {
    private ParallelAnalysis() {}

    public static void analyze(final String[] reports, final String[] keywords) throws InterruptedException {
        if (reports == null || reports.length == 0 || keywords == null || keywords.length == 0) {
            throw new IllegalArgumentException("Provide at least one report and one keyword.");
        }
        final int[] counts = new int[reports.length];
        final String[][] matched = new String[reports.length][keywords.length];
        Thread[] workers = new Thread[reports.length];

        for (int i = 0; i < reports.length; i++) {
            final int index = i;
            workers[i] = new Thread(new Runnable() {
                public void run() {
                    String text = reports[index].toLowerCase();
                    int count = 0;
                    for (int k = 0; k < keywords.length; k++) {
                        if (KMP.contains(text, keywords[k].toLowerCase())) matched[index][count++] = keywords[k];
                    }
                    counts[index] = count;
                }
            });
            workers[i].start();
        }
        for (int i = 0; i < workers.length; i++) workers[i].join();

        int reportsWithFaultTerms = 0;
        for (int i = 0; i < reports.length; i++) {
            if (counts[i] > 0) {
                reportsWithFaultTerms++;
                System.out.println("\nReport " + (i + 1) + ": " + reports[i]);
                System.out.print("  Keywords found: ");
                for (int j = 0; j < counts[i]; j++) {
                    if (j > 0) System.out.print(", ");
                    System.out.print(matched[i][j]);
                }
                System.out.println();
            }
        }
        System.out.println("\nReports with matches: " + reportsWithFaultTerms + " of " + reports.length);
    }

    public static void demo() throws InterruptedException {
        String[] reports = {
            "Motor overheating during shift",
            "Bearing vibration needs inspection",
            "Temperature sensor is disconnected",
            "Material supply is low",
            "Motor and sensor inspected",
            "Packaging line operating normally"
        };
        String[] keywords = {"motor", "sensor", "bearing", "overheating", "material"};
        System.out.println("\nPARALLEL FAULT-REPORT ANALYSIS");
        analyze(reports, keywords);
    }

}
