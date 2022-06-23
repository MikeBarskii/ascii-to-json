package com.vtb.persistence.controller;

import com.vtb.persistence.dto.ArtifactDto;
import com.vtb.persistence.dto.AuthorDto;
import com.vtb.persistence.dto.ColumnDto;
import com.vtb.persistence.dto.ContentDto;
import com.vtb.persistence.dto.FragmentDto;
import com.vtb.persistence.dto.TableDto;
import com.vtb.persistence.dto.ValueDto;
import com.vtb.persistence.dto.VersionDto;
import io.github.swagger2markup.adoc.AsciidocConverter;
import io.github.swagger2markup.adoc.ast.impl.BlockImpl;
import io.github.swagger2markup.adoc.ast.impl.DocumentImpl;
import io.github.swagger2markup.adoc.ast.impl.SectionImpl;
import io.github.swagger2markup.adoc.ast.impl.TableImpl;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.asciidoctor.ast.Section;
import org.asciidoctor.ast.StructuralNode;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class Controller {

  @PostMapping
  public String create(@RequestBody ArtifactDto artifactDto) {
    AsciidocConverter converter = new AsciidocConverter(AsciidocConverter.NAME, new HashMap<>());

    StructuralNode parent = createArtifactBlock(artifactDto);

    artifactDto.getFragments().stream()
        .map(fragment -> toFragmentBlock(parent, fragment))
        .forEach(parent::append);

    String asciidoc = converter.convert(parent, null, Collections.emptyMap());
    return asciidoc;
  }

  private StructuralNode createArtifactBlock(ArtifactDto artifactDto) {
    var document = new DocumentImpl();
    document.setTitle(artifactDto.getTitle());
    document.setAttribute("id", artifactDto.getId(), true);
    document.setAttribute("type", artifactDto.getType(), true);
    document.setAttribute("Дата и время создания артефакта", artifactDto.getCreatedAt(), true);
    document.setAttribute("Дата и время обновления артефакта", artifactDto.getUpdatedAt(), true);
    return document;
  }

  private StructuralNode toFragmentBlock(StructuralNode parent, FragmentDto fragment) {
    DocumentImpl block = new DocumentImpl(parent);
    block.setTitle(fragment.getTitle());

    block.setAttribute("id", fragment.getId(), false);
    block.setAttribute("order", fragment.getOrder(), false);
    block.setAttribute("type", fragment.getType(), false);
    block.setAttribute("source", fragment.getSource(), false);

    VersionDto version = fragment.getVersion();
    if (version != null) {
      block.setAttribute("versionNumber", version.getNumber(), false);
      block.setAttribute("versionTime", version.getDateTime(), false);
    }

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
      String blockLine = content.getName() + ":" + content.getValue();

      var block = new BlockImpl(parent, "listing");
      block.setLines(List.of(blockLine));
      return block;
    }

    throw new IllegalArgumentException("Невалидный тип фрагмента: " + type);
  }

  private String getColumnsSize(String[] columns) {
    String res = ".^4a,".repeat(columns.length);
    return StringUtils.chop(res);
  }
}
