package vtp2022.sdfassessment;

import java.io.*;
import java.net.Socket;

public class Client {
    public static void main(String[] args) {

        Socket sock = null;
        try {
            sock = new Socket("task02.chuklee.com", 80);

            OutputStream os = sock.getOutputStream();
            InputStream is = sock.getInputStream();

            ObjectOutputStream objos = new ObjectOutputStream(os);
            ObjectInputStream objin = new ObjectInputStream(is);

            // use readUTF() to read initial request
            String request = objin.readUTF();
            String id = request.split(" ")[0];
            String[] fStrings = request.split(" ")[1].split(",");
            float[] floats = new float[fStrings.length];

            for (int i = 0; i < fStrings.length; i++) {
                floats[i] = Float.parseFloat(fStrings[i]);
            }

            // calculate average for list of integers
            float sum=0;
            for (int i = 0; i < floats.length; i++) {
                sum += floats[i];
            }
            float avg = sum/floats.length;
            System.out.printf("sum=%f\navg=%f\n", sum, avg);


            // write answer back to server
            objos.writeUTF(id);
            objos.writeUTF("CHENG YING QING");
            objos.writeUTF("yingqing.c@hotmail.com");
            objos.writeFloat(avg);

            // read the server response
            // read response from server with readBoolean() method
            boolean response = objin.readBoolean();
            
            if (response) {
                System.out.println("SUCCESS");
            } else {
                System.out.println("FAILED");
                String error = objin.readUTF();
                System.out.println(error);
                sock.close();
            }
            
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
        }


    }
}