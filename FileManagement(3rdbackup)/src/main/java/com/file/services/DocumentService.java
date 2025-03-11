////package com.file.services;
//// 
////import java.io.File;
////import java.nio.file.Files;
////import java.time.LocalDateTime;
////import java.util.List;
////import java.util.Optional;
////import java.util.UUID;
////
////import javax.crypto.SecretKey;
////
////import org.springframework.stereotype.Service;
////import org.springframework.web.multipart.MultipartFile;
////
////import com.file.entity.Document;
////import com.file.repo.DocumentRepository;
////import com.file.utils.FileEncrpt;
//// 
////@Service
////public class DocumentService 
////{
////    private final DocumentRepository documentRepository;
////    private final String uploadDir = "D:/Documents/";
////    private final String encryptionKey;
//// 
////    public DocumentService(DocumentRepository documentRepository) throws Exception {
////        this.documentRepository = documentRepository;
////        File directory = new File(uploadDir);
////        if (!directory.exists()) {
////            directory.mkdirs();
////        }
//// 
////        // Generate Encryption Key (Store it securely)
////        SecretKey secretKey = FileEncrpt.generateKey();
////        encryptionKey = java.util.Base64.getEncoder().encodeToString(secretKey.getEncoded());
////    }
//// 
////    public Document saveFile(MultipartFile file, String author) throws Exception {
////        // Generate a random file name
////        String randomFileName = UUID.randomUUID().toString();
////        File originalFile = new File(uploadDir + randomFileName);
////        File encryptedFile = new File(uploadDir + "enc_" + randomFileName);
//// 
////        // Save the uploaded file temporarily
////        file.transferTo(originalFile);
//// 
////        // Encrypt the file
////        FileEncrpt.encryptFile(encryptionKey, originalFile, encryptedFile);
//// 
////        // Delete original file after encryption
////        originalFile.delete();
//// 
////        // Save document details in DB
////        Document document = new Document(
////                randomFileName,    // Store only the random name
////                file.getContentType(),
////                file.getSize(),
////                author,
////                LocalDateTime.now()
////        );
//// 
////        return documentRepository.save(document);
////    }
//// 
////    public Optional<Document> getFile(Long id) {
////        return documentRepository.findById(id);
////    }
//// 
////    public byte[] decryptFile(String fileName) throws Exception {
////        File encryptedFile = new File(uploadDir + "enc_" + fileName);
////        File decryptedFile = new File(uploadDir + "dec_" + fileName);
//// 
////        FileEncrpt.decryptFile(encryptionKey, encryptedFile, decryptedFile);
////        byte[] fileContent = Files.readAllBytes(decryptedFile.toPath());
//// 
////        decryptedFile.delete();
//// 
////        return fileContent;
////    }
////    
////    public List<Document> getAllDocuments() {
////        return documentRepository.findAll();
////    }
////
////    public Document updateDocument(Long id, Document updatedDocument) {
////        Document existingDoc = documentRepository.findById(id)
////                .orElseThrow(() -> new RuntimeException("Document not found"));
////
////        existingDoc.setFileName(updatedDocument.getFileName());
////        existingDoc.setFileType(updatedDocument.getFileType());
////        existingDoc.setFileSize(updatedDocument.getFileSize());
////        existingDoc.setAuthor(updatedDocument.getAuthor());
////
////        return documentRepository.save(existingDoc);
////    }
////
////    public void deleteDocument(Long id) {
////        documentRepository.deleteById(id);
////    }
////
////}
//
//
//
//
//package com.file.services;
// 
//import java.io.File;
//import java.nio.file.Files;
//import java.time.LocalDateTime;
//import java.util.List;
//import java.util.Optional;
//import java.util.UUID;
//
//import javax.crypto.SecretKey;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.web.multipart.MultipartFile;
//
//import com.file.entity.Document;
//import com.file.entity.FilesLog;
//import com.file.entity.Users;
//import com.file.repo.DocumentRepository;
//import com.file.repo.FilesLogRepository;
//import com.file.repo.UsersRepository;
//import com.file.utils.FileEncrpt;
// 
//@Service
//public class DocumentService {
//	
//	@Autowired
//	private FilesLogRepository filesLogRepository;
//	
//	@Autowired
//	private UsersRepository userRepository;
//	
//    private final DocumentRepository documentRepository;
//
//    private final String uploadDir = "D:/Documents/";
//
//    private final String encryptionKey;
// 
//    public DocumentService(DocumentRepository documentRepository) throws Exception {
//        this.documentRepository = documentRepository;
//        File directory = new File(uploadDir);
//        if (!directory.exists()) {
//            directory.mkdirs();
//        }
// 
//        SecretKey secretKey = FileEncrpt.generateKey();
//        encryptionKey = java.util.Base64.getEncoder().encodeToString(secretKey.getEncoded());
//        System.out.println("Key : "+encryptionKey);
//    }
// 
////    public Document saveFile(MultipartFile file, String author) throws Exception {
//
////        String randomFileName = UUID.randomUUID().toString();
//
////        File originalFile = new File(uploadDir + randomFileName);
//
////        File encryptedFile = new File(uploadDir + "enc_" + randomFileName);
//
////
//
////        file.transferTo(originalFile);
//
////
//
////        FileEncrpt.encryptFile(encryptionKey, originalFile, encryptedFile);
//
////
//
////        originalFile.delete();
//
////
//
////        Document document = new Document(
//
////                randomFileName,    
//
////                file.getContentType(),
//
////                file.getSize(),
//
////                author,
//
////                LocalDateTime.now()
//
////        );
//
////
//
////        return documentRepository.save(document);
//
////    }
//
// 
// 
//    
//
//   /* public Document saveFile(MultipartFile file, String authorId) throws Exception {
//
//        String randomFileName = UUID.randomUUID().toString();
//
//        File originalFile = new File(uploadDir + randomFileName);
//
//        File encryptedFile = new File(uploadDir + "enc_" + randomFileName);
// 
//        file.transferTo(originalFile);
// 
//        FileEncrpt.encryptFile(encryptionKey, originalFile, encryptedFile);
// 
//        originalFile.delete();
// 
//        Users author = userRepository.findById(Long.valueOf(authorId)).orElse(null);
//
//        String authorName = (author != null) ? author.getUsername() : "Unknown User";
// 
//        Document document = new Document(
//
//                randomFileName,
//
//                file.getContentType(),
//
//                file.getSize(),
//
//                authorName,
//
//                LocalDateTime.now()
//
//        );
//
//        document = documentRepository.save(document);
// 
//        FilesLog filesLog = new FilesLog(
//
//                author, 
//
//                document.getFileName(),
//
//                LocalDateTime.now().toString(),
//
//                null, 
//
//                null, 
//
//                "0", 
//
//                String.valueOf(file.getSize()),
//
//                null 
//
//        );
//
//        filesLogRepository.save(filesLog);
// 
//        return document;
//
//    }
// */
// 
////    public Document saveFile(MultipartFile file, String authorName) throws Exception {
////        // Generate a random file name
////        String randomFileName = UUID.randomUUID().toString();
////        File originalFile = new File(uploadDir + randomFileName);
////        File encryptedFile = new File(uploadDir + "enc_" + randomFileName);
//// 
////        // Save the uploaded file temporarily
////        file.transferTo(originalFile);
//// 
////        // Encrypt the file
////        FileEncrpt.encryptFile(encryptionKey, originalFile, encryptedFile);
//// 
////        // Delete original file after encryption
////        originalFile.delete();
//// 
////        // **Step 1: Save Document**
////        Document document = new Document(
////                randomFileName,
////                file.getContentType(),
////                file.getSize(),
////                authorName, // Directly store author name
////                LocalDateTime.now()
////        );
////        document = documentRepository.save(document); // Save document
//// 
////        Users defaultUser = new Users();
////        defaultUser.setUsername("-");
////        defaultUser = userRepository.save(defaultUser); 
////
////        FilesLog filesLog = new FilesLog(
////        	    null, // No Users reference
////        	    document.getFileName(),
////        	    LocalDateTime.now().toString(),
////        	    "-", // Default lastModified
////        	    defaultUser, // Default lastModifiedBy
////        	    "0", // No downloads yet
////        	    String.valueOf(file.getSize()),
////        	    null // No one has downloaded yet
////        	);
////
////        	filesLogRepository.save(filesLog);
////        	return document;
////    }
//    
//    
//    public Document saveFile(MultipartFile file, String authorName) throws Exception {
//        // Generate a random file name
//        String randomFileName = UUID.randomUUID().toString();
//        File originalFile = new File(uploadDir + randomFileName);
//        File encryptedFile = new File(uploadDir + "enc_" + randomFileName);
//
//        // Save the uploaded file temporarily
//        file.transferTo(originalFile);
//
//        // Encrypt the file
//        FileEncrpt.encryptFile(encryptionKey, originalFile, encryptedFile);
//
//        // Delete original file after encryption
//        originalFile.delete();
//
//        // **Step 1: Fetch User from Database**
////        Users user = userRepository.findByUsername(authorName); // Fetch the uploader from Users table
////        if (user == null) {
////            throw new RuntimeException("User not found: " + authorName);
////        }
//
//        // Step 2: Save Document
//        Document document = new Document(
//                randomFileName,
//                file.getContentType(),
//                file.getSize(),
//                authorName, 
//                LocalDateTime.now()
//        );
//        document = documentRepository.save(document);
//
//        // **Step 3: Create FilesLog Entry**
//        FilesLog filesLog = new FilesLog();
//        filesLog.setDocName(document.getFileName());
//        filesLog.setAddedDate(LocalDateTime.now().toString());
//        filesLog.setLastModified("-"); 
//        filesLog.setDownloadCount("0");
//        filesLog.setSize(String.valueOf(file.getSize()));
////        filesLog.setAuthor(user); 
//
//        // Step 4: Handle Last Modified By
//        filesLog.setLastModifiedBy(null);
//
//        filesLogRepository.save(filesLog);
//
//        return document;
//    }
//
//    public Optional<Document> getFile(Long id) 
//    {
//        return documentRepository.findById(id);
//    }
// 
//    public byte[] decryptFile(String fileName) throws Exception {
//        File encryptedFile = new File(uploadDir + "enc_" + fileName);
//        File decryptedFile = new File(uploadDir + "dec_" + fileName); 
//        FileEncrpt.decryptFile(encryptionKey, encryptedFile, decryptedFile);
//        byte[] fileContent = Files.readAllBytes(decryptedFile.toPath()); 
//        decryptedFile.delete(); 
//        return fileContent;
//    }
//
//
//    public List<Document> getAllDocuments() {
//        return documentRepository.findAll();
//    }
//
//    public Document updateDocument(Long id, String author, LocalDateTime uploadAt, MultipartFile file) 
//    {
//        Optional<Document> optionalDoc = documentRepository.findById(id);
//        if (optionalDoc.isPresent()) {
//            Document existingDoc = optionalDoc.get();
//            existingDoc.setAuthor(author);
//            existingDoc.setUploadedAt(uploadAt); 
//            if (file != null && !file.isEmpty()) {
//                existingDoc.setFileName(file.getOriginalFilename());
//                existingDoc.setFileType(file.getContentType());
//                existingDoc.setFileSize(file.getSize());
//            } 
//            
//            return documentRepository.save(existingDoc);
//        } 
//        
//        else 
//        {
//            throw new RuntimeException("Document not found with ID: " + id);
//        }
//    }
//
//    public void deleteDocument(Long id) {
//        documentRepository.deleteById(id);
//    }
//}

//-------------------------last--------------------------------------[

//

package com.file.services;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.file.entity.Document;
import com.file.repo.DocumentRepository;

@Service
public class DocumentService {
    private static final Logger log = LoggerFactory.getLogger(DocumentService.class);

    private final DocumentRepository documentRepository;
    private final String uploadDir = "D:/Documents/";
    private final String keyFilePath = uploadDir + "secret.key";
    private final String ivFilePath = uploadDir + "secret.iv";
    private byte[] secretKey;
    private byte[] iv;

    public DocumentService(DocumentRepository documentRepository) throws Exception {
        this.documentRepository = documentRepository;
        File directory = new File(uploadDir);
        if (!directory.exists()) {
            directory.mkdirs();
        }
        
        loadOrGenerateKeyAndIV();
    }

    private void loadOrGenerateKeyAndIV() throws Exception {
        File keyFile = new File(keyFilePath);
        File ivFile = new File(ivFilePath);

        if (keyFile.exists() && ivFile.exists()) {
            // Load the existing key and IV
            this.secretKey = Base64.getDecoder().decode(Files.readString(keyFile.toPath()).trim());
            this.iv = Base64.getDecoder().decode(Files.readString(ivFile.toPath()).trim());
            log.info("Loaded existing AES key and IV.");
        } else {
            // Generate a new AES Key and IV
            KeyGenerator keyGen = KeyGenerator.getInstance("AES");
            keyGen.init(256);
            SecretKey key = keyGen.generateKey();
            this.secretKey = key.getEncoded();

            SecureRandom random = new SecureRandom();
            this.iv = new byte[16];
            random.nextBytes(iv);

            // Save key and IV to files
            Files.writeString(keyFile.toPath(), Base64.getEncoder().encodeToString(secretKey), StandardOpenOption.CREATE);
            Files.writeString(ivFile.toPath(), Base64.getEncoder().encodeToString(iv), StandardOpenOption.CREATE);

            log.info("Generated and saved new AES key and IV.");
        }
    }

    public Document saveFile(MultipartFile file, String authorName) throws Exception {
        String randomFileName = UUID.randomUUID().toString();
        File encryptedFile = new File(uploadDir + "enc_" + randomFileName);

        byte[] encryptedContent = encryptFile(file.getBytes());
        Files.write(encryptedFile.toPath(), encryptedContent);

        Document document = new Document(
                randomFileName,
                file.getContentType(),
                file.getSize(),
                authorName,
                LocalDateTime.now()
        );
        return documentRepository.save(document);
    }

    public Optional<Document> getFile(Long id) {
        return documentRepository.findById(id);
    }

    public byte[] decryptFile(String fileName) throws Exception {
        File encryptedFile = new File(uploadDir + "enc_" + fileName);
        byte[] encryptedContent = Files.readAllBytes(encryptedFile.toPath());
        return decryptFile(encryptedContent);
    }

    public byte[] encryptFile(byte[] fileContent) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        SecretKeySpec keySpec = new SecretKeySpec(secretKey, "AES");
        IvParameterSpec ivSpec = new IvParameterSpec(iv);
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);
        return cipher.doFinal(fileContent);
    }

    private byte[] decryptFile(byte[] encryptedContent) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        SecretKeySpec keySpec = new SecretKeySpec(secretKey, "AES");
        IvParameterSpec ivSpec = new IvParameterSpec(iv);
        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
        return cipher.doFinal(encryptedContent);
    }

    public List<Document> getAllDocuments() {
        return documentRepository.findAll();
    }
    public Document updateDocument(Long id, String author, LocalDateTime uploadAt, MultipartFile file) {
        Optional<Document> optionalDoc = documentRepository.findById(id);
        if (optionalDoc.isPresent()) {
            Document existingDoc = optionalDoc.get();
            existingDoc.setAuthor(author);
            existingDoc.setUploadedAt(uploadAt);
            if (file != null && !file.isEmpty()) {
                existingDoc.setFileName(file.getOriginalFilename());
                existingDoc.setFileType(file.getContentType());
                existingDoc.setFileSize(file.getSize());
            }
            return documentRepository.save(existingDoc);
        } else {
            throw new RuntimeException("Document not found with ID: " + id);
        }
    }

    public void deleteDocument(Long id) {
        documentRepository.deleteById(id);
    }
}



