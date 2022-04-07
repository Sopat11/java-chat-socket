package it.sosinski.channel;

import java.util.ArrayList;
import java.util.List;

public class ListChannel implements Channels{

    List<Channel> channels = new ArrayList<>();

    @Override
    public void add(Channel channel) {
        channels.add(channel);
    }

    @Override
    public boolean exists(String channelName) {
        return channels.stream().anyMatch(x -> x.getName().equals(channelName));
    }

    @Override
    public Channel get(String channelName) {
        return channels.stream().filter(x -> x.getName().equals(channelName)).findFirst().orElseThrow();
    }

    @Override
    public int getSize() {
        return channels.size();
    }

    @Override
    public List<Channel> getChannels() {
        return channels;
    }
}
