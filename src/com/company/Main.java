package com.company;

import java.lang.reflect.Field;
import java.util.*;
import java.io.*;

public class Main {

    public static void main(String[] args) {

        Scanner in = new Scanner(System.in);                                    //Сканер консоли, ввод

        while (true) {                                                          //Бесконечный цикл работы программы

            File FileInput;                                                     //Файл
            String InputString;                                                 //Строка которую надо найти

            do {                                                                //Ввод имени файла
                try {
                    System.out.print("Enter the file name: " + new java.io.File("").getAbsolutePath());
                    String input = "\\" + in.next();                            //Считывает ввод из консоли
                    FileInput = new File(new java.io.File("").getAbsolutePath() + input);
                } catch (Throwable e) {
                    System.out.println("Error name file long: " + e);
                    FileInput = new File("bad.txt");
                }
            } while (!Exists(FileInput));                                       //Проверка на существование файла

            do {
                try {
                    System.out.print("Enter the line you want to find: ");
                    InputString = in.next();                                    //Считывает ввод из консоли
                } catch (Throwable e) {                                         //Слишком длинная строка
                    System.out.println("Error string long: " + e);
                    InputString = "";
                }
            } while (InputString.isEmpty());                                    //Строка будет пустой если так ввел пользователь или было исключение

            String InputArea;

            do {
                System.out.print("Enter the total amount for previous/next sentences : ");
                InputArea = in.next();                                          //Считывает ввод из консоли
            } while (!isNumeric(InputArea));                                    //Самописная функция проверки на число

            int Area = Integer.parseInt(InputArea);                             //Сколько строк еще нужно записать

            try (BufferedWriter writer =                                        //Писатель для файла
                         new BufferedWriter(
                                 new OutputStreamWriter(
                                         new FileOutputStream(
                                                 new File("Out.txt")))))
            {

                try (BufferedReader reader = new BufferedReader(                //Читатель для входного файла
                            new FileReader(FileInput)))
                {
                    int Const = 50;

                    String ParagraphThread = "", ParagraphLine;
                    int increment = 0;

                    do{
                        ParagraphLine = reader.readLine();
                        ParagraphThread += ParagraphLine;
                        if(++increment == Const || ParagraphLine == null){

                            ThreadWriterClass ThreadN = new ThreadWriterClass(writer, ParagraphThread, InputString, Area);
                            Thread ThreadWriter = new Thread(ThreadN);
                            ThreadWriter.run();
                            ParagraphThread = "";
                            increment = 0;
                        }
                    }while(ParagraphLine != null);

                } catch (FileNotFoundException e) {
                    System.out.println("Error reader_non_file: " + e);
                }

            } catch (IOException e) { }
        }
    }

    static boolean Exists(File file) {                                          //Проверка существования файла
        if (file.exists())
            return (true);
        else
            System.out.println("##Error: file not found. Please try again.##");
        return (false);
    }

    static boolean isNumeric(String string) {                                   //Проверка на число
        try {
            Integer.parseInt(string);                                           //Если удалось перевести строку в число то вернется true
            return (true);
        } catch (NumberFormatException e) {                                     //Если при переводе произошло исключение, то вернется false
            System.out.println(e);
        }
        return (false);
    }

    static void Output_write(BufferedWriter writer, String request) {
        try {

            writer.write(request);
            writer.newLine();
            writer.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static class ThreadWriterClass
            implements Runnable {

        private BufferedWriter writer;
        private String request;
        private String Find;
        private int Area;

        public ThreadWriterClass(BufferedWriter Write, String Requ, String FindInput, int area){
            writer = Write;
            request = Requ;
            Find = FindInput;
            Area = area+1;
        }

        public void run() {
            String Scence = "";
            String[] AreaScence = new String[Area];
            int AreaRemember = 0;

            for(int i = 0; i < request.length(); i++){
                char element =  request.charAt(i);
                Scence += element;

                if(element == '.' || element == '?' || element == '!'){
                    AreaScence = BackAndDelete(AreaScence, Scence);                 //запоминаем строки которые были перед

                    if(Scence.contains(Find)) {
                        String request = "";
                        for(int j = 0; j < Area; j++)
                            request += AreaScence[j];

                        Output_write(writer, request);
                        AreaRemember = Area;
                    }

                    if (AreaRemember > 0 && AreaRemember-- != Area) {
                        Output_write(writer, Scence);
                    }

                    Scence = "";
                }
            }
        }

        public String[] BackAndDelete(String[] Scences,String Secence){
            for(int i = 0; i < Area-1; i++){
                Scences[i] = Scences[i+1];
            }
            Scences[Area-1] = Secence;

            return Scences;
        }

    }

}
