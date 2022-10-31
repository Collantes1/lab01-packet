package com.example.lab01;

import java.nio.charset.StandardCharsets;

public class msg {
    public String IP;
    public String Port;
    public String Name;
    public String Text;

    public byte[] msqB()
    {
        byte[] buf = (this.IP + "%" + this.Port + "%" + this.Name + "%" + this.Text).getBytes(StandardCharsets.UTF_8);
        return buf;
    }
    public String toString()
    {
        return "IP: " + this.IP + ":" + this.Port + "\n" + "Sender: " + this.Name + "\n" + "Message: " + this.Text + "\n" + "****************************************";
    }
}