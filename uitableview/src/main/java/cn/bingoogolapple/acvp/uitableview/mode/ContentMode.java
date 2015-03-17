package cn.bingoogolapple.acvp.uitableview.mode;

import java.util.ArrayList;
import java.util.List;

public class ContentMode {
    public String attr1;
    public String attr2;

    public ContentMode(String attr1, String attr2) {
        this.attr1 = attr1;
        this.attr2 = attr2;
    }

    public static List<ContentMode> getHeadindexDatas1() {
        List<ContentMode> datas = new ArrayList<ContentMode>();
        for (int i = 0; i < 15; i++) {
            datas.add(new ContentMode("类型11- " + i, "类型12- " + i));
        }
        return datas;
    }

    public static List<ContentMode> getHeadindexDatas2() {
        List<ContentMode> datas = new ArrayList<ContentMode>();
        for (int i = 0; i < 15; i++) {
            datas.add(new ContentMode("类型21- " + i, "类型22- " + i));
        }
        return datas;
    }
}