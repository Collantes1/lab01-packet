package com.example.lab01; // Zaharov 493

import java.nio.charset.StandardCharsets;

public class msg {
    public String IP;
    public String Name;
    public String Port;
    public String Text;

    public byte[] Messages()
    {
        byte[] buffer = (this.IP + "%" + this.Port + "%" + this.Name + "%" + this.Text).getBytes(StandardCharsets.UTF_8);
        return buffer;
    }
    public String toString()
    {
        return this.Name + ": " + this.Text + "\n" + "IP: "+ this.IP + ":"+this.Port;
    }
}
