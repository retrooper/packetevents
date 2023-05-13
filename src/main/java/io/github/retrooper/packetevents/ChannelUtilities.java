package io.github.retrooper.packetevents;

import io.netty.channel.ChannelPipeline;

public class ChannelUtilities {
    public static boolean pipelineContainsSplitterAndPrepender(ChannelPipeline pipeline)
    {
        boolean containsSplitter = false;
        boolean containsPrepender = false;
        for(var handler : pipeline) {
            PacketEventsMod.LOGGER.info(handler.getClass().getName());
            if(handler.getKey().equals("splitter")) {
                containsSplitter = true;
            }
            if(handler.getKey().equals("prepender")) {
                containsPrepender = true;
            }
        }
        return containsSplitter && containsPrepender;
    }
}
