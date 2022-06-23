package com.vtb.persistence.converter;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonValue;
import javax.json.JsonWriter;
import javax.json.JsonWriterFactory;
import javax.json.stream.JsonGenerator;

import org.asciidoctor.ast.Block;
import org.asciidoctor.ast.Cell;
import org.asciidoctor.ast.Column;
import org.asciidoctor.ast.ContentNode;
import org.asciidoctor.ast.Cursor;
import org.asciidoctor.ast.DescriptionList;
import org.asciidoctor.ast.DescriptionListEntry;
import org.asciidoctor.ast.Document;
import org.asciidoctor.ast.ListItem;
import org.asciidoctor.ast.PhraseNode;
import org.asciidoctor.ast.Row;
import org.asciidoctor.ast.Section;
import org.asciidoctor.ast.StructuralNode;
import org.asciidoctor.ast.Table;
import org.asciidoctor.ast.Title;
import org.asciidoctor.converter.AbstractConverter;
import org.asciidoctor.converter.ConverterFor;

@ConverterFor("ast-json")
public class JsonConverter extends AbstractConverter<JsonObject> {

  private String LINE_SEPARATOR = "\n";

  public JsonConverter(String backend, Map<String, Object> opts) {
    super(backend, opts);
  }

  @Override
  public JsonObject convert(ContentNode node, String transform, Map<Object, Object> o) {
    return convertToJsonObject(node);
  }

  private JsonObject convertToJsonObject(ContentNode node) {
    if (node == null) {
      return null;
    }

    JsonObjectBuilder builder = Json.createObjectBuilder();
    Map<String, Object> attributes = node.getAttributes();
    addToBuilder(builder, "id", (String)attributes.get("id"));
    addToBuilder(builder, "source", (String)attributes.get("source"));
    addToBuilder(builder, "type", (String)attributes.get("type"));
    addToBuilder(builder, "order", (String)attributes.get("order"));

    JsonObject version = mapToJsonObject(Map.of(
        "number", attributes.get("versionnumber"),
        "dateTime", attributes.get("versiontime")));
    addToBuilder(builder, "version", version);

    return builder.build();
  }

  @Override
  public void write(JsonObject output, OutputStream out) throws IOException {
    if (output != null) {
      Map<String, Boolean> config = Collections.singletonMap(JsonGenerator.PRETTY_PRINTING, Boolean.TRUE);
      JsonWriterFactory writerFactory = Json.createWriterFactory(config);
      JsonWriter jsonWriter = writerFactory.createWriter(out);

      jsonWriter.write(output);
      jsonWriter.close();
    }
  }

  private JsonObject mapTitleToJsonObject(Title title) {
    if (title == null) {
      return null;
    }
    JsonObjectBuilder builder = Json.createObjectBuilder();
    addToBuilder(builder, "combined", title.getCombined());
    addToBuilder(builder, "main", title.getMain());
    addToBuilder(builder, "subtitle", title.getSubtitle());
    return builder.build();
  }

  private JsonArray convertRowsToJsonArray(List<Row> rows) {
    JsonArrayBuilder builder = Json.createArrayBuilder();
    for (Row row : rows) {
      builder.add(convertToJsonArray(row.getCells()));
    }
    return builder.build();
  }

  private JsonArray convertDescriptionListItemsToJsonArray(DescriptionList descriptionList) {
    JsonArrayBuilder builder = Json.createArrayBuilder();
    for (DescriptionListEntry e : descriptionList.getItems()) {
      builder.add(convertDescriptionListEntryToJsonObject(e));
    }
    return builder.build();
  }

  private JsonObject convertDescriptionListEntryToJsonObject(DescriptionListEntry entry) {
    JsonObjectBuilder builder = Json.createObjectBuilder();
    addToBuilder(builder, "description", convertToJsonObject(entry.getDescription()));
    addToBuilder(builder, "terms", convertToJsonArray(entry.getTerms()));
    return builder.build();
  }

  private JsonObject convertCursorToJsonObject(Cursor cursor) {
    if (cursor == null) {
      return null;
    }
    JsonObjectBuilder builder = Json.createObjectBuilder();
    addToBuilder(builder, "lineNumber", cursor.getLineNumber());
    addToBuilder(builder, "path", cursor.getPath());
    addToBuilder(builder, "dir", cursor.getDir());
    addToBuilder(builder, "file", cursor.getFile());
    return builder.build();
  }

  private JsonArray convertToJsonArray(List<? extends ContentNode> list) {
    if (list != null && list.size() > 0) {
      JsonArrayBuilder builder = Json.createArrayBuilder();
      for (ContentNode node : list) {
        builder.add(convertToJsonObject(node));
      }
      return builder.build();
    }
    return null;
  }

  private JsonArray listToJsonArray(List<String> list) {
    return Json.createArrayBuilder(list).build();
  }

  private JsonObject mapToJsonObject(Map<? extends Object, Object> map) {
    JsonObjectBuilder builder = Json.createObjectBuilder();
    for (Entry<? extends Object, Object> entry : map.entrySet()) {
      if (entry.getValue() == null) {
        builder.addNull(entry.getKey().toString());
      }
      else {
        builder.add(entry.getKey().toString(), entry.getValue().toString());
      }
    }
    return builder.build();
  }

  private static void addToBuilder(JsonObjectBuilder builder, String key, JsonValue value) {
    if (value != null) {
      builder.add(key, value);
    }
  }

  private static void addToBuilder(JsonObjectBuilder builder, String key, String value) {
    if (value != null) {
      builder.add(key, value);
    }
  }

  private static void addToBuilder(JsonObjectBuilder builder, String key, boolean value) {
    builder.add(key, value);
  }

  private static void addToBuilder(JsonObjectBuilder builder, String key, int value) {
    builder.add(key, value);
  }
}
