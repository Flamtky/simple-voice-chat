package de.maxhenkel.voicechat.voice.common;

import net.minecraft.network.FriendlyByteBuf;

public class MicPacket implements Packet<MicPacket> {

    private byte[] data;
    private boolean whispering;
    public boolean toGroup;
    private long sequenceNumber;

    public MicPacket(byte[] data, boolean whispering, boolean toGroup, long sequenceNumber) {
        this.data = data;
        this.whispering = whispering;
        this.toGroup = toGroup;
        this.sequenceNumber = sequenceNumber;
    }

    public MicPacket() {

    }

    @Override
    public long getTTL() {
        return 500L;
    }

    public byte[] getData() {
        return data;
    }

    public long getSequenceNumber() {
        return sequenceNumber;
    }

    public boolean isWhispering() {
        return whispering;
    }

    @Override
    public MicPacket fromBytes(FriendlyByteBuf buf) {
        MicPacket soundPacket = new MicPacket();
        soundPacket.data = buf.readByteArray();
        soundPacket.sequenceNumber = buf.readLong();
        soundPacket.whispering = buf.readBoolean();
        soundPacket.toGroup = buf.readBoolean();
        return soundPacket;
    }

    @Override
    public void toBytes(FriendlyByteBuf buf) {
        buf.writeByteArray(data);
        buf.writeLong(sequenceNumber);
        buf.writeBoolean(whispering);
        buf.writeBoolean(toGroup);
    }
}
