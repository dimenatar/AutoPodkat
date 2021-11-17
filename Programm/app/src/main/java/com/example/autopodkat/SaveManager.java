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
    public static void SaveOrder (List<Order> listOrder, Context context)
    {
        FileOutputStream fileOutputStream = null;
        try
        {
            ContextWrapper cw = new ContextWrapper(context);
            File directory = cw.getDir("saveDirectory", Context.MODE_PRIVATE);
            File file = new File(directory, "orderSave.txt");
            fileOutputStream = new FileOutputStream( file);
            Log.e("file","created");
        } catch (FileNotFoundException e) {
            Log.e("file not found", e.getMessage());
        }
        ObjectOutputStream objectOutputStream = null;
        try {
            objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(listOrder);
            objectOutputStream.flush();
            objectOutputStream.close();
        } catch (IOException e) {
            Log.e("io", e.getMessage());
        }
        Log.e("file","success");
    }
    public static List<Car> LoadCars(Context context)
    {
        ContextWrapper cw = new ContextWrapper(context);
        File directory = cw.getDir("saveDirectory", Context.MODE_PRIVATE);
        File file = new File(directory, "carSave.txt");
        List<Car> listCar = null;
        FileInputStream fileInputStream = null;
        try
        {
            fileInputStream = new FileInputStream(file);
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
            Log.e("load err", e.getMessage());
            return null;
        }
        try
        {
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            listCar = (List<Car>) objectInputStream.readObject();
            Log.e("ex", listCar.toString());
            objectInputStream.close();
        }
        catch (ClassNotFoundException | IOException e)
        {

        }
        return listCar;
    }
    public static List<Order> LoadOrders(Context context)
    {
        ContextWrapper cw = new ContextWrapper(context);
        File directory = cw.getDir("saveDirectory", Context.MODE_PRIVATE);
        File file = new File(directory, "orderSave.txt");
        List<Order> listOrder = null;
        FileInputStream fileInputStream = null;
        try
        {
            fileInputStream = new FileInputStream(file);
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
            Log.e("load err", e.getMessage());
            return null;
        }
        try
        {
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            listOrder = (List<Order>) objectInputStream.readObject();
            Log.e("ex", listOrder.toString());
            objectInputStream.close();
        }
        catch (ClassNotFoundException | IOException e)
        {
            Log.e("load err", e.getMessage());
        }
        return listOrder;
    }
    public static void SaveAcc(User user, Context context)
    {
        FileOutputStream fileOutputStream = null;
        try
        {
            ContextWrapper cw = new ContextWrapper(context);
            File directory = cw.getDir("saveDirectory", Context.MODE_PRIVATE);
            File file = new File(directory, "userSave.txt");
            fileOutputStream = new FileOutputStream( file);
            Log.e("file","created");
        } catch (FileNotFoundException e) {
            Log.e("file not found", e.getMessage());
        }
        ObjectOutputStream objectOutputStream = null;
        try {
            objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(user);
            objectOutputStream.flush();
            objectOutputStream.close();
        } catch (IOException e) {
            Log.e("io", e.getMessage());
        }
        Log.e("file","success");
    }
    public static User LoadUser(Context context)
    {
        ContextWrapper cw = new ContextWrapper(context);
        File directory = cw.getDir("saveDirectory", Context.MODE_PRIVATE);
        File file = new File(directory, "userSave.txt");
        User user = null;
        FileInputStream fileInputStream = null;
        try
        {
            fileInputStream = new FileInputStream(file);
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
            Log.e("load err", e.getMessage());
            return null;
        }
        try
        {
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            user = (User) objectInputStream.readObject();
            objectInputStream.close();
        }
        catch (ClassNotFoundException | IOException e)
        {
            Log.e("load err", e.getMessage());
        }
        return user;
    }
    public static void EraseUser(Context context)
    {
        FileOutputStream fileOutputStream = null;
        try {
            ContextWrapper cw = new ContextWrapper(context);
            File directory = cw.getDir("saveDirectory", Context.MODE_PRIVATE);
            File file = new File(directory, "userSave.txt");
            file.delete();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
