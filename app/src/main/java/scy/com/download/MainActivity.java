package scy.com.download;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.text.DecimalFormat;
import java.util.concurrent.ThreadPoolExecutor;

public class MainActivity extends AppCompatActivity implements OnProgressListener {

    private Button button,setup;
    private DownLoaderManger downLoader = null;
    private FileInfo info;
    private ProgressBar progressBar;
    private TextView current,max;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setup = (Button) findViewById(R.id.setup);
        current = (TextView) findViewById(R.id.current);
        max = (TextView) findViewById(R.id.max);
        progressBar = (ProgressBar) findViewById(R.id.progressbar);
        button = (Button) findViewById(R.id.btn);
        final DbHelper helper = new DbHelper(this);
        downLoader = DownLoaderManger.getInstance(helper, this);
        info = new FileInfo("Kuaiya482.apk", "http://downloadz.dewmobile.net/Official/Kuaiya482.apk");
        downLoader.addTask(info);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (downLoader.getCurrentState(info.getUrl())) {
                    downLoader.stop(info.getUrl());
                    button.setText("开始下载");
                } else {
                    downLoader.start(info.getUrl());
                    button.setText("暂停下载");
                }
            }
        });
    }

    @Override
    public void updateProgress(int max, int progress) {
        current.setText((float)progress/max*100+"%");
        this.max.setText(max+"");
        progressBar.setMax(max);
        progressBar.setProgress(progress);
        if(progress == max){
            Toast.makeText(this, "下载成功", Toast.LENGTH_SHORT).show();
            button.setVisibility(View.GONE);
            setup.setVisibility(View.VISIBLE);
            Intent intent = new Intent();
            // 设置目标应用安装包路径
            intent.setDataAndType(Uri.fromFile(new File(DownLoaderManger.FILE_PATH + "/"+info.getFileName())),
                    "application/vnd.android.package-archive");
            startActivity(intent);
        }
    }
}
