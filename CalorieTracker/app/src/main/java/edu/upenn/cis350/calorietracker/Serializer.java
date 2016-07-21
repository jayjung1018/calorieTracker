package edu.upenn.cis350.calorietracker;

import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;

/**
 * Created by Jay Jung on 4/20/2016.
 * Class to serialize different objects to put into the database
 */
public class Serializer  {

    Serializer(){}

    public static byte[] serializeObject(Object o) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        try {
            ObjectOutput out = new ObjectOutputStream(bos);
            out.writeObject(o);
            out.close();

            byte[] serialized = bos.toByteArray();
            return serialized;
        } catch (IOException e) {
            Log.e("serializeObject", "error", e);

            return null;
        }
    }

    public static Object deserializeObject(byte[] b) {
        try {
            ObjectInput in = new ObjectInputStream(new ByteArrayInputStream(b));
            Object object = in.readObject();
            in.close();

            return object;
        } catch (IOException e) {
            Log.e("deserializeObject", "IOerror", e);
            return null;
        } catch (ClassNotFoundException e) {
            Log.e("deserializeObject", "classerror", e);
            return null;
        }
    }
}
