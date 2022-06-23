package com.vtb;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.networknt.schema.BaseJsonValidator;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SpecVersion;
import com.networknt.schema.ValidationMessage;
import java.util.Set;
import lombok.SneakyThrows;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class Application extends SpringBootServletInitializer {

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);

    testJsonWithSchema();
  }

  @SneakyThrows
  private static void testJsonWithSchema() {
    JsonSchemaFactory schemaFactory = JsonSchemaFactory
        .builder(JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V7))
        .build();
    JsonSchema jsonSchema = schemaFactory.getSchema("{\n"
        + "  \"$id\": \"https://example.com/person.schema.json\",\n"
        + "  \"$schema\": \"http://json-schema.org/draft-07/schema#\",\n"
        + "  \"title\": \"Person\",\n"
        + "  \"type\": \"object\",\n"
        + "  \"properties\": {\n"
        + "    \"firstName\": {\n"
        + "      \"type\": \"string\",\n"
        + "      \"description\": \"The person's first name.\"\n"
        + "    },\n"
        + "    \"lastName\": {\n"
        + "      \"type\": \"string\",\n"
        + "      \"description\": \"The person's last name.\"\n"
        + "    },\n"
        + "    \"age\": {\n"
        + "      \"description\": \"Age in years which must be equal to or greater than zero.\",\n"
        + "      \"type\": \"integer\",\n"
        + "      \"minimum\": 0\n"
        + "    }\n"
        + "  }\n"
        + "}\n");
    ObjectMapper mapper = new ObjectMapper();
    JsonNode jsonNode = mapper.readTree("{\n"
        + "  \"firstName\": \"John\",\n"
        + "  \"lastName\": \"Doe\",\n"
        + "  \"age\": 21\n"
        + "}\n");
    Set<ValidationMessage> errors = jsonSchema.validate(jsonNode);
    System.out.println("errors: " + errors);
  }


}
