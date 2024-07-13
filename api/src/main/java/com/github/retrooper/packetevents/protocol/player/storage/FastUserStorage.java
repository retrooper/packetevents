package com.github.retrooper.packetevents.protocol.player.storage;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Fast User Storage - An API usage class
 * This class provides efficient runtime value association with the User class (the association exists for as along as the User doesn't quit - it's non-permanent).
 * It can be used for many different purposes, such as, but not limited to:
 * 1. Storing the plugin's packet handler for the User even before UUID is known. This could be achieved for example via:
 *
 * <pre> {@code
 *  private final TypedStorageValueId<MyUser> identifier = TypedStorageValueId.identifierFor(this.myPluginInstance);
 *
 *  @Override
 *  public void onUserConnect(UserConnectEvent event) {
 *      event.getUser().storeRuntime(this.identifier, new MyUser());//associate
 *  }
 *
 *  @Override
 *  public void onPacketReceive(PacketReceiveEvent event) {
 *      MyUser user = event.getUser().getStored(this.identifier);
 *      user.packetHandler().handle(event);
 *  }
 * }</pre>
 * <p>
 * 2. Replacing a Map - Even tho Maps are extremely fast, their performance can slightly decrease with size. This Storage class offers constant O(1)
 * performance, independently to the amount of Users or the length of the data array. With 100 users, the performance of this class was often found to be
 * about ~35% faster than that of a UUID Map.
 *
 * @author ShadowOfHeaven
 */

public final class FastUserStorage {

    //Ensure the array's element thread visibility by using MethodHandles
    //It was found to be slightly faster than VarHandles with excessive amount of calls
    private static final MethodHandle setElement = MethodHandles.arrayElementSetter(Object[].class);//array, index, value
    private static final MethodHandle getElement = MethodHandles.arrayElementGetter(Object[].class);//array, index
    private static final Map<Object, Integer> pluginToId = new ConcurrentHashMap<>(2);
    private static volatile int idCounter;
    private volatile Object[] data;
    private final ReentrantLock lock = new ReentrantLock();
    private final Condition condition = lock.newCondition();

    public FastUserStorage() {
        this.data = new Object[idCounter];
    }

    public void store(StorageIdBase identifier, Object value) {
        this.store(identifier.id(), value);
    }

    private void store(int index, Object value) {
        if (index >= data.length) {
            this.lock.lock();
            try {
                this.regrow();
                this.store0(index, value);//perform the storage synchronously to ensure no data can be discarded
            } finally {
                this.lock.unlock();
            }
            return;
        }
        this.store0(index, value);
    }

    private void store0(int index, Object value) {
        try {
            setElement.invokeExact(this.data, index, value);//must be invoked with an int
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public Object get(StorageValueId identifier) {
        return this.get0(identifier.id());
    }

    public <T> T get(TypedStorageValueId<T> identifier) {
        return (T) this.get0(identifier.id());
    }

    private Object get0(int index) {
        if (index >= data.length) {
            this.lock.lock();
            try {
                this.regrow();
            } finally {
                this.lock.unlock();
            }
            return null;
        }
        if (this.lock.isLocked()) {//await during any array modifications
            try {
                this.condition.await();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        try {
            return getElement.invokeExact(this.data, index);//must be invoked with an int
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    //This method is invoked in case of a late initialization of the Identifier by a plugin,
    //where a user joined and the Identifier was only then constructed
    //Normally, this is unlikely to be executed
    private void regrow() {
        Object[] array = new Object[idCounter];
        System.arraycopy(this.data, 0, array, 0, this.data.length);
        this.data = array;
    }

    /**
     * Works as described here {@link TypedStorageValueId#identifierFor TypedStorageValueId.identifierFor}
     */
    public static <T> TypedStorageValueId<T> typedIdentifierFor(Object plugin) {
        int id = pluginToId.computeIfAbsent(plugin, p -> nextId());//Or we can throw an IllegalStateException, since some may think they're creating a completely new identifier.
        return new TypedStorageValueId<>(id);
    }

    /**
     * Works as described here {@link StorageValueId#identifierFor StorageValueId.identifierFor}
     */
    public static StorageValueId identifierFor(Object plugin) {
        int id = pluginToId.computeIfAbsent(plugin, p -> nextId());//Same dilemma here
        return new StorageValueId(id);
    }

    private static synchronized int nextId() {
        return ++idCounter;
    }
}