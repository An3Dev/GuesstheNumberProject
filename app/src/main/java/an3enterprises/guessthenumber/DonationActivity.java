package an3enterprises.guessthenumber;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.anjlab.android.iab.v3.BillingProcessor;

import util.IabHelper;
import util.IabResult;
import util.Inventory;
import util.Purchase;

public class DonationActivity extends Activity{
    BillingProcessor bp;
    IabHelper mHelper;
    private static final String TAG = DonationActivity.class.getName();
    public static String ITEM_SKU = null;

    @Override
    public void onStart(){
        super.onStart();
        super.onStart();

        String base64EncodedPublicKey =
                "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAyGZDHmNf00wEDaSFUd+iIi5Z5hx6a/QSA59r+IMY5Hymc0ZkLmXcwf04bwkd+KzVW8I6wQ27OA6RaQP9pfmHMgYXGTdKHwsgqUT6BY9tWYehNstGAVdMacOc1v/cLJDrPqIIPyqmrliZwmu/3gOiBR7TwKg1cvP29/z1lpgmcmwZO0G8f5pD5fGPqhc2A0pwW0n2y1FH1FEH8v4fDzABf2kUuy3YJhgBrB8RYgyfG/zl2dRM3XhmtsuP3D4sYFzo+vJRDx5XxKbQfB5GTiLCTcrffMtPINI52pgWVAGfD4R2zmfviXYxXwls+08f8agZdZ6VNya4ZUb7yRgmZCDgKQIDAQAB";

        mHelper = new IabHelper(this, base64EncodedPublicKey);

        mHelper.startSetup(new
                   IabHelper.OnIabSetupFinishedListener() {
                       public void onIabSetupFinished(IabResult result)
                       {
                           if (!result.isSuccess()) {
                               Log.d(TAG, "In-app Billing setup failed: " + result);
                           } else {
                               Log.d(TAG, "In-app Billing is set up OK");
                               mHelper.enableDebugLogging(true, TAG);
                           }
                       }
                   });
}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_donation);
//        bp = new BillingProcessor(this, "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAyGZDHmNf00wEDaSFUd+iIi5Z5hx6a/QSA59r+IMY5Hymc0ZkLmXcwf04bwkd+KzVW8I6wQ27OA6RaQP9pfmHMgYXGTdKHwsgqUT6BY9tWYehNstGAVdMacOc1v/cLJDrPqIIPyqmrliZwmu/3gOiBR7TwKg1cvP29/z1lpgmcmwZO0G8f5pD5fGPqhc2A0pwW0n2y1FH1FEH8v4fDzABf2kUuy3YJhgBrB8RYgyfG/zl2dRM3XhmtsuP3D4sYFzo+vJRDx5XxKbQfB5GTiLCTcrffMtPINI52pgWVAGfD4R2zmfviXYxXwls+08f8agZdZ6VNya4ZUb7yRgmZCDgKQIDAQAB\"", this);
//        btnBuy99 = (Button) findViewById(R.id.buy99);
        // Sets the Toolbar to act as the ActionBar for this Activity window.
        // Make sure the toolbar exists in the activity and is not null

    }

//    @Override
//    public void onProductPurchased(String productId, TransactionDetails details) {
//        Toast.makeText(DonationActivity.this, "Purchased Item", Toast.LENGTH_SHORT).show();
//    }
//
//    @Override
//    public void onPurchaseHistoryRestored() {
//
//    }
//
//    @Override
//    public void onBillingError(int errorCode, Throwable error) {
//        Toast.makeText(DonationActivity.this, "Purchase canceled", Toast.LENGTH_SHORT).show();
//    }
//
//    @Override
//    public void onBillingInitialized() {
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (!bp.handleActivityResult(requestCode, resultCode, data)) {
//            super.onActivityResult(requestCode, resultCode, data);
//        }
//    }
//
//    @Override
//    public void onDestroy() {
//        if (bp != null) {
//            bp.release();
//        }
//        super.onDestroy();
//    }

    public void consumeItem() throws IabHelper.IabAsyncInProgressException {
        mHelper.queryInventoryAsync(mReceivedInventoryListener);
    }

    IabHelper.QueryInventoryFinishedListener mReceivedInventoryListener
            = new IabHelper.QueryInventoryFinishedListener() {
        public void onQueryInventoryFinished(IabResult result,
                                             Inventory inventory) throws IabHelper.IabAsyncInProgressException {


            if (result.isFailure()) {
                // Handle failure
            } else {
                mHelper.consumeAsync(inventory.getPurchase(ITEM_SKU),
                        mConsumeFinishedListener);
            }
        }
    };IabHelper.OnConsumeFinishedListener mConsumeFinishedListener =
            new IabHelper.OnConsumeFinishedListener() {
                public void onConsumeFinished(Purchase purchase,
                                              IabResult result) {

                    if (result.isSuccess()) {
                        Toast.makeText(DonationActivity.this, "Success", Toast.LENGTH_SHORT).show();
                    } else {
                        // handle error
                    }
                }
            };

    public void buyInApp99(View view) throws IabHelper.IabAsyncInProgressException {
        ITEM_SKU = "donation99";
        mHelper.launchPurchaseFlow(this, ITEM_SKU, 10001,
                mPurchaseFinishedListener, "donation99");
//        boolean isOneTimePurchaseSupported = bp.isOneTimePurchaseSupported();
//        if(isOneTimePurchaseSupported) {
//            // launch payment flow
//            bp.getPurchaseListingDetails("donation99");
//            bp.purchase(DonationActivity.this, "donation99");
//        }

    }

    public void buyInApp199(View view) throws IabHelper.IabAsyncInProgressException {
        ITEM_SKU = "donation199";
        mHelper.launchPurchaseFlow(this, ITEM_SKU, 10001,
                mPurchaseFinishedListener, "donation199");
//        boolean isOneTimePurchaseSupported = bp.isOneTimePurchaseSupported();
//        if(isOneTimePurchaseSupported) {
//            // launch payment flow
//            bp.getPurchaseListingDetails("donation99");
//            bp.purchase(DonationActivity.this, "donation99");
//        }

    }

    public void buyInApp499(View view) throws IabHelper.IabAsyncInProgressException {
        ITEM_SKU = "donation499";
        mHelper.launchPurchaseFlow(this, ITEM_SKU, 10001,
                mPurchaseFinishedListener, "donation499");
//        boolean isOneTimePurchaseSupported = bp.isOneTimePurchaseSupported();
//        if(isOneTimePurchaseSupported) {
//            // launch payment flow
//            bp.getPurchaseListingDetails("donation99");
//            bp.purchase(DonationActivity.this, "donation99");
//        }

    }

    public void buyInApp999(View view) throws IabHelper.IabAsyncInProgressException {
        ITEM_SKU = "donation999";
        mHelper.launchPurchaseFlow(this, ITEM_SKU, 10001,
                mPurchaseFinishedListener, "donation999");
//        boolean isOneTimePurchaseSupported = bp.isOneTimePurchaseSupported();
//        if(isOneTimePurchaseSupported) {
//            // launch payment flow
//            bp.getPurchaseListingDetails("donation99");
//            bp.purchase(DonationActivity.this, "donation99");
//        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data)
    {
        try {
            if (!mHelper.handleActivityResult(requestCode,
                    resultCode, data)) {
                super.onActivityResult(requestCode, resultCode, data);
            }
        } catch (IabHelper.IabAsyncInProgressException e) {
            e.printStackTrace();
        }
    }

    IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener
            = new IabHelper.OnIabPurchaseFinishedListener() {
        public void onIabPurchaseFinished(IabResult result,
                                          Purchase purchase) throws IabHelper.IabAsyncInProgressException {
            if (result.isFailure()) {
                // Handle error
                return;
            }
            else if (purchase.getSku().equals(ITEM_SKU)) {
                consumeItem();
            }

        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mHelper != null) try {
            mHelper.dispose();
        } catch (IabHelper.IabAsyncInProgressException e) {
            e.printStackTrace();
        }
        mHelper = null;
    }

}
