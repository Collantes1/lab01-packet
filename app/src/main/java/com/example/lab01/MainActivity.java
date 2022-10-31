package com.example.lab01;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Message;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    byte[] send_buffer = new byte[100];
    byte[] receive_buffer = new byte[100];

    DatagramSocket socket;
    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv = findViewById(R.id.textView3);
        tv.setMovementMethod(new ScrollingMovementMethod());
        //Подключение БД
        g.Dialogs = new DB(this, "Dialogs.db", null, 1);

        try
        {
            InetAddress local_network = InetAddress.getByName("0.0.0.0");
            SocketAddress local_address = new InetSocketAddress(local_network, 9000);
            // создание сокета
            socket = new DatagramSocket(local_address);
            socket.setBroadcast(true);

        } catch (UnknownHostException | SocketException e)
        {
            e.printStackTrace();
        }

        // экземпляр класса
        Runnable receiver = new Runnable() {
            @Override
            public void run()
            {
                Log.e("TEST", "RECEIVING THREAD IS RUNNING");
                DatagramPacket received_packet = new DatagramPacket(receive_buffer, receive_buffer.length);
                // Прием сообщений
                while(true)
                {

                    try {
                        socket.receive(received_packet);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    String s = new String(received_packet.getData(), 0, received_packet.getLength());
                    String[] data = s.split("%", 5);
                    msg m = new msg();
                    m.IP = data[0];
                    m.Port = data[1];
                    m.Name = data[2];
                    m.Text = data[3];
                    Log.e("Test", "Recieved packet");
                    Log.e("Test", s);
                    runOnUiThread(() -> {
                        String text = tv.getText().toString();
                        text += "\n" + m.toString();
                        tv.setText(text);
                    });

                }
            }
        };

        Thread receiving_thread = new Thread(receiver);
        receiving_thread.start();
    }

    DatagramPacket send_packet;

    public void onClick(View v)
    {
        EditText tn = findViewById(R.id.txt_name);
        String name = tn.getText().toString();

        EditText ta = findViewById(R.id.txt_address);
        String ip = ta.getText().toString();

        EditText tp = findViewById(R.id.txt_port);
        String port = tp.getText().toString();
                //Integer.parseInt(tp.getText().toString());

        EditText tt = findViewById(R.id.txt_message);
        String message = tt.getText().toString();
        // создание сообщения
        msg m = new msg();
        m.IP = ip;
        m.Port = port;
        m.Name = name;
        m.Text = message;

        byte[] send_buffer = m.msqB();

        try
        {
            // создание пакета и ip адресса
            InetAddress remote_address = InetAddress.getByName(ip);
            send_packet = new DatagramPacket(send_buffer, send_buffer.length, remote_address, Integer.parseInt(port));
        }
        catch (UnknownHostException e)
        {
            e.printStackTrace();
        }
        // экземпляр класса, для потока
        Runnable r = new Runnable() {
            @Override
            public void run()
            {
                // Отправка сообщения
                Log.e("TEST", "SENDING THREAD IS RUNNING");
                try
                {
                    socket.send(send_packet);
                    g.Dialogs.OnSaveDialogs(m);
                    String text = tv.getText().toString();

                    runOnUiThread(() -> {
                        tv.setText(text);
                    });
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        };
        // создание потока
        Thread t = new Thread(r);
        t.start();
    }
}

