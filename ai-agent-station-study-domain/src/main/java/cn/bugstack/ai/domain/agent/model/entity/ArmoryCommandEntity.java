package cn.bugstack.ai.domain.agent.model.entity;

import cn.bugstack.ai.domain.agent.model.valobj.AiAgentEnumVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ArmoryCommandEntity {

    private String commandType;

    private List<String> commandIdList;

    public String getLoadDataStrategy() {
        return AiAgentEnumVO.getByCode(commandType).getLoadDataStrategy();
    }

}
