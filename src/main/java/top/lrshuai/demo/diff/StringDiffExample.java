package top.lrshuai.demo.diff;

import org.bitbucket.cowwoc.diffmatchpatch.DiffMatchPatch;

import java.util.LinkedList;
import java.util.ListIterator;
import java.util.function.Function;

public class StringDiffExample {
    public static void main(String[] args) {
        // 要比较的两段文本
        String text1 = "Hello, world! This is the old text.";
        String text2 = "Hello, Java! This is the new and improved text.";
//        String text1 = "";
//        String text2 = "";

        // 初始化 DiffMatchPatch 对象
        DiffMatchPatch dmp = new DiffMatchPatch();
        // 计算差异
        LinkedList<DiffMatchPatch.Diff> diffs = dmp.diffMain(text1, text2);
        // 优化差异显示（按语义合并）
        dmp.diffCleanupSemantic(diffs);

        // 生成 HTML 格式的差异报告
        String htmlDiff = dmp.diffPrettyHtml(diffs);

        // 输出 HTML 结果
        System.out.println("HTML 差异报告:");
        System.out.println(htmlDiff);

        long eqCount = 0, addCount = 0, missCount = 0;
        for (DiffMatchPatch.Diff diff : diffs) {
            switch (diff.operation) {
                case EQUAL -> eqCount += diff.text.length();
                case INSERT -> addCount += diff.text.length();
                case DELETE -> missCount += diff.text.length();
            }
        }


        // 此外，你也可以直接遍历 Diff 列表进行自定义处理
        System.out.println("\n解析 Diff 对象:");
        System.out.println("原文文本:"+text1);
        System.out.println("转录文本:"+text2);
        System.out.println();
        printDiffs(diffs,diff->diff.operation,diff->diff.text);
        System.out.println();
        System.out.println();
        LinkedList<MyDiff> mergeDiffs = convertToMyDiff(diffs);
        printDiffs(mergeDiffs,diff->diff.operation,diff->diff.text);
    }


    /**
     * 统一的差异打印方法
     * @param diffs 差异列表
     * @param operationExtractor 操作类型提取函数
     * @param textExtractor 文本内容提取函数
     * @param <T> 差异对象类型
     */
    public static <T> void printDiffs(LinkedList<T> diffs,
                                      Function<T, Enum<?>> operationExtractor,
                                      Function<T, String> textExtractor) {
        for (T diff : diffs) {
            Enum<?> operation = operationExtractor.apply(diff);
            String text = textExtractor.apply(diff);

            switch (operation.name()) {
                case "INSERT":
                    System.out.println("✅新增: [%s] ".formatted(text));
                    break;
                case "DELETE":
                    System.out.println("➖缺失: [%s]".formatted(text));
                    break;
                case "EQUAL":
                    System.out.println("\uD83D\uDFF0相同: [%s]".formatted(text));
                    break;
                case "ERROR":
                    System.out.println("❌错误: [%s]".formatted(text));
                    break;
                default:
                    System.out.println("❓未知操作: [" + text + "]");
                    break;
            }
        }
    }


    /**
     * 此方法会尝试合并连续的 DELETE 和 INSERT 操作到一个 ERROR 操作。
     * @param diffs 原始的 DiffMatchPatch.Diff 列表
     * @return 转换后的 MyDiff 列表
     */
    public static LinkedList<MyDiff> convertToMyDiff(LinkedList<DiffMatchPatch.Diff> diffs) {
        LinkedList<MyDiff> myDiffs = new LinkedList<>();
        ListIterator<DiffMatchPatch.Diff> iterator = diffs.listIterator();
        while (iterator.hasNext()) {
            DiffMatchPatch.Diff currentDiff = iterator.next();
            MyOperation myOp;
            String text = currentDiff.text;

            // 基本操作类型映射
            switch (currentDiff.operation) {
                case DELETE:
                    myOp = MyOperation.DELETE;
                    // 检查下一个操作是否是 INSERT，以决定是否合并为 ERROR
                    if (iterator.hasNext()) {
                        DiffMatchPatch.Diff nextDiff = iterator.next();
                        if (nextDiff.operation == DiffMatchPatch.Operation.INSERT) {
                            // 合并 DELETE 和 INSERT 的文本，形成 ERROR 操作
                            myOp = MyOperation.ERROR;
                            text = nextDiff.text;
                        } else {
                            // 将迭代器移回下一个元素之前，即当前下一个元素的位置
                            iterator.previous();
                        }
                    }
                    break;
                case INSERT:
                    myOp = MyOperation.INSERT;
                    break;
                case EQUAL:
                    myOp = MyOperation.EQUAL;
                    break;
                default:
                    // 对于未知操作，可以映射为 ERROR 或其他默认值，这里使用 ERROR
                    myOp = MyOperation.ERROR;
                    break;
            }
            myDiffs.add(new MyDiff(myOp, text));
        }
        return myDiffs;
    }
}
