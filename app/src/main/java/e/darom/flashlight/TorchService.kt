package e.darom.flashlight

import android.app.Service
import android.content.Intent
import android.os.IBinder

/*
1. TorchService 서비스는 Service 클래스를 상속받습니다.

2. TorchService 서비스가 Torch 클래스를 사용해야 합니다. Torch 클래스의 인스턴스를 얻는 방법에는
onCreate() 콜백 메서드를 사용하는 방법과 by lazy 를 사용하는 방법이 있습니다. onCreate() 콜백 메서드를 사용하면 코드가 더 길어지기 때문에 여기서는
by lazy 를 사용한 초기화 지연 방법을 사용했습니다. 이 방법을 사용하면 torch 객체를 처음 사용할 때 초기화됩니다.

3. 외부에서 startService() 메서드로 TorchService 서비스를 호출하면 onStartCommand() 콜백 메서드가 호출됩니다.
보통 인텐트에 action 값을 설정하여 호출하는데 "on"과 "off" 문자열을 액션으로 받았을 때 when 문을 사용하여 각각 플래시를 켜고 끄는 동작을 하도록 코드를 작성했습니다.

서비스는 메모리 부족 등의 이유로 시스템에 의해서 강제로 종료될 수 있습니다.
onStartCommand() 메서드는 다음 중 하나를 반환합니다. 이 값에 따라 시스템이 갖에로 종료한 후에 시스템 자원이 회복되어 다시 서비스를 시작할 수 있을 때 어떻게 할지를 결정합니다.
- START_STICKY : null 인텐트로 다시 시작합니다. 명령을 실행하지는 않기만 무기한으로 실행 중이며 작업을 기다리고 있는 미디어 플레이어와 비슷한 경우에 적합합니다.
- START_NOT_STICKY : 다시 시작하지 않음
- START_REDELIVER_INTENT : 마지막 인텐트로 다시 시작함. 능동적으로 수행 중인 파일 다운로드와 같은 서비스에 적합합니다.

어렵기 때문에 지금은 어떤 것을 사용해야 할지 모를 수 있습니다. 일반적인 경우에는 4. super 클래스의 onStartCommand() 메서드를 호출하면 내부적으로
START_STICKY 를 반환합니다. 이 예제에서는 사실 어떤 것을 사용해도 차이점을 느끼기 어렵습니다.
 */
class TorchService : Service() {        // 1.
    private val torch: Torch by lazy{   // 2.
        Torch(this)
    }

    private var isRunning = false               // 1.

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action){          // 3.
            // 앱에서 실행할 경우
            "on" -> {
                torch.flashOn()
                isRunning = true                // 2.
            }
            "off" -> {
                torch.flashOff()
                isRunning = false               // 3.
            }
            // 서비스에서 실행할 경우 : 액션값이 설정되지 않기 때문에 else 문이 실행됩니다.
            else -> {                           // 4.
                isRunning = !isRunning
                if (isRunning){
                    torch.flashOn()
                } else{
                    torch.flashOff()
                }
            }
        }
        return super.onStartCommand(intent, flags, startId)     // 4.
    }

    override fun onBind(intent: Intent): IBinder {
        TODO("Return the communication channel to the service.")
    }
}
