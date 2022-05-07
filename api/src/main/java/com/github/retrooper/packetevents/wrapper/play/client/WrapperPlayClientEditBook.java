package com.github.retrooper.packetevents.wrapper.play.client;

import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

import java.util.List;

public class WrapperPlayClientEditBook extends PacketWrapper<WrapperPlayClientEditBook> {
    int slot;
    List<String> pages;
    String title;

    public WrapperPlayClientEditBook(PacketReceiveEvent event) {
        super(event);
    }

    @Override
    public void read() {
        slot = readVarInt();

        int size = readVarInt();
        for (int i = 0; i < size; i++) {
            pages.add(readString());
        }

        boolean hasTitle = readBoolean();
        if (hasTitle) {
            title = readString();
        } else {
            title = null;
        }
    }

    @Override
    public void copy(WrapperPlayClientEditBook wrapper) {
        this.slot = wrapper.slot;
        this.pages = wrapper.pages;
        this.title = wrapper.title;
    }

    @Override
    public void write() {
        writeVarInt(slot);
        writeVarInt(pages.size());
        for (String page : pages) {
            writeString(page);
        }
        writeBoolean(title != null);
        if (title != null) {
            writeString(title);
        }
    }

    public int getSlot() {
        return slot;
    }

    public void setSlot(int slot) {
        this.slot = slot;
    }

    public List<String> getPages() {
        return pages;
    }

    public void setPages(List<String> pages) {
        this.pages = pages;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
