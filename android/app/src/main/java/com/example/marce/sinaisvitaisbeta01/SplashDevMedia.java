package com.example.marce.sinaisvitaisbeta01;

import android.app.Activity;
  import android.content.Intent;
  import android.os.Bundle;
  import android.os.Handler;
   
  public class SplashDevMedia extends Activity {
         // Timer da splash screen
         private static int SPLASH_TIME_OUT = 3000;
   
         @Override
         protected void onCreate(Bundle savedInstanceState) {
               super.onCreate(savedInstanceState);
               setContentView(R.layout.activity_splash);
   
               new Handler().postDelayed(new Runnable() {
                      /*
                       * Exibindo splash com um timer.
                       */
                      @Override
                      public void run() {
                             // Esse método será executado sempre que o timer acabar
                             // E inicia a activity principal
                             Intent i = new Intent(SplashDevMedia.this, MainActivity.class);
                             startActivity(i);
   
                             // Fecha esta activity
                             finish();
                      }
               }, SPLASH_TIME_OUT);
         }
  }