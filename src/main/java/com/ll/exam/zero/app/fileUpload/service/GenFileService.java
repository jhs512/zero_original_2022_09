package com.ll.exam.zero.app.fileUpload.service;

import com.ll.exam.zero.app.base.Util;
import com.ll.exam.zero.app.fileUpload.entity.GenFile;
import com.ll.exam.zero.app.fileUpload.repository.GenFileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
@RequiredArgsConstructor
public class GenFileService {
    public static String GEN_FILE_DIR_PATH;

    @Value("${custom.genFileDirPath}")
    public void setGenFileDirPath(String genFileDirPath) {
        GEN_FILE_DIR_PATH = genFileDirPath;
    }

    private final GenFileRepository genFileRepository;

    public void updateFileOnDisk(MultipartFile multipartFile, long fileId, String originFileName, String fileExtTypeCode, String fileExtType2Code, String fileExt, int fileSize) {
    }

    private long saveFile(String relTypeCode, int relId, String typeCode, String type2Code, int fileNo, String fileDir,
                          String originFileName, String fileExtTypeCode, String fileExtType2Code, String fileExt, int fileSize) {

        GenFile genFile = GenFile
                .builder()
                .relTypeCode(relTypeCode)
                .relId(relId)
                .typeCode(typeCode)
                .type2Code(type2Code)
                .fileNo(fileNo)
                .fileDir(fileDir)
                .originFileName(originFileName)
                .fileExtTypeCode(fileExtTypeCode)
                .fileExtType2Code(fileExtType2Code)
                .fileExt(fileExt)
                .fileSize(fileSize)
                .build();

        genFileRepository.save(genFile);

        return genFile.getId();
    }

    public Long saveFileOnDisk(MultipartFile multipartFile, String relTypeCode, int relId, String typeCode, String type2Code, int fileNo, String originFileName, String fileExtTypeCode, String fileExtType2Code, String fileExt, int fileSize) {
        // ??? ????????? ????????? ????????? ??????(???_???)
        String fileDir = Util.getNowYearMonthDateStr();

        // DB??? ???????????? ??????(?????? ???????????? ????????? ????????? ????????? ??? ???????????? file ???????????? ?????? ?????? ?????????)
        long fileId = saveFile(relTypeCode, relId, typeCode, type2Code, fileNo, fileDir, originFileName, fileExtTypeCode,
                fileExtType2Code, fileExt, fileSize);

        // ??? ????????? ????????? ??????(io??????) ?????? ??????
        String targetDirPath = GEN_FILE_DIR_PATH + "/" + relTypeCode + "/" + fileDir;
        File targetDir = new File(targetDirPath);

        // ??? ????????? ????????? ????????? ???????????? ???????????? ??????
        if (targetDir.exists() == false) {
            targetDir.mkdirs();
        }

        String targetFileName = fileId + "." + fileExt;
        String targetFilePath = targetDirPath + "/" + targetFileName;

        System.out.println("targetFilePath : " + targetFilePath);

        // ?????? ??????(???????????? ????????? ????????? ????????? ??????)
        try {
            multipartFile.transferTo(new File(targetFilePath));
        } catch (IllegalStateException | IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        return fileId;
    }

    public long getGenFileId(String relTypeCode, long relId, String typeCode, String type2Code, int fileNo) {
        GenFile genFile = genFileRepository.findByRelTypeCodeAndRelIdAndTypeCodeAndType2CodeAndFileNo(relTypeCode, relId, typeCode, type2Code, fileNo);

        if (genFile == null) {
            return -1;
        }

        return genFile.getId();
    }

    public void deleteFile(long id) {
        // ?????? ?????? ?????? ????????????
        GenFile oldGenFile = getGenFileById(id);

        // ?????? ????????? ???????????? ???????????? ????????? ??????
        String filePath = oldGenFile.getFilePath();

        File file = new File(filePath);
        if (file.exists()) {
            file.delete();
        }

        genFileRepository.delete(oldGenFile);
    }

    private GenFile getGenFileById(long id) {
        return genFileRepository.findById(id).orElse(null);
    }
}
