package cn.bugstack.ai.domain.agent.service.execute.auto.step;

import cn.bugstack.ai.domain.agent.model.entity.ExecuteCommandEntity;
import cn.bugstack.ai.domain.agent.model.valobj.AiAgentClientFlowConfigVO;
import cn.bugstack.ai.domain.agent.service.execute.auto.step.factory.DefaultAutoAgentExecuteStrategyFactory;
import cn.bugstack.wrench.design.framework.tree.StrategyHandler;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service("executeRootNode")
public class RootNode extends AbstractExecuteSupport{

    @Resource
    private Step1AnalyzerNode step1AnalyzerNode;


    @Override
    protected String doApply(ExecuteCommandEntity executeCommandEntity, DefaultAutoAgentExecuteStrategyFactory.DynamicContext dynamicContext) throws Exception {
        log.info("===动态多轮执行测试开始===");
        log.info("用户输入:{}",executeCommandEntity.getMessage());
        log.info("最大执行步数:{}",executeCommandEntity.getMaxStep());
        log.info("会话ID:{}",executeCommandEntity.getSessionId());

        Map<String, AiAgentClientFlowConfigVO> flowConfigVOMap = repository.queryAiAgentClientFlowConfig(executeCommandEntity.getAiAgentId());

        //客户端对话组
        dynamicContext.setAiAgentClientFlowConfigVOMap(flowConfigVOMap);
        //上下文信息
        dynamicContext.setExecutionHistory(new StringBuilder());
        //当前任务信息
        dynamicContext.setCurrentTask(executeCommandEntity.getMessage());
        //最大任务步骤
        dynamicContext.setMaxStep(executeCommandEntity.getMaxStep());

        return router(executeCommandEntity,dynamicContext);


    }

    @Override
    public StrategyHandler<ExecuteCommandEntity, DefaultAutoAgentExecuteStrategyFactory.DynamicContext, String> get(ExecuteCommandEntity executeCommandEntity, DefaultAutoAgentExecuteStrategyFactory.DynamicContext dynamicContext) throws Exception {
        return step1AnalyzerNode;
    }
}
