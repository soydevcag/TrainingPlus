package com.lequar.trainingplus.model.Utilities;

import android.util.Log;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.app.Service;
import android.os.IBinder;
/**
 * Created by lequar on 10/05/16.
 */
public class Notifications extends Service {
    private static final String TAG = "MyService";

    @Override
    public IBinder onBind(Intent i) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {

        new Thread(new Runnable() {

            @Override
            public void run() {
                Log.d(TAG, "Servicio notificaciones inicia");
                // El servicio se finaliza a sí mismo cuando finaliza su
                // trabajo.
                try {
                    // Simulamos trabajo de 1 segundo.
                    Thread.sleep(10000);

                    // Instanciamos e inicializamos nuestro manager.
                    NotificationManager nManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                    Log.d(TAG, "Servicio notificaciones finaliza");
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }
        }).start();

        this.stopSelf();
        return super.onStartCommand(intent, flags, startId);
    }
}