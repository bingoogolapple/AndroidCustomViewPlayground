package cn.bingoogolapple.acvp.indexview;

import java.util.Comparator;

public class PinyinComparator implements Comparator<Model> {

    public int compare(Model o1, Model o2) {
        if (o1.topc.equals("@") || o2.topc.equals("#")) {
            return -1;
        } else if (o1.topc.equals("#") || o2.topc.equals("@")) {
            return 1;
        } else {
            return o1.topc.compareTo(o2.topc);
        }
    }

}