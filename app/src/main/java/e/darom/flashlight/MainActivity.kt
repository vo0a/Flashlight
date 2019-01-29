package e.darom.flashlight

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.intentFor

/*
 플래시를 켜는 방법은 안드로이드 6.0 이상에서 제공하는 방법(API 23)이 가장 간단합니다.
 5.0 에서는 복잡하고, 5.0 미만에서는 공식적으로 플래시를 켜는 방법이 없고 제조사마다 다른 방법을 사용해야 합니다.
 */
/*
앱 실행 없이 위젯을 사용해 플래시를 켜는 기능도 제공해 봅시다. 액티비티가 아닌 서비스에서 조작하면 가능합니다.

구현 순서는 다음과 같습니다.
1. 서비스 소개
2. 서비스의 생명주기
3. 서비스로 손전등 기능 옮기기
4. 액티비티에서 서비스를 사용해 손전등 켜기

서비스란 안드로이드의 4대 컴포넌트 중 하나로 화면이 없고 백그라운드에서 수행하는 작업을 작성하는 컴포넌트입니다.

플래시를 켜는 기능에 화면이 꼭 필요하지는 않습니다. 액티비티는 단순히 플래시를 켜고 끄는 인터페이스만 제공합니다.
이런 경우에는 서비스에서 플래시를 켜고 끄도록 하고 액티비티는 서비스를 호출하는 방법을 사용합니다.
이렇게 되면 플래시 위젯도 서비스를 호출해서 조작할 수 있게 됩니다.

서비스는 크게 바운드된 서비스와 바운드되지 않은 서비스로 나뉩니다. 예제에서는 바운드되지 않은 서비스를 다루고 그냥 '서비스'라고 부르겠습니다.
 */
/*
서비스를 시작하려면 startService() 메서드를 사용합니다.
다음은 Torchservice 를 사용해서 플래시를 켜는 인텐트에 "on" 액션을 설정하여 서비스를 시작하는 코드입니다.

val intent = Intent(this, TorchService::class.java)
intent.action = "on"
startService(intent)

이 코드는 Anko 를 사용하면 다음과 같이 작성할 수 있습니다.
startService(intentFor<TorchService>().setAction("on"))

 */
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val torch = Torch(this)     // 1.

        /*
        buttonView 인자는 상태가 변경된 Switch 객체 자신이고, isChecked 인자는 On/Off 상태를 Boolean 으로 알려줍니다.
        이 값으로 켜졌을 때와 꺼졌을 때의 처리를 할 수 있습니다.

        1. 작성한 Torch 클래스를 인스턴트화 합니다.

        2. 스위치가 켜지면 flashOn() 메서드를 호출하여 플래시를 켜고 3. 스위치가 꺼지면 flashOff() 메서드를 호출하여 플래시를 끕니다.

        앱을 안드로이드 6.0 이상 실제 기기에서 실행하여 플래시가 잘 작동하면 성공입니다.
         */
        //서비스를 사용해 플래시를 켤 수 있도록 코드를 수정.
        flashSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked){
                // On
                //torch.flashOn()            // 2.
                startService(intentFor<TorchService>().setAction("on"))
            } else{
                // Off
                //torch.flashOff()           // 3.
                startService(intentFor<TorchService>().setAction("off"))
            }
        }

    }



}
