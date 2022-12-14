package com.ll.exam.zero.app.fileUpload.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ll.exam.zero.app.base.entity.BaseEntity;
import com.ll.exam.zero.app.fileUpload.service.GenFileService;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@ToString(callSuper = true)
public class GenFile extends BaseEntity {
    private String relTypeCode;
    private int relId;
    private String typeCode;
    private String type2Code;
    private boolean isReady;
    private String fileExtTypeCode;
    private String fileExtType2Code;
    private int fileSize;
    private int fileNo;
    private String fileExt;
    private String fileDir;
    private String originFileName;

    @JsonIgnore
    public String getFilePath() {
        return GenFileService.GEN_FILE_DIR_PATH + getBaseFileUri();
    }

    @JsonIgnore
    public String getBaseFileUri() {
        return "/" + relTypeCode + "/" + fileDir + "/" + getFileName();
    }

    public String getFileName() {
        return getId() + "." + fileExt;
    }
}
