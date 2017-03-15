package algorithm.ali.problems;

import java.util.*;

/**
 * Created by xinszhou on 23/02/2017.
 *
 * 题目 : 现在有 n 台机器，每台机器有两个属性 (机器所在机房、机器所在分组), 为了在发布过程中不中断服务，需要对机器进行均匀分批发布 (比如分 m 个批次，每批 n/m 台机器)，同时为了保证同机房或同分组的机器不会被同时发布，需要把每个属性均匀打散到每批机器中，请设计算法并实现
 *
 * 题目看的不是太明白，不知道批次 m 是参数还是要计算的值，假设是参数吧
 *
 * 思路是把 room 和 group 编号，从 0 -> sizeof(room)-1 and 0 -> sizeof(group)-1, 然后把问题转化成从矩阵中查找点集，这些点不能在同一行或者同一列即是满足条件的一个批次
 */
public class Problem1 {

    // machines[i][j] 表示第 i 个 room 和 第 j 个 group 中的机器数目, 0 表示不存在
    // 所以需要先把 roomId 和 groupId 转到 从 0 开始单调递增的 int 类型
    int [][] machines;

    public Problem1(int[][] machines) {
        this.machines = machines;
    }

    // 在当前 machines 的状态下，下一次 release 的机器
    private List<Machine> doRelease(int targetNm) {

        int lineNm = machines.length;
        int colNm = machines[0].length;

        List<Machine> releasedMachines = new LinkedList<>();

        // 已经被占用的列
        Set<Integer> releasedCol = new HashSet<>();

        for(int i = 0; i < lineNm; i ++) {
            boolean lineFinished = false;
            for(int j = 0; j < colNm && lineFinished; j ++) {
                if(releasedCol.contains(j)) continue;

                if(machines[i][j] > 0) {
                    releasedCol.add(j);
                    releasedMachines.add(new Machine(i, j));
                    machines[i][j] -= 1;
                    if (releasedMachines.size() == targetNm) {
                        return releasedMachines;
                    } else {
                        // go to next line
                        lineFinished = true;
                    }
                }
            }
        }
        return releasedMachines;
    }

    // n 是所有的机器数目，m 是批次数
    public List<List<Machine>> release(int n, int m) {
        assert (m != 0 && n != 0);

        List<List<Machine>> result = new LinkedList<>();
        int nmReleased = 0;

        // 不能精确的保证每次都能分这么多, 但尽量分到这么多
        int machineNmPerRelease = n / m;

        // 同房间同组不能同时 release 的话，可能会分配不均
        while (nmReleased < n) {
            List<Machine> partialResult = doRelease(machineNmPerRelease);
            result.add(partialResult);
            nmReleased += partialResult.size();
        }
        return result;
    }
}
