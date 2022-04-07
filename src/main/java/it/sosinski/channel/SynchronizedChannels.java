package it.sosinski.channel;

import java.util.Collection;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class SynchronizedChannels implements Channels {

    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    private final Channels channels;

    public SynchronizedChannels(Channels channels) {
        this.channels = channels;
    }

    @Override
    public void add(Channel channel) {
        lock.writeLock().lock();
        channels.add(channel);
        lock.writeLock().unlock();
    }

    @Override
    public boolean exists(String name) {
        lock.readLock().lock();
        boolean exists = channels.exists(name);
        lock.readLock().unlock();
        return exists;
    }

    @Override
    public Channel get(String name) {
        lock.readLock().lock();
        Channel channel = channels.get(name);
        lock.readLock().unlock();
        return channel;
    }

    @Override
    public int getSize() {
        lock.readLock().lock();
        int size = channels.getSize();
        lock.readLock().unlock();
        return size;
    }

    @Override
    public Collection<Channel> getChannels() {
        lock.readLock().lock();
        Collection<Channel> channels = this.channels.getChannels();
        lock.readLock().unlock();
        return channels;
    }
}
