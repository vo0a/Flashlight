package e.darom.flashlight

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews

/**
 * 앱 위젯을 클릭할 때의 동작을 작성하는 곳입니다.
 * Implementation of App Widget functionality.
 */
/*
1. 앱 위젯용 파일은 AppWidgetProvider 라는 일종의 브로드캐스트 리시버 클래스를 상속받습니다.

2. onUpdate() 메서드는 위젯이 업데이트되어야 할 때 호출됩니다.

3. 위젯이 여러 개 배치되었다면 모든 위젯을 업데이트해야 합니다.

4. 위젯이 처음 생성될 때 호출됩니다.

5. 여러 개일 경우 마지막 위젯이 제거될 때 호출됩니다.

6. 위젯을 업데이트할 때 수행되는 코드입니다.

7. 위젯은 액티비티에서 레이아웃을 다루는 것과 조금 다릅니다. 위젯에 배치하는 뷰는 따로 있습니다.
그것들은 RemoteViews 객체로 가져올 수 있습니다.

8. setTextViewText() 메서드는 RemoteViews 객체용으로 준비된 텍스트값을 변경하는 메서드입니다.

9. 여기서 위젯을 클릭했을 때의 처리를 추가해야 합니다.

10. 레이아웃을 모두 수정했다면 AppWidgetmanager 를 사용해 위젯을 업데이트합니다.

위젯을 구현하는 것이 이렇게 복잡합니다. 하지만 우리는 9. 부분에 필요한 코드를 추가해야한다는 사실만 기억합니다.
 */
class TorchAppWidget : AppWidgetProvider() {        // 1.
    // 2.
    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {         // 3.
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    // 4.
    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
    }
    // 5.
    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    companion object {
        // 6.
        internal fun updateAppWidget(
            context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int ) {
            val widgetText = context.getString(R.string.appwidget_text)
            // Construct the RemoteViews object : RemoteView 객체를 구성합니다.
            val views = RemoteViews(context.packageName, R.layout.torch_app_widget)     // 7.
            views.setTextViewText(R.id.appwidget_text, widgetText)      // 8.

            // 9. 추가로 작성할 부분
            /*
            RemoteViews 객체는 위젯의 전체 레이아웃의 정보입니다. 여기에 포함된 텍스트 뷰의 글자를 바꿀 때 sestTextViewText() 메서드를 사용했습니다.

            1. 클릭 이벤트를 연결하려면 setOnClickPendingIntent() 메서드를 사용합니다. 여기에는 클릭이 발생할 뷰의 ID와 PendingIntent 객체가 필요합니다.

            PendingIntent 는 실행할 인텐트 정보를 가지고 있다가 수행해줍니다. 다음과 같이 어떤 인텐트를 실행할지에 따라서 다른 메서드를 사용해야 합니다.
            -PendingIntent.getActivity() : 액티비티 실행
            -PendingIntent.getService() : 서비스 실행
            -PendingIntent.getBroadcast() : 브로드캐스트 실행

            TorchService 서비스를 실행하는데 2. PendingIntent.getService() 메서드를 사용합니다.
            전달하는 인자는 컨텍스트, 리퀘스트 코드, 3. 서비스 인텐트, 플래그 4개 입니다. 리퀘스트 코드나 플래그 값은 사용하지 않기 때문에 0을 전달합니다.

            이제 위젯을 클릭하면 TorchService 서비스가 시작됩니다.

            하지만 TorchService 는 인텐트에 ON 또는 OFF 액션을 지정해서 켜거나 껐습니다. 위젯의 경우 어떤 경우가 ON 이고 OFF 인지 알 수 없기 때문에 액션을 지정할 수 없습니다.
            액션이 지정되지 않아도 플래시가 동작하도록 TorchService.kt 파일을 수정해야 합니다.
             */
            //실행할 Intent 작성
            val intent = Intent(context, TorchService::class.java)                               // 3.
            val pendingIntent = PendingIntent.getService(context, 0, intent, 0) // 2.

            //위젯을 클릭하면 위에서 정의한 Intent 실행
            views.setOnClickPendingIntent(R.id.appwidget_layout, pendingIntent)                 // 1.



            // Instruct the widget manager to update the widget : 위젯 관리자에게 위젯을 업데이트하도록 지시
            appWidgetManager.updateAppWidget(appWidgetId, views)        // 10.
        }
    }
}
