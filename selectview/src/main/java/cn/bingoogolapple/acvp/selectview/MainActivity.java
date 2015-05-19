package cn.bingoogolapple.acvp.selectview;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.util.List;

import cn.bingoogolapple.acvp.selectview.model.CascadeModel;
import cn.bingoogolapple.acvp.selectview.model.CityModel;
import cn.bingoogolapple.acvp.selectview.model.DistrictModel;
import cn.bingoogolapple.acvp.selectview.model.ProvinceModel;
import cn.bingoogolapple.acvp.selectview.util.AddressXmlParserHandler;
import cn.bingoogolapple.acvp.selectview.util.CommonAdapter;
import cn.bingoogolapple.acvp.selectview.util.ListViewHolder;
import cn.bingoogolapple.acvp.selectview.widget.SelectView;
import cn.bingoogolapple.bgaannotation.BGAA;
import cn.bingoogolapple.bgaannotation.BGAALayout;
import cn.bingoogolapple.bgaannotation.BGAAView;

@BGAALayout(R.layout.activity_main)
public class MainActivity extends AppCompatActivity implements SelectView.SelectViewDelegate {
    @BGAAView(R.id.sv_main_province)
    private SelectView mProvinceSv;
    @BGAAView(R.id.sv_main_city)
    private SelectView mCitySv;
    @BGAAView(R.id.sv_main_district)
    private SelectView mDistrictSv;
    @BGAAView(R.id.tv_main_address)
    private TextView mAddressTv;

    private SelectViewAdapter mProvinceAdapter;
    private SelectViewAdapter mCityAdapter;
    private SelectViewAdapter mDistrictAdapter;
    private List<ProvinceModel> mProvinces;
    private List<CityModel> mCitys;
    private List<DistrictModel> mDistricts;
    private ProvinceModel mProvinceModel;
    private CityModel mCityModel;
    private DistrictModel mDistrictModel;

    private String mProvinceId = "350000";
    private String mCityId = "350200";
    private String mDistrictId = "350203";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BGAA.injectView2Activity(this);

        initAddress();
        initData();
    }

    private void initAddress() {
        mDistrictAdapter = new SelectViewAdapter(this, null, R.layout.item_address_edit);
        mDistrictSv.setAdapter(mDistrictAdapter);
        mDistrictSv.setDelegate(this);

        mCityAdapter = new SelectViewAdapter(this, null, R.layout.item_address_edit);
        mCitySv.setAdapter(mCityAdapter);
        mCitySv.setDelegate(this);

        mProvinces = AddressXmlParserHandler.getProvinceList(this);
        mProvinceAdapter = new SelectViewAdapter(this, mProvinces, R.layout.item_address_edit);
        mProvinceSv.setAdapter(mProvinceAdapter);
        mProvinceSv.setDelegate(this);
    }

    private void initData() {
        mProvinces = AddressXmlParserHandler.getProvinceList(this);
        setProvinceModel(AddressXmlParserHandler.getSelectedProvinceModel(mProvinces, mProvinceId));
        setCityModel(AddressXmlParserHandler.getSelectedCityModel(mProvinceModel, mCityId));
        setDistrictModel(AddressXmlParserHandler.getSelectedDistrictModel(mCityModel, mDistrictId));
        handleAddressChanged();
    }

    @Override
    public void onSelectViewValueChanged(SelectView selectView, int position) {
        switch (selectView.getId()) {
            case R.id.sv_main_province:
                handleProvinceChanged(position);
                break;
            case R.id.sv_main_city:
                handleCityChanged(position);
                break;
            case R.id.sv_main_district:
                handleDistrictChanged(position);
                break;
            default:
                break;
        }
        handleAddressChanged();
    }

    private void handleProvinceChanged(int position) {
        if (mProvinces != null && mProvinces.size() > 0) {
            setProvinceModel(mProvinces.get(position));
        } else {
            setProvinceModel(null);
        }
        handleCityChanged(0);
    }

    private void handleCityChanged(int position) {
        if (mCitys != null && mCitys.size() > 0) {
            setCityModel(mCitys.get(position));
        } else {
            setCityModel(null);
        }
        handleDistrictChanged(0);
    }

    private void handleDistrictChanged(int position) {
        if (mDistricts != null && mDistricts.size() > 0) {
            setDistrictModel(mDistricts.get(position));
        } else {
            setDistrictModel(null);
        }
    }

    private void setProvinceModel(ProvinceModel provinceModel) {
        mProvinceModel = provinceModel;

        if (mProvinceModel != null) {
            mProvinceSv.setText(mProvinceModel.name);
            mCitys = mProvinceModel.cityList;
        } else {
            mProvinceSv.reset();
            mCitys = null;
        }
        mCityAdapter.setDatas(mCitys);
    }

    private void setCityModel(CityModel cityModel) {
        mCityModel = cityModel;

        if (mCityModel != null) {
            mCitySv.setText(mCityModel.name);
            mDistricts = mCityModel.districtList;
        } else {
            mCitySv.reset();
            mDistricts = null;
        }
        mDistrictAdapter.setDatas(mDistricts);
    }

    private void setDistrictModel(DistrictModel districtModel) {
        mDistrictModel = districtModel;
        if (mDistrictModel != null) {
            mDistrictSv.setText(mDistrictModel.name);
        } else {
            mDistrictSv.reset();
        }
    }

    private void handleAddressChanged() {
        mProvinceId = mProvinceModel == null ? null : mProvinceModel.id;
        mCityId = mCityModel == null ? null : mCityModel.id;
        mDistrictId = mDistrictModel == null ? null : mDistrictModel.id;
        mAddressTv.setText(AddressXmlParserHandler.getCompleteAddress(mProvinces, mProvinceId, mCityId, mDistrictId));
    }

    public static class SelectViewAdapter<T extends CascadeModel> extends CommonAdapter<T> {

        public SelectViewAdapter(Context context, List<T> datas, int itemLayoutId) {
            super(context, datas, itemLayoutId);
        }

        @Override
        public void convert(ListViewHolder viewHolder, T item) {
            viewHolder.setText(R.id.tv_item_address_edit_name, item.name);
        }
    }

}