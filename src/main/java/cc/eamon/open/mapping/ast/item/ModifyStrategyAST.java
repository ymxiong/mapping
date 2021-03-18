package cc.eamon.open.mapping.ast.item;

import cc.eamon.open.mapping.mapper.support.strategy.ModifyEnableStrategy;
import cc.eamon.open.mapping.mapper.support.strategy.ModifyNormalStrategy;
import cc.eamon.open.mapping.mapper.support.strategy.ModifyStrategy;
import cc.eamon.open.mapping.mapper.util.StringUtils;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.Names;

/**
 * @author lzr
 * @date 2021/3/7 22:19
 */
public class ModifyStrategyAST {

    public static JCTree.JCExpression buildRecover(ModifyStrategy strategy, JCTree.JCExpression expression, String varName, TreeMaker treeMaker, Names names){
        if(strategy instanceof ModifyEnableStrategy){
            return treeMaker.Exec(treeMaker.Apply(
                    List.nil(),
                    treeMaker.Select(
                            treeMaker.Ident(names.fromString(varName)),
                            names.fromString(((ModifyEnableStrategy) strategy).getDetail().getRecoverMethodName())
                    ),
                    List.of(expression)
                    )
            ).expr;
        }else if(strategy instanceof ModifyNormalStrategy){
            return expression;
        }else{
            throw new RuntimeException("未找到对应策略");
        }
    }

    public static JCTree.JCExpression buildModify(ModifyStrategy strategy, String fieldName, String varName, TreeMaker treeMaker, Names names){
        if(strategy instanceof ModifyEnableStrategy){
            return treeMaker.Exec(treeMaker.Apply(
                    List.nil(),
                    treeMaker.Select(
                            treeMaker.Ident(names.fromString(varName)),
                            names.fromString(((ModifyEnableStrategy) strategy).getDetail().getModifyMethodName())
                    ),
                    List.of(
                            treeMaker.Exec(treeMaker.Apply(
                                    List.nil(),
                                    treeMaker.Select(
                                            treeMaker.Ident(names.fromString(varName)),
                                            names.fromString("get" + StringUtils.firstWordToUpperCase(fieldName))
                                    ),
                                    List.nil()
                            )).expr
                    )
            )).expr;
        }else if(strategy instanceof ModifyNormalStrategy){
            return treeMaker.Exec(treeMaker.Apply(
                    List.nil(),
                    treeMaker.Select(
                            treeMaker.Ident(names.fromString(varName)),
                            names.fromString("get" + StringUtils.firstWordToUpperCase(fieldName))
                    ),
                    List.nil()
            )).expr;
        }else{
            throw new RuntimeException("未找到对应策略");
        }
    }
}
