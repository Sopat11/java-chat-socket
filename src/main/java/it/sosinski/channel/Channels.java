package it.sosinski.channel;

import java.util.Collection;

public interface Channels {

    void add(Channel channel);

    boolean exists(String name);

    Channel get(String name);

    int getSize();

    Collection<Channel> getChannels();
}
