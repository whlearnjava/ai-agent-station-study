package cn.bugstack.ai;

import cn.bugstack.ai.sdk.model.Message;
import cn.bugstack.ai.sdk.utils.WXAccessTokenUtils;
import com.alibaba.fastjson2.JSON;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.OpenAiApi;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.Scanner;

public class OpenAiCodeReview {
    public static void main(String[] args) throws Exception {
        System.out.println("测试执行 open");

        //github的token
        String token = System.getenv("GITHUB_TOKEN");
        if (null == token || token.isEmpty()) {
            throw new RuntimeException("token is null");
        }
        //1.代码检出
        ProcessBuilder processBuilder = new ProcessBuilder("git", "diff", "HEAD~1", "HEAD");
        processBuilder.directory(new File("."));

        Process process = processBuilder.start();

        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        StringBuilder diffCode = new StringBuilder();
        while ((line = reader.readLine()) != null) {
            diffCode.append(line);
        }
        int exitCode = process.waitFor();
        System.out.println("Exited with code " + exitCode);

        System.out.println("代码评审："+diffCode.toString());
        String log = openai(diffCode.toString());
        System.out.println("代码评审完毕");


        String logUrl = writeLog(token, log);
        System.out.println("日志写入完成");

        System.out.println("log日志地址："+logUrl);

        //消息通知
        System.out.println("pushMessage：" + logUrl);
        pushMessage(logUrl);

    }

    public static String  openai(String code ){
        String baseUrl = "https://juziapi.xin/";
        String apiKey = "sk-dM4fqhydVpnBv56vN1jU9cMiqtjUFGedVI8o9064BvpGF1IP";
        OpenAiApi openAiApi = OpenAiApi.builder()
                .baseUrl(baseUrl)
                .apiKey(apiKey)
                .completionsPath("v1/chat/completions")
                .embeddingsPath("v1/chat/embeddings")
                .build();
        OpenAiChatModel chatModel = OpenAiChatModel.builder()
                .openAiApi(openAiApi)
                .defaultOptions(OpenAiChatOptions.builder()
                        .model("gpt-4.1-mini")
                        .build()).build();

        ChatClient client = ChatClient.create(chatModel);

        String response = client.prompt()
                .system("你是一个高级编程架构师，精通各类场景方案、架构设计和编程语言")
                .user("请根据git diff记录对代码做出评审，代码为：" + code)
                .call()
                .content();
        String chatResponse = chatModel.call(response);




        System.out.println("测试结果："+JSON.toJSONString(chatResponse));
        return JSON.toJSONString(chatResponse);
    }

    private static String writeLog(String token,String log)throws Exception{

        Git git = Git.cloneRepository()
                .setURI("https://github.com/whlearnjava/openai-code-review-log.git")
                .setDirectory(new File("repo"))
                .setCredentialsProvider(new UsernamePasswordCredentialsProvider(token,""))
                .call();

        String dateFolderName = new SimpleDateFormat("yyyyMMdd").format(new Date());
        File dateFolder = new File("repo/" + dateFolderName);
        if(!dateFolder.exists()){
            dateFolder.mkdir();
        }
        String fileName = generateRandomString(12)+".md";
        File newFile = new File(dateFolder, fileName);
        try(FileWriter writer=new FileWriter(newFile)){
            writer.write(log);
        }
        git.add().addFilepattern(dateFolderName+"/"+fileName).call();
        git.commit().setMessage("add new file github actions").call();
        git.push().setCredentialsProvider(new UsernamePasswordCredentialsProvider(token,"")).call();

        System.out.println("Changes have been pushed");

        return "https://github.com/whlearnjava/openai-code-review-log/blob/master/"+dateFolderName+"/"+fileName;
    }





    private static String generateRandomString(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(characters.charAt(random.nextInt(characters.length())));
        }
        return sb.toString();
    }

    /**
     * 微信
     */
    private static void pushMessage(String logUrl) {
        String accessToken = WXAccessTokenUtils.getAccessToken();
        System.out.println(accessToken);

        Message message = new Message();
        message.put("project", "big-market");
        message.put("review", logUrl);
        message.setUrl(logUrl);

        String url = String.format("https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=%s", accessToken);
        sendPostRequest(url, JSON.toJSONString(message));
    }

    private static void sendPostRequest(String urlString, String jsonBody) {
        try {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json; utf-8");
            conn.setRequestProperty("Accept", "application/json");
            conn.setDoOutput(true);

            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = jsonBody.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            try (Scanner scanner = new Scanner(conn.getInputStream(), StandardCharsets.UTF_8.name())) {
                String response = scanner.useDelimiter("\\A").next();
                System.out.println(response);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
