package com.hrishi_3331.hrishi_studio.tronix2021;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;

public class studyViewHolder extends RecyclerView.ViewHolder {

    private View myView;
    private TextView titleView, descriptionView, authorView;
    private String download_url;
    private LinearLayout nodatacase, nodatacard;

    public studyViewHolder(@NonNull View itemView) {
        super(itemView);
        myView = itemView;
        titleView = myView.findViewById(R.id.title_m);
        descriptionView = myView.findViewById(R.id.desc_m);
        authorView = myView.findViewById(R.id.m_notes_author);
        nodatacard = myView.findViewById(R.id.no_data_card);
        nodatacase = myView.findViewById(R.id.nodatacase);
    }

    public void setTitleView(String title, String author, String desc) {

        if (title.equals("nodatafound")){
            nodatacase.setVisibility(View.GONE);
            nodatacard.setVisibility(View.VISIBLE);
        }
        else {
            this.titleView.setText(title);
            this.authorView.setText(author);
            this.descriptionView.setText(desc);
        }
    }

    public void setDownload_url(String url){
        this.download_url = url;
    }

    public void setListners(final Context context){

        myView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HandleDocument document = new HandleDocument(context, titleView.getText().toString(), download_url);
            }
        });
    }

    public class HandleDocument{

        public HandleDocument(Context context, String name, String link){

            File directory = new File(Environment.getExternalStorageDirectory().getPath() + "/" + "TRONIX Documents");

            if (!directory.exists()){
                directory.mkdir();
            }

            File file = new File(directory, name + ".pdf");
            if (!file.exists()){
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                PdfDownloader downloader = new PdfDownloader(context, file, link);
                downloader.downloadFiles();
            }
            else {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(FileProvider.getUriForFile(context, "com.hrishi_3331.hrishi_studio.tronix2021.provider", file), "application/pdf");
                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                context.startActivity(intent);
            }
        }

    }

}
