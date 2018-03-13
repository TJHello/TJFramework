package com.tjbaobao.test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Test {

    private static final List<String> list = Collections.synchronizedList(new ArrayList<>());

    public static void main(String[] args)
    {
        for(int i=0;i<10;i++)
        {
            list.add(""+i);
        }
        new Test();
    }

    public Test()
    {
        Thread thread = new Thread(new AddRunnable());
        thread.start();
        Thread threadPrint = new Thread(new PrintRunnable());
        threadPrint.start();
    }

    private class AddRunnable implements Runnable
    {
        int num = 0;
        @Override
        public void run() {
            while (true)
            {
                for(int i=0;i<10;i++)
                {
                    list.add(""+(num++));
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
    private class RemoveRunnable implements Runnable
    {

        @Override
        public void run() {

        }
    }
    private class PrintRunnable implements Runnable
    {

        @Override
        public void run() {
            while (true)
            {
                synchronized (list)
                {
                    for(String str:list)
                    {
                        System.out.println(str);
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

}
