package cn.bugstack.ai.domain.agent.model.valobj;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AiClientApiVO {

    //api id
    private String apiId;

    //基础url
    private String baseUrl;

    //密钥
    private String apiKey;

    //对话不全路径
    private String completionsPath;

    //嵌入式向量路径
    private String embeddingsPath;

}
