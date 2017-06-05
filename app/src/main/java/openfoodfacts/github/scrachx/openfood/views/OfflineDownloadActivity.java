package openfoodfacts.github.scrachx.openfood.views;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.StringTokenizer;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HttpsURLConnection;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.HttpUrl;
import openfoodfacts.github.scrachx.openfood.R;
import openfoodfacts.github.scrachx.openfood.fragments.BaseFragment;
import openfoodfacts.github.scrachx.openfood.models.OfflineStoredProduct;
import openfoodfacts.github.scrachx.openfood.models.OfflineStoredProductDao;
import openfoodfacts.github.scrachx.openfood.utils.Utils;


public class OfflineDownloadActivity extends BaseActivity {

    private OfflineStoredProductDao mOfflineStoredProductDao;
    private static String csv_url = "https://rwkgha-ch3302.files.1drv.com/y4mT1dZsemsubMS6ASuxfAu3qgZUQgdDtX52rzsRxQ2zUuN5YgQVLBv0Oq7w9badwjfbifBrwUCDxZHIXVBAvtbDVXc98GTtgq09rkyr1K-tqJl6Fwm7TNBXIpaFfWdPIw2h8KPrjXpuWcr3bWhUrhpGlnjDX98Y748engKmFXzIu3q6p9qG6CNAmXrrYjBgdWT/products.csv?download&psid=1";

    private String pathToFile;
    private DownloadManager downloadManager = null;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.offlineDownloadSpinner)
    Spinner spinner;

    @BindView(R.id.offlineDownloadButton)
    Button downloadButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offline_download);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.offline_title);

        mOfflineStoredProductDao = Utils.getAppDaoSession(this).getOfflineStoredProductDao();

        downloadManager = (DownloadManager)getSystemService(DOWNLOAD_SERVICE);

        registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));

    }

    @OnClick(R.id.offlineDownloadButton)
    public void downloadCSV(){
        String selectedCountry = spinner.getSelectedItem().toString();

        List<OfflineStoredProduct> items = mOfflineStoredProductDao.loadAll();
        for(OfflineStoredProduct item : items)
            mOfflineStoredProductDao.delete(item);

        downloadProductsData();

        /*ScheduledExecutorService scheduleTaskExecutor = Executors.newScheduledThreadPool(1);

        scheduleTaskExecutor.scheduleAtFixedRate(new Runnable() {
            public void run() {

                downloadProductsData();
            }
        }, 1, 1, TimeUnit.DAYS);*/
    }

    private void downloadProductsData(){
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(csv_url));
        request.setTitle("Offline Product Data");
        request.setDescription("Offline Product Data is being downloaded...");
        //request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI);
        request.allowScanningByMediaScanner();
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

        String nameOfFile = URLUtil.guessFileName(csv_url, null,
                MimeTypeMap.getFileExtensionFromUrl(csv_url));

        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, nameOfFile);

        pathToFile = Environment.getExternalStorageDirectory().toString()
                + "/" + Environment.DIRECTORY_DOWNLOADS + "/" + nameOfFile;

        File x = new File(pathToFile);
        if(x.exists())
            x.delete();

        downloadManager.enqueue(request);
        downloadButton.setEnabled(false);
    }

    private void dbInit(String fileName){

    }

    BroadcastReceiver onComplete = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            new insertDataToDatabase().execute();
            downloadButton.setEnabled(true);
        }
    };

    class insertDataToDatabase extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            try {
                FileReader fileReader = new FileReader(pathToFile);
                BufferedReader reader = new BufferedReader(fileReader);

                /* Skip the first line */
                String line = reader.readLine();
                if (line != null) {
                    line = reader.readLine();
                    while (line != null) {
                        String[] tokens = new String[10];
                        String[] foundTokens = line.split(",");

                        for (int i = 0; i < tokens.length; i++)
                            tokens[i] = "";

                        for (int i = 0; i < foundTokens.length; i++) {
                            if (foundTokens[i] != null)
                                tokens[i] = foundTokens[i];
                        }

                        OfflineStoredProduct product = new OfflineStoredProduct(tokens[1], tokens[2], tokens[3],
                                tokens[4], tokens[5], tokens[6], tokens[7], tokens[8], tokens[9]);
                        mOfflineStoredProductDao.insert(product);
                        line = reader.readLine();
                    }
                }

                reader.close();
                fileReader.close();

                File fToDelete = new File(pathToFile);
                if (fToDelete.exists()) {
                    fToDelete.delete();
                }
            } catch (FileNotFoundException e) {
                System.err.println(e);
            } catch (IOException e) {
                System.err.println(e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }
}
