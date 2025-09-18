package top.lrshuai.demo.diff;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class MyDiff {
    public MyOperation operation;
    public String text;

}
