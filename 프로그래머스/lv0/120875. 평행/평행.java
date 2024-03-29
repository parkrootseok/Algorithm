import java.util.*;

class Solution {
    
    public double gradient(int x1, int y1, int x2, int y2) {

        return (y2 - y1) / (double) (x2 - x1);

    }

    public int solution(int[][] dots) {

        HashMap<Double, Double> gradients = new HashMap<>();

        for (int i = 0; i < dots.length; i++) {

            int x1 = dots[i][0], y1 = dots[i][1];

            for (int j = i + 1; j < dots.length; j++) {

                double a = gradient(x1, y1, dots[j][0], dots[j][1]);
                double b = y1 - a * x1;

                if (gradients.containsKey(a) && (gradients.get(a) != b || gradients.get(a) == 0)) {
                    return 1;
                }

                gradients.put(a, b);

            }

        }

        return 0;

    }
}