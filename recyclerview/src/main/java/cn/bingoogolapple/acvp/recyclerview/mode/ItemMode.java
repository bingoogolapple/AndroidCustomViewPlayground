package cn.bingoogolapple.acvp.recyclerview.mode;

import java.util.ArrayList;
import java.util.List;

public class ItemMode {
    public String attr1;
    public String attr2;

    public ItemMode(String attr1, String attr2) {
        this.attr1 = attr1;
        this.attr2 = attr2;
    }

    public static List<ItemMode> getHelloworldDatas() {
        List<ItemMode> datas = new ArrayList<ItemMode>();
        for (int i = 0; i < 100; i++) {
            datas.add(new ItemMode("attr1 " + i, "attr2 " + i));
        }
        return datas;
    }

    public static List<ItemMode> getHeadindexDatas1() {
        List<ItemMode> datas = new ArrayList<ItemMode>();
        for (int i = 0; i < 6; i++) {
            datas.add(new ItemMode("attr1 " + i, "attr2 " + i));
        }
        return datas;
    }

    public static List<ItemMode> getHeadindexDatas2() {
        List<ItemMode> datas = new ArrayList<ItemMode>();
        for (int i = 0; i < 9; i++) {
            datas.add(new ItemMode("attr1 " + i, "attr2 " + i));
        }
        return datas;
    }
}