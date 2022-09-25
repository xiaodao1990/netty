package com.jenkin.netty.nio.performance;

import io.netty.util.Recycler;

/**
 * Created by someone on 2022/9/15 11:23.
 */
public class RecyclerDemo {

    private static final Recycler<User> RECYCLER = new Recycler<User>() {
        @Override
        protected User newObject(Handle<User> handle) {
            return new User(handle);
        }
    };

    static class User {

        private final Recycler.Handle<User> handle;

        public User(Recycler.Handle<User> handle) {
            this.handle = handle;
        }

        public void recycle() {
            handle.recycle(this);
        }
    }

    public static void main(String[] args) {
        User user1 = RECYCLER.get();
        final User[] user2 = {null};
        new Thread(new Runnable() {
            @Override
            public void run() {
                user2[0] = RECYCLER.get();
            }
        }, "Thread-2").start();
        while (user2[0] == null) {
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        if (user1 != null && user2[0] != null) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    user1.recycle();
                    user2[0].recycle();
                }
            }, "Thread-3").start();
        }
    }
}
