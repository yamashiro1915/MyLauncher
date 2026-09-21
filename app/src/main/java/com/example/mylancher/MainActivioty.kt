package com.example.mylauncher

import android.content.Intent
import android.content.pm.ResolveInfo
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 画面要素（ListView）をコードだけで組み立てる
        val listView = ListView(this)
        setContentView(listView)

        // スマホにインストールされている「起動可能なアプリ」を取得
        val mainIntent = Intent(Intent.ACTION_MAIN, null).apply {
            addCategory(Intent.CATEGORY_LAUNCHER)
        }
        val appsList: List<ResolveInfo> = packageManager.queryIntentActivities(mainIntent, 0)

        // 表示用のアプリ名リストを作成
        val appNames = appsList.map { it.loadLabel(packageManager).toString() }

        // 画面（ListView）にアプリ名を流し込む
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, appNames)
        listView.adapter = adapter

        // アプリがタップされたときの処理（起動）
        listView.setOnItemClickListener { _, _, position, _ ->
            val selectedApp = appsList[position]
            val packageName = selectedApp.activityInfo.packageName
            val launchIntent = packageManager.getLaunchIntentForPackage(packageName)
            if (launchIntent != null) {
                startActivity(launchIntent)
            }
        }
    }

    // ホームアプリなので「戻るボタン」を押してもアプリが閉じないように無効化する
    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        // 何もしない
    }
}