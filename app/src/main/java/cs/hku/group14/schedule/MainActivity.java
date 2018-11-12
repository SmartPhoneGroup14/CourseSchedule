package cs.hku.group14.schedule;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import cs.hku.group14.schedule.util.ConnectUtil;
import cs.hku.group14.schedule.view.BaseFuncActivity;
import cs.hku.group14.schedule.view.CourseListActivity;

/*
Smart Phone 目标：
1、预设数据库数据：课程编号-课程时间，线程后台查询
2、HKU Portal账号登陆，参考WorkShop1，解析 moodle页面
3、匹配课程编号，展示课程时间为课程表，使用Git 控件
4、提前一段时间，进行消息提示，可以添加Email 提示。
 */

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText txt_UserName, txt_UserPW;
    private Button btn_Login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        doTrustToCertificates();
        CookieHandler.setDefault(new CookieManager());
    }

    private void initView() {
        btn_Login = (Button) findViewById(R.id.btn_Login);
        txt_UserName = (EditText) findViewById(R.id.txt_UserName);
        txt_UserPW = (EditText) findViewById(R.id.txt_UserPW);
        // Register the Login button to click listener
        // Whenever the button is clicked, onClick is called
        btn_Login.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_Login) {
            String uname = txt_UserName.getText().toString();
            String upassword = txt_UserPW.getText().toString();

            //登陆portal, 获取课程
            connect(uname, upassword);
        }
    }


    // trusting all certificate
    public void doTrustToCertificates() {
        TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }

                    public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType) {
                    }

                    public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType) {
                    }
                }
        };

        try {
            // Install the all-trusting trust manager
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void alert(String title, String mymessage) {
        new AlertDialog.Builder(this)
                .setMessage(mymessage)
                .setTitle(title)
                .setCancelable(true)
                .setNegativeButton(android.R.string.cancel,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                            }
                        }
                )
                .show();
    }

    public void parse_HTML_Source_and_Switch_Activity(String HTMLsource) {
        //解析html文件，获取全部course课程
        Pattern p_coursename = Pattern.compile("<h3 class=\"coursename\".*?>.*?>(.*?)</a>");
        Matcher m_course = p_coursename.matcher(HTMLsource);
        Pattern p_teachercandidates = Pattern.compile("<div class=\"teachers\">Teacher: <.*?>(.*?)</a>");
        Matcher m_teachercandidates = p_teachercandidates.matcher(HTMLsource);

        ArrayList<String> cname = new ArrayList<String>();
        ArrayList<String> cteachers = new ArrayList<String>();
        ArrayList<String> cteachersfinal = new ArrayList<String>();
        ArrayList<Integer> cnamePos = new ArrayList<Integer>();
        ArrayList<Integer> cteachersPos = new ArrayList<Integer>();
        ArrayList<Integer> cteachersIdx = new ArrayList<Integer>();

        while (m_course.find()) {
            String course_name = m_course.group(1);
            Integer pos = m_course.start();
            boolean flag = true;
            for (String sss : cname) {
                //检测是否添加过课程
                if (sss.equals(course_name)) {
                    flag = false;
                }
            }
            if (flag) {
                cname.add(course_name);
                cnamePos.add(pos);
            }
        }

        while (m_teachercandidates.find()) {
            String string_teachername = m_teachercandidates.group(1);
            // int nameStartPosition = string_teachername.indexOf(">")+1;
            // int nameEndPosition = string_teachername.indexOf("</a>");
            // String teacher_name = string_teachername.substring(nameStartPosition, nameEndPosition);
            cteachers.add(string_teachername);
            Integer pos = m_teachercandidates.start();
            cteachersPos.add(pos);
        }

        int cIdx = 0;
        for (int i = 0; i < cteachersPos.size(); ) {
            int cpos0 = -1, cpos1 = -1;
            int tpos = cteachersPos.get(i);
            if (cIdx < cnamePos.size()) {
                cpos0 = cnamePos.get(cIdx);
            }
            if (cIdx + 1 < cnamePos.size()) {
                cpos1 = cnamePos.get(cIdx + 1);
            }
            if (cpos0 < 0 || tpos < cpos0) { /// a course with 2 teachers!? Assume the teacher belongs to the previous course
                cteachersIdx.add(cIdx - 1);
                i++;
            } else if (cpos1 < 0 || (cpos0 < tpos && cpos1 > tpos)) {
                cteachersIdx.add(cIdx);
                i++;
                cIdx++;
            } else { /// tpos > cpos1 ==> teacher belongs to next classes
                cIdx++;
            }
        }

        for (int i = 0; i < cname.size(); i++) {
            String tname = "";
            for (int j = 0; j < cteachersIdx.size(); j++) {
                int cidx = cteachersIdx.get(j);
                if (cidx == i) {
                    tname += cteachers.get(j);
                }
            }
            cteachersfinal.add(tname);
        }

        //切换Activity，显示课表
//        Intent intent = new Intent(getBaseContext(), CourseListActivity.class);
        Intent intent = new Intent(getBaseContext(), BaseFuncActivity.class);
        intent.putStringArrayListExtra("CourseName", cname);
        intent.putStringArrayListExtra("Teachers", cteachersfinal);
        startActivity(intent);

    }

    public void connect(final String userName, final String userPW) {
        final ProgressDialog pdialog = new ProgressDialog(this);

        pdialog.setCancelable(false);
        pdialog.setMessage("Logging in ...");
        pdialog.show();

        AsyncTask<String, Void, String> task = new AsyncTask<String, Void, String>() {
            boolean success;
            String moodlePageContent;
            String error = "Fail to login";

            @Override
            protected String doInBackground(String... arg0) {
                success = true;
                moodlePageContent = ConnectUtil.getMoodleFirstPage(userName, userPW);


                if (moodlePageContent.equals("Invalid")) {
                    success = false;
                    error = "Invalid PortalID or Password";

                } else if (moodlePageContent.equals("Fail to login")) {
                    success = false;
                    error = "Fail to login";
                }


                return null;
            }

            @Override
            protected void onPostExecute(String result) {
                if (success) {
                    //解析页面
                    parse_HTML_Source_and_Switch_Activity(moodlePageContent);
                } else {
                    alert("Error", error);
                }
                pdialog.hide();
            }

        }.execute("");
    }

}
