package org.example;

public class runnable {

    public static void main(String[] args) {
//        buffer buffer = new buffer(10);
//        whertthron whertthron = new whertthron(buffer);
//        reader reader = new reader(buffer);
//        whertthron.run();
//        reader.run();

    }


    public static class buffer {

        private final int[] data;
        private int index;

        public buffer(int size) {
            this.data = new int[size];
            index = 0;
        }

        public void write(int value) {
            data[index++] = value;

        }

        public int rede() {
            return data[--index];
        }
    }
    public static class whertthron extends Thread {
        private final buffer bf;
        public whertthron(buffer bf) {
            this.bf = bf;
        }
        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                bf.write(i);
                System.out.println("插入"+i);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public static class reader extends Thread {
        private final buffer bf;

        public reader(buffer bf) {
            this.bf = bf;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                int rede = bf.rede();
                System.out.println(rede);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }


}
