package com.crazymike.product.spec.selector;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.crazymike.R;
import com.crazymike.alert.SpecialistAdviceDialog;
import com.crazymike.alert.SpecialistAdviceMessageDialog;
import com.crazymike.base.BaseActivity;
import com.crazymike.models.ItemDetail;
import com.crazymike.models.Spec;
import com.crazymike.respositories.SpecCounterRepository;
import com.google.gson.Gson;

/**
 * Created by ChaoJen on 2017/2/22.
 */

public class SpecSelectorActivity
        extends BaseActivity
        implements SpecSelectorContract.View, View.OnClickListener, SpecSelectorAdapter.Listener, SpecialistAdviceDialog.Listener, SpecialistAdviceMessageDialog.Listener {

    private static final String ITEM_DETAIL = "ITEM_DETAIL";
    public static final int RESULT_CANCEL = 1;
    public static final int RESULT_SPEC_SELECTED = 2;
    private static final int REQUEST_PERMISSION_CALL_PHONE = 3;

    private ItemDetail itemDetail;
    private SpecSelectorContract.Presenter presenter;
    private SpecialistAdviceDialog specialistAdviceDialog;
    private SpecialistAdviceMessageDialog specialistAdviceMessageDialog;

    public static void startActivity(Activity activity, ItemDetail itemDetail, int requestCode) {
        Intent intent = new Intent(activity, SpecSelectorActivity.class);
        intent.putExtra(ITEM_DETAIL, new Gson().toJson(itemDetail));
        activity.startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        itemDetail = new Gson().fromJson(getIntent().getStringExtra(ITEM_DETAIL), ItemDetail.class);
        presenter = new SpecSelectorPresenter(this, itemDetail);
        initView();
    }

    private void initView() {
        setContentView(R.layout.activity_spec_selector);

        RecyclerView recyclerSpecs = (RecyclerView) findViewById(R.id.recycler_spec);
        ImageView imgCancel = (ImageView) findViewById(R.id.img_cancel);
        LinearLayout linearLayoutSpecialistAdvice = (LinearLayout) findViewById(R.id.linearLayout_specialistAdvice);

        SpecSelectorAdapter specSelectorAdapter = new SpecSelectorAdapter(this, itemDetail, this);
        recyclerSpecs.setLayoutManager(new LinearLayoutManager(this));
        recyclerSpecs.setAdapter(specSelectorAdapter);
        imgCancel.setOnClickListener(this);
        linearLayoutSpecialistAdvice.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_cancel:
                setResult(RESULT_CANCEL);
                finish();
                break;
            case R.id.linearLayout_specialistAdvice:
                String productId = itemDetail.getInfo().getItem_id();
                boolean isTravelProduct = presenter.isTravelProduct(itemDetail.getInfo().getMain_tag());
                specialistAdviceDialog = new SpecialistAdviceDialog(this, this, productId, isTravelProduct, presenter.getCSServiceStartTime(), presenter.getCSServiceEndTime());
                specialistAdviceDialog.show();
                break;
        }
    }

    @Override
    public void onSpecClick(Spec spec) {
        SpecCounterRepository.getInstance().increaseSpecCount(spec);
        setResult(RESULT_SPEC_SELECTED);
        finish();
    }

    @Override
    public void onSpecialistAdviceCallNowClick(String phoneNumber) {
        callPhone(phoneNumber);
    }

    @Override
    public void onSpecialistAdviceMessageBtnClick() {
        specialistAdviceMessageDialog = new SpecialistAdviceMessageDialog(this, this, itemDetail.getInfo().getName(), itemDetail.getInfo().getItem_id());
        specialistAdviceMessageDialog.show();
    }

    public void callPhone(String phoneNumber) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions(this, new String[] {
                    Manifest.permission.CALL_PHONE
            }, REQUEST_PERMISSION_CALL_PHONE);
            return;
        }

        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + phoneNumber));
        startActivity(callIntent);
    }

    @Override
    public void onSendBtnClick(String name, String title, String phoneNumber, String email, String content) {
        presenter.postCSServiceMessage(name, title, phoneNumber, email, content);
    }

    @Override
    public void onCSServiceMessageResponseGet(boolean isSuccess, String message) {
        Toast toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
        toast.getView().setBackgroundResource(R.color.colorPrimary);
        toast.getView().setPadding(20, 20, 20, 20);
        toast.show();

        if (isSuccess) {
            if (specialistAdviceMessageDialog.isShowing()) {
                specialistAdviceMessageDialog.dismiss();
            }
            if (specialistAdviceDialog.isShowing()) {
                specialistAdviceDialog.dismiss();
            }
        }
    }
}
