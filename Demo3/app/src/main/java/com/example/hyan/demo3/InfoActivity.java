package com.example.hyan.demo3;

import android.support.v7.app.ActionBar;
import android.graphics.drawable.Drawable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;


public class InfoActivity extends ActionBarActivity {
ActionBar actionBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        actionBar = getSupportActionBar();
        actionBar.setTitle("免责声明");
        Drawable drawable = this.getResources().getDrawable(R.drawable.action_bar_background);
        actionBar.setBackgroundDrawable(drawable);
        TextView textView = (TextView)findViewById(R.id.information);
        textView.setText("找乐365提醒您：在使用找乐365前，请您务必仔细阅读并透彻理解本声明。 您可以选择不使用找乐365，但如果您使用找乐365，您的使用行为将被视为对本声明全部内容的认可。\n" +
                "鉴于找乐365以非人工检索方式、根据您键入的关键字自动生成到第三方网页的链接， 一切因使用找乐365而可能遭致的意外、疏忽、侵权及其造成的损失， 找乐365对其概不负责，亦不承担任何法律责任。\n" +
                "任何通过使用找乐365而搜索链接到的第三方网页內容均系他人制作或提供，您可能从该第三方网页上 获得资讯及享用服务，找乐365对其合法性概不负责，亦不承担任何法律责任。\n" +
                "找乐365搜索结果根据您键入的关键字自动搜索获得并生成，不代表找乐365赞成被搜索链接到 的第三方网页上的内容或立场。\n" +
                "您应该对使用找乐365的结果自行承担风险。找乐365不做任何形式的保证：不保证搜索结果满足您的要求， 不保证搜索服务不中断，不保证搜索结果的安全性、正确性、及时性、合法性。因网络状况、通讯线路、 第三方网站等任何原因而导致您不能正常使用找乐365，找乐365不承担任何法律责任。\n" +
                "找乐365尊重并保护所有使用找乐365用户的个人隐私权，您注册的用户名、电子邮件地址等个人资料， 非经您亲自许可或根据相关法律、法规的强制性规定，找乐365不会主动地泄露给第三方。 找乐365提醒您：您在使用搜索引擎时输入的关键字将不被认为是您的个人隐私资料。\n" +
                "任何网站如果不想被找乐365收录，应该及时向找乐365反映，否则，找乐365将视其为可收录网站， 并可能对相关内容进行挖掘处理。\n" +
                "任何单位或个人认为通过找乐365搜索链接到的第三方网页内容可能涉嫌侵犯其信息网络传播权， 应该及时向找乐365提出书面权利通知，并提供身份证明、权属证明及详细侵权情况证明。 找乐365在收到上述法律文件后，将会依法尽快断开相关链接内容。");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_info, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
