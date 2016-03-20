package io.github.tatjsn.dropboxexp;

import android.databinding.DataBindingUtil;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.dropbox.core.android.Auth;
import com.dropbox.core.v2.files.FileMetadata;

import io.github.tatjsn.dropboxexp.databinding.ActivityMainBinding;

public class MainActivity extends DropboxActivity {
    private ActivityMainBinding binding;
    private String fileToSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
//        binding.mainAuth.setOnClickListener(v -> Auth.startOAuth2Authentication(
//                        MainActivity.this, getString(R.string.app_key)));
        binding.mainAuth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Auth.startOAuth2Authentication(
                        MainActivity.this, getString(R.string.secret_key));
            }
        });
        binding.mainCreateFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                (new AsyncTask<Void, Void, String>() {
                    @Override
                    protected String doInBackground(Void... voids) {
                        try {
                            return CacheFileWriter.writeCacheFile(getCacheDir());
                        } catch (CacheFileWriter.Exception e) {
                            e.printStackTrace();
                        }
                        return null;
                    }

                    @Override
                    protected void onPreExecute() {
                        binding.mainCreateFile.setEnabled(false);
                    }

                    @Override
                    protected void onPostExecute(String fileName) {
                        binding.mainCreateFile.setText(fileName);
                        fileToSend = fileName;
                    }
                }).execute();
            }
        });
        binding.mainUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fileToSend == null) {
                    return;
                }
                (new UploadFileTask(MainActivity.this, DropboxClientFactory.getClient(),
                        new UploadFileTask.Callback() {
                            @Override
                            public void onUploadComplete(FileMetadata result) {
                                binding.mainUpload.setText("Done");
                            }

                            @Override
                            public void onError(Exception e) {
                                binding.mainUpload.setText("Error " + e.getMessage());
                            }
                        })).execute(fileToSend, "");
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        binding.mainAuth.setEnabled(!hasToken());
    }

    @Override
    protected void loadData() {
        Log.d("tatdbg", "Dropbox client ready");
    }
}
