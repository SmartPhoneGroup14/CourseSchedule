package cs.hku.group14.schedule.util;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import javax.net.ssl.HttpsURLConnection;

public class ConnectUtil {

    private static final String TAG = "ConnectUtil";

    public static String ReadBufferedHTML(BufferedReader reader, char[] htmlBuffer, int bufSz) throws java.io.IOException {
        htmlBuffer[0] = '\0';
        int offset = 0;
        do {
            int cnt = reader.read(htmlBuffer, offset, bufSz - offset);
            if (cnt > 0) {
                offset += cnt;
            } else {
                break;
            }
        } while (true);
        return new String(htmlBuffer);
    }

    public static String getMoodleFirstPage(String userName, String userPW) {
        HttpsURLConnection conn_portal = null;
        URLConnection conn_moodle = null;

        final int HTML_BUFFER_SIZE = 2 * 1024 * 1024;
        char htmlBuffer[] = new char[HTML_BUFFER_SIZE];

        final int HTTPCONNECTION_TYPE = 0;
        final int HTTPSCONNECTION_TYPE = 1;
        int moodle_conn_type = HTTPCONNECTION_TYPE;

        try {
            /////////////////////////////////// HKU portal //////////////////////////////////////

            String url_tmp = "https://hkuportal.hku.hk/cas/login?service=http://moodle.hku.hk/login/index.php?authCAS=CAS&username="
                    + userName + "&password=" + userPW;

            URL url_portal = new URL(url_tmp);
            conn_portal = (HttpsURLConnection) url_portal.openConnection();

            BufferedReader reader_portal = new BufferedReader(new InputStreamReader(conn_portal.getInputStream()));
            String HTMLSource = ReadBufferedHTML(reader_portal, htmlBuffer, HTML_BUFFER_SIZE);
            int ticketIDStartPosition = HTMLSource.indexOf("ticket=") + 7;
            if (ticketIDStartPosition == 6) {
                return "Invalid";
            }
            String ticketID = HTMLSource.substring(ticketIDStartPosition, HTMLSource.indexOf("\";", ticketIDStartPosition));

            Log.i(TAG, "TicketId : " + ticketID);

            reader_portal.close();
            /////////////////////////////////// HKU portal //////////////////////////////////////

            /////////////////////////////////// Moodle //////////////////////////////////////
            URL url_moodle = new URL("http://moodle.hku.hk/login/index.php?authCAS=CAS&ticket=" + ticketID);
            conn_moodle = url_moodle.openConnection();
            ((HttpURLConnection) conn_moodle).setInstanceFollowRedirects(true);

            BufferedReader reader_moodle = new BufferedReader(new InputStreamReader(conn_moodle.getInputStream()));

            /// handling redirects to HTTPS protocol
            while (true) {
                String redirect_moodle = conn_moodle.getHeaderField("Location");
                if (redirect_moodle != null) {
                    URL new_url_moodle = new URL(url_moodle, redirect_moodle);
                    if (moodle_conn_type == HTTPCONNECTION_TYPE) {
                        ((HttpURLConnection) conn_moodle).disconnect();
                    } else {
                        ((HttpsURLConnection) conn_moodle).disconnect();
                    }
                    conn_moodle = new_url_moodle.openConnection();
                    if (new_url_moodle.getProtocol().equals("http")) {
                        moodle_conn_type = HTTPCONNECTION_TYPE;
                        ((HttpURLConnection) conn_moodle).setInstanceFollowRedirects(true);
                    } else {
                        moodle_conn_type = HTTPSCONNECTION_TYPE;
                        ((HttpsURLConnection) conn_moodle).setInstanceFollowRedirects(true);
                    }

                    url_moodle = new_url_moodle;

                    //String cookie = conn_moodle.getHeaderField("Set-Cookie");
                    //if (cookie != null) {
                    //    conn_moodle2.setRequestProperty("Cookie", cookie);
                    //}
                    reader_moodle = new BufferedReader(new InputStreamReader(conn_moodle.getInputStream()));
                } else {
                    break;
                }
            }

            HTMLSource = ReadBufferedHTML(reader_moodle, htmlBuffer, HTML_BUFFER_SIZE);
            reader_moodle.close();
            return HTMLSource;
            /////////////////////////////////// Moodle //////////////////////////////////////

        } catch (Exception e) {
            Log.e(TAG, e.toString());

            e.printStackTrace();

            return "Fail to login";
        } finally {
            // When HttpClient instance is no longer needed,
            // shut down the connection manager to ensure
            // immediate deallocation of all system resources
            if (conn_portal != null) {
                conn_portal.disconnect();
            }
            if (conn_moodle != null) {
                if (moodle_conn_type == HTTPCONNECTION_TYPE) {
                    ((HttpURLConnection) conn_moodle).disconnect();
                } else {
                    ((HttpsURLConnection) conn_moodle).disconnect();
                }
            }
        }
    }


    public static String getCourseData() {
        String result = "";
        try {
            URL url = new URL("http://39.104.136.13:9080/api/getAllCourses");
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            if (urlConnection.getResponseCode() != 200) {
                Log.e(TAG, "请求url失败 : " + urlConnection.getResponseCode());
                Log.e(TAG, urlConnection.getResponseMessage());
            }

            InputStream in = new BufferedInputStream(urlConnection.getInputStream());

            Log.i(TAG, urlConnection.getContent().toString());
            Log.i(TAG, urlConnection.getResponseMessage());

            result = inputStreamToString(in);

        } catch (Exception e) {
            e.printStackTrace();

        }

        return result;
    }

    private static String inputStreamToString(InputStream is) {
        String rLine = "";
        StringBuilder answer = new StringBuilder();

        InputStreamReader isr = new InputStreamReader(is);

        BufferedReader rd = new BufferedReader(isr);

        try {
            while ((rLine = rd.readLine()) != null) {
                answer.append(rLine);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return answer.toString();
    }

    public static String getExamData() {
        String result = "";
        try {
            URL url = new URL("http://39.104.136.13:9080/api/getAllExams");
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            if (urlConnection.getResponseCode() != 200) {
                Log.e(TAG, "请求url失败 : " + urlConnection.getResponseCode());
                Log.e(TAG, urlConnection.getResponseMessage());
            }

            InputStream in = new BufferedInputStream(urlConnection.getInputStream());

            Log.i(TAG, urlConnection.getContent().toString());
            Log.i(TAG, urlConnection.getResponseMessage());

            result = inputStreamToString(in);

        } catch (Exception e) {
            e.printStackTrace();

        }

        return result;
    }
}
