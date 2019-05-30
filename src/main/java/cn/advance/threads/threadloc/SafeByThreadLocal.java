package cn.advance.threads.threadloc;

import org.apache.commons.lang3.RandomStringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author zhoueq
 */
public class SafeByThreadLocal {

    public static void main(String[] args) {
        Resource resource = new Resource();

        ExecutorService service = Executors.newFixedThreadPool(5);

        for (int i = 0; i < 10; i++) {
            service.execute(() -> {
                List<String> data = new ArrayList<>();
                String f = RandomStringUtils.randomAscii(5);
                String s = RandomStringUtils.randomAscii(5);
                data.add(f);
                data.add(s);

                String p = Thread.currentThread().getId() + "\t 输入数据" + data + "\t";

                resource.add(data);

                try {
                    TimeUnit.SECONDS.sleep(3L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                List<String> result = resource.get();

                p += Thread.currentThread().getId() + "\t 获取数据" + result + "\t";
                p += "\t" + (data == result);
                System.out.println(p);
            });
        }

        service.shutdown();
    }

}

class Resource {
    static ThreadLocal<List<String>> threadLocal = new ThreadLocal<>();

    void add(List<String> data) {
        threadLocal.set(data);
    }

    List<String> get() {
        return threadLocal.get();
    }

    void release() {
        threadLocal.remove();
    }
}