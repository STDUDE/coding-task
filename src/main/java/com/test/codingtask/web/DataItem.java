package com.test.codingtask.web;

import com.opencsv.bean.CsvBindByName;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "DATA_ITEM")
public class DataItem implements Serializable {

  @CsvBindByName(column = "primary_key", required = true)
  private @Id String id;

  @CsvBindByName(column = "name", required = true)
  private String name;

  @CsvBindByName(column = "description", required = true)
  private String description;

  @CsvBindByName(column = "updated_timestamp", required = true)
  private Timestamp updated;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Timestamp getUpdated() {
    return updated;
  }

  public void setUpdated(Timestamp updated) {
    this.updated = updated;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    DataItem dataItem = (DataItem) o;
    return Objects.equals(id, dataItem.id) &&
            Objects.equals(name, dataItem.name) &&
            Objects.equals(description, dataItem.description) &&
            Objects.equals(updated, dataItem.updated);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, description, updated);
  }

  @Override
  public String toString() {
    return "DataItem{" +
            "id='" + id + '\'' +
            ", name='" + name + '\'' +
            ", description='" + description + '\'' +
            ", updated=" + updated +
            '}';
  }
}