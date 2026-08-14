package com.leetcode;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

class SnapshotArray {
    private class ValueSnapId {
        int value;
        int snapId;

        ValueSnapId(int value, int snapId) {
            this.value = value;
            this.snapId = snapId;
        }
    }

    Map<Integer, List<ValueSnapId>> map;
    private int globalSnapId;

    public SnapshotArray(int length) {
        this.map = new HashMap<>();
        this.globalSnapId = 0;
    }

    public void set(int index, int val) {
        map.putIfAbsent(index, new ArrayList<>());
        List<ValueSnapId> list = map.get(index);
        if (!list.isEmpty() && list.get(list.size() - 1).snapId == globalSnapId) {
            list.get(list.size() - 1).value = val;
        } else {
            list.add(new ValueSnapId(val, globalSnapId));
        }
    }

    public int snap() {
        globalSnapId++;
        return globalSnapId - 1;
    }

    public int get(int index, int snap_id) {
        List<ValueSnapId> list = map.getOrDefault(index, new ArrayList<>());
        int n = list.size();
        for (int i = n - 1; i >= 0; i--) {
            if (list.get(i).snapId <= snap_id)
                return list.get(i).value;
        }
        return 0;
    }
}