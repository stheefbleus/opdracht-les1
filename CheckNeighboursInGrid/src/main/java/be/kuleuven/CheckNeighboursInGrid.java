package be.kuleuven;

import java.util.ArrayList;
import java.util.List;

public class CheckNeighboursInGrid {
    private static List<Integer> result = new ArrayList<>();
    public static Iterable<Integer> getSameNeighboursIds(Iterable<Integer> grid,int width, int height, int indexToCheck) {
        int rij = indexToCheck / width;
        int kolom = indexToCheck % width;
        int[] breedteBuren = {-1, 0, 1};
        int[] hoogteBuren = {-1, 0, 1};

        for (int i : breedteBuren) {
            for (int j : hoogteBuren) {
                int nieuweRij = rij + i;
                int nieuweKolom = kolom + j;
                if (nieuweRij >= 0 && nieuweRij < height && nieuweKolom >= 0 && nieuweKolom < width) {
                    int neighborIndex = nieuweRij * width + nieuweKolom;
                    if (neighborIndex != indexToCheck && grid instanceof List) {
                        if (((List<Integer>) grid).get(neighborIndex).equals(((List<Integer>) grid).get(indexToCheck))) {
                            result.add(neighborIndex);
                        }
                    }
                }
            }
        }
        return result;
    }

    public static void main(String[] args) {
        List<Integer> grid = new ArrayList<>(List.of(0, 0, 1, 0,
                1, 1, 0, 2,
                2, 0, 1, 3,
                0, 1, 1, 1));
        result.clear();
        getSameNeighboursIds(grid, 4, 4, 5);
        System.out.println(result.toString());
    }
}
