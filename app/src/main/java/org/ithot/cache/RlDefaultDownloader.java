package org.ithot.cache;

import android.os.AsyncTask;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 简易默认下载器
 */

public class RlDefaultDownloader extends RlDownloader {

    private String remote; // 下载地址
    private String local; // 本地文件地址
    private static final long MAX_PROGRESS = 100; // 下载进度
    private final static int DOWNLOAD_BUFFER_SIZE = 4096; // 下载块大小
    private DownloadAsyncTask task; // 下载任务
    private IRlStrict strict;

    public static String path() {
        String p = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "rlCache" + File.separator + "apk";
        File path = new File(p);
        if (!path.exists()) {
            path.mkdirs();
        }
        return path.getAbsolutePath();
    }

    @Override
    public void start(String remote, IRlStrict strict) {
        if (strict == null || remote == null) {
            throw new RuntimeException("strict and remote not be null");
        }
        this.strict = strict;
        this.remote = remote;
        task = new DownloadAsyncTask();
        task.execute(remote);
    }

    @Override
    public void cancel() {
        if (task != null) {
            task.cancel(true);
        }
    }

    private class DownloadAsyncTask extends AsyncTask<String, Integer, Boolean> {

        @Override
        protected Boolean doInBackground(String... params) {
            HttpURLConnection conn = null;
            try {
                URL url = new URL(params[0]);
                conn = (HttpURLConnection) url.openConnection();
                if (conn.getResponseCode() == 200) {
                    int totalSize = conn.getContentLength();
                    local = path() + File.separator + new File(params[0]).getName();
                    File file = new File(local);
                    if (!file.exists()) {
                        file.createNewFile();
                    }
                    byte[] buffer = new byte[DOWNLOAD_BUFFER_SIZE];
                    int bufferLength = 0;
                    long downloadedSize = 0;
                    FileOutputStream fileOut = new FileOutputStream(file);
                    InputStream in = conn.getInputStream();
                    while ((bufferLength = in.read(buffer)) > 0) {
                        fileOut.write(buffer, 0, bufferLength);
                        downloadedSize += bufferLength;
                        long __s__ = downloadedSize * MAX_PROGRESS;
                        long __t__ = totalSize;
                        int progress = (int) (__s__ / __t__);
                        publishProgress(new Integer[]{progress});
                    }
                    fileOut.close();
                    in.close();
                    if (downloadedSize == totalSize) {
                        return true;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (conn != null) {
                    conn.disconnect();
                }
            }
            return false;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            // unfinished progress part
        }

        @Override
        protected void onPostExecute(Boolean is_success) {
            if (is_success) {
                RlLog.debug(remote + "：download success " + local);
                // 下载成功
                Rl.put(remote, local, true);
                strict.get(local);
            } else {
                RlLog.debug(remote + "：download failed ");
                // 下载失败
                strict.get(null);
            }
        }
    }

}
