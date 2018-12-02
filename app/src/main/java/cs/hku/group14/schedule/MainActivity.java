package cs.hku.group14.schedule;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.AnimatedImageDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import cs.hku.group14.schedule.custom.DBManager;
import cs.hku.group14.schedule.model.SessionEntity;
import cs.hku.group14.schedule.util.ConnectUtil;
import cs.hku.group14.schedule.view.BottomNavigationActivity;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, EasyPermissions.PermissionCallbacks {

    private EditText txt_UserName, txt_UserPW;
    private Button btn_Login;
    private CheckBox check_pwd;

    private boolean rememberPwdFlag = false;
    private boolean queryFlag = false;
    private String classJson;
    private String examJson;
    private String username;

    DBManager dbManager;

    /**
     * 随便赋值的一个唯一标识码
     */
    public static final int OPERATE_CALENDAR = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkPerm();

        initView();
        queryCourseSchedule();
        querySession();
        doTrustToCertificates();
        CookieHandler.setDefault(new CookieManager());
    }

    private void initView() {
        btn_Login = (Button) findViewById(R.id.btn_Login);
        txt_UserName = (EditText) findViewById(R.id.txt_UserName);
        txt_UserPW = (EditText) findViewById(R.id.txt_UserPW);
        check_pwd = findViewById(R.id.checkbox_pwd);
        // Register the Login button to click listener
        // Whenever the button is clicked, onClick is called
        btn_Login.setOnClickListener(this);
        check_pwd.setOnCheckedChangeListener(listener);

        dbManager = new DBManager(this);
    }

    /**
     * 查询Session
     */
    private void querySession() {
        SessionEntity entity = dbManager.querySession();
        if (entity == null) {
            // 上一次登陆未选择记住密码
            rememberPwdFlag = false;
        } else {
//            if ((System.currentTimeMillis() - entity.getTime()) > 5 * 24 * 3600 * 1000) {
            txt_UserName.setText(entity.getUsername());
            txt_UserPW.setText(entity.getPwd());
            check_pwd.setChecked(true);
        }
    }

    /**
     * 记住密码checkbox
     */
    private CompoundButton.OnCheckedChangeListener listener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (buttonView.getId() == R.id.checkbox_pwd) {
                if (isChecked) {
                    rememberPwdFlag = true;
                }
            }
        }
    };

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_Login) {
            username = txt_UserName.getText().toString();
            String upassword = txt_UserPW.getText().toString();

            //登陆portal, 获取课程
            connect(username, upassword);
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

    protected void alert(String title, String msg) {
        new AlertDialog.Builder(this)
                .setMessage(msg)
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

    /**
     * 解析HTML
     */
    public void parse_HTML_Source_and_Switch_Activity(String HTMLsource) {
        //解析html文件，获取全部course课程
        Pattern p_coursename = Pattern.compile("<h3 class=\"coursename\".*?>.*?>(.*?)</a>");
        Matcher m_course = p_coursename.matcher(HTMLsource);

        ArrayList<String> cname = new ArrayList<>();

        while (m_course.find()) {
            String course_name = m_course.group(1);

            String tmp_course = course_name.substring(0, 8);
            if (course_name.contains("Section 1A")) {
                tmp_course += "A";
            } else if (course_name.contains("Section 1B")) {
                tmp_course += "B";
            }

//            Integer pos = m_course.start();
            boolean flag = true;
            for (String sss : cname) {
                //检测是否添加过课程
                if (sss.equals(tmp_course)) {
                    flag = false;
                }
            }
            if (flag) {
                cname.add(tmp_course);
//                cnamePos.add(pos);
            }
        }

        //切换Activity，显示课表
        Intent intent = new Intent(getBaseContext(), BottomNavigationActivity.class);

        intent.putStringArrayListExtra("CourseName", cname);

        if (!queryFlag) {
            Log.e("Error", "未获取服务器数据,使用备用数据");
            classJson = "[{\"id\":0,\"course\":\"COMP7103A\",\"name\":\"Data mining\",\"room\":\"TT-403\",\"teacher\":\"Prof. Ben Kao\",\"weekList\":[1,2,3,4,6,8,9,10,11,12],\"start\":0,\"step\":3,\"day\":1,\"term\":\"18-19 Semester1\",\"colorRandom\":1}]\"";
        }
        intent.putExtra("classJsonStr", classJson);
        intent.putExtra("examJsonStr", examJson);
        intent.putExtra("username", username);
        startActivity(intent);
    }

    /**
     * 连接HKU portal
     *
     * @param userName
     * @param userPW
     */
    public void connect(final String userName, final String userPW) {
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Logging in ...");
        pDialog.show();

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
                    // 登陆成功
                    if (rememberPwdFlag == true) {
                        SessionEntity entity = new SessionEntity();
                        entity.setId("0");
                        entity.setUsername(userName);
                        entity.setPwd(userPW);
                        entity.setTime(System.currentTimeMillis());
                        dbManager.saveSession(entity);
                    }else {

                    }

                    //解析页面
                    parse_HTML_Source_and_Switch_Activity(moodlePageContent);
                } else {
                    alert("Error", error);
                }
                pDialog.hide();
            }

        }.execute("");
    }

    //从服务器查询课程时间表
    public void queryCourseSchedule() {
        AsyncTask<String, Void, String> task = new AsyncTask<String, Void, String>() {
            @Override
            protected String doInBackground(String... strings) {
                classJson = ConnectUtil.getCourseData();
                examJson = ConnectUtil.getExamData();
                if (classJson.length() > 0) {
                    queryFlag = true;
                    Log.i("MainActivity", "查询课表数据成功");
                } else {
                    queryFlag = false;
                    Log.i("MainActivity", "查询课表数据失败");
                }
                return null;
            }
        }.execute("");
    }

    /**
     * 检查权限
     */
    @AfterPermissionGranted(OPERATE_CALENDAR)
    private void checkPerm() {
        String[] params = {Manifest.permission.WRITE_CALENDAR, Manifest.permission.READ_CALENDAR};
        if (EasyPermissions.hasPermissions(this, params)) {
            Log.i("MainActivity", "有权限读写日历");
        } else {
            Log.i("MainActivity", "申请读写日历权限");

            EasyPermissions.requestPermissions(this, "Auth to Use Calendar", OPERATE_CALENDAR, params);
        }
    }


    //申请结果会回调到Activity 的 onRequestPermissionsResult() 方法中
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //requstCode 是 requestPermissions 传入的第三个参数
        //permissions[] 是申请的权限的数组
        //grantResults[] 是申请的结果
        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
//        Log.i("MainActivity","申请结果 : " + grantResults[0] + " , " + grantResults[1]);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        //如果checkPerm方法，没有注解AfterPermissionGranted，也可以在这里调用该方法。

    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            //这里需要重新设置Rationale和title，否则默认是英文格式
            new AppSettingsDialog.Builder(this)
//                    .setRationale("没有该权限，此应用程序可能无法正常工作。打开应用设置屏幕以修改应用权限")
//                    .setTitle("必需权限")
                    .build()
                    .show();
        }
    }

}