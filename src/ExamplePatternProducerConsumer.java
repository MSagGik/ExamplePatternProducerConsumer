import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class ExamplePatternProducerConsumer {

    // потокобезопасная очередь для множества потоков (певый зашёл - первый вышел)
    private static BlockingQueue<Integer> queue = new ArrayBlockingQueue<>(10); // предельный размер очереди 10

    public static void main(String[] args) throws InterruptedException {
        // создание двух потоков: производителя и потребителя
        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    produce();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    consumer();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        // запуск потоков
        thread1.start();
        thread2.start();
        // ожидание выполнения потоков
        thread1.join();
        thread2.join();
    }

    // производитель (кладёт элементы в ArrayBlockingQueue)
    private static void produce() throws InterruptedException {
        Random random = new Random();

        while (true) {
            // помещение случайного числа в очередь queue
            queue.put(random.nextInt(100)); // если очередь заполненна, то put() подождёт пока очередь не освободится
            // здесь 100 это ограничение рандобных чисел [0,99]
        }
    }
    // потребитель (берёт элементы из ArrayBlockingQueue)
    private static void consumer() throws InterruptedException {
        Random random = new Random();
        while (true) {
            // взятие элементов из queue (в каждые 100 мс)
            Thread.sleep(100);
            // задание избирательности потребителя
            if(random.nextInt(10) == 1) { // потребитель берёт только в случае выпадения числа 1
                System.out.println(queue.take()); // если в ArrayBlockingQueue нет элементов,
                // то метод take() ждёт пока элементы будут добавлены
                System.out.println("Queue size is " + queue.size());
            }
        }
    }
}
