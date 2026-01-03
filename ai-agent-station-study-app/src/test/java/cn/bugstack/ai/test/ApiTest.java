package cn.bugstack.ai.test;

import com.google.common.base.Function;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.ai.tool.function.FunctionToolCallback;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class ApiTest {

    @Test
    public void test() {
        log.info("测试完成");
    }
    @Before
    public void init_client(){
        OpenAiChatModel.builder()
                .openAiApi(OpenAiApi.builder()
                        .baseUrl("")
                        .apiKey("")
                        .completionsPath("/v1/chat/completions")
                        .embeddingsPath("/v1/embeddings")
                        .build())
                .defaultOptions(OpenAiChatOptions.builder()
                        .model("")
                        .build())
        .build();

    }

}
