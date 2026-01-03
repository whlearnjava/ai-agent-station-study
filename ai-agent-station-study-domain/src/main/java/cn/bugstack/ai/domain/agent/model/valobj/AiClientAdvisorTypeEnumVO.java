package cn.bugstack.ai.domain.agent.model.valobj;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.ai.chat.client.advisor.PromptChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.vectorstore.VectorStore;

import java.util.HashMap;
import java.util.Map;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum AiClientAdvisorTypeEnumVO {

    CHAT_MEMORY("ChatMemory","上下文记忆（内存模式）" ){
        @Override
        public Advisor createAdvisor(AiClientAdvisorVO aiClientAdvisorVO,VectorStore vectorStore) {
            AiClientAdvisorVO.ChatMemory chatMemory = aiClientAdvisorVO.getChatMemory();
            return PromptChatMemoryAdvisor.builder(
                    MessageWindowChatMemory.builder()
                            .maxMessages(chatMemory.getMaxMessages())
                            .build()
            ).build();
        }
    }
//    RAG_ANSWER("RagAnswer", "知识库") {
//        @Override
//        public Advisor createAdvisor(AiClientAdvisorVO aiClientAdvisorVO, VectorStore vectorStore) {
//            AiClientAdvisorVO.RagAnswer ragAnswer = aiClientAdvisorVO.getRagAnswer();
//            return new RagAnswerAdvisor(vectorStore, SearchRequest.builder()
//                    .topK(ragAnswer.getTopK())
//                    .filterExpression(ragAnswer.getFilterExpression())
//                    .build());
//        }
//    }
    ;

    private String code;
    private String info;

    private static final Map<String, AiClientAdvisorTypeEnumVO> CODE_MAP = new HashMap<>();


    static {
        for(AiClientAdvisorTypeEnumVO enumVO : values()){
            CODE_MAP.put(enumVO.getCode(), enumVO);
        }
    }

    //创建顾问对象
    public abstract Advisor createAdvisor(AiClientAdvisorVO aiClientAdvisorVO, VectorStore vectorStore);

    //根据code获取
    public static AiClientAdvisorTypeEnumVO getByCode(String code) {
        AiClientAdvisorTypeEnumVO enumVO = CODE_MAP.get(code);
        if(enumVO == null){
            throw new RuntimeException("不存在");
        }
        return enumVO;
    }
}
