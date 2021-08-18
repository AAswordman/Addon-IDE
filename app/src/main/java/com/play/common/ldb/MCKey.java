package com.play.common.ldb;

import com.mithrilmania.blocktopograph.map.Dimension;
import com.play.common.chunk.ChunkTag;
import bms.helper.tools.LOG;

public class MCKey {
    private byte[] key;

    private Dimension dimension;

    private int x;

    private int z;

    private byte dataID;

    private byte num;


    public MCKey(byte[] key) {
        this.key = key;
        if (key.length >= 9) {
            this.x = getIntFromBytes(key[3], key[2], key[1], key[0]);
            this.z = getIntFromBytes(key[7], key[6], key[5], key[4]);
            if (key.length <= 10) {
                this.dimension = Dimension.OVERWORLD;
                this.dataID = key[8];
                if (key.length == 10) {
                    this.num = key[9];
                }
            } else {
                if (key.length >= 13) {
                    this.dimension = Dimension.getDimension(key[8]);
                    this.dataID = key[12];
                    if (key.length >= 14) {
                        this.num = key[13];
                    }
                } else {
                    LOG.print("", key);
                }
            }
        } else {
            LOG.print("", key);
        }

    }
    public int getX() {
        return x;
    }
    public int getSubChunk() {
        return num;
    }
    public int getZ() {
        return z;
    }
    public byte getDataID() {
        return dataID;
    }
    public static int getIntFromBytes(byte high_h, byte high_l, byte low_h, byte low_l) {
        return (high_h & 0xff) << 24 | (high_l & 0xff) << 16 | (low_h & 0xff) << 8 | low_l & 0xff;
    }
    public static byte[] getChunkDataKey(int x, int z, ChunkTag type, Dimension dimension, byte subChunk, boolean asSubChunk) {
        if (dimension == Dimension.OVERWORLD) {
            byte[] key = new byte[asSubChunk ? 10 : 9];
            System.arraycopy(getReversedBytes(x), 0, key, 0, 4);
            System.arraycopy(getReversedBytes(z), 0, key, 4, 4);
            key[8] = type.dataID;
            if (asSubChunk) key[9] = subChunk;
            return key;
        } else {
            byte[] key = new byte[asSubChunk ? 14 : 13];
            System.arraycopy(getReversedBytes(x), 0, key, 0, 4);
            System.arraycopy(getReversedBytes(z), 0, key, 4, 4);
            System.arraycopy(getReversedBytes(dimension.id), 0, key, 8, 4);
            key[12] = type.dataID;
            if (asSubChunk) key[13] = subChunk;
            return key;
        }
    }
    public static byte[] getReversedBytes(int i) {
        return new byte[]{
            (byte) i,
            (byte) (i >> 8),
            (byte) (i >> 16),
            (byte) (i >> 24)
        };
    }




}
