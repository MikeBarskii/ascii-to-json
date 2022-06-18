package com.vtb.persistence.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TableDto {
  private List<ColumnDto> columns;
  private List<RowDto> rows;
}
