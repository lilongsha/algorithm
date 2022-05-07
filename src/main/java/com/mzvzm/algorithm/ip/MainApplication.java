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
    public static volatile ConcurrentHashMap<String, Long> TOP_IP = new ConcurrentHashMap<>();
    public static final Long DEFAULT = 1L;
    public static final int TOP_SIZE = 10;
    public static AtomicLong findNum = new AtomicLong(0);
    public static final String RESULT_FILE_NAME = "001.txt";

    public static void main(String[] args) throws IOException {
//        generatorIp();
        long start = System.currentTimeMillis();
        for (int i = 0; i < 100; i++) {
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

//        getTop();
        getTop(1L, (long) (100 * 1000));
        printResult();
        System.out.printf("耗时：%d%n", System.currentTimeMillis() - start);
//        test();
    }

    public static void test() {
        IP_NUM.forEach((key, value) -> {
            if (value > 61) {
                System.out.printf("%s::%d%n", key, value);
            }
        });
    }

    public static void getTop(Long l, Long r) {
        if (findNum.get() == TOP_SIZE || l >= r) {
            return;
        }
        long mid = (l + r) / 2;
        getTop(mid + 1, r);
        getTop(l, mid);
        if (!IP_NUM.containsValue(r)) {
            r = r - 1;
            return;
        }
        if (findNum.get() <= TOP_SIZE && IP_NUM.containsValue(r)) {
            Long finalR = r;
            IP_NUM.forEach((key, value) -> {
                if (Objects.equals(value, finalR) && !TOP_IP.containsKey(key) && findNum.addAndGet(1) <= TOP_SIZE) {
                    TOP_IP.put(key, value);
                }
            });
        }
    }

    public static void getTop() {
        for (int i = 100 * 1000; i > 0; i--) {
                Long finalI = (long) i;
                IP_NUM.forEach((key, value) -> {
                    if (Objects.equals(value, finalI) && !TOP_IP.containsKey(key) && findNum.addAndGet(1) <= TOP_SIZE) {
                        TOP_IP.put(key, value);
                        System.out.printf("%s::%d::%d%n", key, value, finalI);
                    }
                });
        }
    }

    public static void printResult() throws IOException {
        File file = new File(FILE_PATH, RESULT_FILE_NAME);
        FileOutputStream fos = new FileOutputStream(file);
        TOP_IP.forEach((key, num) -> {
            try {
                fos.write(String.format("%s::%d\n", key, num).getBytes(StandardCharsets.UTF_8));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        fos.flush();
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
        for (int i = 0; i < 100; i++) {
            File file = new File(FILE_PATH, String.format("ip%s.txt", i));
            if (!file.exists()) {
                file.createNewFile();
            }
            if (file.exists()) {
                FileOutputStream fos = new FileOutputStream(file);
                for (int j = 0; j < 1000; j++) {
                    switch (j % 10) {
                        case 0:
                            fos.write(String.format(String.format("192.168.0.%d\n", random.nextInt(255) + 1)).getBytes(StandardCharsets.UTF_8));
                            break;
                        case 1:
                            fos.write(String.format(String.format("192.168.1.%d\n", random.nextInt(255) + 1)).getBytes(StandardCharsets.UTF_8));
                            break;
                        case 2:
                            fos.write(String.format(String.format("192.168.2.%d\n", random.nextInt(255) + 1)).getBytes(StandardCharsets.UTF_8));
                            break;
                        case 3:
                            fos.write(String.format(String.format("192.168.3.%d\n", random.nextInt(255) + 1)).getBytes(StandardCharsets.UTF_8));
                            break;
                        case 4:
                            fos.write(String.format(String.format("192.168.4.%d\n", random.nextInt(255) + 1)).getBytes(StandardCharsets.UTF_8));
                            break;
                        case 5:
                            fos.write(String.format(String.format("192.168.5.%d\n", random.nextInt(255) + 1)).getBytes(StandardCharsets.UTF_8));
                            break;
                        case 6:
                            fos.write(String.format(String.format("192.168.6.%d\n", random.nextInt(255) + 1)).getBytes(StandardCharsets.UTF_8));
                            break;
                        case 7:
                            fos.write(String.format(String.format("192.168.7.%d\n", random.nextInt(255) + 1)).getBytes(StandardCharsets.UTF_8));
                            break;
                        case 8:
                            fos.write(String.format(String.format("192.168.8.%d\n", random.nextInt(255) + 1)).getBytes(StandardCharsets.UTF_8));
                            break;
                        default:
                            fos.write(String.format(String.format("192.168.9.%d\n", random.nextInt(255) + 1)).getBytes(StandardCharsets.UTF_8));
                            break;
                    }
                }
                fos.flush();
                fos.close();
            }
        }
    }
}