package com.mzvzm.algorithm.ip;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @Author lilongsha
 * @Description 热点IP
 * @Date 2022/5/7 09:12
 */
public class MainApplication {
    public static final String FILE_PATH = "src/main/resources/ip";
    /**
     * 热点IP
     */
    public static ConcurrentHashMap<String, Long> IP_NUM = new ConcurrentHashMap<>();
    public static ArrayList<String> TOP_IP = new ArrayList<>();
    public static ArrayList<Long> NUM_ORDER = new ArrayList<>();
    public static final int GENERATOR_FILE_SIZE = 100;
    public static final int GENERATOR_ROW_SIZE = 100000;
    public static final Long DEFAULT = 1L;
    public static final int TOP_SIZE = 100;
    public static long MAX_NUM = -1;
    public static AtomicLong findNum = new AtomicLong(0);
    public static final String RESULT_FILE_NAME = "001.txt";

    public static void main(String[] args) throws IOException {
//        generatorIp();
        long start = System.currentTimeMillis();
        for (int i = 0; i < GENERATOR_FILE_SIZE; i++) {
            ArrayList<String> ips = readFile(String.format("ip%s.txt", i));
            ips.forEach(ip -> {
                if (IP_NUM.containsKey(ip)) {
                    Long num = IP_NUM.get(ip);
                    IP_NUM.put(ip, ++num);
                } else {
                    IP_NUM.put(ip, DEFAULT);
                }
            });
        }
        getMaxValue();
//        getTop(GENERATOR_FILE_SIZE * GENERATOR_ROW_SIZE);
        getTop(MAX_NUM);
//        getTop();
        printResult();
        System.out.printf("耗时：%d%n", System.currentTimeMillis() - start);
    }

    public static void getMaxValue() {
        IP_NUM.forEach((key, value) -> {
            if (value > MAX_NUM) {
                MAX_NUM = value;
            }
        });
    }

    public static void getTop() {
        IP_NUM.forEach((key, value) -> {
            NUM_ORDER.add(value);
        });
        NUM_ORDER.sort((a1, a2) -> {
            if ((a1 - a2 < 0)) {
                return 1;
            } else {
                return 0;
            }
        });
        for (int i = 0; i < TOP_SIZE; i++) {
            long finalI = NUM_ORDER.get(i);
            IP_NUM.forEach((key, value) -> {
                if (Objects.equals(value, finalI) && findNum.addAndGet(1) <= TOP_SIZE) {
                    TOP_IP.add(key);
                    TOP_IP.add(String.valueOf(value));
                    System.out.printf("%s::%d::%d%n", key, value, finalI);
                }
            });
        }
    }

    public static void getTop(long i) {
        for (; i > 0; i--) {
            Long finalI = i;
            IP_NUM.forEach((key, value) -> {
                if (Objects.equals(value, finalI) && !TOP_IP.contains(key) && findNum.addAndGet(1) <= TOP_SIZE) {
                    TOP_IP.add(key);
                    TOP_IP.add(String.valueOf(value));
                    System.out.printf("%s::%d::%d%n", key, value, finalI);
                }
            });
        }
    }

    public static void printResult() throws IOException {
        File file = new File(FILE_PATH, RESULT_FILE_NAME);
        FileOutputStream fos = new FileOutputStream(file);
        for (int i = 0; i < TOP_IP.size(); i = i + 2) {
            fos.write(String.format("IP::%s  ", TOP_IP.get(i)).getBytes(StandardCharsets.UTF_8));
            fos.write(String.format("NUM::%s\n", TOP_IP.get(i + 1)).getBytes(StandardCharsets.UTF_8));
            fos.flush();
        }
        fos.close();
    }

    public static ArrayList<String> readFile(String fileName) throws IOException {
        ArrayList<String> ips = new ArrayList<>();
        File file = new File(FILE_PATH, fileName);
        FileInputStream fis = new FileInputStream(file);
        InputStreamReader reader = new InputStreamReader(fis, StandardCharsets.UTF_8);
        BufferedReader br = new BufferedReader(reader);
        String ip = "";
        while ((ip = br.readLine()) != null) {
            ips.add(ip);
        }
        return ips;
    }

    public static void generatorIp() throws IOException {
        Random random = new Random();
        for (int i = 0; i < GENERATOR_FILE_SIZE; i++) {
            File file = new File(FILE_PATH, String.format("ip%s.txt", i));
            if (!file.exists()) {
                file.createNewFile();
            }
            if (file.exists()) {
                FileOutputStream fos = new FileOutputStream(file);
                for (int j = 0; j < GENERATOR_ROW_SIZE; j++) {
                    fos.write(String.format(String.format("192.168.%d.%d\n", random.nextInt(255) + 1, random.nextInt(255) + 1)).getBytes(StandardCharsets.UTF_8));
                }
                fos.flush();
                fos.close();
            }
        }
    }
}