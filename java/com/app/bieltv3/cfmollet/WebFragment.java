package com.app.bieltv3.cfmollet;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

public class WebFragment extends Fragment {

    private WebView web;
    private ProgressBar loading;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Rebem la informació
        String url = getArguments().getString("url");

        View view = inflater.inflate(R.layout.fragment_webview, container, false);

        web = (WebView) view.findViewById(R.id.webview);
        loading = (ProgressBar) view.findViewById(R.id.progess);

        web.setDrawingCacheEnabled(true);

        // Si clickem el webwiew
        web.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                // Agafem la url clickada i la carreguem
                DownloadPage load = new DownloadPage(url);
                load.execute();

                // Sobreposem la nostra carregada a la habitual
                return true;
            }
        });

        DownloadPage load = new DownloadPage(url);
        load.execute(url);

        return view;
    }

    protected String formatURL(String html) {
        return html;
    }

    private class DownloadPage extends AsyncTask<String, String, String> {
        String url;

        public DownloadPage (String url) {
            // Rebem la url que ens envien
            this.url = url;
        }

        protected String doInBackground(String... urls) {
            return Connect.getHtml(url);
        }

        protected void onPostExecute(String url) {
            url = formatURL(url);
            web.loadDataWithBaseURL(url, url, "text/html", "UTF-8", null);
            web.buildDrawingCache();

            loading.setVisibility(View.INVISIBLE);
        }
    }
}