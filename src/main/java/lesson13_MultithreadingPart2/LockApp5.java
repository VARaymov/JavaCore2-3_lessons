package lesson13_MultithreadingPart2;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class LockApp5 {

    public static void main(String[] args) {

        /*
        Есть такой интерфейс. Reentrancy - термин, означающий повторную входимость.
        То есть если вы зашли к врачу в кабинет, по сути захватили Lock врача, потом вышли, забрали у друга свой полис, который вы забыли и вернулись обратно. В этот момент времени вам не нужно заново брать Lock, потому что Lock уже захвачен вами. И Reentrancy это повторная входимость. Если поток вызывает Lock первый раз и вызывает Lock второй раз где то внутри (тот же самый), он может его вызвать и получить. Это не монитор, который один раз покраснел красным и вы его не можете забрать. Если вы вызываете lock опять, lock понимает что это тот же поток, он его знает и повторно его захватывает.
        Это одна из реализаций интерфейса Lock.
        Все тоже самое, только мы не Object object создаем, а создаем объект lock-а.
        Чем лучше, чем обычный синхронизованный метод. Во первых здесь можно использовать тайм ауты (tryLock() - вернет true или false, которы не блокирующий.
        tryLock() - пытается захватить lock, и вернет false - если не смог.
        И еще один плюс. Мы можем захватить lock в одном потоке, а отпустить в другом. В случае синхронизированных блоков мы такое делать не можем.
        Например у нас в чате на старте сервера есть старт и стоп. В старте мы можем захватить какую нибудь блокировку, а в стопе ее отпустить.
         */
        Lock lock = new ReentrantLock();

        new Thread(() -> {
            try {
                // критическая секция.
                lock.lock();
//                lock.tryLock(10, TimeUnit.SECONDS);// попытайся захватить lock 10 секунд, если через 10 секунд не сможет - то вернет false.
            } finally {
                lock.unlock();// отпустили lock
            }
        }).start();

        // Вот этот код вобще ничем не отличается от синхронизированного блока. За исключеним того, что мы имеем возможность работать с тайм аутом.
//     try {
//        // критическая секция.
//        lock.lock();
////                lock.tryLock(10, TimeUnit.SECONDS);// попытайся захватить lock 10 секунд, если через 10 секунд не сможет - то вернет false.
//    } finally {
//        lock.unlock();// отпустили lock
//    }


        Lock lock2 = new ReentrantLock();

        new Thread(() -> {
            try {
                lock.lock();
//                /*
//    Вопросы собеса.
//    Как бороться с дедлоком?
//    Можно использовать lock с таймаутом и сказать например:
//     */
                try {
                    if (!lock.tryLock(10, TimeUnit.SECONDS)) {
                        System.out.println("Не удалось захватить блокировку");
                    }
                    // В таком случае кто первый не дождется, разорвет наш порочный круг и дедлока больше не будет. Один из потоков свалится в ошибку.
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } finally {
                lock.unlock();// отпустили lock
            }
        }).start();


        /*
        Еще одна реализация. Чем отличается?
        Если обычный lock блокирует объект наглухо, то readWriteLock работает чуть иначе. И для того чтобы получить lock, мы можем вызвать readLock() - лок на чтение или writeLock() - лок на запись.

         */
        ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
        readWriteLock.writeLock();// блокируемся на запись
        readWriteLock.readLock();// блокируемся не чтение
        // Пока хотя бы один поток пишет - читать нельзя никому. И lock на запись нельзя захватить, пока кто то читает.



        /*
        В домашке. Все классы которые сегодня изучали нужно сделать. Использовать придется все.
        Там гонка и одно из заданией в ней - определение победителя. Потому что каждый считает победителем себя. Это не так. Здеась нужно подумать как можно определить ровно одного победителя. Он должен на финише палочку схватить или флаг поднять или что то еще.
         */

    }
}