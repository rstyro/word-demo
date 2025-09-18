package top.lrshuai.demo.diff;

public enum MyOperation {
    DELETE,
    INSERT,
    EQUAL,
    /**
     * 把相连的 (DELETE和INSERT) 当成一次 ERROR
     */
    ERROR
    ;
}
