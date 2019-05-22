package com.hrishi_3331.hrishi_studio.tronix2021;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Looper;
import android.support.v4.content.FileProvider;
import android.widget.Toast;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class PdfDownloader {

        private Context context;
        private String url;
        private File file;
        private ProgressDialog progressDialog;


        PdfDownloader(Context context, File file, String url) {

            this.context = context;
            this.file = file;
            this.url = url;
            progressDialog = new ProgressDialog(context);
        }

        public void downloadFiles(){
            new Downloader().execute();
        }

        private class Downloader extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {

                try {
                    Looper.prepare();
                    URL download_url =  new URL(url);
                    HttpURLConnection connection = (HttpURLConnection) download_url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.connect();

                    FileOutputStream fOutput = new FileOutputStream(file);
                    InputStream iStream = connection.getInputStream();
                    byte[] buffer = new byte[1024*1024];
                    int len = 0;
                    while ((len = iStream.read(buffer)) != -1 ){
                        fOutput.write(buffer, 0, len);
                    }
                    fOutput.close();
                    iStream.close();

                } catch (Exception e) {
                    Toast.makeText(context, "Download failed!", Toast.LENGTH_SHORT).show();
                }
                return null;
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog.setTitle("Downloading File");
                progressDialog.setMessage("Please wait");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                progressDialog.dismiss();
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(FileProvider.getUriForFile(context, "com.hrishi_3331.hrishi_studio.tronix2021.provider", file), "application/pdf");
                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                context.startActivity(intent);
            }
        }


    }

