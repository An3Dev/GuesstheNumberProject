package an3enterprises.guessthenumber;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;
import android.widget.Toolbar;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.TransactionDetails;

public class DonationActivity extends Activity implements BillingProcessor.IBillingHandler{
    BillingProcessor bp;
    Button btnBuy99;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_donation);
        bp = new BillingProcessor(this, "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAyGZDHmNf00wEDaSFUd+iIi5Z5hx6a/QSA59r+IMY5Hymc0ZkLmXcwf04bwkd+KzVW8I6wQ27OA6RaQP9pfmHMgYXGTdKHwsgqUT6BY9tWYehNstGAVdMacOc1v/cLJDrPqIIPyqmrliZwmu/3gOiBR7TwKg1cvP29/z1lpgmcmwZO0G8f5pD5fGPqhc2A0pwW0n2y1FH1FEH8v4fDzABf2kUuy3YJhgBrB8RYgyfG/zl2dRM3XhmtsuP3D4sYFzo+vJRDx5XxKbQfB5GTiLCTcrffMtPINI52pgWVAGfD4R2zmfviXYxXwls+08f8agZdZ6VNya4ZUb7yRgmZCDgKQIDAQAB", this);
        btnBuy99 = (Button) findViewById(R.id.buy99);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        // Sets the Toolbar to act as the ActionBar for this Activity window.
        // Make sure the toolbar exists in the activity and is not null
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setActionBar(toolbar);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_donation, menu);
        return true;
    }

    @Override
    public void onProductPurchased(String productId, TransactionDetails details) {
        Toast.makeText(DonationActivity.this, "Purchased Item", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPurchaseHistoryRestored() {

    }

    @Override
    public void onBillingError(int errorCode, Throwable error) {
        Toast.makeText(DonationActivity.this, "Purchase canceled", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBillingInitialized() {
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!bp.handleActivityResult(requestCode, resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onDestroy() {
        if (bp != null) {
            bp.release();
        }
        super.onDestroy();
    }

    public void buyInApp99(View view) {
        boolean isOneTimePurchaseSupported = bp.isOneTimePurchaseSupported();
        if(isOneTimePurchaseSupported) {
            // launch payment flow
            bp.getPurchaseListingDetails("donation99");
            bp.purchase(DonationActivity.this, "donation99");
        }

    }

    public void buyInApp199(View view) {
        boolean isOneTimePurchaseSupported = bp.isOneTimePurchaseSupported();
        if(isOneTimePurchaseSupported) {
            // launch payment flow
            bp.getPurchaseListingDetails("donation199");
            bp.purchase(DonationActivity.this, "donation199");
        }

    }

    public void buyInApp499(View view) {
        boolean isOneTimePurchaseSupported = bp.isOneTimePurchaseSupported();
        if(isOneTimePurchaseSupported) {
            // launch payment flow
            bp.getPurchaseListingDetails("donation499");
            bp.purchase(DonationActivity.this, "donation499");
        }

    }

    public void buyInApp999(View view) {
        boolean isOneTimePurchaseSupported = bp.isOneTimePurchaseSupported();
        if(isOneTimePurchaseSupported) {
            // launch payment flow
            bp.getPurchaseListingDetails("donation999");
            bp.purchase(DonationActivity.this, "donation999");
        }

    }
}
