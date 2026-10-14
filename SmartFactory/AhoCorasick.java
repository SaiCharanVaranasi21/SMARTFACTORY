public class AhoCorasick {

    private static final int ALPHABET = 128;

    private int[][] next;
    private int[] fail;
    private int[] output;
    private int nodes;

    public AhoCorasick(int maxNodes) {
        next = new int[maxNodes][ALPHABET];
        fail = new int[maxNodes];
        output = new int[maxNodes];
        nodes = 1;

        for (int i = 0; i < maxNodes; i++) {
            for (int j = 0; j < ALPHABET; j++) {
                next[i][j] = -1;
            }
        }
    }

    public void addPattern(String pattern) {
        int current = 0;

        for (int i = 0; i < pattern.length(); i++) {
            int c = pattern.charAt(i);

            if (c >= ALPHABET) {
                continue;
            }

            if (next[current][c] == -1) {
                next[current][c] = nodes++;
            }

            current = next[current][c];
        }

        output[current]++;
    }

    public void build() {
        int[] queue = new int[nodes];
        int front = 0;
        int rear = 0;

        for (int c = 0; c < ALPHABET; c++) {
            if (next[0][c] == -1) {
                next[0][c] = 0;
            } else {
                fail[next[0][c]] = 0;
                queue[rear++] = next[0][c];
            }
        }

        while (front < rear) {
            int current = queue[front++];

            output[current] += output[fail[current]];

            for (int c = 0; c < ALPHABET; c++) {
                if (next[current][c] == -1) {
                    next[current][c] = next[fail[current]][c];
                } else {
                    fail[next[current][c]] = next[fail[current]][c];
                    queue[rear++] = next[current][c];
                }
            }
        }
    }

    public int countMatches(String text) {
        int state = 0;
        int matches = 0;

        for (int i = 0; i < text.length(); i++) {
            int c = text.charAt(i);

            if (c >= ALPHABET) {
                state = 0;
                continue;
            }

            state = next[state][c];
            matches += output[state];
        }

        return matches;
    }
}
