package util;

import java.util.BitSet;

/**
 * <p>
 * TODO
 * </p>
 *
 * @author violet
 * @version 1.0
 * @since 2020/2/1 15:13
 */
public class BloomFilter {

    private static final int DEFAULT_SIZE = 2 << 27;
    private static final int[] SEEDS = {5, 7, 11, 13, 31, 33, 61};
    private SimpleHash[] simpleHashes = new SimpleHash[SEEDS.length];

    private BitSet bitSets = new BitSet(DEFAULT_SIZE);

    public BloomFilter() {
        for (int i = 0; i < simpleHashes.length; i++) {
            simpleHashes[i] = new SimpleHash(DEFAULT_SIZE, SEEDS[i]);
        }
    }

    public void add(String value) {
        for (SimpleHash simpleHash : simpleHashes) {
            bitSets.set(simpleHash.hash(value));
        }
    }

    public boolean contains(String value) {
        for (SimpleHash simpleHash : simpleHashes) {
            if (!bitSets.get(simpleHash.hash(value))) {
                return false;
            }
        }
        return true;
    }
    /**
     *  简单hash算法
     */
    public static class SimpleHash {
        //容量
        private int capacity;
        //随机数
        private int seed;

        public SimpleHash(int capacity, int seed) {
            this.capacity = capacity;
            this.seed = seed;
        }

        public int hash(String value) {
            int result = 0;

            int length = value.length();
            for (int i = 0; i < length; i++) {
                result = result * seed + value.charAt(i);
            }

            return (capacity - 1) & result;
        }
    }

    public BitSet getBitSets() {
        return bitSets;
    }

    public void setBitSets(BitSet bitSets) {
        this.bitSets = bitSets;
    }
}
