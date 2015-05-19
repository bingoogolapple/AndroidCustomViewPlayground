package cn.bingoogolapple.acvp.selectview.util;

import android.content.Context;
import android.text.TextUtils;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import cn.bingoogolapple.acvp.selectview.model.CityModel;
import cn.bingoogolapple.acvp.selectview.model.DistrictModel;
import cn.bingoogolapple.acvp.selectview.model.ProvinceModel;

/**
 * 作者:王浩 邮件:bingoogolapple@gmail.com
 * 创建时间:15/5/11 15:56
 * 描述:地址解析工具类
 */
public class AddressXmlParserHandler extends DefaultHandler {
    private List<ProvinceModel> provinceList = new ArrayList<ProvinceModel>();
    private ProvinceModel provinceModel;
    private CityModel cityModel;
    private DistrictModel districtModel;

    @Override
    public void startDocument() throws SAXException {
        // 当读到第一个开始标签的时候，会触发这个方法
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        // 当遇到开始标记的时候，调用这个方法
        if (qName.equals("province")) {
            provinceModel = new ProvinceModel();
            provinceModel.id = attributes.getValue(0);
            provinceModel.name = attributes.getValue(1);
            provinceModel.cityList = new ArrayList<CityModel>();
        } else if (qName.equals("city")) {
            cityModel = new CityModel();
            cityModel.id = attributes.getValue(0);
            cityModel.name = attributes.getValue(1);
            cityModel.districtList = new ArrayList<DistrictModel>();
        } else if (qName.equals("district")) {
            districtModel = new DistrictModel();
            districtModel.id = attributes.getValue(0);
            districtModel.name = attributes.getValue(1);
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        // 遇到结束标记的时候，会调用这个方法
        if (qName.equals("district")) {
            cityModel.districtList.add(districtModel);
        } else if (qName.equals("city")) {
            provinceModel.cityList.add(cityModel);
        } else if (qName.equals("province")) {
            provinceList.add(provinceModel);
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
    }

    public static List<ProvinceModel> getProvinceList(Context context) {
        AddressXmlParserHandler parserHandler = new AddressXmlParserHandler();
        try {
            InputStream input = context.getAssets().open("province_data.xml");
            // 创建一个解析xml的工厂对象
            SAXParserFactory spf = SAXParserFactory.newInstance();
            // 解析xml
            SAXParser parser = spf.newSAXParser();

            parser.parse(input, parserHandler);
            input.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return parserHandler.provinceList;
    }

    public static ProvinceModel getSelectedProvinceModel(List<ProvinceModel> provinceList, String provinceId) {
        ProvinceModel result = null;
        for (ProvinceModel provinceModel : provinceList) {
            if (provinceModel.id.equals(provinceId)) {
                result = provinceModel;
            }
        }
        return result;
    }

    public static CityModel getSelectedCityModel(ProvinceModel provinceModel, String cityId) {
        CityModel result = null;
        if (provinceModel != null) {
            for (CityModel cityModel : provinceModel.cityList) {
                if (cityModel.id.equals(cityId)) {
                    result = cityModel;
                }
            }
        }
        return result;
    }

    public static DistrictModel getSelectedDistrictModel(CityModel cityModel, String districtId) {
        DistrictModel result = null;
        if (cityModel != null) {
            for (DistrictModel districtModel : cityModel.districtList) {
                if (districtModel.id.equals(districtId)) {
                    result = districtModel;
                }
            }
        }
        return result;
    }

    public static String getCompleteAddress(List<ProvinceModel> provinceList, String provinceId, String cityId, String districtId) {
        ProvinceModel provinceMode = getSelectedProvinceModel(provinceList, provinceId);
        String result = provinceMode.name;
        if (!TextUtils.isEmpty(cityId)) {
            CityModel cityMode = getSelectedCityModel(provinceMode, cityId);
            if (cityMode != null) {
                DistrictModel districtModel = getSelectedDistrictModel(cityMode, districtId);
                String districtName = districtModel == null ? "" : districtModel.name;
                result = provinceMode.name + cityMode.name + districtName;
            }
        }
        return result;
    }
}