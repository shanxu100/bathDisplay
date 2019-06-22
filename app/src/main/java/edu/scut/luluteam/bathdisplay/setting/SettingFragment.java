package edu.scut.luluteam.bathdisplay.setting;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import org.apache.commons.lang.StringUtils;

import java.util.List;

import edu.scut.luluteam.bathdisplay.R;
import edu.scut.luluteam.bathdisplay.constant.AppConstant;
import edu.scut.luluteam.bathdisplay.data.DeviceAdapter;
import edu.scut.luluteam.bathdisplay.manager.ToastManager;
import edu.scut.luluteam.bathdisplay.manager.WaterMeterManager;
import edu.scut.luluteam.bathdisplay.mqtt.MqttClientManager;
import edu.scut.luluteam.serialportlibrary.Device;


/**
 * @author Guan
 */
public class SettingFragment extends Fragment implements SettingContract.IView, View.OnClickListener {

    private TextView tvPort;
    private Button btnChoosePort;
    private EditText etServer1;
    private EditText etServer2;
    private EditText etServer3;
    private EditText etServer4;
    private EditText etServer5;
    private Button btnOk;

    private EditText etWaterAdjustedValue;
    private Button btnWaterAdjustedValueOk;
    private Button btnPitSet;
    private Button btnAddressSet;

    private Button btnCancel;
    private SettingContract.IPresenter mPresenter;


    private static final String TAG = "SettingFragment";


    public SettingFragment() {
        // Required empty public constructor
    }


    public static SettingFragment newInstance() {
        SettingFragment fragment = new SettingFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //outState.putString("tempEditText1",etServer1.getText().toString());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_setting, container, false);
        mPresenter.subscribe();
        initUI(view);
        return view;

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.unsubscribe();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    private void initUI(View view) {

        tvPort = (TextView) view.findViewById(R.id.tv_port);
        btnChoosePort = (Button) view.findViewById(R.id.btn_choose_port);
        etServer1 = (EditText) view.findViewById(R.id.et_server_1);
        etServer2 = (EditText) view.findViewById(R.id.et_server_2);
        etServer3 = (EditText) view.findViewById(R.id.et_server_3);
        etServer4 = (EditText) view.findViewById(R.id.et_server_4);
        etServer5 = (EditText) view.findViewById(R.id.et_server_5);
        btnOk = (Button) view.findViewById(R.id.btn_ok);
        etWaterAdjustedValue = (EditText) view.findViewById(R.id.et_water_adjusted_value);
        btnWaterAdjustedValueOk = (Button) view.findViewById(R.id.btn_water_adjusted_value_ok);
        btnPitSet = (Button) view.findViewById(R.id.btn_pit_set);
        btnAddressSet = (Button) view.findViewById(R.id.btn_address_set);

        btnCancel = (Button) view.findViewById(R.id.btn_cancel);

        btnChoosePort.setOnClickListener(this);
        btnOk.setOnClickListener(this);
        btnPitSet.setOnClickListener(this);
        btnAddressSet.setOnClickListener(this);
        btnCancel.setOnClickListener(this);


        tvPort.setText(mPresenter.getCurrentSerialPort());

        String[] mqttBroker = MqttClientManager.getInstance().getBroker().substring(6).split("\\.|:");
        etServer1.setText(mqttBroker[0]);
        etServer2.setText(mqttBroker[1]);
        etServer3.setText(mqttBroker[2]);
        etServer4.setText(mqttBroker[3]);
        etServer5.setText(mqttBroker[4]);

        etWaterAdjustedValue.setText(String.format("%.2f", WaterMeterManager.getInstance().getWaterAdjustedValue().getValue()));
        btnWaterAdjustedValueOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String value = etWaterAdjustedValue.getText().toString();
                    value = StringUtils.isEmpty(value) ? "0" : value;
                    mPresenter.updateWaterAdjustedValue(Float.parseFloat(value));
                    new AlertDialog.Builder(getContext())
                            .setTitle("设置成功，校正值为：" + value)
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }).show();

                } catch (Exception e) {

                }
            }
        });

    }


    @Override
    public void setPresenter(SettingContract.IPresenter presenter) {
        this.mPresenter = presenter;
    }

    public void showChooseDialog() {

        final Dialog dialog = new Dialog(getContext());
        //获取串口列表view，并嵌入在dialog里
        View contentView = View.inflate(getContext(), R.layout.content_dialog, null);
        dialog.setContentView(contentView);
        //获取listview
        ListView listView = (ListView) contentView.findViewById(R.id.listview);

        final List<Device> devices = mPresenter.findSerialPort();
        //获取adapter
        DeviceAdapter deviceAdapter = new DeviceAdapter(getContext(), devices);
        //Toast.makeText(getContext(), "devices的个数为" + devices.size(), Toast.LENGTH_SHORT).show();
        //对listview设置adapter
        listView.setAdapter(deviceAdapter);
        //设置监听事件
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Device device = devices.get(position);
                mPresenter.openSerialPort(device);
//                Toast.makeText(getContext(), "串口的路径为" + device.getFile().getPath(), Toast.LENGTH_SHORT).show();
                tvPort.setText(device.getFile().getPath());
                dialog.dismiss();
            }
        });

        dialog.show();


    }

    /**
     * 显示设置坑位数量的窗口
     */
    private void showPitSetDialog() {

        View contentView = View.inflate(getContext(), R.layout.pit_set_dialog, null);
        final EditText et_man_pit = (EditText) contentView.findViewById(R.id.et_man_pit);
        final EditText et_man_urinal = (EditText) contentView.findViewById(R.id.et_man_urinal);
        final EditText et_woman_pit = (EditText) contentView.findViewById(R.id.et_woman_pit);
        final EditText et_disable_pit = (EditText) contentView.findViewById(R.id.et_disable_pit);
        final EditText et_disable_urinal = (EditText) contentView.findViewById(R.id.et_disable_urinal);
        et_man_pit.setText(AppConstant.pitNumData.getNumManSit() + "");
        et_man_urinal.setText(AppConstant.pitNumData.getNumManStand() + "");
        et_woman_pit.setText(AppConstant.pitNumData.getNumWomanSit() + "");
        et_disable_pit.setText(AppConstant.pitNumData.getNumDisabledSit() + "");
        et_disable_urinal.setText(AppConstant.pitNumData.getNumDisabledStand() + "");
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext())
                .setView(contentView)
                .setTitle("坑位数目")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//
                        if (!TextUtils.isEmpty(et_man_pit.getText()) && !TextUtils.isEmpty(et_man_urinal.getText()) && !TextUtils.isEmpty(et_woman_pit.getText()) && !TextUtils.isEmpty(et_disable_pit.getText()) && !TextUtils.isEmpty(et_disable_urinal.getText())) {
//                            String pitJson = "{\"numManSit\":" + numManSit + ",\"numManStand\":" + et_man_urinal.getText() + ",\"numWomanSit\":" + et_woman_pit.getText() + ",\"numDisabledSit\":" + et_disable_pit.getText() + ",\"numDisabledStand\":" + et_disable_urinal.getText() + "}";
                            //TODO 体验优化：AlertDialog点击确定的时候，如果设置失败，建议改doalog不要消失；如果设置成功再消失
                            int numManSit = Integer.parseInt(et_man_pit.getText().toString().trim());
                            int numManStand = Integer.parseInt(et_man_urinal.getText().toString().trim());
                            int numWomanSit = Integer.parseInt(et_woman_pit.getText().toString().trim());
                            int numDisabledSit = Integer.parseInt(et_disable_pit.getText().toString().trim());
                            int numDisabledStand = Integer.parseInt(et_disable_urinal.getText().toString().trim());
                            mPresenter.updatePitNumData(numManSit, numManStand, numWomanSit, numDisabledSit, numDisabledStand);
                        } else {
                            ToastManager.newInstance("更改失败！").show();
                        }

                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        builder.create().show();


    }

    /**
     * 显示设置厕所名称与数量的dialog
     */
    private void showAddressSetDialog() {
        View contentView = View.inflate(getContext(), R.layout.address_set_dialog, null);
        final EditText et_toilet_name = (EditText) contentView.findViewById(R.id.et_toilet_name);
        final EditText et_toilet_address = (EditText) contentView.findViewById(R.id.et_toilet_address);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext())
                .setView(contentView)
                .setTitle("设置厕所名称及地址")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (!TextUtils.isEmpty(et_toilet_name.getText()) && !TextUtils.isEmpty(et_toilet_address.getText())) {
                            mPresenter.updateToiletNameAndAddress(et_toilet_name.getText().toString(), et_toilet_address.getText().toString());
                        } else {
                            ToastManager.newInstance("更改失败！").show();
                        }
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        builder.create().show();


    }


    @Override
    public void onClick(View v) {
        if (v.getId() == btnChoosePort.getId()) {
            showChooseDialog();
        } else if (v == btnOk) {
            if (!TextUtils.isEmpty(etServer1.getText()) && !TextUtils.isEmpty(etServer2.getText()) && !TextUtils.isEmpty(etServer3.getText()) && !TextUtils.isEmpty(etServer4.getText()) && !TextUtils.isEmpty(etServer5.getText())) {
                String broker = "tcp://" + etServer1.getText().toString() + "." + etServer2.getText().toString() + "." + etServer3.getText().toString() + "." + etServer4.getText().toString() + ":" + etServer5.getText().toString();
                mPresenter.setMqttServer(broker);
            } else {
                ToastManager.newInstance("地址格式错误！").show();
            }
        } else if (v == btnCancel) {
            this.getActivity().finish();

        } else if (v == btnPitSet) {
            showPitSetDialog();
        } else if (v == btnAddressSet) {
            showAddressSetDialog();
        }
    }


}



