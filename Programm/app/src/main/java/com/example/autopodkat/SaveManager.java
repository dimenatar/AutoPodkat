package com.example.autopodkat;

import android.content.Context;
import android.content.ContextWrapper;
import android.os.Environment;
import android.util.Log;

import java.io.*;
import java.util.List;

public class SaveManager implements Serializable
{
    public static void SaveCars (List<Car> listCar, Context context)
    {
                FileOutputStream fileOutputStream = null;
                try
                {
                    ContextWrapper cw = new ContextWrapper(context);
                    File directory = cw.getDir("saveDirectory", Context.MODE_PRIVATE);
                    File file = new File(directory, "carSave.txt");
                    fileOutputStream = new FileOutputStream( file);
                    Log.e("file","created");
                } catch (FileNotFoundException e) {
                    Log.e("file not found", e.getMessage());
                }
                ObjectOutputStream objectOutputStream = null;
                try {
                    objectOutputStream = new ObjectOutputStream(fileOutputStream);
                    objectOutputStream.writeObject(listCar);
                    objectOutputStream.flush();
                    objectOutputStream.close();
                } catch (IOException e) {
                    Log.e("io", e.getMessage());
                }
                Log.e("file","success");
    }
    public static List<Car> LoadCars()
    {

        List<Car> listCar = null;
        FileInputStream fileInputStream = null;
        try
        {
            fileInputStream = new FileInputStream("carSave.txt");
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
            return null;
        }

        try
        {
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            listCar = (List<Car>) objectInputStream.readObject();
            objectInputStream.close();
        }
        catch (ClassNotFoundException | IOException e)
        {
            e.printStackTrace();
        }
        return listCar;
    }
}
