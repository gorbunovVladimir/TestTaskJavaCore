package ru.tandemservice.test.task1;

/**
 * Created by Vl on 17.01.2016.
 */
import java.util.*;
public class Main {
    public static void main(String[] args) {
        List<String[]> rows = new ArrayList<String[]>();
        rows.add(new String[]{"4", "746"});
        rows.add(new String[]{"2", "2bdf"});
        rows.add(new String[]{"1", "093"});
        rows.add(new String[]{"11", ""});
        rows.add(new String[]{"a1", null});
        rows.add(new String[]{"ab", "c123"});
        rows.add(new String[]{"a", "zwe3"});
        rows.add(new String[]{"101", null});
        rows.add(new String[]{"20", ""});
        rows.add(new String[]{"11x", "7fa2"});
        rows.add(new String[]{"3", "7fa1"});
        rows.add(new String[]{"1a", "dsa1"});
        rows.add(new String[]{"a11x", "dsa"});
        // "1", "1a", "2", "3", "4", "11", "11x", "20", "101", "a", "ab", "a1", "a11x"
        IStringRowsListSorter sorter = Task1Impl.INSTANCE;
        sorter.sort(rows, 1);
        //sorter.sort(rows, 0);
        for(String[] a : rows) {
            System.out.println(Arrays.asList(a));
        }

    }
}
