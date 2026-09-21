package com.example.mylauncher

import android.content.Intent
import android.content.pm.ResolveInfo
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

// アプリの情報を保持するデータ構造
data class AppInfo(
    val name: String,
    val packageName: String,
    val icon: Drawable
)

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 1. レイアウトファイル（activity_main.xml）を画面にセット
        setContentView(R.layout.activity_main)

        // 2. 画面上の RecyclerView を取得
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)

        // ★【Xperia風ポイント】リストを横4列のグリッド（マス目）に設定
        recyclerView.layoutManager = GridLayoutManager(this, 4)

        // 3. アプリ一覧を取得してグリッドにセット
        val appsList = getInstalledApps()
        recyclerView.adapter = AppAdapter(appsList) { packageName ->
            // タップされたアプリを起動
            val launchIntent = packageManager.getLaunchIntentForPackage(packageName)
            if (launchIntent != null) {
                startActivity(launchIntent)
            }
        }
    }

    // インストール済みアプリの一覧とアイコンを取得する処理
    private fun getInstalledApps(): List<AppInfo> {
        val mainIntent = Intent(Intent.ACTION_MAIN, null).apply {
            addCategory(Intent.CATEGORY_LAUNCHER)
        }
        val resolveInfoList = packageManager.queryIntentActivities(mainIntent, 0)

        return resolveInfoList.map { resolveInfo ->
            val packageName = resolveInfo.activityInfo.packageName
            val appName = resolveInfo.loadLabel(packageManager).toString()
            val iconDrawable = resolveInfo.loadIcon(packageManager)

            AppInfo(appName, packageName, iconDrawable)
        }
    }

    // ホームアプリのため「戻るボタン」を無効化
    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        // 何もしない
    }
}

// 4列グリッドにアイコンと名前を配置するためのアダプター
class AppAdapter(
    private val appList: List<AppInfo>,
    private val onItemClick: (String) -> Unit
) : RecyclerView.Adapter<AppAdapter.AppViewHolder>() {

    class AppViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val appIcon: ImageView = view.findViewById(R.id.appIcon)
        val appName: TextView = view.findViewById(R.id.appName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_app, parent, false)
        return AppViewHolder(view)
    }

    override fun onBindViewHolder(holder: AppViewHolder, position: Int) {
        val app = appList[position]
        holder.appName.text = app.name
        holder.appIcon.setImageDrawable(app.icon)

        holder.itemView.setOnClickListener {
            onItemClick(app.packageName)
        }
    }

    override fun getItemCount(): Int = appList.size
}

    // ホームアプリなので「戻るボタン」を押してもアプリが閉じないように無効化する
    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        // 何もしない
    }
}
