package com.vtb.persistence.controller;

import com.vtb.persistence.dto.ArtifactDto;
import com.vtb.persistence.dto.AuthorDto;
import com.vtb.persistence.dto.ColumnDto;
import com.vtb.persistence.dto.ContentDto;
import com.vtb.persistence.dto.FragmentDto;
import com.vtb.persistence.dto.TableDto;
import com.vtb.persistence.dto.ValueDto;
import io.github.swagger2markup.adoc.AsciidocConverter;
import io.github.swagger2markup.adoc.ast.impl.BlockImpl;
import io.github.swagger2markup.adoc.ast.impl.CellImpl;
import io.github.swagger2markup.adoc.ast.impl.ColumnImpl;
import io.github.swagger2markup.adoc.ast.impl.DocumentImpl;
import io.github.swagger2markup.adoc.ast.impl.RowImpl;
import io.github.swagger2markup.adoc.ast.impl.SectionImpl;
import io.github.swagger2markup.adoc.ast.impl.TableImpl;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.asciidoctor.ast.Cell;
import org.asciidoctor.ast.Column;
import org.asciidoctor.ast.StructuralNode;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class TestController {


  @PostMapping
  public String create(@RequestBody ArtifactDto artifactDto) {
    AsciidocConverter converter = new AsciidocConverter(AsciidocConverter.NAME, new HashMap<>());

    StructuralNode parent = createArtifactBlock(artifactDto);

    StructuralNode authorBlock = toAuthorBlock(parent, artifactDto.getAuthor());
    parent.append(authorBlock);

    artifactDto.getFragments().stream()
        .map(fragment -> toFragmentBlock(parent, fragment))
        .forEach(parent::append);

    String asciidoc = converter.convert(parent, null, Collections.emptyMap());
    System.out.println(asciidoc);
    return asciidoc;
  }

  private StructuralNode createArtifactBlock(ArtifactDto artifactDto) {
    var document = new DocumentImpl();
    document.setTitle(artifactDto.getTitle());
    document.setAttribute("ID", artifactDto.getId(), true);
    document.setAttribute("Тип артефакта", artifactDto.getType(), true);
    document.setAttribute("Дата и время создания артефакта", artifactDto.getCreatedAt(), true);
    document.setAttribute("Дата и время обновления артефакта", artifactDto.getUpdatedAt(), true);
    return document;
  }

  private StructuralNode toAuthorBlock(StructuralNode parent, AuthorDto author) {
    var guid = "Уникальный идентификатор пользователя: " + author.getGuid();
    var displayName = "ФИО: " + author.getDisplayName();
    var avatar = "Идентификатор изображения: " + author.getAvatar();
    var email = "Почтовый ящик: " + author.getEmail();
    var position = "Должность: " + author.getPosition();

    BlockImpl block = new BlockImpl(parent, "listing");
    block.setTitle("Автор документа");
    block.setLines(Arrays.asList(guid, displayName, avatar, email, position));
    block.setAttribute("node_type", "artifact", true);

    return block;
  }

  private StructuralNode toFragmentBlock(StructuralNode parent, FragmentDto fragment) {
    DocumentImpl block = new DocumentImpl(parent);
    block.setTitle(fragment.getTitle());

    block.setAttribute("Идентификатор фрагмента", fragment.getId(), false);
    block.setAttribute("Порядковый номер фрагмента", fragment.getOrder(), false);
    block.setAttribute("Тип содержимого", fragment.getType(), false);
    block.setAttribute("Источник данных", fragment.getSource(), false);

    if (fragment.getContent() != null) {
      var contentBlock = toContentBlock(block, fragment.getContent(), fragment.getType());
      block.append(contentBlock);
    }

    if (fragment.getChildren() != null) {
      fragment.getChildren().stream()
          .map(child -> toFragmentBlock(block, child))
          .forEach(block::append);
    }

    return block;
  }

  private StructuralNode toContentBlock(StructuralNode parent, ContentDto content, String type) {
    if (type.equals("table")) {
      var tableBlock = new TableImpl(parent);

      TableDto tableDto = content.getTable();
      String[] columns = tableDto.getColumns().stream()
          .map(ColumnDto::getTitle)
          .toArray(String[]::new);
      tableBlock.setHeaderRow(columns);

      String columnsSize = getColumnsSize(columns);
      tableBlock.setAttribute("cols", columnsSize, true);

      tableDto.getRows().stream()
          .map(rowDto -> rowDto.getValues().stream()
              .map(ValueDto::getValue)
              .toArray(String[]::new))
          .forEach(tableBlock::addRow);

      return tableBlock;
    } else if (type.equals("text")) {
      var attribute = "Атрибут: " + content.getName();
      var value = "Значение атрибута: " + content.getValue();

      var block = new BlockImpl(parent, "listing");
      block.setLines(Arrays.asList(attribute, value));
      return block;
    }

    throw new IllegalArgumentException("Невалидный тип фрагмента: " + type);
  }

  private String getColumnsSize(String[] columns) {
    String res = ".^4a,".repeat(columns.length);
    return StringUtils.chop(res);
  }
}
