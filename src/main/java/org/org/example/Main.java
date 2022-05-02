package org.example;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

/***
 * Используя варианты заданий, представленные ниже, разработать программу, которая позволяла бы работать с тремя файлами.
 * Исходная информация должна хранить в файле file1.txt(программа ее считывает и осуществляет с ней преобразования),
 * затем преобразованная информация записывается в файл file2.txt,
 * а в файл file3.txt программа должна записать исходную и преобразованную информацию.
 *
 * Написать программу для создания прямоугольного массива, содержащего таблицу умножения от 1×1 до 12×12.
 * Вывести таблицу как 13 столбцов с числовыми значениями, выровненными справа в столбцах (рис. 1.3).
 * Первая строка вывода – это заголовки столбцов без заголовка для первого столбца, затем числа от 1 до 12 для остальных
 * столбцов. Первый элемент изменяющимся от 1 до 12.
 */
public class Main {
    private static final String DATA_FILE = "file1.txt";
    private static final String RESULT_FILE = "file2.txt";
    private static final String REPORT_FILE = "file3.txt";

    /***
     * Функция для чтения файла с данными.
     * Читаем файл file1.txt и преобразует даныне файла в массив чисел типа Integer
     * Исходя из условия задачи, подразумеваем, что числа разделены пробелом
     * @return - массив чистел типа Integer
     */
    private static int[] readDataFile() {
        //создаем объект FileInputStream
        FileInputStream fis = null;
        int[] dataArray = new int[0];
        try {
            //открываем файл для чтения
            File file = new File(DATA_FILE);
            //создаем массив байтов из содержимого массива
            byte[] bytes = new byte[(int) file.length()];
            //инициализируем FileInputStream
            fis = new FileInputStream(file);
            //читаем содержимое файла в FileInputStream
            fis.read(bytes);
            //cоздаем массив строк из содержимого файла, зная что чиста разделены пробелом
            String[] valueStr = new String(bytes).trim().split("\\s+");
            //создаем массив integer
            dataArray = new int[valueStr.length];
            //преобразуем массив строк в массив чисел
            for (int i = 0; i < valueStr.length; i++) {
                dataArray[i] = getInteger(valueStr[i]);
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    System.out.println("Failed to close the stream: " + e.getMessage());
                }
            }
        }
        return dataArray;
    }

    /***
     * Метод для преобразования строки в число типа Integer
     * @param stringValue - строковое значение для преобразования
     * @return - число типа Integer
     */
    private static Integer getInteger(final String stringValue) {
        try {
            return Integer.parseInt(stringValue);
        } catch (NumberFormatException e) {
            System.out.println("Failed to transform value to Integer: " + stringValue);
        }
        return null;
    }

    /***
     * Функция для создания файла file3.txt с результатом работы где записана исходная и преобразованная информация
     * Функция читаем файлы file1.txt и file2.txt, добавляем комментации и сохраняет содержимое обеих файлов в file3.txt
     */
    private static void generateReportFile() {
        //откроем DATA_FILE для чтения
        RandomAccessFile reportFile = null;
        try {
            //открываем файл для записи
            reportFile = new RandomAccessFile(new File(REPORT_FILE), "rw");
            //очищаем файл
            reportFile.setLength(0);
            //записываем содержимое DATA_FILE
            reportFile.writeUTF("Исходные данные:\n");
            reportFile.writeBytes(getFileData(DATA_FILE));
            reportFile.writeBytes("\n\n");

            //записываем содержимое RESULT_FILE
            reportFile.writeUTF("Результат работы:\n");
            reportFile.writeBytes(getFileData(RESULT_FILE));

        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        } catch (IOException e) {
            System.out.println("Failed to write to the file");
        } finally {
            try {
                if (reportFile != null) {
                    reportFile.close();
                }
            } catch (IOException e) {
                System.out.println("Failed to close the file");
            }
        }
    }

    /***
     * Функция принимает имя файла и возвращает содержимое как строку
     * @param fileName - имя файла
     * @return - содержимое файла как строка
     */
    private static String getFileData(final String fileName) {
        Path filePath = Path.of(String.format("./%s", fileName));
        String content = "";
        try {
            content = Files.readString(filePath);
        } catch (IOException e) {
            System.out.println("Failed to open file " + fileName);
        }
        return content;
    }

    /***
     * Функкция создает строку заданной длины из переданного числа
     * @param integer - число из которого необходимо создать строку
     * @return - строка длинной 3 символа
     */
    private static String fixedLengthString(int integer) {
        StringBuilder ss = new StringBuilder(String.valueOf(integer));
        while (ss.length() < 3) {
            ss.insert(0, ' ');
        }
        ss.append(" ");
        return ss.toString();
    }

    /***
     * Функция приимает массив чисел и записывает в файл file2.txt таблицу умножения
     * @param integers - массив чисел
     */
    private static void generateMultiplicationTable(final int[] integers) {
        RandomAccessFile fio = null;
        try {
            //открываем файл для записи
            fio = new RandomAccessFile(new File(RESULT_FILE), "rw");
            fio.setLength(0);
            //первая строка - заголовок из 13ти столбцов
            fio.writeBytes("    ");

            for (int i = 0; i < integers.length; i++) {
                String x = fixedLengthString(integers[i]);
                fio.writeBytes(x);
            }
            fio.writeBytes("\n");

            for (int i = 0; i < integers.length; i++) {
                //первый столбец - заголовок
                fio.writeBytes(fixedLengthString(integers[i]));

                for (int j = 0; j < integers.length; j++) {
                    int x = integers[i] * integers[j];
                    fio.writeBytes(fixedLengthString(x));
                }
                fio.writeBytes("\n");
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        } catch (IOException e) {
            System.out.println("Failed to write to the file");
        } finally {
            try {
                if (fio != null) {
                    fio.close();
                }
            } catch (IOException e) {
                System.out.println("Failed to close the file");
            }
        }
    }

    public static void main(String[] args) {
        //читем файл с данныеми file1.txt и сохраняем в массив
        int[] integers = readDataFile();

        //cоздаем таблицу умножения и сохраняем в файл file2.txt
        generateMultiplicationTable(integers);

        //создаем file3.txt с исходной и преобразованной информацией
        generateReportFile();
    }
}