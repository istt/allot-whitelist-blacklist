package com.ft.service.dto;


import java.time.ZonedDateTime;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the DataFile entity.
 */
public class DataFileDTO implements Serializable {

    private String id;

    private byte[] dataFile;
    private String dataFileContentType;

    private ZonedDateTime createdAt;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public byte[] getDataFile() {
        return dataFile;
    }

    public void setDataFile(byte[] dataFile) {
        this.dataFile = dataFile;
    }

    public String getDataFileContentType() {
        return dataFileContentType;
    }

    public void setDataFileContentType(String dataFileContentType) {
        this.dataFileContentType = dataFileContentType;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        DataFileDTO dataFileDTO = (DataFileDTO) o;
        if(dataFileDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), dataFileDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "DataFileDTO{" +
            "id=" + getId() +
            ", dataFile='" + getDataFile() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            "}";
    }
}
