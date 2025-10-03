package net.plastoid501.throwitems.util;

import com.mojang.serialization.DynamicOps;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.*;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.Registries;
import net.minecraft.storage.NbtReadView;
import net.minecraft.storage.NbtWriteView;
import net.minecraft.util.ErrorReporter;
import net.plastoid501.throwitems.ThrowItems;
import net.plastoid501.throwitems.config.Configs;
import net.plastoid501.throwitems.mixin.INbtReadView;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class NbtUtil {
    /**
     * Reference: {@link net.minecraft.nbt.NbtIo}
     * Reference: {@link net.minecraft.client.option.HotbarStorage}
     */
    private static final String THROW_ITEM_LIST_FILE = "list.nbt";
    private static final NbtCompound throwItemList = new NbtCompound();


    static {
        throwItemList.put("whitelist", new NbtList());
        throwItemList.put("blacklist", new NbtList());
    }

    public static void generateDefaultNbt() {
        Path path = FileUtil.getConfigPath().resolve(ThrowItems.MOD_ID).resolve(THROW_ITEM_LIST_FILE);
        if (Files.notExists(path)) {
            try {
                NbtIo.write(throwItemList, path);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void addItemStack(ItemStack stack, DynamicRegistryManager registries) {
        NbtCompound nbt = readItemListNbt();
        if (nbt == null) {
            return;
        }
        if (nbt.contains(Configs.throwItems.getSelected())) {
            Optional<NbtList> list = nbt.getList(Configs.throwItems.getSelected());
            List<ItemStack> list2 = toList(list.orElse(new NbtList()), false, registries);
            list2.add(stack);
            sortList(list2);
            nbt.put(Configs.throwItems.getSelected(), toNbtList(list2, registries));
            saveItemListNbt(nbt, registries);
        }
    }

    public static void addItemStack(NbtList list, int index, ItemStack stack, DynamicRegistryManager registries) {
        list.addElement(index, putItemStack(stack, registries));
    }

    public static void removeItemStack(int index, DynamicRegistryManager registries) {
        NbtCompound nbt = readItemListNbt();
        if (nbt == null) {
            return;
        }
        if (nbt.contains(Configs.throwItems.getSelected())) {
            Optional<NbtList> list = nbt.getList(Configs.throwItems.getSelected());
            List<ItemStack> list2 = toList(list.orElse(new NbtList()), false, registries);
            list2.remove(index);
            nbt.put(Configs.throwItems.getSelected(), toNbtList(list2, registries));
            saveItemListNbt(nbt, registries);
        }
    }

    public static boolean addNewNbtList(String key, DynamicRegistryManager registries) {
        NbtCompound nbt = readItemListNbt();
        if (nbt == null) {
            return false;
        }
        key = key.replace(" ", "_");
        if (nbt.contains(key)) {
            return false;
        }
        nbt.put(key, new NbtList());
        saveItemListNbt(nbt, registries);
        return true;
    }

    public static boolean removeNbtList(String key, DynamicRegistryManager registries) {
        NbtCompound nbt = readItemListNbt();
        if (nbt == null) {
            return false;
        }
        if (!nbt.contains(key)) {
            return false;
        }
        nbt.remove(key);
        saveItemListNbt(nbt, registries);
        return true;
    }

    public static List<ItemStack> toList(NbtList list, boolean sort, DynamicRegistryManager registries) {
        List<ItemStack> items = new ArrayList<>();
        for (NbtElement element : list) {
            if (element == null) {
                continue;
            }

            DynamicOps<NbtElement> ops = NbtOps.INSTANCE;
            items.add(ops.getMap(element).flatMap(map -> ItemStack.MAP_CODEC.decode(ops, map)).getOrThrow());
        }

        return sort ? sortList(items) : items;
    }

    public static NbtList toNbtList(List<ItemStack> stacks, DynamicRegistryManager registries) {
        NbtList list = new NbtList();
        for (ItemStack stack : stacks) {
            list.add(putItemStack(stack, registries));
        }
        return list;
    }

    public static List<ItemStack> sortList(Collection<ItemStack> list) {
        List<ItemStack> sorted = new ArrayList<>();
        for (Item item : Registries.ITEM) {
            List<ItemStack> items = new ArrayList<>();
            for (ItemStack stack : list) {
                if (stack.isOf(item)) {
                    if (stack.getComponents().isEmpty()) {
                        items.addLast(stack);
                    } else {
                        items.addFirst(stack);
                    }
                }
            }

            sorted.addAll(items);
        }
        list.clear();
        list.addAll(sorted);
        return sorted;
    }

    private static NbtCompound putItemStack(ItemStack stack, DynamicRegistryManager registries) {
        NbtReadView nbtReadView = (NbtReadView) NbtReadView.create(ErrorReporter.EMPTY, registries, (NbtCompound) ItemStack.CODEC.encodeStart(registries.getOps(NbtOps.INSTANCE), stack).getOrThrow());
        return ((INbtReadView) nbtReadView).getNbt();
    }

    public static NbtCompound readItemListNbt(){
        return readNbtFile(FileUtil.getConfigPath().resolve(ThrowItems.MOD_ID).resolve(THROW_ITEM_LIST_FILE));
    }

    public static NbtCompound readNbtFile(Path path){
        NbtCompound nbt = null;
        try {
            nbt = NbtIo.read(path);
        } catch (IOException e) {
            ThrowItems.LOGGER.info("Cannot read {}", path.getFileName().toString());
        }

        return nbt;
    }

    public static void saveItemListNbt(NbtCompound nbt, DynamicRegistryManager registries) {
        writeNbtFile(FileUtil.getConfigPath().resolve(ThrowItems.MOD_ID).resolve(THROW_ITEM_LIST_FILE), nbt);
        updateNbt(registries);
    }

    public static void writeNbtFile(Path path, NbtCompound nbt) {
        try {
            NbtIo.write(nbt, path);
        } catch (IOException e) {
            ThrowItems.LOGGER.info("Cannot save {}", path.getFileName().toString());
        }
    }

    public static void updateNbt(DynamicRegistryManager registries) {
        NbtCompound nbt = readItemListNbt();
        if (nbt == null) {
            saveItemListNbt(throwItemList, registries);
            return;
        }
        Map<String, List<ItemStack>> listMap = new LinkedHashMap<>();
        for (String key : nbt.getKeys()) {
            listMap.put(key, toList(nbt.getList(key).orElse(new NbtList()), true, registries));
        }
        Configs.throwItems.setStacks(listMap);
    }
}
