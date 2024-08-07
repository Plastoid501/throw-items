package net.plastoid501.throwitems.util;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtIo;
import net.minecraft.nbt.NbtList;
import net.minecraft.registry.Registries;
import net.plastoid501.throwitems.ThrowItems;
import net.plastoid501.throwitems.config.Configs;

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

    public static void addItemStack(ItemStack stack) {
        NbtCompound nbt = readItemListNbt();
        if (nbt == null) {
            return;
        }
        if (nbt.contains(Configs.throwItems.getSelected())) {
            NbtList list = nbt.getList(Configs.throwItems.getSelected(), 10);
            List<ItemStack> list2 = toList(list, false);
            list2.add(stack);
            sortList(list2);
            nbt.put(Configs.throwItems.getSelected(), toNbtList(list2));
            saveItemListNbt(nbt);
        }
    }

    public static void addItemStack(NbtList list, int index, ItemStack stack) {
        list.addElement(index, putItemStack(new NbtCompound(), stack));
    }

    public static void removeItemStack(int index) {
        NbtCompound nbt = readItemListNbt();
        if (nbt == null) {
            return;
        }
        if (nbt.contains(Configs.throwItems.getSelected())) {
            NbtList list = nbt.getList(Configs.throwItems.getSelected(), 10);
            List<ItemStack> list2 = toList(list, false);
            list2.remove(index);
            nbt.put(Configs.throwItems.getSelected(), toNbtList(list2));
            saveItemListNbt(nbt);
        }
    }

    public static boolean addNewNbtList(String key) {
        NbtCompound nbt = readItemListNbt();
        if (nbt == null) {
            return false;
        }
        key = key.replace(" ", "_");
        if (nbt.contains(key)) {
            return false;
        }
        nbt.put(key, new NbtList());
        saveItemListNbt(nbt);
        return true;
    }

    public static boolean removeNbtList(String key) {
        NbtCompound nbt = readItemListNbt();
        if (nbt == null) {
            return false;
        }
        if (!nbt.contains(key)) {
            return false;
        }
        nbt.remove(key);
        saveItemListNbt(nbt);
        return true;
    }

    public static List<ItemStack> toList(NbtList list, boolean sort) {
        List<ItemStack> items = new ArrayList<>();
        for (NbtElement element : list) {
            if (element == null) {
                continue;
            }
            NbtCompound compound = (NbtCompound) element;
            items.add(ItemStack.fromNbt(compound));
        }

        return sort ? sortList(items) : items;
    }

    public static NbtList toNbtList(List<ItemStack> stacks) {
        NbtList list = new NbtList();
        for (ItemStack stack : stacks) {
            list.add(putItemStack(new NbtCompound(), stack));
        }
        return list;
    }

    public static List<ItemStack> sortList(Collection<ItemStack> list) {
        List<ItemStack> sorted = new ArrayList<>();
        for (Item item : Registries.ITEM) {
            List<ItemStack> items = new ArrayList<>();
            for (ItemStack stack : list) {
                if (stack.isOf(item)) {
                    if (stack.hasNbt()) {
                        items.add(stack);
                    } else {
                        items.add(0, stack);
                    }
                }
            }

            sorted.addAll(items);
        }
        list.clear();
        list.addAll(sorted);
        return sorted;
    }

    private static NbtCompound putItemStack(NbtCompound nbt, ItemStack stack) {
        return stack.writeNbt(nbt);
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

    public static void saveItemListNbt(NbtCompound nbt) {
        writeNbtFile(FileUtil.getConfigPath().resolve(ThrowItems.MOD_ID).resolve(THROW_ITEM_LIST_FILE), nbt);
        updateNbt();
    }

    public static void writeNbtFile(Path path, NbtCompound nbt) {
        try {
            NbtIo.write(nbt, path);
        } catch (IOException e) {
            ThrowItems.LOGGER.info("Cannot save {}", path.getFileName().toString());
        }
    }

    public static void updateNbt() {
        NbtCompound nbt = readItemListNbt();
        if (nbt == null) {
            saveItemListNbt(throwItemList);
            return;
        }
        Map<String, List<ItemStack>> listMap = new LinkedHashMap<>();
        for (String key : nbt.getKeys()) {
            listMap.put(key, toList(nbt.getList(key, 10), true));
        }
        Configs.throwItems.setStacks(listMap);
    }
}
