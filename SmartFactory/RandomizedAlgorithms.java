import java.util.Random;

public class RandomizedAlgorithms {
    private RandomizedAlgorithms() {}

    public static void randomizedQuickSort(int[] values) {
        quickSort(values, 0, values.length - 1, new Random());
    }

    private static void quickSort(int[] a, int low, int high, Random random) {
        if (low >= high) return;
        int pivotIndex = low + random.nextInt(high - low + 1);
        int pivot = a[pivotIndex];
        swap(a, pivotIndex, high);
        int store = low;
        for (int i = low; i < high; i++) {
            if (a[i] <= pivot) {
                swap(a, i, store++);
            }
        }
        swap(a, store, high);
        quickSort(a, low, store - 1, random);
        quickSort(a, store + 1, high, random);
    }

    private static void swap(int[] a, int i, int j) {
        int temp = a[i]; a[i] = a[j]; a[j] = temp;
    }

    public static void demo() {
        int[] sample = {42, 12, 31, 7, 25, 18, 7, 50, 3};
        System.out.println("\nRANDOMIZED QUICKSORT");
        System.out.print("Unsorted maintenance durations: ");
        print(sample);
        randomizedQuickSort(sample);
        System.out.print("Sorted durations: ");
        print(sample);
    }

    private static void print(int[] values) {
        for (int value : values) System.out.print(value + " ");
        System.out.println();
    }
}
