package kr.co.dk.sample.api.common.file.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class FileService {
    private static final Logger log = LogManager.getLogger(FileService.class);
    private static final long imageUploadSizeLimit = 5000000;
    private static final long fileUploadSizeLimit = 10000000;
    private static final int THUMBNAIL_WIDTH = 400;
    private static final int THUMBNAIL_HEIGHT = 300;
    @Value("${profile.activate}")
    private String profileActivated;
    @Value("${goodch.file.upload.path}")
    private String fileUploadPath;
    @Value("${goodch.file.resource.server}")
    private String fileResourceServer;

//    public List<AbstractFile> uploadImage(List<MultipartFile> multipartFiles, FileCategory category) throws CustomException {
//        List<AbstractFile> abstractFiles = new ArrayList<>();
//        LocalDate today = LocalDate.now();
//
//        for(MultipartFile image : multipartFiles){
//            if(image.getSize() > imageUploadSizeLimit) throw new CustomException(ErrorCode.CODE_1503);
//        }
//
//        for(MultipartFile file : multipartFiles) {
//            if (file.isEmpty()) {
//                continue;
//            }else if(isImage(file)){
//                FileUploadInfo fileUploadInfo = uploadImageWithThumbnail(file, category, today);
//                AbstractFile abstractFile = FileUploadInfoMapper.INSTANCE.fileInfoToAbstractFile(fileUploadInfo);
//                abstractFiles.add(abstractFile);
//            }
//        }
//
//        return  abstractFiles;
//    }
//
//    public List<AbstractFile> uploadDocument(List<MultipartFile> multipartFiles, FileCategory category) throws CustomException{
//        List<AbstractFile> abstractFiles = new ArrayList<>();
//        LocalDate today = LocalDate.now();
//
//        for(MultipartFile image : multipartFiles){
//            if(image.getSize() > fileUploadSizeLimit) throw new CustomException(ErrorCode.CODE_1503);
//        }
//
//        for(MultipartFile file : multipartFiles) {
//            if (file.isEmpty()) {
//                continue;
//            }else {
//                FileUploadInfo fileUploadInfo = uploadFile(file, FileType.DOCUMENT,category, today);
//                AbstractFile abstractFile = FileUploadInfoMapper.INSTANCE.fileInfoToAbstractFile(fileUploadInfo);
//                abstractFiles.add(abstractFile);
//            }
//        }
//
//        return  abstractFiles;
//    }
//
//    private FileUploadInfo uploadImageWithThumbnail(MultipartFile file,FileCategory category, LocalDate today)  throws CustomException{
//        FileUploadInfo original = createFileUploadInfo(file, FileType.IMAGE, category,today);
//        uploadFile(original);
//        uploadAsThumbnail(original);
//        return original;
//    }
//
//    private void uploadAsThumbnail(FileUploadInfo original) throws CustomException{
//        try {
//            Thumbnails.of(original.getAbsoluteFilePathWithName())
//                    .size(THUMBNAIL_WIDTH, THUMBNAIL_HEIGHT)
//                    .toFiles(Rename.SUFFIX_HYPHEN_THUMBNAIL);
//        }catch (Exception e){
//            log.error("uploadAsThumbnail Exception : {}", e);
//            throw new CustomException(ErrorCode.CODE_1502);
//        }
//    }
//
//    private FileUploadInfo uploadFile(MultipartFile file, FileType fileType,FileCategory category, LocalDate today) throws CustomException{
//        FileUploadInfo fileUploadInfo = createFileUploadInfo(file,fileType,category,today);
//        uploadFile(fileUploadInfo);
//        return fileUploadInfo;
//    }
//
//    private FileUploadInfo createFileUploadInfo(MultipartFile file, FileType fileType,FileCategory category, LocalDate today){
//
//        String originalFileName = StringUtils.cleanPath(file.getOriginalFilename());
//        String extension = StringUtils.getFilenameExtension(originalFileName);
//        String newFileName = UUID.randomUUID().toString();
//        String relativeFilePathWithoutName = fileType.getUploadPath()+ File.separator + category.getName() + File.separator + today.getYear()+ File.separator + today.getMonth().getValue() + File.separator + today.getDayOfMonth() + File.separator;
//        String relativeFilePathWithName = relativeFilePathWithoutName + newFileName + "." + extension;
//        String absoluteFilePathWithoutName = fileUploadPath + relativeFilePathWithoutName;
//        String absoluteFilePathWithName = absoluteFilePathWithoutName + newFileName + "." + extension;
//
//        String uri = profileActivated.equalsIgnoreCase("local")? new File(absoluteFilePathWithName).getAbsolutePath():fileResourceServer + relativeFilePathWithName;
//
//        return FileUploadInfo.builder()
//                .file(file)
//                .originalFileName(originalFileName)
//                .extension(extension)
//                .newFileName(newFileName)
//                .relativeFilePathWithName(relativeFilePathWithName)
//                .absoluteFilePathWithName(absoluteFilePathWithName)
//                .absoluteFilePathWithoutName(absoluteFilePathWithoutName)
//                .uri(uri)
//                .size(String.valueOf(file.getSize()))
//                .type(fileType)
//                .build();
//    }
//
//    private static boolean isImage(MultipartFile file) {
//        String extension = StringUtils.getFilenameExtension(file.getOriginalFilename());
//        return extension.equalsIgnoreCase("jpg") ||
//                extension.equalsIgnoreCase("jpeg") ||
//                extension.equalsIgnoreCase("gif") ||
//                extension.equalsIgnoreCase("png") ||
//                extension.equalsIgnoreCase("bmp");
//    }
//
//    private void uploadFile (FileUploadInfo fileUploadInfo) throws CustomException{
//        try{
//            Files.createDirectories(Paths.get(fileUploadInfo.getAbsoluteFilePathWithoutName()));
//            Path path = Paths.get(fileUploadInfo.getAbsoluteFilePathWithName());
//            Files.write(path, fileUploadInfo.getFile().getBytes());
//        }catch (Exception e){
//            log.error("uploadFile Exception : {}",e);
//            throw new CustomException(ErrorCode.CODE_1501);
//        }
//    }
}
