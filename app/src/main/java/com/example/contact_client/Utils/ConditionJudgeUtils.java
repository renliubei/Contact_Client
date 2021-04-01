package com.example.contact_client.Utils;

import com.example.contact_client.interactive_creator.Condition.ConditionJudge;
import com.example.contact_client.interactive_creator.VideoNode;

/**
 * 用于各种条件判断
 */
public class ConditionJudgeUtils {
    public static final int  OVER = 1;
    public static final int BELOW = 2;
    public static final int EQUAL = 3;

    /**
     * 判断一个结点是否可到达
     * @param node 判断结点
     * @return true表示结点满足到达条件
     */
    public static boolean judgeNode(VideoNode node){
        boolean flag=true;
        for(ConditionJudge judge : node.getJudges()){
            switch (judge.getJudge()){
                case OVER:
                   flag = flag&&judge.getRequiredValue()>judge.getCurrentValue();
                   break;
                case BELOW:
                    flag = flag&&judge.getRequiredValue()<judge.getCurrentValue();
                    break;
                case EQUAL:
                    flag = flag&&judge.getRequiredValue()==judge.getCurrentValue();
                    break;
            }
        }
        return flag;
    }
}
