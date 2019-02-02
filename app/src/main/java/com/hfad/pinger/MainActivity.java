package com.hfad.pinger;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.view.View;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Random;


public class MainActivity extends AppCompatActivity {

    String[] beaconList = {"www.andi.dz", "waib.gouv.bj", "www.univ-ouaga.bf", "www.assemblee.bi", "www.anor.cm"};
    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv = (TextView) findViewById(R.id.ping_output);
    }


    public void bPing(View view){
        Ping();
    }

    public void Ping(){
        new Thread(new Runnable() {
            public void run() {
                Random random = new Random();
                for(int i=0;i<beaconList.length;i++){//
                    //int i = random.nextInt(beaconList.length);
                    Editable host = new SpannableStringBuilder(beaconList[i]);
                    int count=0;
                    Process p = null;
                    try {
                        String pingCmd = "ping -n -c 10 -w 30 -i 1 -s 100 " + host;//-D doesnt work on android  ping -n -w %deadline -c %count -i %interval -s %packetsize %destination
                        String pingResult = "";

                        Runtime r = Runtime.getRuntime();
                        p = r.exec(pingCmd);
                        BufferedReader in = new BufferedReader(new
                                InputStreamReader(p.getInputStream()));
                        String inputLine;

                        while ((inputLine = in.readLine()) != null) {
                            //System.out.println("Tester "+inputLine+" space "+inputLine.toLowerCase().contains("rtt"));

                            //Starting point check when ping issued
                            if((!inputLine.toLowerCase().contains("ping"))){
                            }
                            inputLine = "[" + System.currentTimeMillis() / 1000 + "]" + inputLine;

                            System.out.println(inputLine);

                            //keep adding to block
                            pingResult += inputLine+"\n";
                            final String ping_result=pingResult;

                            //Show progress in UI
                            tv.post(new Runnable() {
                                public void run() {
                                    tv.setText(ping_result);
                                }
                            });

                        }
                        System.out.print("Error Stream: " + p.getErrorStream());
                        in.close();
                    }//try
                    catch (IOException e) {
                        System.out.println(e);
                        System.out.print("Error Stream: " + p.getErrorStream());
                    }
                }
            }
        }).start();
    }

}
