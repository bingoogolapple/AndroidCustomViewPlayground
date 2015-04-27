package cn.bingoogolapple.acvp.recyclerview.mode;

import java.util.ArrayList;
import java.util.List;

public class Mode {
    public String attr1;
    public String attr2;

    public Mode(String attr1, String attr2) {
        this.attr1 = attr1;
        this.attr2 = attr2;
    }

    public static List<Mode> getHelloworldDatas() {
        List<Mode> datas = new ArrayList<Mode>();
        for (int i = 0; i < 10; i++) {
            datas.add(new Mode("attr1 " + i, "attr2 " + i));
        }
        return datas;
    }

    public static List<Mode> getItemDecorationDatas() {
        List<Mode> datas = new ArrayList<Mode>();
        for (int i = 0; i < 50; i++) {
            datas.add(new Mode("attr1 " + i, "attr2 " + i));
        }
        return datas;
    }

    public static List<Mode> getHeadindexDatas1() {
        List<Mode> datas = new ArrayList<Mode>();
        for (int i = 0; i < 20; i++) {
            datas.add(new Mode("类型11 " + i, "类型12- " + i));
        }
        return datas;
    }

    public static List<Mode> getHeadindexDatas2() {
        List<Mode> datas = new ArrayList<Mode>();
        for (int i = 0; i < 20; i++) {
            datas.add(new Mode("类型21- " + i, "类型22- " + i));
        }
        return datas;
    }

    public static List<Mode> getDownloadingDatas() {
        List<Mode> datas = new ArrayList<Mode>();
        for (int i = 0; i < 20; i++) {
            datas.add(new Mode("正在下载11 " + i, "正在下载12- " + i));
        }
        return datas;
    }

    public static List<Mode> getDownloadedDatas() {
        List<Mode> datas = new ArrayList<Mode>();
        for (int i = 0; i < 20; i++) {
            datas.add(new Mode("下载完成21- " + i, "下载完成22- " + i));
        }
        return datas;
    }
}