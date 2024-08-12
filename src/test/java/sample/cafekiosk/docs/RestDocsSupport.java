package sample.cafekiosk.docs;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;

@ExtendWith(RestDocumentationExtension.class)
public abstract class RestDocsSupport {

    protected MockMvc mockMvc;
    protected ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 스프링을 띄워서 문서를 작성한다.
     */
//    @BeforeEach
//    void setUp(WebApplicationContext webApplicationContext,
//               RestDocumentationContextProvider contextProvider) throws Exception {
//        this.mockMvc =
//            MockMvcBuilders.webAppContextSetup(webApplicationContext)
//                .apply(documentationConfiguration(contextProvider))
//                .build();
//    }
    @BeforeEach
    void setUp(RestDocumentationContextProvider contextProvider) throws Exception {
        this.mockMvc = MockMvcBuilders.standaloneSetup(initController())
            .apply(documentationConfiguration(contextProvider))
            .build();
    }

    protected abstract Object initController();
}
